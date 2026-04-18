package blablabli.devispeinture2026;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Devispeinture2026 {

    public static void main(String[] args) {
        List<Revetement> catalogue = chargerRevetements("Revetement.txt");
        
        System.out.println("Nombre de revêtements chargés : "+ catalogue.size());       
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
