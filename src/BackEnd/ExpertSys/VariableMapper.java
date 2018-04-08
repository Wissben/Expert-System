package BackEnd.ExpertSys;

import BackEnd.RuleBase;
import BackEnd.Types.VariableValue;

import java.io.Serializable;
import java.util.HashMap;

public class VariableMapper implements Serializable
{


    private HashMap<String, VariableValue> variableValueHashMap;

    public VariableMapper(RuleBase ruleBase) {
        this.variableValueHashMap = new HashMap<>();
        this.setHashMap(ruleBase);
    }

    public VariableMapper()
    {
        this.variableValueHashMap = new HashMap<>();
    }

    private void setHashMap(RuleBase ruleBase)
    {
        for (String key : ruleBase.getVariableList().keySet()) {
            this.variableValueHashMap.put(key,ruleBase.getVariableValue(key));
        }
    }

    public void initRuleBase(RuleBase ruleBase)
    {
        for (String key: variableValueHashMap.keySet())
            ruleBase.setVariableValue(key,variableValueHashMap.get(key));
    }

    public String[] getVariables()
    {
        return (String[]) variableValueHashMap.keySet().toArray();
    }

    public void addVariableValue(String variable, VariableValue value)
    {
        variableValueHashMap.put(variable,value);
    }

    public VariableValue getVariableValue(String variable)
    {
        return variableValueHashMap.get(variable);
    }

    public HashMap<String, VariableValue> getVariableValueHashMap() {
        return variableValueHashMap;
    }

    public void setVariableValueHashMap(HashMap<String, VariableValue> variableValueHashMap) {
        this.variableValueHashMap = variableValueHashMap;
    }

    public void clear()
    {
        variableValueHashMap.clear();
    }

}
