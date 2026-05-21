package com.mycompany.projetpeinture2026;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class MainApp extends Application {
    // Notre "Modèle" / l'objet qui contiendra toutes les données du projet
    private Batiment batimentActuel;
    private List<Revetement> catalogue; // Liste pour le catalogue 

    @Override
    public void start(Stage primaryStage) {
        catalogue = chargerRevetements("Revetement.txt"); // Chargement du catalogue
        batimentActuel = new Batiment(1, false, new ArrayList<>());  // Initialisation 

        primaryStage.setTitle("Devis Estimatif de Bâtiment");

        // Création du conteneur principal avec des onglets
        TabPane tabPane = new TabPane();

        // ONGLET 1 : SAISIE DES DONNÉES 
        Tab tabSaisie = new Tab("1. Saisie des éléments", creerVueSaisie());
        tabSaisie.setClosable(false); // Empêche de fermer l'onglet avec une petite croix

        // ONGLET 2 : CALCUL DU DEVIS 
        Tab tabDevis = new Tab("2. Détail du devis", creerVueDevis());
        tabDevis.setClosable(false);

        // ONGLET 3 : PLAN 2D
        Tab tabPlan = new Tab("3. Visualisation 2D", creerVuePlan());
        tabPlan.setClosable(false);

        // Ajout des onglets dans la fenêtre
        tabPane.getTabs().addAll(tabSaisie, tabDevis, tabPlan);

        // Définition de la scène principale (largeur 900px, hauteur 600px)
        Scene scene = new Scene(tabPane, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private List<Revetement> chargerRevetements(String cheminFichier) {
    List<Revetement> liste = new ArrayList<>();
    try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(cheminFichier))) {
        br.readLine(); // Sauter la ligne d'entête
        String ligne;
        while ((ligne = br.readLine()) != null) {
            String[] d = ligne.split(";");
            if (d.length == 6) {
                liste.add(new Revetement(
                    Double.parseDouble(d[5]), 
                    Integer.parseInt(d[0]), 
                    d[1], 
                    d[2].equals("1"), 
                    d[3].equals("1"), 
                    d[4].equals("1")
                ));
            }
        }
    } catch (Exception e) {
        System.out.println("Erreur lors du chargement du catalogue : " + e.getMessage());
    }
    return liste;
}
    // Méthode pour construire l'interface de saisie
    private VBox creerVueSaisie() {
        VBox conteneur = new VBox(15); // Espacement de 15 pixels entre les éléments
        conteneur.setPadding(new Insets(20)); // Marge autour du conteneur

        Label titre = new Label("Configuration du bâtiment");
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Choix du type (Maison/Immeuble)
        HBox typeBox = new HBox(10);
        Label lblType = new Label("Type de bâtiment :");
        ComboBox<String> comboType = new ComboBox<>();
        comboType.getItems().addAll("Maison", "Immeuble");
        comboType.setValue(batimentActuel.isType() ? "Immeuble" : "Maison");
        
        // Action pour mettre à jour le modèle quand on change le type
        comboType.setOnAction(e -> {
        batimentActuel.setType(comboType.getValue().equals("Immeuble"));
        });
        typeBox.getChildren().addAll(lblType, comboType);
        
        // Liste visuelle des niveaux
        VBox sectionNiveaux = new VBox(10);
        sectionNiveaux.setStyle("-fx-border-color: #cccccc; -fx-padding: 10; -fx-border-radius: 5;");
        Label lblNiv = new Label("Gestion des Niveaux");
        lblNiv.setStyle("-fx-font-weight: bold;");
    
        ListView<String> listeNiveauxUI = new ListView<>();
        HBox boutonsNiveau = new HBox(10); // Pour aligner les boutons horizontalement
        Button btnAjouterNiveau = new Button("Ajouter un niveau (+)");
        Button btnGererNiveau = new Button("Gérer le niveau sélectionné");
        btnGererNiveau.setDisable(true); // Désactivé par défaut
                
        // Lien interface-classes
        btnAjouterNiveau.setOnAction( e -> {
            Niveau n = new Niveau(batimentActuel.getNiveaux().size() + 1, 2.50, 0, new ArrayList<>());
            batimentActuel.ajouterNiveau(n);
            listeNiveauxUI.getItems().add("Niveau " + n.getIdNiveau() + " (H: " + n.getH() + "m)");
        });
        
        // Ecouteur de sélection
        // Activer le bouton de gestion uniquement si on clique sur un niveau
        listeNiveauxUI.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            btnGererNiveau.setDisable(newVal == null);
        });
        
        // Action pour ouvrir la fenêtre de gestion
        btnGererNiveau.setOnAction(e -> {
            int indexSelectionne = listeNiveauxUI.getSelectionModel().getSelectedIndex();
            if (indexSelectionne >= 0) {
                Niveau niveauChoisi = batimentActuel.getNiveaux().get(indexSelectionne);
                ouvrirFenetreGestionNiveau(niveauChoisi);
            }
        });
        
        boutonsNiveau.getChildren().addAll(btnAjouterNiveau, btnGererNiveau);
        sectionNiveaux.getChildren().addAll(lblNiv, listeNiveauxUI, boutonsNiveau);
        
        // Visualisation des revêtements du catalogue
        VBox sectionCatalogue = new VBox(5);
        Label lblCat = new Label("Matériaux disponibles dans le catalogue :");
        ComboBox<String> comboCatalogue = new ComboBox<>();
        for (Revetement r : catalogue) {
        comboCatalogue.getItems().add(r.getType() + " - " + r.getPrix() + " €/m²");
        }
        sectionCatalogue.getChildren().addAll(lblCat, comboCatalogue);
        
        conteneur.getChildren().addAll(titre, typeBox, sectionNiveaux, sectionCatalogue);
        return conteneur;
    }
    
    // Méthode pour ouvrir une fenêtre de gestion d'un niveau spécifique
    private void ouvrirFenetreGestionNiveau(Niveau niveau) {
        Stage stageNiveau = new Stage();
        stageNiveau.setTitle("Gestion du Niveau " + niveau.getIdNiveau());

        VBox conteneur = new VBox(15);
        conteneur.setPadding(new Insets(20));

        Label titre = new Label("Appartements du Niveau " + niveau.getIdNiveau());
        titre.setStyle("-fx-font-weight: bold;");

        // Liste pour afficher les appartements de ce niveau
        ListView<String> listeAppartsUI = new ListView<>();
        Label titrePieces = new Label("Pièces de l'appartement sélectionné :");
        titrePieces.setStyle("-fx-font-weight: bold;");                                 //Pour afficahge et suprression des pièces
        ListView<String> listePiecesUI = new ListView<>();
        Button btnSupprimerPiece = new Button("Supprimer la pièce sélectionnée");
        btnSupprimerPiece.setDisable(true); // Désactivé par défaut
        
        // Méthode de rafraichissement sécurisée
        Runnable rafraichirListes = () -> {
            int indexAppartSelectionne = listeAppartsUI.getSelectionModel().getSelectedIndex();
            
            // Recharger la liste des appartements
            listeAppartsUI.getItems().clear();
            for (Appartement app : niveau.getApparts()) {
                listeAppartsUI.getItems().add("Appartement " + app.getIdAppart() + " (" + app.getPieces().size() + " pièces)");
            }
            
            // Conserver la sélection de l'appartement (déclenchera automatiquement le nouvel écouteur d'index)
            if (indexAppartSelectionne >= 0 && indexAppartSelectionne < niveau.getApparts().size()) {
                listeAppartsUI.getSelectionModel().select(indexAppartSelectionne);
            }
        };
        
        
        // Charger les appartements déjà existants s'il y en a
        for (Appartement app : niveau.getApparts()) {
            listeAppartsUI.getItems().add("Appartement " + app.getIdAppart() + " (" + app.getPieces().size() + " pièces)");
        }

        Button btnAjouterAppart = new Button("Ajouter un Appartement (+)");
        Button btnAjouterPiece = new Button("Ajouter une Pièce à l'appartement sélectionné");
        btnAjouterPiece.setDisable(true); // Désactivé tant qu'aucun appart n'est cliqué

        // Action : Créer un nouvel appartement
        btnAjouterAppart.setOnAction(e -> {
            int nouvelId = niveau.getApparts().size() + 1;
            Appartement nouvelAppart = new Appartement(nouvelId, 0, new ArrayList<>());
            niveau.ajouterAppartement(nouvelAppart);
            listeAppartsUI.getItems().add("Appartement " + nouvelId + " (0 pièces)");
        });

        // Écouteur de sélection des appartements : active les boutons et charge les pièces
        listeAppartsUI.getSelectionModel().selectedIndexProperty().addListener((obs, oldIdx, newIdx) -> {
            int index = newIdx.intValue();
            listePiecesUI.getItems().clear(); // On vide l'ancienne liste
            btnSupprimerPiece.setDisable(true); // On désactive la suppression par sécurité
            
            if (index >= 0 && index < niveau.getApparts().size()) {
                btnAjouterPiece.setDisable(false);
                Appartement app = niveau.getApparts().get(index);
                // On remplit la liste du bas avec les pièces de l'appartement
                for (Piece p : app.getPieces()) {
                    listePiecesUI.getItems().add("Pièce " + p.getIdPiece() + " : " + p.getUsage());
                }
            } else {
                btnAjouterPiece.setDisable(true);
            }
        });

        // Écouteur de sélection des pièces : active le bouton de suppression
        listePiecesUI.getSelectionModel().selectedIndexProperty().addListener((obs, oldIdx, newIdx) -> {
            btnSupprimerPiece.setDisable(newIdx.intValue() < 0);
        });

        // Action : Lancer la création d'une pièce
        btnAjouterPiece.setOnAction(e -> {
            int indexAppart = listeAppartsUI.getSelectionModel().getSelectedIndex();
            if (indexAppart >= 0) {
                Appartement appartSelectionne = niveau.getApparts().get(indexAppart);
                ouvrirFenetreAjoutPiece(appartSelectionne, niveau);
                rafraichirListes.run(); // Met à jour l'interface dès que la fenêtre se ferme  
            }
        });
        
        // NOUVEAU : Action pour supprimer la pièce sélectionnée
        btnSupprimerPiece.setOnAction(e -> {
            int indexAppart = listeAppartsUI.getSelectionModel().getSelectedIndex();
            int indexPiece = listePiecesUI.getSelectionModel().getSelectedIndex();
            
            if (indexAppart >= 0 && indexPiece >= 0) {
                Appartement app = niveau.getApparts().get(indexAppart);
                
                // Supprime la pièce de la liste d'objets
                app.getPieces().remove(indexPiece);
                
                // Met à jour le compteur d'attribut de l'appartement
                app.setNbrPiece(app.getPieces().size());
                
                // Rafraîchit l'interface graphique
                rafraichirListes.run();
                System.out.println("Pièce supprimée avec succès.");
            }
        });
        HBox boutonsAppart = new HBox(10, btnAjouterAppart, btnAjouterPiece);
        conteneur.getChildren().addAll(titre, listeAppartsUI, boutonsAppart, titrePieces, listePiecesUI, btnSupprimerPiece);
        
        Scene scene = new Scene(conteneur, 450, 520);
        stageNiveau.setScene(scene);
        stageNiveau.show();
    }

    // Méthode pour construire l'interface du devis
    private VBox creerVueDevis() {
        VBox conteneur = new VBox(15);
        conteneur.setPadding(new Insets(20));

        Button btnCalculer = new Button("Calculer le devis");
        TextArea zoneAffichageDevis = new TextArea();
        zoneAffichageDevis.setEditable(false); // On empêche l'utilisateur d'écrire dedans

        btnCalculer.setOnAction(evenement -> {
            Devis devis = new Devis(batimentActuel);
            devis.calculer();
            zoneAffichageDevis.setText("Montant total estimé : " + devis.getMontantTotal() + " euros\n\n" +
                                       "(Ici, nous afficherons le détail par revêtement plus tard)");
        });

        conteneur.getChildren().addAll(btnCalculer, zoneAffichageDevis);
        return conteneur;
    }

    // Méthode pour construire l'interface du plan 2D
    private Pane creerVuePlan() {
        Pane zoneDessin = new Pane();
        zoneDessin.setStyle("-fx-background-color: #f4f4f4;");
        // Le code pour dessiner les murs viendra s'insérer ici
        return zoneDessin;
    }
    
    // Méthode pour ouvrir le formulaire de création d'une pièce
    private void ouvrirFenetreAjoutPiece(Appartement appart, Niveau niveauContext) {
        Stage stagePiece = new Stage();
        stagePiece.setTitle("Ajouter une Pièce - Appartement " + appart.getIdAppart());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Saisie de l'usage
        grid.add(new Label("Usage de la pièce (ex: Salon) :"), 0, 0);
        TextField txtUsage = new TextField();
        grid.add(txtUsage, 1, 0);

        // Dimensions de la pièce rectangulaire
        grid.add(new Label("Longueur (m) :"), 0, 1);
        TextField txtLongueur = new TextField();
        grid.add(txtLongueur, 1, 1);

        grid.add(new Label("Largeur (m) :"), 0, 2);
        TextField txtLargeur = new TextField();
        grid.add(txtLargeur, 1, 2);

        // Section Revêtements
        grid.add(new Label("--- Revêtements ---"), 0, 3, 2, 1);

        // Menus déroulants pour les revêtements
        ComboBox<String> comboMur = new ComboBox<>();
        ComboBox<String> comboSol = new ComboBox<>();
        ComboBox<String> comboPlafond = new ComboBox<>();

        comboMur.getItems().add("Aucun");
        comboSol.getItems().add("Aucun");
        comboPlafond.getItems().add("Aucun");
        comboMur.setValue("Aucun");
        comboSol.setValue("Aucun");
        comboPlafond.setValue("Aucun");

        // Filtrage du catalogue selon la destination du revêtement
        for (Revetement r : catalogue) {
            String affichage = r.getType() + " (Réf " + r.getIdRev() + ") - " + r.getPrix() + "€";
            if (r.isPourMur()) comboMur.getItems().add(affichage);
            if (r.isPourSol()) comboSol.getItems().add(affichage);
            if (r.isPourPlafond()) comboPlafond.getItems().add(affichage);
        }

        grid.add(new Label("Murs :"), 0, 4);
        grid.add(comboMur, 1, 4);
        grid.add(new Label("Sol :"), 0, 5);
        grid.add(comboSol, 1, 5);
        grid.add(new Label("Plafond :"), 0, 6);
        grid.add(comboPlafond, 1, 6);

        Button btnValider = new Button("Créer la pièce");
        grid.add(btnValider, 1, 7);

        // Action lors du clic sur Valider
        btnValider.setOnAction(e -> {
            try {
                String usage = txtUsage.getText();
                float L = Float.parseFloat(txtLongueur.getText());
                float l = Float.parseFloat(txtLargeur.getText());
                float surfaceBrute = L * l;

                // Récupération des objets Revetement correspondants
                Revetement revMur = trouverRevetement(comboMur.getValue());
                Revetement revSol = trouverRevetement(comboSol.getValue());
                Revetement revPlafond = trouverRevetement(comboPlafond.getValue());

                // Création du Sol et du Plafond (Strates)
                List<Revetement> listeRevSol = revSol != null ? new ArrayList<>(List.of(revSol)) : new ArrayList<>();
                Strate sol = new Strate(1, surfaceBrute, listeRevSol, new ArrayList<>(), new ArrayList<>());
                
                List<Revetement> listeRevPlafond = revPlafond != null ? new ArrayList<>(List.of(revPlafond)) : new ArrayList<>();
                Strate plafond = new Strate(2, surfaceBrute, new ArrayList<>(), listeRevPlafond, new ArrayList<>());

                // Création des 4 Murs rectangulaires (relatifs)
                List<Revetement> listeRevMur = revMur != null ? new ArrayList<>(List.of(revMur)) : new ArrayList<>();
                Mur m1 = new Mur(1, new float[]{0, 0, L, 0}, true, new ArrayList<>(listeRevMur), new ArrayList<>());
                Mur m2 = new Mur(2, new float[]{L, 0, L, l}, true, new ArrayList<>(listeRevMur), new ArrayList<>());
                Mur m3 = new Mur(3, new float[]{L, l, 0, l}, true, new ArrayList<>(listeRevMur), new ArrayList<>());
                Mur m4 = new Mur(4, new float[]{0, l, 0, 0}, true, new ArrayList<>(listeRevMur), new ArrayList<>());
                
                List<Mur> murs = new ArrayList<>(List.of(m1, m2, m3, m4));

                // Création et ajout de la pièce
                int idPiece = appart.getPieces().size() + 1;
                Piece nouvellePiece = new Piece(idPiece, usage, murs, plafond, sol);
                appart.ajouterPiece(nouvellePiece);

                System.out.println("Pièce '" + usage + "' ajoutée à l'appartement " + appart.getIdAppart() + " !");
                stagePiece.close(); // Fermer la fenêtre après validation

            } catch (NumberFormatException ex) {
                System.out.println("Veuillez entrer des nombres valides pour les dimensions.");
            }
        });

        Scene scene = new Scene(grid, 450, 400);
        stagePiece.setScene(scene);
        stagePiece.showAndWait(); //  "AndWait" pour que la fenêtre précédente se mette à jour après qu'on ait fermé la fenêtre de modification
    }
    
    
    
    
    // Méthode pour retrouver l'objet Revetement à partir de son affichage
    private Revetement trouverRevetement(String affichageChoisi) {
        if (affichageChoisi == null || affichageChoisi.equals("Aucun")) return null;
        
        for (Revetement r : catalogue) {
            String affichage = r.getType() + " (Réf " + r.getIdRev() + ") - " + r.getPrix() + "€";
            if (affichageChoisi.equals(affichage)) {
                return r;
            }
        }
        return null;
    }


    
    public static void main(String[] args) {
        // Lance l'application JavaFX
        launch(args);
    }
}

