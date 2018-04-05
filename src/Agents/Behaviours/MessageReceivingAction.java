package Agents.Behaviours;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Created by ressay on 05/04/18.
 */
public abstract class MessageReceivingAction
{
    protected MessageTemplate template;

    public MessageReceivingAction(MessageTemplate template) {
        this.template = template;
    }

    public abstract boolean action(ACLMessage msg);

    public MessageTemplate getTemplate() {
        return template;
    }
}
