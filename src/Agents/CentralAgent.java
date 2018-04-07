package Agents;

import Agents.Behaviours.CentralReceiveFindMe;
import Agents.Behaviours.MessageReceivingAction;
import Agents.Behaviours.MessageReceivingBehaviour;
import Agents.ExpertAgent.AgentVariableValue;
import Agents.ExpertAgent.FindAgentRuleInitializer;
import BackEnd.Database.QueryAnswer;
import BackEnd.ExpertSys.Expert;
import FrontEnd.UIQuery;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.LinkedList;

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

    }


    public AgentQueryAnswers answer(UIQuery query) {
        AgentQueryAnswers answer = new AgentQueryAnswers();
        expert.resetVariableValues();
        // look for annexAgents that can answer query
        expert.forwardChain(query.getMapper());
        // get agents resulted from the forwardChain
        AgentVariableValue agvv = (AgentVariableValue) expert.getRuleBase().getVariableValue(agentVariableValue);
        int numberOfAgents = agvv.getValue().size();


        // prepare message replier to "requestAnswer" conversation
        MessageTemplate template = MessageTemplate.MatchConversationId(AnnexAgent.requestAnswer);
        // what to do when the annex agent answers

        addMessageAction(new MessageReceivingAction(template) {
            int repliedAgents = 0;
            @Override
            public boolean action(ACLMessage msg) {
                try {
                    QueryAnswer qans = (QueryAnswer) msg.getContentObject();
                    answer.addAnswer(msg.getSender().getLocalName(),qans);
                    repliedAgents++;
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
                return repliedAgents == numberOfAgents; // if we reach number of agents, we stop this action
            }
        });

        // request from all possible agents
        for (String agentName : agvv.getValue())
            requestAnswerFromAgent(agentName,query);

        // TODO change this to not only wait, but if all agents replied, then stop waiting
        try {
            Thread.sleep(300); // wait for answers
        } catch (InterruptedException e) {
            e.printStackTrace();
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
//        System.out.println("sending to: "+name);
        send(m);
    }


    public void addRuleInitializer(FindAgentRuleInitializer initializer)
    {
        expert.addInitializer(initializer);
        ruleInitializers.add(initializer);
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
