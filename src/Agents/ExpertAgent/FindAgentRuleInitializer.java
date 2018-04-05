package Agents.ExpertAgent;

import BackEnd.Initializers.*;

import java.io.Serializable;

/**
 * Created by ressay on 05/04/18.
 */
public abstract class FindAgentRuleInitializer extends RuleInitializer implements Serializable
{
    // The agent that the rule set allows you to find
    AgentVariableValue agent;

    public FindAgentRuleInitializer()
    {

    }

    public FindAgentRuleInitializer(AgentVariableValue agent) {
        this.agent = agent;
    }
}
