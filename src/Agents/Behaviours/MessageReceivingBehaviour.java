package Agents.Behaviours;

import Agents.CentralAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.Serializable;
import java.util.Iterator;
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

    public boolean containsMessageAction(MessageReceivingAction messageAction)
    {
        return actions.contains(messageAction);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if(msg!=null)
        {
            Iterator<MessageReceivingAction> iter = actions.iterator();
            while (iter.hasNext()) {
                MessageReceivingAction action = iter.next();
                if (action.getTemplate() == null || action.getTemplate().match(msg))
                    if (action.action(msg))
                        removeAction(action);
            }
        }
    }
}
