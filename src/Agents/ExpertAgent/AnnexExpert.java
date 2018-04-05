package Agents.ExpertAgent;

import BackEnd.ExpertSys.AskUserCallBack;
import BackEnd.ExpertSys.Expert;
import BackEnd.Initializers.RuleInitializer;

import java.io.Serializable;

/**
 * Created by ressay on 05/04/18.
 */
public class AnnexExpert extends Expert implements Serializable
{
    // a set of rules that helps the Central Agent to find out if this expert
    // can answer the received query
    protected FindAgentRuleInitializer findMe = null;

    public AnnexExpert(RuleInitializer initializer, AskUserCallBack userAsker,
                       FindAgentRuleInitializer findMe) {
        super(initializer, userAsker);
        this.findMe = findMe;
    }

    public AnnexExpert(RuleInitializer initializer, AskUserCallBack userAsker) {
        super(initializer, userAsker);
    }



    public FindAgentRuleInitializer getFindMe()
    {
        return findMe;
    }

    public void setFindMe(FindAgentRuleInitializer findMe)
    {
        this.findMe = findMe;
    }
}
