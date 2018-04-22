package FrontEnd.UI;

import BackEnd.ExpertSys.VariableMapper;
import BackEnd.Types.VariableValue;

import java.io.Serializable;

/**
 * Created by ressay on 07/04/18.
 */
public class UIQuery implements Serializable
{
    VariableMapper mapper;

    public UIQuery(VariableMapper mapper)
    {
        this.mapper = mapper;
    }

    public String[] getVariables()
    {
        return mapper.getVariables();
    }

    public VariableValue getVariableValue(String variable)
    {
        return mapper.getVariableValue(variable);
    }

    public VariableMapper getMapper()
    {
        return mapper;
    }
}
