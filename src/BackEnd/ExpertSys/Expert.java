package BackEnd.ExpertSys;

import BackEnd.RuleBase;
import BackEnd.Types.StringVariableValue;

/**
 * Created by ressay on 03/04/18.
 */
public class Expert
{
    RuleBase ruleBase;

    public Expert(RuleInitializer initializer,AskUserCallBack userAsker)
    {
        ruleBase = new RuleBase("RuleBaseTest", userAsker);
        initializer.initRuleBase(ruleBase);
    }

    // TODO to be removed!!
    public void tryForward()
    {
        ruleBase.setVariableValue("Position",new StringVariableValue("Torso"));
        ruleBase.setVariableValue("SleeveLength",new StringVariableValue("Short"));
        ruleBase.setVariableValue("Season",new StringVariableValue("Hto"));
        ruleBase.setVariableValue("Material",new StringVariableValue("Cotton"));
        ruleBase.forwardChain();
        ruleBase.displayVariables();
        ruleBase.displayRules();

    }

    public void tryBackWard()
    {
        ruleBase.setVariableValue("Cloth",new StringVariableValue("Tshirt"));
        ruleBase.backwardChain("Cloth");
        ruleBase.displayVariables();
    }

}
