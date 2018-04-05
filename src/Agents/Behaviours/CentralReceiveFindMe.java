package Agents.Behaviours;

import Agents.CentralAgent;
import Agents.ExpertAgent.FindAgentRuleInitializer;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.Serializable;

/**
 * Created by ressay on 05/04/18.
 */
public class CentralReceiveFindMe extends MessageReceivingAction implements Serializable {

    CentralAgent agent;

    public CentralReceiveFindMe(CentralAgent agent) {
        super(MessageTemplate.MatchConversationId(CentralAgent.findMeID));
        this.agent = agent;
    }


    public boolean action(ACLMessage msg) {
        if (msg != null) {
            try {
                FindAgentRuleInitializer findMe = (FindAgentRuleInitializer) msg.getContentObject();
                if(findMe != null)
                    agent.addRuleInitializer(findMe);
                System.out.println("find me from " + msg.getSender().getLocalName());
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        }
        return false; // keep action going
    }
}
