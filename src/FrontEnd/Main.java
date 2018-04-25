package FrontEnd;

import Agents.AnnexAgent;
import Agents.AnnexVendors.FindAgentRuleInitializer1;
import Agents.CentralAgent;
import Agents.ExpertAgent.AnnexExpert;
import Agents.RegistrationAgent;
import BackEnd.Condition;
import BackEnd.Database.DBconnection;
import BackEnd.Database.RuleBaseToTableConverter;
import BackEnd.ExpertSys.AskUserConsole;
import BackEnd.ExpertSys.Expert;
import BackEnd.Initializers.SimpleClothRulesInit;
import BackEnd.Types.*;
import Environment.ContainerManager;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static DBconnection dBconnection;
    public static RuleBaseToTableConverter allArtcilesTableGenerator;
    public static String varsPath = "ruleVariables";
    public static String rulesPath = "rules";
    public static AttributeChoice attributeController = null;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AttributeChoice.fxml"));
        Parent root = fxmlLoader.load();
        attributeController = fxmlLoader.getController();
        if(attributeController != null)
            System.out.println("it's not null");
        else
            System.out.println("it's null");
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        startAgents();
    }

    public static AttributeChoice getAttributeController()
    {
        return attributeController;
    }

    public static void main(String[] args) throws Exception {
//
        initDatabase();

//
//        Thread.sleep(100);
//        m.addAgent(CentralAgent.newAgent("central1",new Expert())).start();
//
//        Scanner s = new Scanner(System.in);
//        int x;
//        do {
//            x = s.nextInt();
//            if(x > 0)
//                m.addAgent(AnnexAgent.newAgent("agent"+x,expert)).start();
//            if(x < 0)
//                m.addAgent(CentralAgent.newAgent("central"+x,expert)).start();
//        }while (x != 0);
        launch(args);
    }

    static void startAgents() throws StaleProxyException
    {
        ContainerManager m = new ContainerManager();
        String varsPath = "src/ruleVariables";
        String rulesPath = "src/rules";
        AnnexExpert expert = new AnnexExpert(SimpleClothRulesInit.generateRuleBaseFromFiles(varsPath,rulesPath), new AskUserConsole()
                , new FindAgentRuleInitializer1("src/ruleFindMe1","agent1"));
        AnnexExpert expert2 = new AnnexExpert(SimpleClothRulesInit.generateRuleBaseFromFiles(varsPath,rulesPath), new AskUserConsole()
                , new FindAgentRuleInitializer1("src/ruleFindMe2","agent2"));
        Expert central = new Expert();

        m.addAgent(RegistrationAgent.newAgent("reg1")).start();
        m.addAgent(AnnexAgent.newAgent("agent1",1,expert)).start();
        m.addAgent(AnnexAgent.newAgent("agent2",2,expert2)).start();
        m.addAgent(CentralAgent.newAgent("central",central)).start();
    }

    static void tryIntervals() throws ConflictException {
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
    public static boolean debugging = false;
    public static void print(String str)
    {
        if(debugging)
            System.out.println(str);
    }

    private static void initDatabase()
    {
        dBconnection = new DBconnection("jdbc:mysql://localhost:3306/",
                "root",
                "Resssay95",
                "TechAgent");
    }

    public static DBconnection getdBconnection()
    {
        if(dBconnection == null)
            initDatabase();
        return dBconnection;
    }

}
