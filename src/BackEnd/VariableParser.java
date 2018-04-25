package BackEnd;

import Agents.ExpertAgent.AgentVariableValue;
import BackEnd.Types.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableParser {
    private HashMap<String, String[]> mapRhsToLhs;
    private HashMap<String, Instantiator> mapTypeToVariableValue;

    public VariableParser(String pathToFile) throws IOException {
        initMapTypeToVariableValue();
        loadRulesFromFile(pathToFile);
    }


    public void startParsing(RuleBase ruleBase) {
        for (String key : this.mapRhsToLhs.keySet())
        {
            String[] keys = key.split(":");

            if(ruleBase.containsVariable(keys[0]))
                continue;
            RuleVariable temp = new RuleVariable(keys[0]);
            String labels = "";
            for (int i = 0; i < mapRhsToLhs.get(key).length; i++)
            {
                labels += mapRhsToLhs.get(key)[i] + " ";
            }
            temp.setValue((VariableValue) mapTypeToVariableValue.get(keys[1]).instantiate(null));
            labels = labels.substring(0, labels.length() - 1);
            temp.setLabels(labels);
            ruleBase.getVariableList().put(temp.name, temp);
        }
    }

    public void loadRulesFromFile(String pathToFile) throws IOException {
        File inputFile = new File(pathToFile);
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        String line;
        String regex = "<(.+:.+)>(.*)<\\/.+>";//
        Pattern pattern = Pattern.compile(regex);
        this.mapRhsToLhs = new HashMap<>();
        while ((line = reader.readLine()) != null) {
            final Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String currVar = matcher.group(1);
                String[] currValues = matcher.group(2).split(",");
                mapRhsToLhs.put(currVar, currValues);
            }
        }
    }


    private void initMapTypeToVariableValue()
    {
        this.mapTypeToVariableValue = new HashMap<>();
        mapTypeToVariableValue.put("String", param -> new StringVariableValue((String) param));

        mapTypeToVariableValue.put("Integer", param -> new IntegerValue((IntervalUnion<Integer>) param));

        mapTypeToVariableValue.put("Double", param -> new DoubleValue((IntervalUnion<Double>) param));

        mapTypeToVariableValue.put("Agent", param -> new AgentVariableValue());
    }
}
