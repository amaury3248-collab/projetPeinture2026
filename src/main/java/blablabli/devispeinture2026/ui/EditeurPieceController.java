package blablabli.devispeinture2026.ui;

import blablabli.devispeinture2026.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EditeurPieceController {

    @FXML private ToggleButton btnSol;
    @FXML private ToggleButton btnPlafond;
    @FXML private ToggleButton btnMurFond;
    @FXML private ToggleButton btnMurDroite;
    @FXML private ToggleButton btnMurGauche;
    @FXML private ToggleButton btnMurArriere;
    @FXML private ToggleButton btnExterieur;

    @FXML private TextField tfLargeur;
    @FXML private TextField tfLongueur;
    @FXML private TextField tfHauteur;

    @FXML private ComboBox<Revetement> cbRevetement;

    @FXML private CheckBox chkOuverture;
    @FXML private ComboBox<TypeOuverture> cbTypeOuverture;
    @FXML private TextField tfHauteurBasOuverture;

    @FXML private Label lblErreur;

    private TypeEntite entiteCourante;
    private Niveau niveauCourant;
    private Piece pieceCourante;

    public void setContexte(Niveau niveau, Piece piece) {
        this.niveauCourant = niveau;
        this.pieceCourante = piece;
    }

    @FXML
    public void initialize() {

        btnSol.setOnAction(e -> onEntiteChoisie(TypeEntite.SOL_INTERIEUR));
        btnPlafond.setOnAction(e -> onEntiteChoisie(TypeEntite.PLAFOND));
        btnMurFond.setOnAction(e -> onEntiteChoisie(TypeEntite.MUR_FOND));
        btnMurDroite.setOnAction(e -> onEntiteChoisie(TypeEntite.MUR_DROITE));
        btnMurGauche.setOnAction(e -> onEntiteChoisie(TypeEntite.MUR_GAUCHE));
        btnMurArriere.setOnAction(e -> onEntiteChoisie(TypeEntite.MUR_ARRIERE));
        btnExterieur.setOnAction(e -> onEntiteChoisie(TypeEntite.MUR_EXTERIEUR));

        cbTypeOuverture.getItems().setAll(TypeOuverture.values());
        cbTypeOuverture.setDisable(true);
        tfHauteurBasOuverture.setDisable(true);

        chkOuverture.selectedProperty().addListener((obs, oldV, newV) -> {
            cbTypeOuverture.setDisable(!newV);
            tfHauteurBasOuverture.setDisable(!newV);
        });
    }

    private void onEntiteChoisie(TypeEntite type) {
        entiteCourante = type;
        lblErreur.setText("");

        switch (type) {
            case SOL_INTERIEUR:
            case SOL_EXTERIEUR:
                cbRevetement.getItems().setAll(CatalogueRevetements.getPourSol());
                tfHauteur.setDisable(true);
                break;

            case PLAFOND:
                cbRevetement.getItems().setAll(CatalogueRevetements.getPourPlafond());
                tfHauteur.setDisable(true);
                break;

            default:
                cbRevetement.getItems().setAll(CatalogueRevetements.getPourMur());
                tfHauteur.setDisable(false);
                break;
        }
    }

    @FXML
    private void onValiderEntite() {
        lblErreur.setText("");

        if (entiteCourante == null) {
            lblErreur.setText("Choisissez une entité.");
            return;
        }

        double largeur, longueur, hauteur = 0;

        try {
            largeur = Double.parseDouble(tfLargeur.getText());
            longueur = Double.parseDouble(tfLongueur.getText());
            if (!tfHauteur.isDisabled()) {
                hauteur = Double.parseDouble(tfHauteur.getText());
            }
        } catch (Exception e) {
            lblErreur.setText("Dimensions invalides.");
            return;
        }

        if (!tfHauteur.isDisabled()) {
            double hNiv = niveauCourant.getH();
            if (Math.abs(hauteur - hNiv) > 1e-6) {
                lblErreur.setText("Hauteur différente du niveau (" + hNiv + " m).");
                return;
            }
        }

        Revetement rev = cbRevetement.getValue();
        if (rev == null) {
            lblErreur.setText("Choisissez un revêtement.");
            return;
        }

        if (chkOuverture.isSelected()) {
            TypeOuverture t = cbTypeOuverture.getValue();
            if (t == null) {
                lblErreur.setText("Type d'ouverture manquant.");
                return;
            }

            double hBas = Double.parseDouble(tfHauteurBasOuverture.getText());

            if (t == TypeOuverture.PORTE &&
                entiteCourante == TypeEntite.MUR_EXTERIEUR &&
                hBas > 0.01) {

                lblErreur.setText("Porte interdite en hauteur sur mur externe.");
                return;
            }
        }

        lblErreur.setText("Entité enregistrée.");
    }
}
