package BackEnd;/*  Clause class

   Constructing Intelligent Agents with Java
   (C) Joseph P.Bigus and Jennifer Bigus 1997
    

*/

import BackEnd.Types.ConflictException;
import BackEnd.Types.VariableValue;

import java.util.Vector;
//import java.io.*;


public class Clause {
    Vector ruleRefs;
    RuleVariable lhs;
    VariableValue rhs;
    Condition cond;
    Boolean consequent; // true or false
    Boolean truth;      // states = null(unknown), true or false

    public Clause(RuleVariable Lhs, Condition Cond, VariableValue Rhs) {

        lhs = Lhs;
        cond = Cond;
        rhs = Rhs;
        lhs.addClauseRef(this);
        ruleRefs = new Vector();
        truth = null;
        consequent = false;
    }

    void addRuleRef(Rule ref) {
        ruleRefs.addElement(ref);
    }

    Boolean check() {
        if (consequent) return null;
        if (lhs.value == null) {
            return truth = null;    // Var value is undefined
        } else {
            switch (cond.index) {
                case 1:
                    truth = lhs.value.equals(rhs);
                    break;
                case 2:
                    truth = lhs.value.compareTo(rhs) > 0;
                    break;
                case 3:
                    truth = lhs.value.compareTo(rhs) < 0;
                    break;
                case 4:
                    truth = lhs.value.compareTo(rhs) != 0;
                    break;
            }
            return truth;
        }
    }

    public void updateLhs() throws ConflictException {
        lhs.setValue(rhs,cond);
    }

    void isConsequent() {
        consequent = Boolean.TRUE;
    }

    Rule getRule() {
        if (consequent)
            return (Rule) ruleRefs.firstElement();
        else return null;
    }
};