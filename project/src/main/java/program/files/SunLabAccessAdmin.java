package program.files;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SunLabAccessAdmin extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SunLabAccessLogin.class.getResource("sun-lab-access-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 400);
        stage.setTitle("Sun Lab Access Admin Panel");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        launch();

    }

}
