package FrontEnd;

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

import java.util.Scanner;

public class Main
{

    public static DBconnection dBconnection;
    public static RuleBaseToTableConverter allArtcilesTableGenerator;
    public static String varsPath = "/home/wiss/CODES/TP-AGENT/Expert-System/src/ruleVariables";
    public static String rulesPath = "/home/wiss/CODES/TP-AGENT/Expert-System/src/rules";
    //    static void tryIntervals() throws ConflictException {
//        IntegerValue val = new IntegerValue(15);
//
//        IntegerValue val1 = new IntegerValue(18);
//        IntervalUnion<Integer> full = new IntervalUnion<>(new Interval<>());
//        IntervalVariableValue<Integer> halfd = new IntervalVariableValue<>(130,5,true,true);
//        IntervalVariableValue<Integer> halfu = new IntervalVariableValue<>(120,5,true,true);
//        halfd.affect(new Condition("="),val1);
//        System.out.println(halfd.getValue());
//        System.out.println(halfu.getValue());
//        System.out.println("intersection: " + halfd.getValue().intersects(halfu.getValue()));
//        System.out.println("union: " + halfd.getValue().union(halfu.getValue()));
//        System.out.println("remove: " + halfd.getValue().remove(halfu.getValue()));
//        full = full.remove(val.getValue());
//        System.out.println("val : " + val + " val1 " + val1 + " full " + full);
//        System.out.println(halfd.isMoreThan(halfu) + " " + halfd.isLessThan(halfu) + " " +halfd.equals(halfu));
//    }
    public static boolean debugging = false;

    public static void main(String[] args) throws Exception {
        ContainerManager m = new ContainerManager();
        AnnexExpert expert = new AnnexExpert(SimpleClothRulesInit.generateRuleBaseFromFiles(varsPath,rulesPath), new AskUserConsole()
                , new FindAgentRuleInitializer1("/home/wiss/CODES/TP-AGENT/Expert-System/src/ruleFindMe1"));
        AnnexExpert expert2 = new AnnexExpert(SimpleClothRulesInit.generateRuleBaseFromFiles(varsPath,rulesPath), new AskUserConsole()
                , new FindAgentRuleInitializer1("/home/wiss/CODES/TP-AGENT/Expert-System/src/ruleFindMe2"));

        initDatabase();
        allArtcilesTableGenerator = new RuleBaseToTableConverter(expert.getRuleBase(), "test", dBconnection);
        allArtcilesTableGenerator.createTableQuery();
//        allArtcilesTableGenerator.getDbQuery().executeUpdateQuery();

        m.addAgent(RegistrationAgent.newAgent("reg1")).start();
        m.addAgent(AnnexAgent.newAgent("agent1",expert)).start();
        m.addAgent(AnnexAgent.newAgent("agent2",expert2)).start();
        Thread.sleep(100);
        m.addAgent(CentralAgent.newAgent("central1",new Expert())).start();

        Scanner s = new Scanner(System.in);
        int x;
        do {
            x = s.nextInt();
            if(x > 0)
                m.addAgent(AnnexAgent.newAgent("agent"+x,expert)).start();
            if(x < 0)
                m.addAgent(CentralAgent.newAgent("central"+x,expert)).start();
        }while (x != 0);
    }
    public static void print(String str)
    {
        if(debugging)
            System.out.println(str);
    }

    private static void initDatabase()
    {
        dBconnection = new DBconnection("jdbc:mysql://localhost:3306/",
                "root",
                "wissben69",
                "TechAgent");
    }

}
