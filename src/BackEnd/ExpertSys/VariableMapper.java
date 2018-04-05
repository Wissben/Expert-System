package BackEnd.ExpertSys;

import BackEnd.RuleBase;
import BackEnd.Types.VariableValue;

import java.util.HashMap;

public class VariableMapper {

    private String[] variables;
    private HashMap<String, VariableValue> variableValueHashMap;

    public VariableMapper(RuleBase ruleBase) {
        this.variables = new String[ruleBase.getVariableList().size()];
        this.variableValueHashMap = new HashMap<>();
        this.setHashMap(ruleBase);
    }

    private void setHashMap(RuleBase ruleBase)
    {
        for (Object key : ruleBase.getVariableList().keySet()) {
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

}
