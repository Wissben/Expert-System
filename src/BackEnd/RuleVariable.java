package BackEnd;/* RuleVariable class

  Constructing Intelligent Agents with Java
   (C) Joseph P.Bigus and Jennifer Bigus 1997

*/

import java.util.Enumeration;
import java.util.Vector;

public class RuleVariable extends Variable {

    public RuleVariable(String Name) {
        super(Name);
        clauseRefs = new Vector();
    }

    void setValue(String val) {
        value = val;
        updateClauses();
    }


    // prompt a user to provide a value for a variable during inferencing
    String askUser() {
        String answer = RuleApplet.waitForAnswer(promptText, getLabels()); // show dialog
        RuleBase.appendText("\n  !!! Looking for " + name + ". User entered: " + answer);

        setValue(answer); // need to set value from textField here
        return value;
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

    ;

    public int normalize(String inValue, float[] outArray, int inx) {
        return inx;
    }

};




