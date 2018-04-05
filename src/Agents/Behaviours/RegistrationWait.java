package Agents.Behaviours;

import Agents.RegistrationAgent;
import jade.core.Agent;
import jade.core.Service;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.Serializable;

/**
 * Created by ressay on 05/04/18.
 */
public abstract class RegistrationWait extends MessageReceivingAction implements Serializable
{


    public abstract void register(String registrarName);

    public RegistrationWait(Agent agent) {
        super(MessageTemplate.MatchSender(agent.getDefaultDF()));
    }

    public boolean action(ACLMessage msg)
    {
        if (msg != null)
        {
            try {
                DFAgentDescription[] dfds =
                        DFService.decodeNotification(msg.getContent());
                if (dfds.length > 0)
                {
                    String name = dfds[0].getName().getLocalName();
                    register(name);
                    return true;
                }
            }
            catch (Exception ex) {}
        }
        return false;
    }

}