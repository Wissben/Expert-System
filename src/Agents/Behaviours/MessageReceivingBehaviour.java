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
    protected boolean paused = false;

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
        if(paused)return;
        ACLMessage msg = myAgent.receive();
        if(msg!=null)
        {
            for (int i = 0; i < actionsSize(); i++)
            {
                MessageReceivingAction action = getAction(i);
                if (action.getTemplate() == null || action.getTemplate().match(msg))
                    if (action.action(msg))
                        removeAction(action);
            }
        }
    }
    synchronized protected MessageReceivingAction getAction(int index)
    {
        return actions.get(index);
    }

    synchronized protected int actionsSize()
    {
        return actions.size();
    }

    public void pause()
    {
        paused = true;
    }

    public void resume()
    {
        paused = false;
    }
}

