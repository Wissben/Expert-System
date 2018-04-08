package Agents;

import BackEnd.Database.QueryAnswer;

import java.util.LinkedList;

/**
 * Created by ressay on 07/04/18.
 */
public class AgentQueryAnswers
{
    LinkedList<QueryAnswer> answers = new LinkedList<>();
    LinkedList<String> agents = new LinkedList<>();

    public AgentQueryAnswers() {
    }

    public void addAnswer(String agentName,QueryAnswer answer)
    {
        answers.add(answer);
        agents.add(agentName);
    }

    public QueryAnswer getAnswer(int index)
    {
        return answers.get(index);
    }

    public QueryAnswer getAnswer(String agentName)
    {
        return answers.get(agents.indexOf(agentName));
    }

    public String getAgentName(int index)
    {
        return agents.get(index);
    }

    public String getAgentName(QueryAnswer answer)
    {
        return agents.get(answers.indexOf(answer));
    }

    public LinkedList<QueryAnswer> getAnswers() {
        return answers;
    }

    public LinkedList<String> getAgents() {
        return agents;
    }
}
