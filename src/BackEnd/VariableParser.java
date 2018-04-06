package BackEnd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableParser {
    private HashMap<String, String[]> mapRuleVarNameToValues;

    public VariableParser(String pathToFile) throws IOException {
        loadRulesFromFile(pathToFile);
    }


    public void startParsing(RuleBase ruleBase) {
        for (String key : this.mapRuleVarNameToValues.keySet()) {
            RuleVariable temp = new RuleVariable(key);
            String labels = "";
            for (int i = 0; i < mapRuleVarNameToValues.get(key).length; i++) {
                labels += mapRuleVarNameToValues.get(key)[i]+" ";
            }
            labels = labels.substring(0, labels.length() - 1);
            temp.setLabels(labels);
            ruleBase.getVariableList().put(temp.name, temp);
        }
    }

    public void loadRulesFromFile(String pathToFile) throws IOException {
        File inputFile = new File(pathToFile);
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        String line;
        String regex = "<(.+):.+>(.*)<\\/.+>";
        Pattern pattern = Pattern.compile(regex);
        this.mapRuleVarNameToValues = new HashMap<>();
        while ((line = reader.readLine()) != null) {
            final Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String currVar = matcher.group(1);
                String[] currValues = matcher.group(2).split(",");
                mapRuleVarNameToValues.put(currVar, currValues);
            }
        }
    }


}
