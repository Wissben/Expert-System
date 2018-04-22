package Agents.AnnexVendors;

import Agents.ExpertAgent.FindAgentRuleInitializer;
import BackEnd.RuleBase;
import BackEnd.RuleParser;
import BackEnd.VariableParser;
import FrontEnd.Main;

import java.io.IOException;

/**
 * Created by ressay on 07/04/18.
 */
public class FindAgentRuleInitializer1 extends FindAgentRuleInitializer
{
    String path;

    public FindAgentRuleInitializer1(String path) {
        this.path = path;
    }

    @Override
    protected void initRuleBaseRules(RuleBase ruleBase) {
        try {
            RuleParser ruleParser = new RuleParser(path);
            ruleParser.startParsing(ruleBase);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initRuleBaseVariables(RuleBase ruleBase) {
        try {
            VariableParser variableParser = new VariableParser(Main.varsPath);
            variableParser.startParsing(ruleBase);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
