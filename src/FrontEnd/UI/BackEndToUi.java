package FrontEnd.UI;

import Agents.AnnexAgent;
import Agents.AnnexVendors.FindAgentRuleInitializer1;
import Agents.CentralAgent;
import Agents.ExpertAgent.AnnexExpert;
import Agents.RegistrationAgent;
import BackEnd.Database.DBconnection;
import BackEnd.Database.RuleBaseToTableConverter;
import BackEnd.ExpertSys.AskUserConsole;
import BackEnd.ExpertSys.Expert;
import BackEnd.Initializers.SimpleClothRulesInit;
import Environment.ContainerManager;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;

/**
 * CREATED BY wiss ON 19:54 INSIDE FrontEnd.UI FOR THE PROJECT Expert-System
 **/

public class BackEndToUi
{
    public static DBconnection dBconnection;
    public static RuleBaseToTableConverter allArtcilesTableGenerator;
    public ArrayList<AnnexExpert> experts;
    public String varsPath = "/home/wiss/CODES/TP-AGENT/Expert-System/src/ruleVariables";
    public String rulesPath = "/home/wiss/CODES/TP-AGENT/Expert-System/src/rules";
    private Controller controller;

    public BackEndToUi(Controller controller)
    {
        this.controller = controller;
        this.experts = new ArrayList<>();
    }

    private static void initDatabase()
    {
        dBconnection = new DBconnection("jdbc:mysql://localhost:3306/",
                "root",
                "wissben69",
                "TechAgent");
    }

    public void initAgents() throws StaleProxyException, InterruptedException
    {
        initDatabase();
        ContainerManager m = new ContainerManager();
//        addExpert("ruleFindMe1",varsPath,rulesPath);
//        addExpert("ruleFindMe2",varsPath,rulesPath);
        allArtcilesTableGenerator = RuleBaseToTableConverter.getInstance();
        allArtcilesTableGenerator.createTableQuery();
//        allArtcilesTableGenerator.getDbQuery().executeCreateQuery();

        m.addAgent(RegistrationAgent.newAgent("reg1")).start();
//        m.addAgent(AnnexAgent.newAgent("agent1", experts.get(0))).start();
//        m.addAgent(AnnexAgent.newAgent("agent2", experts.get(1))).start();
        Thread.sleep(100);
        m.addAgent(CentralAgent.newAgent("central1", new Expert())).start();

    }

//    public void addExpert(String ruleFindMe, String varsPath, String rulesPath)
//    {
//
//        this.experts.add(new AnnexExpert(SimpleClothRulesInit.generateRuleBaseFromFiles(varsPath, rulesPath), new AskUserConsole()
//                , new FindAgentRuleInitializer1(ruleFindMe)));
//
//    }
}
