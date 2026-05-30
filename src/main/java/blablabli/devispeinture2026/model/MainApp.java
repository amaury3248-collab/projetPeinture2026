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
    // l'objet Batiment contiendra toutes les données du projet
    private Batiment batimentActuel;
    private List<Revetement> catalogue; // Liste pour le catalogue 

    @Override
    public void start(Stage primaryStage) {
        catalogue = chargerRevetements("Revetement.txt"); // Chargement du catalogue
        batimentActuel = new Batiment(1, true, new ArrayList<>());  // Initialisation (immeuble = true)

        primaryStage.setTitle("Devis Estimatif de Bâtiment"); // Titre sur la bordure de fenetre

        // Création du conteneur principal avec des onglets
        TabPane tabPane = new TabPane();

        // 1er onglet 
        Tab tabSaisie = new Tab("1. Saisie des éléments", creerVueSaisie());
        tabSaisie.setClosable(false); // Empêche de fermer l'onglet avec une petite croix

        // 2eme onglet (calcul devis) 
        Tab tabDevis = new Tab("2. Détail du devis", creerVueDevis());
        tabDevis.setClosable(false);

        // 3eme onglet (plan 2D)
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
        conteneur.setPadding(new Insets(20)); // Marge autour de la zone du conteneur

        Label titre = new Label("Configuration du bâtiment"); 
        titre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Choix du type (Maison/Immeuble)
        HBox typeBox = new HBox(10);
        Label lblType = new Label("Type de bâtiment :");
        ComboBox<String> comboType = new ComboBox<>();
        comboType.getItems().addAll("Immeuble","Maison");
        comboType.setValue(batimentActuel.isType() ? "Immeuble" : "Maison");
        
        // Action pour mettre à jour le modèle quand on change le type
        comboType.setOnAction(e -> {
        batimentActuel.setType(comboType.getValue().equals("Immeuble"));
        });
        typeBox.getChildren().addAll(lblType, comboType);
        
        // Liste des niveaux (par ligne)
        VBox sectionNiveaux = new VBox(10); // VBox (Vertical Box) occupe toute la largeur
        sectionNiveaux.setStyle("-fx-border-color: #cccccc; -fx-padding: 10; -fx-border-radius: 5;");
        Label lblNiv = new Label("Gestion des Niveaux");
        lblNiv.setStyle("-fx-font-weight: bold;");
    
        ListView<String> listeNiveauxUI = new ListView<>();
        HBox boutonsNiveau = new HBox(10); // Pour aligner les boutons horizontalement
        Button btnAjouterNiveau = new Button("Ajouter un niveau");
        Button btnGererNiveau = new Button("Gérer le niveau sélectionné");
        btnGererNiveau.setDisable(true); // Désactivé par défaut
                
        // Lien interface-classes (ici création d'un niveau)
        btnAjouterNiveau.setOnAction( e -> {
            Niveau n = new Niveau(batimentActuel.getNiveaux().size() + 1, 2.50, 0, new ArrayList<>());
            batimentActuel.ajouterNiveau(n);
            listeNiveauxUI.getItems().add("Niveau " + n.getIdNiveau() + " (H: " + n.getH() + "m)");
        });
        
        // Ecouteur de sélection : active le bouton de gestion uniquement si on clique sur un niveau
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
        
        // Implémentation des modifications
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
       
        

        // Section sauvegarde et chargement 
        VBox sectionFichiers = new VBox(10);
        sectionFichiers.setStyle("-fx-border-color: #cccccc; -fx-padding: 10; -fx-border-radius: 5;");
        Label lblFichiers = new Label("Gestion des fichiers du projet");
        lblFichiers.setStyle("-fx-font-weight: bold;");
        
        HBox actionsBox = new HBox(10);
        TextField txtNomFichier = new TextField("sauvegarde_batiment.txt");
        txtNomFichier.setPrefWidth(180);
        
        Button btnSauvegarder = new Button("Sauvegarder");
        Button btnCharger = new Button("Charger le fichier");
        
        // Action Sauvegarde
        btnSauvegarder.setOnAction(e -> {
            String nom = txtNomFichier.getText();
            if (nom != null && !nom.trim().isEmpty()) {
                Batiment.sauvegarderBatiment(batimentActuel, nom);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Projet sauvegardé dans " + nom);
                alert.showAndWait();
            }
        });
        
        // Action Chargement
        btnCharger.setOnAction(e -> {
            String nom = txtNomFichier.getText();
            if (nom != null && !nom.trim().isEmpty()) {
                // On utilise la méthode statique de Batiment en lui passant le catalogue en paramètrte
                Batiment batCharge = Batiment.chargerBatiment(nom, catalogue);
                if (batCharge != null) {
                    batimentActuel = batCharge; // Remplace le modèle actuel par celui défini dans le fichier externe
                    
                    // Rafraîchir l'affichage graphique de la liste des niveaux
                    listeNiveauxUI.getItems().clear();
                    for (Niveau n : batimentActuel.getNiveaux()) {
                        listeNiveauxUI.getItems().add("Niveau " + n.getIdNiveau() + " (H: " + n.getH() + "m)");
                    }
                    
                    // Mettre à jour la ComboBox du type de bâtiment
                    comboType.setValue(batimentActuel.isType() ? "Immeuble" : "Maison");
                    
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Projet chargé avec succès !");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Impossible de charger le fichier.");
                    alert.showAndWait();
                }
            }
        });
        
        actionsBox.getChildren().addAll(txtNomFichier, btnSauvegarder, btnCharger);
        sectionFichiers.getChildren().addAll(lblFichiers, actionsBox);
        
        // Ajout global au conteneur
        conteneur.getChildren().addAll(titre, typeBox, sectionNiveaux, sectionCatalogue, sectionFichiers);
        return conteneur;
    }
    
    // Méthode pour ouvrir une fenêtre de gestion d'un niveau spécifique
    private void ouvrirFenetreGestionNiveau(Niveau niveau) {
        Stage stageNiveau = new Stage(); // Objet fenetre --> Stage = nouvelle fenetre qui s'ouvre
        stageNiveau.setTitle("Gestion du Niveau " + niveau.getIdNiveau());

        VBox conteneur = new VBox(15);
        conteneur.setPadding(new Insets(20));

        Label titre = new Label("Appartements du Niveau " + niveau.getIdNiveau());
        titre.setStyle("-fx-font-weight: bold;");

        // Liste pour afficher les appartements de ce niveau
        ListView<String> listeAppartsUI = new ListView<>();
        Label titrePieces = new Label("Pièces de l'appartement sélectionné :");
        titrePieces.setStyle("-fx-font-weight: bold;");
        ListView<String> listePiecesUI = new ListView<>();
        
        Button btnSupprimerPiece = new Button("Supprimer la pièce sélectionnée");
        btnSupprimerPiece.setStyle(
            "-fx-background-color: #FFB7B2; " + //Couleur rosée
            "-fx-text-fill: #333333; " + //gris
            "-fx-background-radius: 5;"
        );
        btnSupprimerPiece.setDisable(true); // Désactivé par défaut
        
        // Méthode de rafraichissement
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

        Button btnAjouterAppart = new Button("Ajouter un Appartement");
        Button btnAjouterPiece = new Button("Ajouter une Pièce à l'appartement sélectionné");
        btnAjouterPiece.setDisable(true); // Désactivé tant qu'aucun appart n'est cliqué

        // Action création nouvel appartement
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
        
        // Action pour supprimer la pièce sélectionnée
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
            
        // Agrandissement automatique : On demande à la zone de texte de s'étirer verticalement autant que possible
        VBox.setVgrow(zoneAffichageDevis, Priority.ALWAYS);
            
            zoneAffichageDevis.setText(devis.afficherDetail());
        });

        conteneur.getChildren().addAll(btnCalculer, zoneAffichageDevis);
        return conteneur;
    }

    
    // Méthode pour ouvrir le formulaire de création d'une pièce
    private void ouvrirFenetreAjoutPiece(Appartement appart, Niveau niveauContext) {
        Stage stagePiece = new Stage();
        stagePiece.setTitle("Ajouter une Pièce - Appartement " + appart.getIdAppart());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Saisie utilisateur
        grid.add(new Label("Usage de la pièce (ex: Salon) :"), 0, 0);
        TextField txtUsage = new TextField();
        grid.add(txtUsage, 1, 0);

        // Dimensions et position initiale de la pièce rectangulaire
        grid.add(new Label("Position X de départ (m) :"), 0, 1);
        TextField txtX = new TextField("0"); // Par défaut à 0
        grid.add(txtX, 1, 1);
        
        grid.add(new Label("Position Y de départ (m) :"), 0, 2);
        TextField txtY = new TextField("0");
        grid.add(txtY, 1, 2);
        
        grid.add(new Label("Longueur (m) :"), 0, 3);
        TextField txtLongueur = new TextField();
        grid.add(txtLongueur, 1, 3);

        grid.add(new Label("Largeur (m) :"), 0, 4);
        TextField txtLargeur = new TextField();
        grid.add(txtLargeur, 1, 4);

        // Section Revêtements
        grid.add(new Label("--- Revêtements ---"), 0, 5, 2, 1);

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

        grid.add(new Label("Murs :"), 0, 6);
        grid.add(comboMur, 1, 6);
        grid.add(new Label("Sol :"), 0, 7);
        grid.add(comboSol, 1, 7);
        grid.add(new Label("Plafond :"), 0, 8);
        grid.add(comboPlafond, 1, 8);

        // Formulaire pour les ouvertures
        grid.add(new Label("--- Ouvertures ---"), 0, 9, 2, 1);
        
        grid.add(new Label("Nombre de portes :"), 0, 10);
        Spinner<Integer> spinPortes = new Spinner<>(0, 10, 0); // Minimum 0, Maximum 10, Valeur par défaut 0
        grid.add(spinPortes, 1, 10);

        grid.add(new Label("Nombre de fenêtres :"), 0, 11);
        Spinner<Integer> spinFenetres = new Spinner<>(0, 11, 0);
        grid.add(spinFenetres, 1, 11);

        // On replace le bouton valider sur la ligne suivante
        Button btnValider = new Button("Créer la pièce");
        // Fond vert menthe pastel, texte gris foncé, avec des bords légèrement arrondis
        btnValider.setStyle(
            "-fx-background-color: #77DD77; " +
            "-fx-text-fill: #333333; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 5;"
        );
        grid.add(btnValider, 1, 12);

        // Action lors du clic sur Valider
        btnValider.setOnAction(e -> {
            try {
                String usage = txtUsage.getText();
                double startX = Double.parseDouble(txtX.getText());
                double startY = Double.parseDouble(txtY.getText());
                double L = Double.parseDouble(txtLongueur.getText());
                double l = Double.parseDouble(txtLargeur.getText());
                double surfaceBrute = L * l;

                // Récupération des objets Revetement correspondants
                Revetement revMur = trouverRevetement(comboMur.getValue());
                Revetement revSol = trouverRevetement(comboSol.getValue());
                Revetement revPlafond = trouverRevetement(comboPlafond.getValue());

                // Création du Sol et du Plafond (Strates)
                List<Revetement> listeRevSol = revSol != null ? new ArrayList<>(List.of(revSol)) : new ArrayList<>();
                Strate sol = new Strate(1, surfaceBrute, listeRevSol, new ArrayList<>(), new ArrayList<>());
                
                List<Revetement> listeRevPlafond = revPlafond != null ? new ArrayList<>(List.of(revPlafond)) : new ArrayList<>();
                Strate plafond = new Strate(2, surfaceBrute, new ArrayList<>(), listeRevPlafond, new ArrayList<>());

                // Création des 4 Murs rectangulaires ((~~relatifs~)), on est passés à absolus avec la vue 2D (coordonnées)
                List<Revetement> listeRevMur = revMur != null ? new ArrayList<>(List.of(revMur)) : new ArrayList<>();
                Mur m1 = new Mur(1, new double[]{startX, startY, startX + L, startY}, true, new ArrayList<>(listeRevMur), new ArrayList<>());
                Mur m2 = new Mur(2, new double[]{startX + L, startY, startX + L, startY + l}, true, new ArrayList<>(listeRevMur), new ArrayList<>());
                Mur m3 = new Mur(3, new double[]{startX + L, startY + l, startX, startY + l}, true, new ArrayList<>(listeRevMur), new ArrayList<>());
                Mur m4 = new Mur(4, new double[]{startX, startY + l, startX, startY}, true, new ArrayList<>(listeRevMur), new ArrayList<>());
                
                List<Mur> murs = new ArrayList<>(List.of(m1, m2, m3, m4));
                
                // Répartition des ouvertures sur les murs créés
                int nbPortes = spinPortes.getValue();
                int nbFenetres = spinFenetres.getValue();
                int indexMur = 0;
                
                // On distribue cycliquement les portes sur les murs
                for (int i = 0; i < nbPortes; i++) {
                    murs.get(indexMur % 4).ajouterOuverture(new Porte());
                    indexMur++;
                }
                // On distribue cycliquement les fenêtres à la suite
                for (int i = 0; i < nbFenetres; i++) {
                    murs.get(indexMur % 4).ajouterOuverture(new Fenetre());
                    indexMur++;
                }

                // Création et ajout de la pièce
                int idPiece = appart.getPieces().size() + 1;
                Piece nouvellePiece = new Piece(idPiece, usage, murs, plafond, sol);
                appart.ajouterPiece(nouvellePiece);

                System.out.println("Pièce '" + usage + "' ajoutée à l'appartement " + appart.getIdAppart() + " !");
                stagePiece.close(); // ferme la fenêtre après validation

            } catch (NumberFormatException ex) {
                System.out.println("Veuillez entrer des nombres valides pour les dimensions.");
            }
        });

        Scene scene = new Scene(grid, 450, 550);
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
    

    // Méthode pour construire l'interface du plan 2D
    private VBox creerVuePlan() {
        VBox conteneur = new VBox(15);
        conteneur.setPadding(new Insets(20));

        HBox barreOutils = new HBox(15);
        Button btnActualiser = new Button("Actualiser le plan");
        ComboBox<String> comboNiveau = new ComboBox<>();
        barreOutils.getChildren().addAll(btnActualiser, comboNiveau);

        Pane zoneDessin = new Pane();
        // Fond blanc avec bordure pour la zone de dessin
        zoneDessin.setStyle("-fx-background-color: white; -fx-border-color: #aaaaaa; -fx-border-width: 2px;");
        zoneDessin.setPrefSize(800, 500);

        // Action du bouton d'actualisation
        btnActualiser.setOnAction(e -> {
            comboNiveau.getItems().clear();
            if (batimentActuel != null && !batimentActuel.getNiveaux().isEmpty()) {
                for (Niveau n : batimentActuel.getNiveaux()) {
                    comboNiveau.getItems().add("Niveau " + n.getIdNiveau());
                }
                comboNiveau.getSelectionModel().select(0); // Sélectionne le 1er niveau par défaut
                dessinerNiveau(zoneDessin, batimentActuel.getNiveaux().get(0));
            }
        });

        // Changement de niveau dans le menu déroulant
        comboNiveau.setOnAction(e -> {
            int index = comboNiveau.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                dessinerNiveau(zoneDessin, batimentActuel.getNiveaux().get(index));
            }
        });

        conteneur.getChildren().addAll(barreOutils, zoneDessin);
        return conteneur;
    }

    // Méthode pour dessiner les murs sur le Pane
    private void dessinerNiveau(Pane zoneDessin, Niveau niveau) {
        zoneDessin.getChildren().clear(); // On nettoie le dessin précédent

        double echelle = 40.0; // 1 mètre = 40 pixels à l'écran (d'où échelle)
        
        // Marge
        double margeEcranX = 50.0; 
        double margeEcranY = 50.0;

        for (Appartement appart : niveau.getApparts()) {
            for (Piece piece : appart.getPieces()) {
                
                double[] coordsPiece = piece.getMurs().get(0).getCoords(); //Coords 1er murs pour placer l'étiquette
                // Dessin de chaque mur de la pièce
                for (Mur mur : piece.getMurs()) {
                    double[] c = mur.getCoords();
                    
                    javafx.scene.shape.Line ligneMur = new javafx.scene.shape.Line(
                            (c[0] * echelle) + margeEcranX, (c[1] * echelle) + margeEcranY,
                            (c[2] * echelle) + margeEcranX, (c[3] * echelle) + margeEcranY
                    );
                    
                    ligneMur.setStrokeWidth(4); // Épaisseur du mur
                    // Mur extérieur en bleu, mur intérieur en noir
                    if (mur.isMurExt()) {
                        ligneMur.setStroke(javafx.scene.paint.Color.DARKBLUE);
                    } else {
                        ligneMur.setStroke(javafx.scene.paint.Color.BLACK);
                    }
                    
                    zoneDessin.getChildren().add(ligneMur);
                }
                
                // Ajout d'une petite étiquette de texte au centre de la pièce
                javafx.scene.text.Text etiquette = new javafx.scene.text.Text(
                        (coordsPiece[0] * echelle) + margeEcranX + 10, 
                        (coordsPiece[1] * echelle) + margeEcranY + 20, 
                        piece.getUsage() + " (Apt " + appart.getIdAppart() + ")"
                );
                etiquette.setFill(javafx.scene.paint.Color.GRAY);
                zoneDessin.getChildren().add(etiquette);
                
                
                }
            }
        }
    



    
    public static void main(String[] args) {
        // Lance l'application JavaFX
        launch(args);
    }
}
