package BackEnd.ExpertSys;

import BackEnd.RuleVariable;
import BackEnd.Types.StringVariableValue;

import java.util.Scanner;

/**
 * Created by ressay on 05/04/18.
 */
public class AskUserConsole implements AskUserCallBack
{

    @Override
    public void askUser(RuleVariable ruleVariable) {
        // method callback when asking user
        // basically when we have a GUI change implementation here.
        //
        // that's how u don't mix front and back ends
        // so when u start working on GUI and u need live updates
        // create interfaces with callBack methods that display whatever u want
        // send them in parameter like this
        // you might want to create a class that contains all callBacks and send it
        // if you judge there are too many callbacks to be sent one by one
        String promptText = ruleVariable.getPromptText();
        String name = ruleVariable.getName();
        System.out.println(promptText);
        String answer = new Scanner(System.in).next(); // getting user input
        System.out.println("\n Looking for " + name + ". User entered: " + answer);
        ruleVariable.setValue(new StringVariableValue(answer));
    }
}
