package com.mycompany.projetpeinture2026;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


public class ProjetPeinture2026 {

    public static void main(String[] args) {
        System.out.println("--- DEBUT DU TEST DU MOTEUR DE DEVIS ---");

        Revetement peintureMur = new Revetement(10.95, 1, "Peinture Murs", true, false, false);
        Revetement carrelageSol = new Revetement(49.75, 2, "Carrelage Sol", false, true, false);
        Revetement peinturePlafond = new Revetement(29.90, 9, "Peinture Plafond", false, false, true);

        // 2. Création des ouvertures standards
        Porte porteEntree = new Porte();   // Surface: 0.90 * 2.10 = 1.89 m2
        Fenetre fenetreSalon = new Fenetre(); // Surface: 1.20 * 1.20 = 1.44 m2

        // 3. Création des 4 murs du salon (4 x 4)
        // Mur Nord : avec la porte d'entrée
        Mur murNord = new Mur(1, new float[]{0, 0, 4, 0}, true, 
                new ArrayList<>(Arrays.asList(peintureMur)), 
                new ArrayList<>(Arrays.asList(porteEntree)));

        // Mur Est : avec la fenêtre
        Mur murEst = new Mur(2, new float[]{4, 0, 4, 4}, true, 
                new ArrayList<>(Arrays.asList(peintureMur)), 
                new ArrayList<>(Arrays.asList(fenetreSalon)));

        // Murs Sud et Ouest : murs pleins sans ouvertures
        Mur murSud = new Mur(3, new float[]{4, 4, 0, 4}, true, 
                new ArrayList<>(Arrays.asList(peintureMur)), new ArrayList<>());
        Mur murOuest = new Mur(4, new float[]{0, 4, 0, 0}, true, 
                new ArrayList<>(Arrays.asList(peintureMur)), new ArrayList<>());

        List<Mur> mursSalon = new ArrayList<>(Arrays.asList(murNord, murEst, murSud, murOuest));

        // 4. Création du Sol et du Plafond (Surface de base = 4m x 4m = 16 m2)
        Strate sol = new Strate(1, 16.0f, new ArrayList<>(Arrays.asList(carrelageSol)), new ArrayList<>(), new ArrayList<>());
        Strate plafond = new Strate(2, 16.0f, new ArrayList<>(), new ArrayList<>(Arrays.asList(peinturePlafond)), new ArrayList<>());

        // 5. Assemblage final : Pièce -> Appartement -> Niveau -> Bâtiment
        Piece salon = new Piece(1, "Salon", mursSalon, plafond, sol);
        Appartement monAppart = new Appartement(101, 1, new ArrayList<>(Arrays.asList(salon)));
        
        // On définit une hauteur sous plafond de 2.50m pour le niveau
        Niveau rezDeChaussee = new Niveau(0, 2.50, 1, new ArrayList<>(Arrays.asList(monAppart)));
        
        // false pour dire que c'est une maison (ou true pour immeuble selon ta logique)
        Batiment maMaison = new Batiment(1, false, new ArrayList<>(Arrays.asList(rezDeChaussee)));

        // Calcul et Affichage du Devis
        System.out.println("Batiment assemble. Lancement du calcul...");
        Devis monDevis = new Devis(maMaison);
        monDevis.calculer();
        monDevis.afficherDetail();
        
        
        List<Revetement> catalogue = chargerRevetements("Revetement.txt");
        
        System.out.println("Nombre de revetements charges : "+ catalogue.size());       
    }
    public static List<Revetement> chargerRevetements(String cheminFichier){
        List<Revetement> listeRevetements = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(cheminFichier))){ // Ferme automatiquement le fichier à la fin, meme ene cas d'erreur
            String ligne = br.readLine(); // lit la ligne entete (1) sans rien faire (aucune données)
            
            while ((ligne = br.readLine()) != null){ // Lire les lignes suivantes jusqu'a la fin du fichier
                String[] donnees = ligne.split(";"); // à chaque ";" on coupe la ligne qui est transformé en tableau String
                
                if (donnees.length == 6){
                    
                    int idRev = Integer.parseInt(donnees[0]); // conversion des textes de type String en nombres exploitables pour les calculs ("2" --> 2)
                    String type = donnees[1];                   
                    boolean pourMur = donnees[2].equals("1");
                    boolean pourSol = donnees[3].equals("1");
                    boolean pourPlafond = donnees[4].equals("1");                     
                    double prix = Double.parseDouble(donnees[5]);
                    
                    Revetement rev = new Revetement(prix, idRev, type, pourMur, pourSol, pourPlafond);
                    listeRevetements.add(rev);                  
                }
            }
            
        }catch (IOException e) { // pour tous problèmes relatifs à la gestion du ficher
            System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        } catch (NumberFormatException e) { // par exemple si on a une entrée inconnues comme un nombre écrit en toutes lettres
            System.out.println("Erreur de format dans les données du fichier : " + e.getMessage());
        }
        
        return listeRevetements;
    }
}
