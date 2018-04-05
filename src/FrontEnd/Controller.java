package FrontEnd;

import BackEnd.*;
import BackEnd.Initializers.SimpleClothRulesInit;
import BackEnd.Types.StringVariableValue;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {


    private Expert expert; // trust me

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            expert = new Expert(new SimpleClothRulesInit(), ruleVariable -> {
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
                System.out.println("\n  !!! Looking for " + name + ". User entered: " + answer);
                ruleVariable.setValue(new StringVariableValue(answer));
            });
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        expert.tryForward();
    }
}
