package BackEnd;/* RuleBase class

 Constructing Intelligent Agents with Java
   (C) Joseph P.Bigus and Jennifer Bigus 1997

*/

import java.awt.*;
import java.util.*;

public class RuleBase {
    String name;
    private Hashtable variableList; // all variables in the rulebase
    private ArrayList<Clause> clauseVarList;
    private Vector ruleList; // list of all rules
    private Vector conclusionVarList; // queue of variables
    private Rule rulePtr; // working pointer to current rule
    //Clause clausePtr; // working pointer to current clause
    private Stack goalClauseStack; // for goals (cons clauses ) and subgoals
    private static TextArea textArea1;

    public void setDisplay(TextArea txtArea) {
        textArea1 = txtArea;
    }

    public RuleBase(String Name) {
        this.variableList=new Hashtable();
        this.clauseVarList=new ArrayList<>();
        this.ruleList=new Vector();
        this.conclusionVarList=new Vector();
        this.goalClauseStack = new Stack();
        name = Name;
    }

    public static void appendText(String text) {
        textArea1.appendText(text);
    }

// for trace purposes  - display all variables and their value

    public void displayVariables(TextArea textArea) {
        Enumeration enume = variableList.elements();
        while (enume.hasMoreElements()) {
            RuleVariable temp = (RuleVariable) enume.nextElement();
            textArea.appendText("\n" + temp.name + " value = " + temp.value);
        }
    }

// for trace purposes - display all rules in text format


    public void displayRules(TextArea textArea) {
        textArea.appendText("\n" + name + " Rule Base: " + "\n");
        Enumeration enume = ruleList.elements();
        while (enume.hasMoreElements()) {
            Rule temp = (Rule) enume.nextElement();
            temp.display(textArea);
        }
    }

    // for trace purposes - display all rules in the conflict set
    public void displayConflictSet(Vector ruleSet) {
        textArea1.appendText("\n" + " -- UI in conflict set:\n");
        Enumeration enume = ruleSet.elements();
        while (enume.hasMoreElements()) {
            Rule temp = (Rule) enume.nextElement();
            textArea1.appendText(temp.getRuleName() + "(" + temp.numAntecedents() + "), ")
            ;
        }
    }
// reset the rule base for another round of inferencing
// by setting all variable values to null

    public void reset() {
        textArea1.appendText("\n Setting all " + name + " variables to null");
        Enumeration enume = variableList.elements();
        while (enume.hasMoreElements()) {
            RuleVariable temp = (RuleVariable) enume.nextElement();
            temp.setValue(null);
        }

    }
//...

    public void forwardChain() {
        Vector conflictRuleSet = new Vector();
        // first test all rules, based on initial data
        conflictRuleSet = match(true); // see which rules can fire
        while (conflictRuleSet.size() > 0) {
            Rule selected = selectRule(conflictRuleSet); // select the "best" rule
            selected.fire(); // fire the rule
            // do the consequent action/assignment
            // update all clauses and rules
            conflictRuleSet = match(false); // see which rules can fire
            // displayVariables("Forward Chaining") ; // display variable bindings
        }
    }




    //used for forward chaining only
//determine which rules can fire, return a Vector
    public Vector match(boolean test) {
        Vector matchList = new Vector();
        Enumeration enume = ruleList.elements();
        while (enume.hasMoreElements()) {
            Rule testRule = (Rule) enume.nextElement();
            if (test) testRule.check(); // test the rule antecedents
            if (testRule.getTruth() == null) continue;
            // fire the rule only once for now
            if ((testRule.getTruth().booleanValue() == true) &&
                    (testRule.isFired() == false)) matchList.addElement(testRule);
        }
        displayConflictSet(matchList);
        return matchList;
    }

    //used for forward chaining only
//select a rule to fire based on specificity
    public Rule selectRule(Vector ruleSet) {
        Enumeration enume = ruleSet.elements();
        long numClauses;
        Rule nextRule;
        Rule bestRule = (Rule) enume.nextElement();
        long max = bestRule.numAntecedents();
        while (enume.hasMoreElements()) {
            nextRule = (Rule) enume.nextElement();
            if ((numClauses = nextRule.numAntecedents()) > max) {
                max = numClauses;
                bestRule = nextRule;
            }
        }
        return bestRule;
    }

    public void backwardChain(String goalVarName) {
        RuleVariable goalVar = (RuleVariable) variableList.get(goalVarName);
        Enumeration goalClauses = goalVar.clauseRefs.elements();
        while (goalClauses.hasMoreElements()) {
            Clause goalClause = (Clause) goalClauses.nextElement();
            if (goalClause.consequent.booleanValue() == false) continue;
            goalClauseStack.push(goalClause);

            Rule goalRule = goalClause.getRule();
            Boolean ruleTruth = goalRule.backChain(); // find rule truth value
            if (ruleTruth == null) {
                textArea1.appendText("\nRule " + goalRule.getRuleName() + " is null, can't determine truth value.");
            } else if (ruleTruth.booleanValue() == true) {
                // rule is OK, assign consequent value to variable


                goalVar.setValue(goalClause.rhs);
                goalVar.setRuleName(goalRule.getRuleName());
                goalClauseStack.pop(); // clear item from subgoal stack

                textArea1.appendText("\nRule " + goalRule.getRuleName() + " is true, setting " + goalVar.name + ": =" + goalVar.value);
                if (goalClauseStack.empty() == true) {
                    textArea1.appendText("\n +++ Found Solution for goal: " + goalVar.name);
                    break; // for now, only find first solution, then stop
                }

            } else {
                goalClauseStack.pop(); // clear item from subgoal stack
                textArea1.appendText("\nRule " + goalRule.getRuleName() + " is false, can�t set " + goalVar.name);
            }
        }
        if (goalVar.value == null) {
            textArea1.appendText("\n +++ Could Not Find Solution for goal: " + goalVar.name);
        }
    }

    public void addRule(Rule rule)
    {
        this.ruleList.addElement(rule);
    }

    public void removeRule(Rule rule)
    {
        this.ruleList.remove(rule);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Hashtable getVariableList() {
        return variableList;
    }

    public void setVariableList(Hashtable variableList) {
        this.variableList = variableList;
    }

    public ArrayList<Clause> getClauseVarList() {
        return clauseVarList;
    }

    public void setClauseVarList(ArrayList<Clause> clauseVarList) {
        this.clauseVarList = clauseVarList;
    }

    public Vector getRuleList() {
        return ruleList;
    }

    public void setRuleList(Vector ruleList) {
        this.ruleList = ruleList;
    }

    public Vector getConclusionVarList() {
        return conclusionVarList;
    }

    public void setConclusionVarList(Vector conclusionVarList) {
        this.conclusionVarList = conclusionVarList;
    }

    public Rule getRulePtr() {
        return rulePtr;
    }

    public void setRulePtr(Rule rulePtr) {
        this.rulePtr = rulePtr;
    }

    public Stack getGoalClauseStack() {
        return goalClauseStack;
    }

    public void setGoalClauseStack(Stack goalClauseStack) {
        this.goalClauseStack = goalClauseStack;
    }

    public static TextArea getTextArea1() {
        return textArea1;
    }

    public static void setTextArea1(TextArea textArea1) {
        RuleBase.textArea1 = textArea1;
    }
}

