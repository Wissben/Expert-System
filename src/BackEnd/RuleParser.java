package BackEnd;

import Agents.ExpertAgent.AgentVariableValue;
import BackEnd.Types.DoubleValue;
import BackEnd.Types.IntegerValue;
import BackEnd.Types.StringVariableValue;
import BackEnd.Types.VariableValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleParser
{
    private HashMap<String, Instantiator> mapClassNameToConstructor;
    private HashMap<String, String> mapRhsToLhs;
    private RuleBase ruleBase;

    public RuleParser(String pathToFile) throws IOException
    {
        initMapClassNameToConstructor();
        loadRulesFromFile(pathToFile);
    }


    public void startParsing(RuleBase ruleBase)
    {
        this.ruleBase = ruleBase;
        for (String key : mapRhsToLhs.keySet())
        {
            String[] rhs = key.substring(1, key.length() - 1).split("/"); //<sd/sdsds/sdds>
            String[] lhs = mapRhsToLhs.get(key).split(",");
            ArrayList<Clause> temporaryLeftClauses = new ArrayList<>();
            for (int i = 0; i < lhs.length; i++)
            {
                lhs[i] = lhs[i].substring(1, lhs[i].length() - 1);
                String[] lhsComponent = lhs[i].split("/");
                temporaryLeftClauses.add(new Clause(ruleBase.getVariableList().get(lhsComponent[0]),
                        new Condition(lhsComponent[2]), (VariableValue) mapClassNameToConstructor.get(lhsComponent[1]).instantiate(lhsComponent[3])));
            }
//            System.out.println("RHS " + ruleBase.getVariableList().get(rhs[0]));
            ruleBase.addRule(new Rule(rhs[0], temporaryLeftClauses,
                    new Clause(ruleBase.getVariableList().get(rhs[0]),
                            new Condition(rhs[2]), (VariableValue) mapClassNameToConstructor.get(rhs[1]).instantiate(rhs[3]))));

        }
    }

    private void initMapClassNameToConstructor()
    {
        this.mapClassNameToConstructor = new HashMap<>();
        mapClassNameToConstructor.put("String", param -> new StringVariableValue((String) param));

        mapClassNameToConstructor.put("Integer", param -> new IntegerValue(Integer.valueOf((String) param)));

        mapClassNameToConstructor.put("Double", param -> new DoubleValue(Double.valueOf((String) param)));

        mapClassNameToConstructor.put("Agent", param -> new AgentVariableValue((String) param));
    }

    public void loadRulesFromFile(String pathToFile) throws IOException
    {
        File inputFile = new File(pathToFile);
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        String line;
        String patternModel = "(<.+/.+/./.+>)=(<.+/.+/./.+>,)+";
        Pattern pattern = Pattern.compile(patternModel);
        this.mapRhsToLhs = new HashMap<>();
        while ((line = reader.readLine()) != null)
        {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches())
            {
                //System.out.println(matcher.group(2));
                this.mapRhsToLhs.put(matcher.group(1), matcher.group(2));
            }
        }
    }


}
