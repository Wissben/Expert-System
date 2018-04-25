package FrontEnd.Widgets;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * Created by ressay on 22/04/18.
 */
public class BodyChoice implements Initializable
{
    @FXML
    private Circle head;

    @FXML
    private Ellipse torso;

    @FXML
    private QuadCurve rightLeg;

    @FXML
    private QuadCurve leftLeg;

    @FXML
    private QuadCurve leftHand;

    @FXML
    private QuadCurve rightHand;

    @FXML
    private Ellipse leftFoot;

    @FXML
    private Ellipse rightFoot;

    private Shape[] allShapes;
    private LinkedList<Shape> selected = new LinkedList<>();
    private HashMap<Shape,String> map = new HashMap<>();
    private Paint initFill = null;
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        initFill = rightFoot.getFill();
        addMouseEffects(rightFoot.getFill(),rightFoot,leftFoot);
        addMouseEffects(rightLeg.getFill(),leftLeg,rightLeg);
        addMouseEffects(rightHand.getFill(),rightHand,leftHand);
        addMouseEffects(torso.getFill(),torso);
        addMouseEffects(head.getFill(),head);
        Shape[] temp = {rightFoot,rightHand,rightLeg,leftFoot,leftHand,leftLeg,head,torso};
        allShapes = temp;
        map.put(rightFoot,"Feet");
        map.put(leftFoot,"Feet");
        map.put(rightHand,"Hand");
        map.put(leftHand,"Hand");
        map.put(rightLeg,"Legs");
        map.put(leftLeg,"Legs");
        map.put(head,"Head");
        map.put(torso,"Torso");
    }

    protected void addMouseEffects(Paint initialFill,Shape... shapes)
    {
        addHoverEffect(initialFill,shapes);
        addSelection(initialFill,shapes);
    }

    protected void addHoverEffect(Paint initialFill,Shape... shapes)
    {
        for (int i = 0; i < shapes.length; i++)
        {
            final int index = i;
            shapes[i].setOnMouseEntered(event -> {
                if(!selected.contains(shapes[index]))
                for (int j = 0; j < shapes.length; j++)
                {
                    shapes[j].setFill(new Color(0,0.6,1,1));
                }
            });

            shapes[i].setOnMouseExited(event -> {
                if(!selected.contains(shapes[index]))
                for (int j = 0; j < shapes.length; j++)
                {
                    shapes[j].setFill(initialFill);
                }
            });
        }
    }

    protected void addSelection(Paint initialFill,Shape... shapes)
    {


        for (int i = 0; i < shapes.length; i++)
        {
            final int index = i;
            shapes[i].setOnMousePressed(event -> {
                if(!selected.contains(shapes[index]))
                {
                    selected.clear();
                    for (int j = 0; j < allShapes.length; j++)
                    {
                        allShapes[j].setFill(initialFill);
                    }
                    for (int j = 0; j < shapes.length; j++)
                    {
                        shapes[j].setFill(new Color(0,0.3,0.8,1));
                        selected.add(shapes[j]);
                    }
                }
                else
                {
                    selected.clear();
                    for (int j = 0; j < allShapes.length; j++)
                    {
                        allShapes[j].setFill(initialFill);
                    }
                }
            });
        }
    }

    public void unselectAll()
    {
        for(Shape s : selected)
        {
            s.setFill(initFill);
        }
        selected.clear();
    }

    public String getSelectedPart()
    {
        if(selected.isEmpty())
            return null;
        else
            return map.get(selected.get(0));
    }
}
