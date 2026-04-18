package blablabli.devispeinture2026.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/blablabli/devispeinture2026/ui/main.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setTitle("Projet Peinture 2026");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
