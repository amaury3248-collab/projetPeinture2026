package blablabli.devispeinture2026.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ChargeurCatalogue {

    public static Catalogue charger(String chemin) {
        Catalogue catalogue = new Catalogue();

        try (BufferedReader br = new BufferedReader(new FileReader(chemin))) {

            String ligne = br.readLine(); // ignore l'en-tête

            while ((ligne = br.readLine()) != null) {
                String[] d = ligne.split(";");

                if (d.length == 6) {
                    int idRev = Integer.parseInt(d[0]);
                    String type = d[1];
                    boolean pourMur = d[2].equals("1");
                    boolean pourSol = d[3].equals("1");
                    boolean pourPlafond = d[4].equals("1");
                    double prix = Double.parseDouble(d[5]);

                    Revetement r = new Revetement(prix, idRev, type, pourMur, pourSol, pourPlafond);
                    catalogue.ajouter(r);
                }
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println("Erreur lors du chargement du catalogue : " + e.getMessage());
        }

        return catalogue;
    }
}
