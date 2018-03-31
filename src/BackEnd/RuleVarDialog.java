package BackEnd;/*  RuleVarDialog class

 Constructing Intelligent Agents with Java
   (C) Joseph P.Bigus and Jennifer Bigus 1997

*/

import java.awt.*;

public class RuleVarDialog extends Dialog {

    void button1_Clicked(Event event) {
        answer = textField1.getText();   // set
    }

    void button2_Clicked(Event event) {
        answer = "";
        textField1.setText("");
    }


    public RuleVarDialog(Frame parent, boolean modal) {

        super(parent, modal);

        //{{INIT_CONTROLS
        setLayout(null);
        addNotify();
        resize(insets().left + insets().right + 352, insets().top + insets().bottom + 214);
        label1 = new Label("");
        label1.reshape(insets().left + 0, insets().top + 12, 407, 61);
        add(label1);
        textField1 = new TextField();
        textField1.setText("");
        textField1.reshape(insets().left + 192, insets().top + 84, 97, 39);
        add(textField1);
        button1 = new Button("Set");
        button1.reshape(insets().left + 24, insets().top + 144, 124, 41);
        add(button1);
        setTitle("Rule Applet -- Ask User");
        //}}

    }

    public RuleVarDialog(Frame parent, String title, boolean modal) {
        this(parent, modal);
        setTitle(title);
    }

    public boolean gotFocus(Event e, Object arg) {
        textField1.requestFocus();
        return false;
    }

    public synchronized void show() {
        Rectangle bounds = getParent().bounds();
        Rectangle abounds = bounds();

        move(bounds.x + (bounds.width - abounds.width) / 2, bounds.y + (bounds.height - abounds.height) / 2);

        super.show();
    }

    public boolean handleEvent(Event event) {

        // bug workaround --- 1004 is button press ???
        if (event.id == 1004 & event.target == button1) {
            button1_Clicked(event);  // set
            hide();
            dispose();
            return true;
        }
        return super.handleEvent(event);
    }


    public String getText() {
        return answer;
    }

    //{{DECLARE_CONTROLS
    Label label1;
    TextField textField1;
    Button button1;
    //}}
    String answer = new String("");
}

