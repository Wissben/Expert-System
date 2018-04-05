package Agents;

import Agents.Behaviours.RegistrationWait;
import Agents.Behaviours.ReplyFindMe;
import Agents.ExpertAgent.AnnexExpert;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

/**
 * Created by ressay on 03/04/18.
 */
public class AnnexAgent extends MessageReceiverAgent
{
    private AnnexExpert expert;
    public static String annexSellService = "annexSeller";
    public static AgentDescription newAgent(String name,AnnexExpert expert)
    {
        Object[] arguments = new Object[1];
        arguments[0] = expert;
        return new AgentDescription(name,AnnexAgent.class.getName(),arguments);
    }

    @Override
    public void setup()
    {
        readArguments();
        RegistrationAgent.FindMeInfos infos =
                new RegistrationAgent.FindMeInfos(getLocalName(),expert.getFindMe());

        Registration reg = new Registration(this,infos,AnnexAgent.class.getName());
        reg.registerMe();

        addSelfToDF();
        addMessageAction(new ReplyFindMe() {
            @Override
            public void reply(String agentName, String message) {
                try {
                    sendFindMeMessage(agentName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    public void readArguments(){
        Object[] args = getArguments();
        if(args != null)
        {
            expert = (AnnexExpert) args[0];
        }

    }





    public void sendFindMeMessage(String agentName) throws IOException {
        ACLMessage m = new ACLMessage(ACLMessage.INFORM);
        m.addReceiver(new AID(agentName,AID.ISLOCALNAME));
        System.out.println("sending find me msg from "+getLocalName()+" to "+agentName);
        m.setConversationId(CentralAgent.findMeID);
        m.setContentObject(expert.getFindMe());
        send(m);
    }



    @SuppressWarnings("Duplicates")
    protected DFAgentDescription createMyDescription()
    {
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd  = new ServiceDescription();
        sd.setType( annexSellService );
        sd.setName( getLocalName() );
        dfd.addServices(sd);
        dfd.setName(getAID());
        return dfd;
    }

    protected void addSelfToDF()
    {
        try {
            DFService.register(this, createMyDescription() );
        }
        catch (FIPAException fe) { fe.printStackTrace(); }
    }



    protected void takeDown()
    {
        try { DFService.deregister(this); }
        catch (Exception e) {}
    }
}
