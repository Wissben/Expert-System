package FrontEnd;

import BackEnd.Database.DBQuery;
import FrontEnd.Widgets.BodyChoice;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Created by ressay on 08/05/18.
 */
public class AddAgent implements Initializable
{
    @FXML
    private AnchorPane bodyAnchor;

    @FXML
    private Button addButton;

    protected BodyChoice bodyController;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        bodyController = (BodyChoice) initController(bodyAnchor,"Widgets/BodyChoice.fxml");
        addButton.setOnAction(event -> addAgent());
    }

    protected Initializable initController(Pane pane, String path)
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

    protected void addAgent()
    {
        if(bodyController.getSelectedPart() == null)
        {
            ((Stage)addButton.getScene().getWindow()).close();
        }
        try
        {
            File inputFile = new File("src/agentCount");
            Scanner reader = new Scanner(new FileReader(inputFile));
            int agentCount = reader.nextInt()+1;
            reader.close();
            String output = "<Agents/Agent/=/agent"+agentCount+">=<Position/String/!=/"+bodyController.getSelectedPart()+">,";
            PrintWriter writer = new PrintWriter("src/ruleFindMe"+agentCount);
            writer.write(output);
            writer.close();
            writer = new PrintWriter("src/agentCount");
            writer.write(""+(agentCount));
            writer.close();
            String query = "UPDATE Product SET agentID = "+agentCount+
                    " WHERE agentID is NULL AND Position = '"+bodyController.getSelectedPart()+"'";
            DBQuery dbQuery = new DBQuery(Main.getdBconnection());
            dbQuery.setQuery(query);
            dbQuery.executeUpdateQuery();
            Main.addAgent(agentCount);

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        ((Stage)addButton.getScene().getWindow()).close();
    }
}
