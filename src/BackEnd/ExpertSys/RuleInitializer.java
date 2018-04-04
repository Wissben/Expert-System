package BackEnd.ExpertSys;

import BackEnd.RuleBase;

/**
 * Created by ressay on 03/04/18.
 */
public abstract class RuleInitializer
{
    public void initRuleBase(RuleBase ruleBase)
    {
        initRuleBaseVariables(ruleBase);
        initRuleBaseRules(ruleBase);
    }
    protected abstract void initRuleBaseRules(RuleBase ruleBase);
    protected abstract void initRuleBaseVariables(RuleBase ruleBase);
}
