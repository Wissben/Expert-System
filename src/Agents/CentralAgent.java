package Agents;

import Agents.Behaviours.CentralReceiveFindMe;
import Agents.Behaviours.MessageReceivingAction;
import Agents.Behaviours.MessageReceivingBehaviour;
import Agents.ExpertAgent.AgentVariableValue;
import Agents.ExpertAgent.FindAgentRuleInitializer;
import BackEnd.Database.QueryAnswer;
import BackEnd.ExpertSys.Expert;
import BackEnd.ExpertSys.VariableMapper;
import BackEnd.Initializers.RuleInitializer;
import BackEnd.Types.StringVariableValue;
import FrontEnd.Main;
import FrontEnd.UIQuery;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by ressay on 03/04/18.
 */
public class CentralAgent extends MessageReceiverAgent
{
    Expert expert;
    public static String findMeID = "findMe";
    public static String agentVariableValue = "Agents";
    LinkedList<FindAgentRuleInitializer> ruleInitializers = new LinkedList<>();

    public static AgentDescription newAgent(String name,Expert expert)
    {
        Object[] arguments = new Object[1];
        arguments[0] = expert;
        return new AgentDescription(name,CentralAgent.class.getName(),arguments);
    }

    @Override
    public void setup()
    {

        Object[] args = getArguments();
        if(args != null)
        {
            expert = (Expert) args[0];
        }
        Registration reg = new Registration(this,null,CentralAgent.class.getName());
        reg.registerMe(); // to receive FindMes when a new annexAgent is added
        addMessageAction(new CentralReceiveFindMe(this)); // to receive new AnnexAgent's FindMes
        requestAllAvailableRuleInitializers(); // request FindMes of already registered annexAgents

        addBehaviour(new TickerBehaviour(this,2000) {

            @Override
            protected void onTick() {
                VariableMapper mapper = new VariableMapper();
                mapper.addVariableValue("Article",new StringVariableValue("Skirt"));
                mapper.addVariableValue("Position",new StringVariableValue("Torso"));
                UIQuery query = new UIQuery(mapper);
                System.out.println("start answer ");
                AgentQueryAnswers queryAnswers = answer(query);
                System.out.println("end answer ");
                for (String agent : queryAnswers.getAgents())
                    System.out.println("received 1 from "+ agent);
            }
        });
//        try {

//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            Thread.sleep(1000);
//            System.out.println("sending 2");
//            VariableMapper mapper = new VariableMapper();
//            mapper.addVariableValue("Article",new StringVariableValue("Skirt"));
//            UIQuery query = new UIQuery(mapper);
//            AgentQueryAnswers queryAnswers = answer(query);
//            for (String agent : queryAnswers.getAgents())
//                System.out.println("received 2 from "+ agent);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            Thread.sleep(1000);
//            System.out.println("sending 3");
//            VariableMapper mapper = new VariableMapper();
//            mapper.addVariableValue("Article",new StringVariableValue("Skirt"));
//            mapper.addVariableValue("Position",new StringVariableValue("Torso"));
//            UIQuery query = new UIQuery(mapper);
//            AgentQueryAnswers queryAnswers = answer(query);
//            for (String agent : queryAnswers.getAgents())
//                System.out.println("received 3 from "+ agent);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }


    public AgentQueryAnswers answer(UIQuery query) {
        AgentQueryAnswers answer = new AgentQueryAnswers();
//        System.out.println("showing rules ***********");
//        expert.getRuleBase().displayRules();
//        System.out.println("showing rules ***********");
        Main.debugging = true;
        prepareExpert();
        Main.debugging = false;

        // look for annexAgents that can answer query
        expert.forwardChain(query.getMapper());
        // get agents resulted from the forwardChain
        AgentVariableValue agvv = (AgentVariableValue) expert.getRuleBase().getVariableValue(agentVariableValue);
        if(agvv == null || agvv.getValue() == null)
            return new AgentQueryAnswers();
        int numberOfAgents = agvv.getValue().size();

        System.out.println("passed here!!");



        // request from all possible agents
        for (String agentName : agvv.getValue())
            requestAnswerFromAgent(agentName,query);
        // prepare message replier to "requestAnswer" conversation
        MessageTemplate template = MessageTemplate.MatchConversationId(AnnexAgent.requestAnswer);
        // what to do when the annex agent answers

        int repliedAgents = 0;
        while (repliedAgents != numberOfAgents)
        {
            ACLMessage msg = blockingReceive(template);
            System.out.println("received answer!");
            QueryAnswer qans = null;
            try {
                qans = (QueryAnswer) msg.getContentObject();
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
            answer.addAnswer(msg.getSender().getLocalName(),qans);
            repliedAgents++;
        }
        // by this time answer should contain all annexAgents replies
        return answer;
    }

    protected void requestAnswerFromAgent(String agentName,UIQuery query)
    {
        ACLMessage m = new ACLMessage(ACLMessage.INFORM);
        m.addReceiver(new AID(agentName,AID.ISLOCALNAME));
        m.setConversationId(AnnexAgent.requestAnswer);
        try {
            m.setContentObject(query);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("requesting answer from " + agentName);
        send(m);
    }


    public void addRuleInitializer(FindAgentRuleInitializer initializer)
    {
        Main.debugging = true;
        expert.addInitializer(initializer);
        ruleInitializers.add(initializer);
        Main.debugging = false;
    }

    public void prepareExpert()
    {
        expert.initRuleBase();
        for(RuleInitializer ruleInitializer : ruleInitializers)
            expert.addInitializer(ruleInitializer);

    }

    public void requestAllAvailableRuleInitializers()
    {
        DFAgentDescription[] agents = getAllAnnexSellers();
        if(agents != null) {

            for (int i = 0; i < agents.length; i++)
            {
                ACLMessage m = new ACLMessage(ACLMessage.INFORM);
                String name = agents[i].getName().getLocalName();
                m.addReceiver(new AID(name,AID.ISLOCALNAME));
                m.setConversationId(findMeID);
                m.setContent(name);
                System.out.println("sending to: "+name);
                send(m);
            }
        }
    }

    public DFAgentDescription[] getAllAnnexSellers()
    {
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd  = new ServiceDescription();
        sd.setType( AnnexAgent.annexSellService );
        dfd.addServices(sd);
        DFAgentDescription[] result = null;
        try {
            result = DFService.search(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        if(result == null || result.length == 0)
            return null;
        return result;
    }
}
