package Agents;

import Agents.Behaviours.CentralReceiveFindMe;
import Agents.ExpertAgent.AgentVariableValue;
import Agents.ExpertAgent.FindAgentRuleInitializer;
import BackEnd.Database.Product;
import BackEnd.Database.QueryAnswer;
import BackEnd.ExpertSys.Expert;
import BackEnd.ExpertSys.VariableMapper;
import BackEnd.Initializers.RuleInitializer;
import FrontEnd.Main;
import FrontEnd.UI.UIQuery;
import FrontEnd.AttributeChoice;
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
 *
 * Created by ressay on 03/04/18.
 */
public class CentralAgent extends MessageReceiverAgent
{
    Expert expert;
    public static String findMeID = "findMe";
    public static String agentVariableValue = "Agents";
    AttributeChoice controller = null;
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
//        requestAllAvailableRuleInitializers(); // request FindMes of already registered annexAgents
        controller = Main.getAttributeController();
        controller.setAgent(this);
//        addBehaviour(new TickerBehaviour(this,2000) {
//
//            @Override
//            protected void onTick() {
//                VariableMapper mapper = new VariableMapper();
////                mapper.addVariableValue("Article",new StringVariableValue("Skirt"));
//                mapper.addVariableValue("Position",new StringVariableValue("Torso"));
//                IntervalVariableValue value = new IntervalVariableValue(260000.0, 15.0, false, true);
//                mapper.addVariableValue("Price", value);
//
//            }
//        });
    }

    public void sendQuery(VariableMapper mapper)
    {
        UIQuery query = new UIQuery(mapper);
        AgentQueryAnswers queryAnswers = answer(query);
        for(String agent : queryAnswers.getAgents())
        {
            System.out.println("received from " + agent);
            for(Product product: queryAnswers.getAnswer(agent).getProducts())
                product.displayProduct();
        }
        System.out.println("sending query ended");
        controller.showAnswer(queryAnswers);
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
        expert.getRuleBase().displayRules();
        System.out.println(query.getMapper());
        expert.forwardChain(query.getMapper());
        // get agents resulted from the forwardChain
        expert.getRuleBase().displayVariables();
        AgentVariableValue agvv = (AgentVariableValue) expert.getRuleBase().getVariableValue(agentVariableValue);
        if(agvv == null || agvv.getValue() == null)
            return new AgentQueryAnswers();
        int numberOfAgents = agvv.getValue().size();
        numberOfAgents = ruleInitializers.size() - numberOfAgents;
        System.out.println("passed here!! number of agents: "+numberOfAgents);
        for(FindAgentRuleInitializer init : ruleInitializers)
            System.out.println("rule init: " + init.getAgent() );


        // request from all possible agents
        for (String agentName : getAvailableAnnexAgents())
            if(!agvv.getValue().contains(agentName))
                requestAnswerFromAgent(agentName,query);
        // prepare message replier to "requestAnswer" conversation
        MessageTemplate template = MessageTemplate.MatchConversationId(AnnexAgent.requestAnswer);
        // what to do when the annex agent answers
        pauseReceiving();
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
        resumeReceiving();
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

    protected String[] getAvailableAnnexAgents()
    {
        String[] agents = new String[ruleInitializers.size()];
        int i =0;
        for(FindAgentRuleInitializer init : ruleInitializers)
            agents[i++] = init.getAgent();
        return agents;
    }

    public void prepareExpert()
    {
        expert.initRuleBase();
        System.out.println("number of inits: " + ruleInitializers.size());
        for(RuleInitializer ruleInitializer : ruleInitializers)
        {
            expert.addInitializer(ruleInitializer);
        }

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
