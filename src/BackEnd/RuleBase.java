package BackEnd;/* RuleBase class

 Constructing Intelligent Agents with Java
   (C) Joseph P.Bigus and Jennifer Bigus 1997

*/

import BackEnd.ExpertSys.AskUserCallBack;
import BackEnd.Types.VariableValue;
import FrontEnd.Main;

import java.util.*;

public class RuleBase {
    String name;
//    private Hashtable variableList; // all variables in the rulebase
    private HashMap<String,RuleVariable> variableList;
    private Vector ruleList; // list of all rules
    //Clause clausePtr; // working pointer to current clause
    private Stack goalClauseStack; // for goals (cons clauses ) and subgoals
    AskUserCallBack userAsker;

    public RuleBase(String Name,AskUserCallBack userAsker) {
        this.variableList=new HashMap<>();
        this.ruleList=new Vector();
        this.goalClauseStack = new Stack();
        this.userAsker = userAsker;
        name = Name;
    }


// reset the rule base for another round of inferencing
// by setting all variable values to null

    public void reset() {
        for (String var : variableList.keySet()) {
            variableList.get(var).setValue(null);
        }
    }
//...

    public void forwardChain() {
        Vector conflictRuleSet;
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
            if ((testRule.getTruth()) &&
                    (!testRule.isFired())) matchList.addElement(testRule);
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
        RuleVariable goalVar = variableList.get(goalVarName);
        Enumeration goalClauses = goalVar.clauseRefs.elements();
        while (goalClauses.hasMoreElements()) {
            Clause goalClause = (Clause) goalClauses.nextElement();
            if (!goalClause.consequent) continue;
            goalClauseStack.push(goalClause);

            Rule goalRule = goalClause.getRule();
            Boolean ruleTruth = goalRule.backChain(this,userAsker); // find rule truth value
            if (ruleTruth == null) {
                System.out.println("\nRule " + goalRule.getRuleName() + " is null, can't determine truth value.");
            } else if (ruleTruth) {
                // rule is OK, assign consequent value to variable


                goalVar.setValue(goalClause.rhs);
                goalVar.setRuleName(goalRule.getRuleName());
                goalClauseStack.pop(); // clear item from subgoal stack

                System.out.println("\nRule " + goalRule.getRuleName() + " is true, setting " + goalVar.name + ": =" + goalVar.value);
                if (goalClauseStack.empty()) {
                    System.out.println("\n +++ Found Solution for goal: " + goalVar.name);
                    break; // for now, only find first solution, then stop
                }

            } else {
                goalClauseStack.pop(); // clear item from subgoal stack
                System.out.println("\nRule " + goalRule.getRuleName() + " is false, can�t set " + goalVar.name);
            }
        }
        if (goalVar.value == null) {
            System.out.println("\n +++ Could Not Find Solution for goal: " + goalVar.name);
        }
    }

    public void addRule(Rule rule)
    {
        this.ruleList.addElement(rule);
//        rule.display();
    }

    public void removeRule(Rule rule)
    {
        this.ruleList.remove(rule);
    }

    public void setVariableValue(String variableName, VariableValue value)
    {
        if(containsVariable(variableName)) {
            RuleVariable ruleVariable = getVariableList().get(variableName);
            ruleVariable.setValue(value);
        }
    }

    public HashMap<String, RuleVariable> getVariableList() {
        return variableList;
    }

    public void addVariable(String name, RuleVariable variable)
    {
        if(!variableList.containsKey(name))
            variableList.put(name,variable);
    }

    public VariableValue getVariableValue(String variableName)
    {
        if(variableList.get(variableName) == null)
            return null;
        return variableList.get(variableName).getValue();
    }

    public boolean containsVariable(String variableName)
    {
        return variableList.containsKey(variableName);
    }


    public static void appendText(String text) {
        System.out.println(text);
    }

// for trace purposes  - display all variables and their value

    public void displayVariables() {

        for (String key : variableList.keySet()) {
            RuleVariable temp = variableList.get(key);
            System.out.println("\n" + temp.name + " value = " + temp.value);
        }
    }

// for trace purposes - display all rules in text format


    public void displayRules() {
        System.out.println("\n" + name + " Rule Base: " + "\n");
        Enumeration enume = ruleList.elements();
        while (enume.hasMoreElements()) {
            Rule temp = (Rule) enume.nextElement();
            temp.display();
        }
    }

    // for trace purposes - display all rules in the conflict set
    public void displayConflictSet(Vector ruleSet) {
        System.out.println("\n" + " -- UI in conflict set:\n");
        Enumeration enume = ruleSet.elements();
        while (enume.hasMoreElements()) {
            Rule temp = (Rule) enume.nextElement();
            System.out.println(temp.getRuleName() + "(" + temp.numAntecedents() + "), ");
        }
    }
}

