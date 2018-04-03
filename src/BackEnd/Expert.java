package BackEnd;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by ressay on 03/04/18.
 */
public class Expert
{
    RuleBase ruleBase;

    public Expert(RuleInitializer initializer)
    {
        ruleBase = new RuleBase("RuleBaseTest", params -> {
            // method callback when asking user, basically when we have a GUI
            // we use it here.
            String promptText = params[0];
            String name = params[1];
            System.out.println(promptText);
            String answer = new Scanner(System.in).next(); // getting user input
            System.out.println("\n  !!! Looking for " + name + ". User entered: " + answer);
            return answer;
        });
        initializer.initRuleBase(ruleBase);
    }

    // TODO to be removed!!
    public void tryForward()
    {
        ruleBase.setVariableValue("Position","Torso");
        ruleBase.setVariableValue("SleeveLength","Short");
        ruleBase.setVariableValue("Season","Hto");
        ruleBase.setVariableValue("Material","Cotton");
        ruleBase.forwardChain();
        ruleBase.displayVariables();
    }

    public void tryBackWard()
    {
        ruleBase.setVariableValue("Cloth","Tshirt");
        ruleBase.backwardChain("Cloth");
        ruleBase.displayVariables();
    }

}
