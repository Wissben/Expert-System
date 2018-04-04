package BackEnd;/* Variable class

  Constructing Intelligent Agents with Java
   (C) Joseph P.Bigus and Jennifer Bigus 1997
 

*/

import BackEnd.Types.VariableValue;

import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

public abstract class Variable {
    public String name;
    VariableValue value;
    int column;

    public Variable() {
    }


    public Variable(String Name) {
        name = Name;
        value = null;
    }

    public void setValue(VariableValue val) {
        value = val;
    }

    VariableValue getValue() {
        return value;
    }

    // used by categorical only
    Vector labels;

    public void setLabels(String Labels) {
        labels = new Vector();
        StringTokenizer tok = new StringTokenizer(Labels, " ");
        while (tok.hasMoreTokens()) {
            labels.addElement(new String(tok.nextToken()));
        }
    }

    // return the label with the specified index
    String getLabel(int index) {
        return (String) labels.elementAt(index);
    }

    // return a string containing all labels
    String getLabels() {
        String labelList = new String();
        Enumeration enume = labels.elements();
        while (enume.hasMoreElements()) {
            labelList += enume.nextElement() + " ";
        }
        return labelList;
    }

    // given a label, return its index
    int getIndex(String label) {
        int i = 0, index = 0;
        Enumeration enume = labels.elements();
        while (enume.hasMoreElements()) {
            if (label.equals(enume.nextElement())) {
                index = i;
                break;
            }
            i++;
        }
        return i;
    }


    boolean categorical() {
        if (labels != null) {
            return true;
        } else {
            return false;
        }
    }

    // used by the DataSet class
    public void setColumn(int col) {
        column = col;
    }

    public abstract void computeStatistics(String inValue);

    public abstract int normalize(String inValue, float[] outArray, int inx);

    public int normalizedSize() {
        return 1;
    }

    public String getDecodedValue(float[] act, int index) {
        return String.valueOf(act[index]);
    }

    public String getName() {
        return name;
    }
}