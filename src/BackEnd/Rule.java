package BackEnd;/* Rule class

   Constructing Intelligent Agents with Java
   (C) Joseph P. Bigus and Jennifer Bigus 1997
    
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

//import java.io.*;

public class Rule {
    private String ruleName;
    private static TextArea textArea1;//////////en plus
    private ArrayList<Clause> antecedents; // allow up to 4 antecedents for now
    private Clause consequent; //only 1 consequent clause allowed
    private Boolean truth; // states = (null=unknown, true, or false)
    private boolean fired = false;


    public Rule(String ruleName, ArrayList<Clause> lefts, Clause right)
    {
        this.ruleName = ruleName;
        this.antecedents = new ArrayList<>(lefts);
        for (int i = 0; i < lefts.size(); i++) {
            lefts.get(i).addRuleRef(this);
        }
        this.consequent = right;
        right.addRuleRef(this);
        right.isConsequent();
        truth = null;
    }

    long numAntecedents() {
        return antecedents.size();
    }
//. . .

    //used by forward chaining only !
    Boolean check() { // if antecedent is true and rule has not fired
        RuleBase.appendText("\nTesting rule " + ruleName);
        for (int i = 0; i < antecedents.size(); i++) {
            if (antecedents.get(i).truth == null) return null;
            if (antecedents.get(i).truth.booleanValue() == true) {
                continue;
            } else {
                return truth = new Boolean(false); //don�t fire this rule
            }
        } // endfor
        return truth = new Boolean(true); // could fire this rule
    }

    //used by forward chaining only !
//fire this rule -- perform the consequent clause
//if a variable is changes, update all clauses where
//it is references, and then all rules which contain
//those clauses
    void fire() {
        truth = new Boolean(true);
        fired = true;
//set the variable value and update clauses
        consequent.lhs.setValue(consequent.rhs);
//now retest any rules whose clauses just changed
        checkRules(consequent.lhs.clauseRefs);
    }
//used by forward chaining only !
//a variable value was found, so retest all clauses
//that reference that variable, and then all rules which
//references those clauses


    public static void checkRules(Vector clauseRefs) {
        Enumeration enume = clauseRefs.elements();
        while (enume.hasMoreElements()) {
            Clause temp = (Clause) enume.nextElement();
            Enumeration enume2 = temp.ruleRefs.elements();
            while (enume2.hasMoreElements()) {
                ((Rule) enume2.nextElement()).check(); // retest the rule
            }
        }
    }


    //display the rule in text format
    void display() {
        System.out.println(ruleName + ": IF ");
        for (int i = 0; i < antecedents.size(); i++) {
            Clause nextClause = antecedents.get(i);
            System.out.println(nextClause.lhs.name +
                    nextClause.cond.asString() +
                    nextClause.rhs + " ");
            if ((i + 1) < antecedents.size())
                System.out.println("\n AND ");
        }
        System.out.println("\n THEN ");
        System.out.println(consequent.lhs.name +
                consequent.cond.asString() + consequent.rhs + "\n");
    }
//if rule is false then pop, continue
//if rule is null then we couldnt find a value

    Boolean backChain(RuleBase ruleBase,AskUserCallBack userAsker) {
        for (int i = 0; i < antecedents.size(); i++) { // test each clause
            if (antecedents.get(i).truth == null)
                ruleBase.backwardChain(antecedents.get(i).lhs.name);
            if (antecedents.get(i).truth == null) { // couldn�t prove t or f
                RuleVariable lhs = antecedents.get(i).lhs;
                lhs.setValue(userAsker.askUser(lhs.promptText,lhs.name));

                truth = antecedents.get(i).check(); // redundant?
            }
            if (!antecedents.get(i).truth) {
                return truth = Boolean.FALSE; // exit one is false
            }
        }
        return truth = Boolean.TRUE; // all antecedents are true
    }


    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public static TextArea getTextArea1() {
        return textArea1;
    }

    public static void setTextArea1(TextArea textArea1) {
        Rule.textArea1 = textArea1;
    }

    public ArrayList<Clause> getAntecedents() {
        return antecedents;
    }

    public void setAntecedents(ArrayList<Clause> antecedents) {
        this.antecedents = antecedents;
    }

    public Clause getConsequent() {
        return consequent;
    }

    public void setConsequent(Clause consequent) {
        this.consequent = consequent;
    }

    public Boolean getTruth() {
        return truth;
    }

    public void setTruth(Boolean truth) {
        this.truth = truth;
    }

    public boolean isFired() {
        return fired;
    }

    public void setFired(boolean fired) {
        this.fired = fired;
    }
}