package Agents;

import BackEnd.ExpertSys.Expert;
import jade.core.Agent;

/**
 * Created by ressay on 03/04/18.
 */
public class CentralAgent extends Agent
{
    Expert expert;

    public static AgentDescription newAgent(String name,Expert expert)
    {
        Object[] arguments = new Object[1];
        arguments[0] = expert;
        return new AgentDescription(name,"Agents.CentralAgent",arguments);
    }

    @Override
    public void setup()
    {

        Object[] args = getArguments();
        if(args != null)
        {
            expert = (Expert) args[0];
        }
        expert.tryForward();
    }
}
