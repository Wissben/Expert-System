package Agents;

import Agents.Behaviours.MessageReceivingAction;
import Agents.Behaviours.ReplyFindMe;
import Agents.ExpertAgent.AnnexExpert;
import BackEnd.Database.FeaturesToQuerryConverter;
import BackEnd.Database.Product;
import BackEnd.Database.QueryAnswer;
import BackEnd.Database.QueryToProductsConverter;
import BackEnd.ExpertSys.VariableMapper;
import BackEnd.Types.IntegerValue;
import FrontEnd.Main;
import FrontEnd.UI.UIQuery;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;

/**
 * Created by ressay on 03/04/18.
 */
public class AnnexAgent extends MessageReceiverAgent
{
    private AnnexExpert expert;
    public static String annexSellService = "annexSeller";
    public static String requestAnswer = "requestAnswer";
    protected int id;

    public static AgentDescription newAgent(String name,int id, AnnexExpert expert)
    {
        Object[] arguments = new Object[2];
        arguments[0] = expert;
        arguments[1] = id;
        return new AgentDescription(name, AnnexAgent.class.getName(), arguments);
    }

    @Override
    public void setup()
    {

        // agents arguments
        readArguments();
        // generate infos of how to find this agent from a query
        RegistrationAgent.FindMeInfos infos =
                new RegistrationAgent.FindMeInfos(getLocalName(), expert.getFindMe());


        // register agent so it's infos can be available to other agents
        Registration reg = new Registration(this, infos, AnnexAgent.class.getName());
        reg.registerMe();
        // add service to DF
        addSelfToDF();


        // reply to agents requesting how to find this agent from a query
        addMessageAction(new ReplyFindMe()
        {
            @Override
            public void reply(String agentName, String message)
            {
                try
                {
                    sendFindMeMessage(agentName);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });

        // reply to agents sending a query
        MessageTemplate template = MessageTemplate.MatchConversationId(AnnexAgent.requestAnswer);
        addMessageAction(new MessageReceivingAction(template)
        {
            @Override
            public boolean action(ACLMessage msg)
            {
                try
                {
                    // get query from parameters
                    UIQuery query = (UIQuery) msg.getContentObject();
                    // send answer to query
                    sendAnswerToQuery(msg.getSender().getLocalName(), query);

                } catch (UnreadableException e)
                {
                    e.printStackTrace();
                }
                return false;
            }
        });

//        expert.tryForward();
    }

    public void sendAnswerToQuery(String requester, UIQuery query)
    {
        // get query answer from database
        QueryAnswer answer = queryDataBase(query);
        // send the answer
        ACLMessage m = new ACLMessage(ACLMessage.INFORM);
        m.addReceiver(new AID(requester, AID.ISLOCALNAME));
        m.setConversationId(AnnexAgent.requestAnswer);
        try
        {
            m.setContentObject(answer);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("annexAgent sending answer");
        send(m);
    }

    public QueryAnswer queryDataBase(UIQuery query)
    {
        // find product's features
        VariableMapper mapper = query.getMapper();
        mapper.getVariableValueHashMap().remove(CentralAgent.agentVariableValue);
        expert.initRuleBase();
        expert.forwardChain(mapper);
        // generate features from rule base after inference
        VariableMapper features = new VariableMapper(expert.getRuleBase());
        return queryDataBase(features);
    }

    public QueryAnswer queryDataBase(VariableMapper features)
    {
        features.addVariableValue("agentID",new IntegerValue(id));
        FeaturesToQuerryConverter featuresToQuerryConverter =
                new FeaturesToQuerryConverter(features);
        featuresToQuerryConverter.generateQuery();
        System.out.println(featuresToQuerryConverter.getDbQuery().getQuery());
        QueryToProductsConverter queryToProductsConverter =
                new QueryToProductsConverter(featuresToQuerryConverter);
        Product[] products = queryToProductsConverter.queryDB();
        /**Debugging
         for (int i = 0; i < products.length; i++)
         {
         products[i].displayProduct();
         }
         **/
        return new QueryAnswer(products);
    }

    public void readArguments()
    {
        Object[] args = getArguments();
        if (args != null)
        {
            expert = (AnnexExpert) args[0];
            id = (int) args[1];
        }

    }


    public void sendFindMeMessage(String agentName) throws IOException
    {
        ACLMessage m = new ACLMessage(ACLMessage.INFORM);
        m.addReceiver(new AID(agentName, AID.ISLOCALNAME));
        System.out.println("sending find me msg from " + getLocalName() + " to " + agentName);
        m.setConversationId(CentralAgent.findMeID);
        m.setContentObject(expert.getFindMe());
        send(m);
    }


    @SuppressWarnings("Duplicates")
    protected DFAgentDescription createMyDescription()
    {
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(annexSellService);
        sd.setName(getLocalName());
        dfd.addServices(sd);
        dfd.setName(getAID());
        return dfd;
    }

    protected void addSelfToDF()
    {
        try
        {
            DFService.register(this, createMyDescription());
        } catch (FIPAException fe)
        {
            fe.printStackTrace();
        }
    }


    protected void takeDown()
    {
        try
        {
            DFService.deregister(this);
        } catch (Exception e)
        {
        }
    }
}
