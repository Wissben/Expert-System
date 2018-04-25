package FrontEnd.Widgets;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Created by ressay on 22/04/18.
 */
public class WeatherChoice implements Initializable
{
    @FXML
    private ImageView rainyImg;

    @FXML
    private ImageView cloudyImg;

    @FXML
    private ImageView sunnyImg;

    private ImageView selected = null;

    private HashMap<ImageView,String> map = new HashMap<>();
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        addMouseEffects(sunnyImg);
        addMouseEffects(cloudyImg);
        addMouseEffects(rainyImg);
        map.put(sunnyImg,"Summer");
        map.put(rainyImg,"Winter");
        map.put(cloudyImg,"Autumn");
    }

    protected void addMouseEffects(ImageView img)
    {
        addHoverEffect(img);
        addSelection(img);
    }

    protected void addHoverEffect(ImageView img)
    {
        img.setOnMouseEntered(event -> {
            if(selected != img)
                img.setEffect(new DropShadow(10, Color.RED));
        });

        img.setOnMouseExited(event -> {
            if(selected != img)
                img.setEffect(new DropShadow(0, Color.BLUE));
        });

    }

    protected void addSelection(ImageView img)
    {
        img.setOnMousePressed(event -> {
            if(selected != null)
            selected.setEffect(new DropShadow(0, Color.BLUE));
            if(selected != img)
            {
                selected = img;
                img.setEffect(new DropShadow(15, new Color(0,0.5,0,1)));
            }
            else selected = null;
        });
    }

    public void unselectAll()
    {
        if(selected!=null)
        selected.setEffect(new DropShadow(0, Color.BLUE));
        selected = null;
    }

    public String getSelectedPart()
    {
        if(selected == null)
            return null;
        else
            return map.get(selected);
    }
}
