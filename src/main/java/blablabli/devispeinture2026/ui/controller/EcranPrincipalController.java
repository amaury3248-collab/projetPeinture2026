package blablabli.devispeinture2026.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EcranPrincipalController {

    @FXML
    public void ouvrirEditeur(ActionEvent e) {
        try {
            // Charge le fichier FXML de l’éditeur
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/blablabli/devispeinture2026/ui/editeur_piece.fxml")
            );

            // Crée la nouvelle scène
            Scene scene = new Scene(loader.load());

            // Récupère la fenêtre actuelle
            Stage stage = (Stage) ((javafx.scene.Node)e.getSource())
                    .getScene().getWindow();

            // Remplace l’écran actuel par l’éditeur
            stage.setScene(scene);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
