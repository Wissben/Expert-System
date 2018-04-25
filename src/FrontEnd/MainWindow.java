package FrontEnd;

import Agents.AgentQueryAnswers;
import BackEnd.Database.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by ressay on 22/04/18.
 */
public class MainWindow implements Initializable
{
    @FXML
    private ListView<Product> productList;

    @FXML
    private Button showProduct;

    protected String defaultImage = "clothesImages/default.png";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productList.setCellFactory(listView -> new ListCell<Product>() {
            private ImageView imageView = new ImageView();
            @Override
            public void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String path = defaultImage;
                    if(product.getPathToImage() != null)
                        path = product.getPathToImage();
                    File file = new File(path);
                    Image image = new Image(file.toURI().toString());
                    imageView.setImage(image);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(130);
                    setText(product+"");
                    setGraphic(imageView);
                }
            }
        });


    }

    public void updateWindow(AgentQueryAnswers answers)
    {
        ObservableList<Product> items = FXCollections.observableArrayList ();
        for(String agent : answers.getAgents())
        {
            items.addAll(answers.getAnswer(agent).getProducts());
        }
        productList.setItems(items);
    }
}
