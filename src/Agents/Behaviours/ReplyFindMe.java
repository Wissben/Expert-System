package Agents.Behaviours;

import Agents.CentralAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.Serializable;

/**
 * Created by ressay on 05/04/18.
 */
public abstract class ReplyFindMe extends MessageReceivingAction implements Serializable {

    public ReplyFindMe() {
        super(MessageTemplate.MatchConversationId(CentralAgent.findMeID));
    }

    public abstract void reply(String agentName, String message);

    public boolean action(ACLMessage msg) {
        if (msg != null) {
            reply(msg.getSender().getLocalName(), msg.getContent());
        }
        return false;
    }
}
