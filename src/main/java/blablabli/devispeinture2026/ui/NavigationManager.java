package blablabli.devispeinture2026.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NavigationManager {

    public static void goTo(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(
                NavigationManager.class.getResource("/blablabli/devispeinture2026/ui/" + fxml)
            );

            Scene scene = new Scene(loader.load());
            Stage stage = MainApp.getPrimaryStage();
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
