package FrontEnd.UI;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * CREATED BY wiss ON 19:52 INSIDE FrontEnd.UI FOR THE PROJECT Expert-System
 **/

public class MainWindow extends Application
{
    public static void main(String[] args)
    {
        launch(args);

    }


    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("FrontEnd/UI/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.onCloseRequestProperty().setValue(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent event)
            {
                primaryStage.close();
            }
        });
    }

}
