package Agents;

import Agents.Behaviours.CentralReceiveFindMe;
import Agents.Behaviours.MessageReceivingBehaviour;
import Agents.ExpertAgent.FindAgentRuleInitializer;
import BackEnd.ExpertSys.Expert;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.LinkedList;

/**
 * Created by ressay on 03/04/18.
 */
public class CentralAgent extends MessageReceiverAgent
{
    Expert expert;
    public static String findMeID = "findMe";
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
