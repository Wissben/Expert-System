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
    public static String varsPath = "src/ruleVariables";
    public static String rulesPath = "src/rules";
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
        startCentral();
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
    static ContainerManager m = new ContainerManager();
    public static void startAgents()
    {
        try
        {
            String varsPath = "src/ruleVariables";
            String rulesPath = "src/rules";
            AnnexExpert expert = new AnnexExpert(SimpleClothRulesInit.generateRuleBaseFromFiles(varsPath, rulesPath), new AskUserConsole()
                    , new FindAgentRuleInitializer1("src/ruleFindMe1", "agent1"));
            AnnexExpert expert2 = new AnnexExpert(SimpleClothRulesInit.generateRuleBaseFromFiles(varsPath, rulesPath), new AskUserConsole()
                    , new FindAgentRuleInitializer1("src/ruleFindMe2", "agent2"));
            AnnexExpert expert3 = new AnnexExpert(SimpleClothRulesInit.generateRuleBaseFromFiles(varsPath, rulesPath), new AskUserConsole()
                    , new FindAgentRuleInitializer1("src/ruleFindMe3", "agent3"));
            AnnexExpert expert4 = new AnnexExpert(SimpleClothRulesInit.generateRuleBaseFromFiles(varsPath, rulesPath), new AskUserConsole()
                    , new FindAgentRuleInitializer1("src/ruleFindMe4", "agent4"));
            AnnexExpert expert5 = new AnnexExpert(SimpleClothRulesInit.generateRuleBaseFromFiles(varsPath, rulesPath), new AskUserConsole()
                    , new FindAgentRuleInitializer1("src/ruleFindMe5", "agent5"));


            m.addAgent(RegistrationAgent.newAgent("reg1")).start();
            m.addAgent(AnnexAgent.newAgent("agent1", 1, expert)).start();
            m.addAgent(AnnexAgent.newAgent("agent2", 2, expert2)).start();
            m.addAgent(AnnexAgent.newAgent("agent3", 3, expert3)).start();
            m.addAgent(AnnexAgent.newAgent("agent4", 4, expert4)).start();
            m.addAgent(AnnexAgent.newAgent("agent5", 5, expert5)).start();
        }catch (Exception e)
        {

        }
    }

    static void startCentral() throws StaleProxyException
    {
        Expert central = new Expert();
        m.addAgent(CentralAgent.newAgent("central",central)).start();
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
                "TechAgent2");
    }

    public static DBconnection getdBconnection()
    {
        if(dBconnection == null)
            initDatabase();
        return dBconnection;
    }

}
