package BackEnd;/* RuleVariable class

  Constructing Intelligent Agents with Java
   (C) Joseph P.Bigus and Jennifer Bigus 1997

*/

import BackEnd.Types.ConflictException;
import BackEnd.Types.VariableValue;

import java.util.Enumeration;
import java.util.Scanner;
import java.util.Vector;

public class RuleVariable extends Variable {

    public RuleVariable(String Name) {
        super(Name);
        clauseRefs = new Vector();
    }

    public void setValue(VariableValue val) {
        value = val;
        updateClauses();
    }

    public void setValue(VariableValue val,Condition condition) throws ConflictException {
        if(value == null)
            value = val;
        value.affect(condition,val);
        updateClauses();
    }

    Vector clauseRefs;   // clauses which refer to this var

    void addClauseRef(Clause ref) {
        clauseRefs.addElement(ref);
    }

    void updateClauses() {
        Enumeration enume = clauseRefs.elements();
        while (enume.hasMoreElements()) {
            ((Clause) enume.nextElement()).check(); // retest the truth condition
        }
    }

    String promptText;  // used to prompt user for value
    String ruleName;      // if value is inferred, null = user provided

    void setRuleName(String rname) {
        ruleName = rname;
    }

    public void setPromptText(String text) {
        promptText = text;
    }

    // these methods are not used in rule variables
    public void computeStatistics(String inValue) {
    }


    public int normalize(String inValue, float[] outArray, int inx) {
        return inx;
    }

    public String getPromptText() {
        return promptText;
    }

}




