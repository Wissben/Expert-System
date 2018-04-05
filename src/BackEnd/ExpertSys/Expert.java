package BackEnd;

import BackEnd.Initializers.RuleInitializer;
import BackEnd.Types.DoubleValue;
import BackEnd.Types.IntegerValue;
import BackEnd.Types.StringVariableValue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by ressay on 03/04/18.
 */
public class Expert
{
    RuleBase ruleBase;

    public Expert(RuleInitializer initializer, AskUserCallBack userAsker) throws NoSuchMethodException, IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        ruleBase = new RuleBase("RuleBaseTest", userAsker);
        initializer.initRuleBase(ruleBase);
    }

    // TODO to be removed!!
    public void tryForward()
    {
        ruleBase.setVariableValue("Position",new StringVariableValue("Torso"));
        ruleBase.setVariableValue("SleeveLength",new StringVariableValue("Short"));
        ruleBase.setVariableValue("Season",new StringVariableValue("Hot"));
        ruleBase.setVariableValue("Material",new StringVariableValue("Cotton"));
        ruleBase.setVariableValue("Price",new DoubleValue(20.0));
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
