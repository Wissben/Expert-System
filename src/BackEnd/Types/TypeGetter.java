package BackEnd.Types;

import java.sql.ResultSet;

public interface TypeGetter
{

    VariableValue getValue(String column, ResultSet resultSet);
}
