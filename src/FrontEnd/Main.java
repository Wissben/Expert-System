package FrontEnd;

import BackEnd.Condition;
import BackEnd.ExpertSys.Expert;
import BackEnd.ExpertSys.SimpleClothRulesInit;
import BackEnd.Types.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) throws Exception {
//        launch(args);
//        ContainerManager m = new ContainerManager();
        Expert expert = new Expert(new SimpleClothRulesInit(), ruleVariable -> {
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
        });
//        m.addAgent(AnnexAgent.newAgent("agent1",expert)).start();
        IntegerValue val = new IntegerValue(15);

        IntegerValue val1 = new IntegerValue(18);
        IntervalUnion<Integer> full = new IntervalUnion<>(new Interval<>());
        IntervalVariableValue<Integer> halfd = new IntervalVariableValue<>(130,5,true,true);
        IntervalVariableValue<Integer> halfu = new IntervalVariableValue<>(120,5,true,true);
        halfd.affect(new Condition("="),val1);
        System.out.println(halfd.getValue());
        System.out.println(halfu.getValue());
        System.out.println("intersection: " + halfd.getValue().intersects(halfu.getValue()));
        System.out.println("union: " + halfd.getValue().union(halfu.getValue()));
        System.out.println("remove: " + halfd.getValue().remove(halfu.getValue()));
        full = full.remove(val.getValue());
        System.out.println("val : " + val + " val1 " + val1 + " full " + full);
        System.out.println(halfd.isMoreThan(halfu) + " " + halfd.isLessThan(halfu) + " " +halfd.equals(halfu));
    }
}
