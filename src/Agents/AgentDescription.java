package Agents;

/**
 * Created by ressay on 03/04/18.
 */
public class AgentDescription
{
    String name,className;
    Object[] arguments;

    public AgentDescription(String name, String className, Object[] arguments) {
        this.name = name;
        this.className = className;
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
