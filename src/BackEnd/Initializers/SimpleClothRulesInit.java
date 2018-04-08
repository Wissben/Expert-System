package BackEnd.Initializers;

import BackEnd.Condition;
import BackEnd.Database.DBconnection;
import BackEnd.RuleBase;
import BackEnd.RuleParser;
import BackEnd.VariableParser;

import java.io.IOException;

/**
 * a class to add rules to a rule base
 * Created by ressay on 03/04/18.
 */
public class SimpleClothRulesInit extends RuleInitializer {

    private DBconnection dbConnection;
    protected static Condition cEquals = new Condition("=");
    protected static Condition cNotEquals = new Condition("!=");
    protected static Condition cLessThan = new Condition("<");
    protected RuleParser ruleParser;
    protected VariableParser variableParser;


    public static SimpleClothRulesInit generateRuleBaseFromFiles(String pathToVariablesFile,String pathToRulesFile)
    {
     SimpleClothRulesInit simpleClothRulesInit = new SimpleClothRulesInit();
        try {
            simpleClothRulesInit.ruleParser = new RuleParser(pathToRulesFile);
            simpleClothRulesInit.variableParser =new VariableParser(pathToVariablesFile);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return simpleClothRulesInit;
    }

    @Override
    protected void initRuleBaseRules(RuleBase ruleBase) {
        if(ruleParser!=null) {
            ruleParser.startParsing(ruleBase);
        }
        else
            throw new java.lang.RuntimeException("ruleParser NullPointer");
    }

    @Override
    protected void initRuleBaseVariables(RuleBase ruleBase) {
        if(variableParser!=null) {
            variableParser.startParsing(ruleBase);
        }
        else
            throw new java.lang.RuntimeException("variableParser NullPointer");
    }


    private void setupDBconnection()
    {

    }
}
