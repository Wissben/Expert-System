package Agents.Behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ressay on 05/04/18.
 */
public class MessageReceivingBehaviour extends CyclicBehaviour implements Serializable
{
    List<MessageReceivingAction> actions = new LinkedList<>();

    public void addAction(MessageReceivingAction action)
    {
        actions.add(action);
    }

    public void removeAction(MessageReceivingAction action)
    {
        actions.remove(action);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.blockingReceive();
        for (MessageReceivingAction action : actions)
        {
            if(action.getTemplate() == null || action.getTemplate().match(msg))
                if(action.action(msg))
                    removeAction(action);
        }
    }
}
