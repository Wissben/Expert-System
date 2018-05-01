package FrontEnd;

import Agents.AgentQueryAnswers;
import Agents.CentralAgent;
import BackEnd.Database.Product;
import BackEnd.ExpertSys.VariableMapper;
import BackEnd.Types.*;
import FrontEnd.Widgets.BodyChoice;
import FrontEnd.Widgets.WeatherChoice;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;


/**
 * Created by ressay on 22/04/18.
 */
public class AttributeChoice implements Initializable
{

    @FXML
    private AnchorPane bodyPane;

    @FXML
    private Button lookup;

    @FXML
    private Slider lengthSlider;

    @FXML
    private Slider tempSlider;

    @FXML
    private ChoiceBox<String> usage;

    @FXML
    private TextField priceField;

    @FXML
    private TextField weightField;

    @FXML
    private TextField heightField;

    @FXML
    private AnchorPane weatherPane;

    @FXML
    private Text tempText;

    @FXML
    private Button reset;

    protected BodyChoice bodyController;

    protected WeatherChoice weatherController;

    protected LinkedList<TextField> fields = new LinkedList<>();

    boolean tempChanged = false;
    boolean lengthChanged = false;
    CentralAgent agent = null;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        bodyController = (BodyChoice) initController(bodyPane,"Widgets/BodyChoice.fxml");
        weatherController = (WeatherChoice) initController(weatherPane,"Widgets/WeatherChoice.fxml");
        initButton();
        initSlider();
        initUsages();
        initNumTextFields();
        fields.add(priceField);
        fields.add(weightField);
        fields.add(heightField);
    }

    protected Initializable initController(Pane pane,String path)
    {
//        FXMLLoader fxmlLoader = new FXMLLoader();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        try
        {

            Pane p = loader.load();
            pane.getChildren().addAll(p);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return loader.getController();
    }

    protected void initUsages()
    {
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll("Sport","Casual","Accessory");
        usage.setItems(list);
    }

    protected void initButton()
    {
        lookup.setOnAction(event -> {
            VariableMapper mapper = generateMapper();
            agent.sendQuery(mapper);
        });

        reset.setOnAction(event -> reset());
    }

    protected void reset()
    {
        for(TextField field : fields)
            field.clear();


        weatherController.unselectAll();
        bodyController.unselectAll();

        tempSlider.setValue(50);
        lengthSlider.setValue(50);
        tempSlider.setStyle("");
        tempText.setText("None");
        lengthSlider.setStyle("");
        tempChanged = false;
        lengthChanged = false;

        usage.getSelectionModel().clearSelection();
    }

    public void showAnswer(AgentQueryAnswers answers)
    {
        Parent root;
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Answer");
            stage.setScene(new Scene(root));
            stage.show();
            MainWindow controller = loader.getController();
            controller.updateWindow(answers);
        } catch (IOException e)
        {
            e.printStackTrace();
        }


    }

    protected void initSlider()
    {
        tempSlider.setValue(50);
        lengthSlider.setValue(50);
        tempSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            tempChanged = true;
            double red = newValue.doubleValue()/100;
            double other = 1 - newValue.doubleValue()/100;
            if(red > 0.5)
                other -= 0.1;
            if(other<0) other = 0;
            int temp = -15+newValue.intValue()*65/100;
            String color = new Color(red,other,other,1).toString().substring(2,8);
            tempSlider.setStyle("-fx-control-inner-background: #"+color);
            tempText.setText(temp+"");
        });
        lengthSlider.valueProperty().addListener((observable) ->{
            lengthChanged = true;
            lengthSlider.setStyle("-fx-control-inner-background: #00CA5A");
        });

    }

    protected void initNumTextFields()
    {
        forceFieldToNumbers(priceField);
        forceFieldToNumbers(weightField);
        forceFieldToNumbers(heightField);
    }

    protected void forceFieldToNumbers(TextField textField)
    {
        textField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    protected VariableValue getTFValue(TextField textField)
    {
        if(textField.getText().length()==0)
            return null;
        return new DoubleValue(Double.parseDouble(textField.getText()));
    }

    protected VariableValue getTemperature()
    {
        if(!tempChanged)
            return null;
        int temp = -15+(int)tempSlider.getValue()*65/100;
        return new DoubleValue(temp);
    }

    protected VariableValue getLength()
    {
        if(!lengthChanged)
            return null;
        double length = lengthSlider.getValue()/100;
        return new DoubleValue(length);
    }

    protected VariableValue getPrice()
    {
        if(priceField.getText().length()==0)
            return null;
        double price = Double.parseDouble(priceField.getText());
        return new IntervalVariableValue<>(price,null,true,false);
    }

    protected VariableValue getWeather()
    {
        if(weatherController.getSelectedPart() == null)
            return null;
        return new StringVariableValue(weatherController.getSelectedPart());
    }

    protected VariableValue getPosition()
    {
        if(bodyController.getSelectedPart() == null)
            return null;
        return new StringVariableValue(bodyController.getSelectedPart());
    }

    protected VariableValue getUsages()
    {
        if(usage.getSelectionModel().getSelectedItem() == null)
            System.out.println("usage is null!!");
        if(usage.getSelectionModel().getSelectedItem() == null)
            return null;
        return new StringVariableValue(usage.getValue());
    }


    public VariableMapper generateMapper()
    {
        VariableMapper mapper = new VariableMapper();
        mapper.addVariableValue("Usages",getUsages());
        mapper.addVariableValue("Weight",getTFValue(weightField));
        mapper.addVariableValue("Height",getTFValue(heightField));
        mapper.addVariableValue("Temperature",getTemperature());
        mapper.addVariableValue("L",getLength());
        mapper.addVariableValue("Price",getPrice());
        mapper.addVariableValue("Position",getPosition());
        mapper.addVariableValue("Season", getWeather());
        return mapper;
    }

    public CentralAgent getAgent()
    {
        return agent;
    }

    public void setAgent(CentralAgent agent)
    {
        this.agent = agent;
    }
}
