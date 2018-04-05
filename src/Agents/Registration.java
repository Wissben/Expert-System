package Agents;

import Agents.Behaviours.RegistrationWait;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by ressay on 05/04/18.
 */
public class Registration
{

    MessageReceiverAgent agent;
    RegistrationMessage content;
    public static class RegistrationMessage implements Serializable
    {
        Serializable content;
        String type;

        public RegistrationMessage(Serializable content, String type) {
            this.content = content;
            this.type = type;
        }

        public Serializable getContent() {
            return content;
        }

        public String getType() {
            return type;
        }
    }

    public Registration(MessageReceiverAgent agent, Serializable content,String type) {
        this.agent = agent;
        this.content = new RegistrationMessage(content,type);
    }

    public void registerMe()
    {
        DFAgentDescription desc = findRegistrar();


        if(desc != null)
            try {
                sendMessageToRegistrar(desc.getName().getLocalName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        else
            subscribeAgentToDF();
    }

    public void sendMessageToRegistrar(String registrarName) throws IOException {
        ACLMessage m = new ACLMessage(ACLMessage.INFORM);
        m.addReceiver(new AID(registrarName,AID.ISLOCALNAME));
        m.setConversationId(RegistrationAgent.registerService);
        m.setContentObject(content);
        agent.send(m);
    }

    public DFAgentDescription findRegistrar()
    {
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd  = new ServiceDescription();
        sd.setType( RegistrationAgent.registerService );
        dfd.addServices(sd);
        DFAgentDescription[] result = null;
        try {
            result = DFService.search(agent, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        if(result == null || result.length < 1)
            return null;
        return result[0];
    }

    public void subscribeAgentToDF()
    {
        agent.addMessageAction(new RegistrationWait(agent) {
            @Override
            public void register(String registrarName) {
                try {
                    sendMessageToRegistrar(registrarName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(RegistrationAgent.registerService);
        dfd.addServices(sd);
        SearchConstraints sc = new SearchConstraints();
        sc.setMaxResults(1L);
        agent.send(DFService.createSubscriptionMessage(agent, agent.getDefaultDF(),
                dfd, sc));
    }
}
