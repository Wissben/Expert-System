package Agents;

import Agents.Behaviours.MessageReceivingAction;
import Agents.Behaviours.MessageReceivingBehaviour;
import Agents.Behaviours.ReplyFindMe;
import Agents.ExpertAgent.FindAgentRuleInitializer;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by ressay on 05/04/18.
 */
public class RegistrationAgent extends MessageReceiverAgent
{
    public static String registerService = "Registrar";
    public static AgentDescription newAgent(String name)
    {
        Object[] arguments = null;
        return new AgentDescription(name,RegistrationAgent.class.getName(),arguments);
    }

    public static class FindMeInfos implements Serializable
    {
        String agentName = "";
        FindAgentRuleInitializer findMe = null;

        public FindMeInfos() {
        }

        public FindMeInfos(String name, FindAgentRuleInitializer findMe) {
            this.agentName = name;
            this.findMe = findMe;
        }
    }

    protected HashMap<String,FindAgentRuleInitializer> agentsFindMes = new HashMap<>();
    protected LinkedList<AID> centralAgents = new LinkedList<>();


    @Override
    public void setup()
    {
        addSelfToDF();

        addMessageAction(new MessageReceivingAction(MessageTemplate.MatchConversationId(registerService)) {
            @Override
            public boolean action(ACLMessage msg) {
                if(msg != null)
                    receivedMessageCallBack(msg);
                return false;
            }
        });

        addMessageAction(new ReplyFindMe() {
            @Override
            public void reply(String agentName, String message) {
                try {
                    sendFindMeMessage(message,agentName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    public void receivedMessageCallBack(ACLMessage msg)
    {
        System.out.println("is adding");
        try {
            Registration.RegistrationMessage content = (Registration.RegistrationMessage) msg.getContentObject();
            String type = content.getType();

            if(type.compareTo(AnnexAgent.class.getName()) == 0) { // add annex agent
                FindMeInfos infos = (FindMeInfos) content.getContent();
                addAgent(infos);
            }
            else if(type.compareTo(CentralAgent.class.getName()) == 0) // add central agent
                addCentralAgent(msg.getSender());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void addCentralAgent(AID agent)
    {
        centralAgents.add(agent);
        System.out.println("added central agent: " + agent.getLocalName());
    }

    public void addAgent(FindMeInfos infos) throws IOException {
        agentsFindMes.put(infos.agentName,infos.findMe);
        System.out.println("added annex agent");
        sendFindMeToAllCentralAgents(infos.agentName);
    }

    public FindAgentRuleInitializer getAgentsFindMe(String agentName)
    {
        return agentsFindMes.get(agentName);
    }

    public void sendFindMeMessage(String agentName,String receiverName) throws IOException {
        ACLMessage m = new ACLMessage(ACLMessage.INFORM);
        m.addReceiver(new AID(receiverName,AID.ISLOCALNAME));
        m.setConversationId(CentralAgent.findMeID);
        m.setContentObject(getAgentsFindMe(agentName));
        send(m);
    }

    public void sendFindMeToAllCentralAgents(String agentName) throws IOException {
        for(AID receiver : centralAgents)
            sendFindMeMessage(agentName,receiver.getLocalName());
    }

    @SuppressWarnings("Duplicates")
    protected DFAgentDescription createMyDescription()
    {
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd  = new ServiceDescription();
        sd.setType( registerService );
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