package BackEnd.Initializers;

import BackEnd.*;
import BackEnd.Types.IntegerValue;
import BackEnd.Types.IntervalVariableValue;
import BackEnd.Types.StringVariableValue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * a class to add rules to a rule base
 * Created by ressay on 03/04/18.
 */
public class SimpleClothRulesInit extends RuleInitializer
{

    protected static Condition cEquals = new Condition("=");
    protected static Condition cNotEquals = new Condition("!=");
    protected static Condition cLessThan = new Condition("<");


    @Override
    protected void initRuleBaseRules(RuleBase ruleBase){
        RuleParser ruleParser = null;
        try {
            ruleParser = new RuleParser("/home/wiss/CODES/TP-AGENT/PART1/src/rules");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ruleParser.startParsing(ruleBase);
    }

    @Override
    protected void initRuleBaseVariables(RuleBase ruleBase) {
        RuleVariable article = new RuleVariable("Article");
        article.setLabels("Tshirt Pant Short Underwear Boot Running Glove Sweatshirt " +
                          "Pull Jacket Skirt Top Dress Watch Bag Sock Ring scarf " +
                          "Glasses Hat Cap Liquette Hijab");
        article.setPromptText("What kind of article is it?");
        ruleBase.getVariableList().put(article.name, article);


        RuleVariable wearingPosition = new RuleVariable("Position");
        wearingPosition.setLabels("Head Neck Hand Torso Legs Feet Wrist Finger");
        wearingPosition.setPromptText("Where the article is worn");
        ruleBase.getVariableList().put(wearingPosition.name, wearingPosition);


        RuleVariable articleGender = new RuleVariable("Gender");
        articleGender.setLabels("Male Female");
        articleGender.setPromptText("Which gender is the article for ?");
        ruleBase.getVariableList().put(articleGender.name, articleGender);

        RuleVariable articleAge = new RuleVariable("Age");
        articleAge.setLabels("");
        articleAge.setPromptText("To which age range is the article is destined");
        ruleBase.getVariableList().put(articleAge.name, articleAge);

        RuleVariable articleSize = new RuleVariable("Size");
        articleSize.setLabels("XXXS XXS XS Small Medium Large XL XXL 3XL 4XL");
        articleSize.setPromptText("What size is the article?");
        ruleBase.getVariableList().put(articleSize.name, articleSize);

        RuleVariable articleMaterial = new RuleVariable("Material");
        articleMaterial.setLabels("Cotton Leather Fur Jean Metal");
        articleMaterial.setPromptText("What material is the article made of ?");
        ruleBase.getVariableList().put(articleMaterial.name, articleMaterial);

        RuleVariable articleLength = new RuleVariable("Length");
        articleMaterial.setLabels("Short Medium Long");
        articleMaterial.setPromptText("");
        ruleBase.getVariableList().put(articleMaterial.name, articleMaterial);

        RuleVariable articleSleeveLength = new RuleVariable("SleeveLength");
        articleSleeveLength.setLabels("None Short Medium Long");
        articleSleeveLength.setPromptText("How long the sleeves are ?");
        ruleBase.getVariableList().put(articleSleeveLength.name, articleSleeveLength);

        RuleVariable articleSeason = new RuleVariable("Season");
        articleSeason.setLabels("Summer Winter Autumn Spring");
        articleSeason.setPromptText("For what season this article is for ? ");
        ruleBase.getVariableList().put(articleSeason.name, articleSeason);

        RuleVariable articlePrice = new RuleVariable("Price");
        ruleBase.getVariableList().put(articlePrice.name,articlePrice);

    }
}
