package BackEnd;

import BackEnd.Types.VariableValue;

import java.util.HashMap;

public class VariableMapper {

    private String[] variables;
    private HashMap<String, VariableValue> variableValueHashMap;
    private RuleBase ruleBase;

    public VariableMapper(RuleBase ruleBase) {
        this.ruleBase=ruleBase;
        this.variables = new String[this.ruleBase.getVariableList().size()];
        this.variableValueHashMap = new HashMap<>();
        this.setHashMap();
    }

    private void setHashMap()
    {
        for (Object key : this.ruleBase.getVariableList().keySet()) {
            this.variableValueHashMap.put((String)key, (VariableValue) ruleBase.getVariableList().get(key));
        }
    }

    public String[] getVariables() {
        return variables;
    }

    public void setVariables(String[] variables) {
        this.variables = variables;
    }

    public HashMap<String, VariableValue> getVariableValueHashMap() {
        return variableValueHashMap;
    }

    public void setVariableValueHashMap(HashMap<String, VariableValue> variableValueHashMap) {
        this.variableValueHashMap = variableValueHashMap;
    }

    public RuleBase getRuleBase() {
        return ruleBase;
    }

    public void setRuleBase(RuleBase ruleBase) {
        this.ruleBase = ruleBase;
    }
}
