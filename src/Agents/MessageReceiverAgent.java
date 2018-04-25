package Agents;

import Agents.Behaviours.MessageReceivingAction;
import Agents.Behaviours.MessageReceivingBehaviour;
import jade.core.Agent;

/**
 *
 * Created by ressay on 05/04/18.
 */
public class MessageReceiverAgent extends Agent
{
    protected MessageReceivingBehaviour messageBehaviour;

    public MessageReceivingBehaviour getMessageBehaviourInstance()
    {
        if(messageBehaviour == null) {
            messageBehaviour = new MessageReceivingBehaviour();
            addBehaviour(messageBehaviour);
        }
        return messageBehaviour;
    }

    public void addMessageAction(MessageReceivingAction messageAction)
    {
        getMessageBehaviourInstance().addAction(messageAction);
    }

    public void removeMessageAction(MessageReceivingAction messageAction)
    {
        getMessageBehaviourInstance().removeAction(messageAction);
    }

    public boolean containsMessageAction(MessageReceivingAction messageAction)
    {
        return getMessageBehaviourInstance().containsMessageAction(messageAction);
    }

    public void pauseReceiving()
    {
        getMessageBehaviourInstance().pause();
    }

    public void resumeReceiving()
    {
        getMessageBehaviourInstance().resume();
    }
}
