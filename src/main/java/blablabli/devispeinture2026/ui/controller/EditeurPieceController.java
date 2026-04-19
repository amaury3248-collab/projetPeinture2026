package blablabli.devispeinture2026.ui.controller;

import blablabli.devispeinture2026.model.*;
import blablabli.devispeinture2026.ui.utils.VerificateurCoherence;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EditeurPieceController {

    @FXML private CheckBox chkOuverture;
    @FXML private ComboBox<String> cbTypeOuverture;
    @FXML private TextField tfHauteurBasOuverture;
    @FXML private Label lblErreur;

    private Piece pieceCourante;
    private Mur murCourant;

    @FXML
    public void initialize() {

        // Exemple minimal
        pieceCourante = new Piece("Salon", 4, 3, 2.5);
        murCourant = pieceCourante.getMurs().get(0);

        cbTypeOuverture.getItems().addAll("PORTE", "FENETRE");

        cbTypeOuverture.setDisable(true);
        tfHauteurBasOuverture.setDisable(true);

        chkOuverture.selectedProperty().addListener((obs, oldV, newV) -> {
            cbTypeOuverture.setDisable(!newV);
            tfHauteurBasOuverture.setDisable(!newV);
        });
    }

    @FXML
    public void valider() {

        lblErreur.setText("");

        try {
            if (chkOuverture.isSelected()) {

                String type = cbTypeOuverture.getValue();
                double hBas = Double.parseDouble(tfHauteurBasOuverture.getText());

                Ouverture ouv;

                if ("PORTE".equals(type)) {
                    ouv = new Porte(hBas);
                } else {
                    ouv = new Fenetre(hBas);
                }

                VerificateurCoherence.verifierOuverture(murCourant, ouv);
                murCourant.ajouterOuverture(ouv);
            }

        } catch (Exception e) {
            lblErreur.setText(e.getMessage());
        }
    }
}
