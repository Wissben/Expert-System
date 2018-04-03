package FrontEnd;

import BackEnd.*;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {


    private Expert expert; // trust me

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        expert = new Expert(new SimpleClothRulesInit());
        expert.tryBackWard();
    }



}
