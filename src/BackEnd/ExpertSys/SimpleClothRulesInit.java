package BackEnd.ExpertSys;

import BackEnd.*;
import BackEnd.Types.StringVariableValue;

import java.util.ArrayList;

/**
 * a class to add rules to a rule base
 * Created by ressay on 03/04/18.
 */
public class SimpleClothRulesInit extends RuleInitializer
{

    @Override
    protected void initRuleBaseRules(RuleBase ruleBase) {
        // Note: at this point all variables values are NULL
        Condition cEquals = new Condition("=");
        Condition cNotEquals = new Condition("!=");
        Condition cLessThan = new Condition("<");
// define rules
//        ruleBase.setRuleList(new Vector());
        ArrayList<Clause> temporaryLeftClauses = new ArrayList<>();
        temporaryLeftClauses.add(new Clause((RuleVariable) ruleBase.getVariableList().get("Position"),
                cEquals, new StringVariableValue("Torso")));
        temporaryLeftClauses.add(new Clause((RuleVariable) ruleBase.getVariableList().get("Season"),
                cEquals, new StringVariableValue("Hto")));
        temporaryLeftClauses.add(new Clause((RuleVariable) ruleBase.getVariableList().get("SleeveLength"),
                cEquals, new StringVariableValue("Short")));
        temporaryLeftClauses.add(new Clause((RuleVariable) ruleBase.getVariableList().get("Material"),
                cEquals, new StringVariableValue("Cotton")));
        Rule Tshirt = new Rule("Tshirt", temporaryLeftClauses,
                new Clause((RuleVariable) ruleBase.getVariableList().get("Cloth"),
                        cEquals, new StringVariableValue("Tshirt")));
        ruleBase.addRule(Tshirt);
    }

    @Override
    protected void initRuleBaseVariables(RuleBase ruleBase) {
        RuleVariable cloth = new RuleVariable("Cloth");
        cloth.setLabels("Tshirt Pant Short Underwear Boot Running Glove Sweatshirt Pull Jacket Skirt Top Dress");
        cloth.setPromptText("What kind of cloth is it?");
        ruleBase.getVariableList().put(cloth.name, cloth);


        RuleVariable wearingPosition = new RuleVariable("Position");
        wearingPosition.setLabels("Head Hand Torso Legs Feet Wrist");
        wearingPosition.setPromptText("Where the cloth is worn");
        ruleBase.getVariableList().put(wearingPosition.name, wearingPosition);


        RuleVariable clothGender = new RuleVariable("Gender");
        clothGender.setLabels("Male Female");
        clothGender.setPromptText("Which gender is the cloth for ?");
        ruleBase.getVariableList().put(clothGender.name, clothGender);

        RuleVariable clothAge = new RuleVariable("Age");
        clothAge.setLabels("Baby Teen Adult");
        clothAge.setPromptText("To which age range is the cloth is destined");
        ruleBase.getVariableList().put(clothAge.name, clothAge);

        RuleVariable clothSize = new RuleVariable("Size");
        clothSize.setLabels("XXXS XXS XS Small Medium Large XL XXL 3XL 4XL");
        clothSize.setPromptText("What size is the cloth?");
        ruleBase.getVariableList().put(clothSize.name, clothSize);

        RuleVariable clothMaterial = new RuleVariable("Material");
        clothMaterial.setLabels("Cotton Leather Fur");
        clothMaterial.setPromptText("What material is the cloth made of ?");
        ruleBase.getVariableList().put(clothMaterial.name, clothMaterial);

        RuleVariable clothSleeveLength = new RuleVariable("SleeveLength");
        clothSleeveLength.setLabels("Short Medium Long");
        clothSleeveLength.setPromptText("How long the sleeves are ?");
        ruleBase.getVariableList().put(clothSleeveLength.name, clothSleeveLength);

        RuleVariable clothSeason = new RuleVariable("Season");
        clothSeason.setLabels("Hot Cold");
        clothSeason.setPromptText("For what season this cloth is for ? ");
        ruleBase.getVariableList().put(clothSeason.name, clothSeason);
    }
}
