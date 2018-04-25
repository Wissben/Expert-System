package FrontEnd.UI;

import BackEnd.ExpertSys.Expert;
import jade.wrapper.StaleProxyException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable
{


    BackEndToUi backEndToUi;
    private Expert expert; // trust me
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="addAnnexAgent"
    private Button addAnnexAgent; // Value injected by FXMLLoader

    @FXML // fx:id="display"
    private TextArea display; // Value injected by FXMLLoader


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {


        this.backEndToUi = new BackEndToUi(this);

        addAnnexAgent.setOnAction((ActionEvent event) ->
        {
//            backEndToUi.addExpert("/home/wiss/CODES/TP-AGENT/Expert-System/ruleFindMe1", backEndToUi.varsPath, backEndToUi.rulesPath);
//            backEndToUi.addExpert("/home/wiss/CODES/TP-AGENT/Expert-System/ruleFindMe2", backEndToUi.varsPath, backEndToUi.rulesPath);

            try
            {
                backEndToUi.initAgents();
            } catch (StaleProxyException e)
            {
                e.printStackTrace();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        });
    }
}
