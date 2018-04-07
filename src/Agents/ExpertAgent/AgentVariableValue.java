package Agents.ExpertAgent;

import BackEnd.Condition;
import BackEnd.Types.ConflictException;
import BackEnd.Types.VariableValue;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ressay on 05/04/18.
 */
public class AgentVariableValue extends VariableValue<List<String >>
{
    public AgentVariableValue() {
        value = new LinkedList<>();
    }

    public AgentVariableValue(String agent)
    {
        value = new LinkedList<>();
        value.add(agent);
    }

    @Override
    public void affect(Condition condition, VariableValue<List<String>> variableValue) throws ConflictException {
        for (String agent : variableValue.getValue())
            if(!hasAgent(agent))
                value.add(agent);
//        value.addAll(variableValue.getValue());
    }


    @Override
    public boolean equals(Object object) {
        AgentVariableValue agvv = (AgentVariableValue) object;
        if(agvv.value.size() != value.size())
            return false;
        for (String s:value)
            if(!agvv.hasAgent(s))
                return false;
        for (String s:agvv.getValue())
            if(!hasAgent(s))
                return false;

        return true;
    }

    public boolean hasAgent(String agentName)
    {
        for (String s:value)
            if(s.compareTo(agentName)==0)
                return true;
        return false;
    }

    @Override
    public boolean isLessThan(VariableValue<List<String>> o) {
        return false;
    }

    @Override
    public boolean isMoreThan(VariableValue<List<String>> o) {
        return false;
    }

    @Override
    public VariableValue<List<String>> defaultConstructor() {
        return new AgentVariableValue();
    }

    @Override
    public String toString()
    {
        String result = "";
        for (String agent : value)
            result += agent + ", ";
        return result;
    }
}
