package com.mycompany.projetpeinture2026;

import java.util.List;

public class Batiment {
    private int idBatiment;
    private boolean type;
    private List<Niveau> niveaux;

    public Batiment(int idBatiment, boolean type, List<Niveau> niveaux) {
        this.idBatiment = idBatiment;
        this.type = type;
        this.niveaux = niveaux;
    }

    public int getIdBatiment() {
        return idBatiment;
    }
    public boolean isType() {
        return type;
    }
    public List<Niveau> getNiveaux() {
        return niveaux;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public void ajouterNiveau(Niveau n) {
        if (n != null) this.niveaux.add(n);
    }
    
    
    
    
    public static void sauvegarderBatiment(Batiment b, String nomFichier) {
    try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(nomFichier))) {
        
        writer.println("BATIMENT;" + b.getIdBatiment() + ";" + (b.isType() ? "Immeuble" : "Maison"));

        for (Niveau niv : b.getNiveaux()) {
            writer.println("NIVEAU;" + niv.getIdNiveau() + ";" + niv.getH());

            for (Appartement app : niv.getApparts()) {
                writer.println("APPART;" + app.getIdAppart());

                for (Piece p : app.getPieces()) {
                    writer.println("PIECE;" + p.getIdPiece() + ";" + p.getUsage());
                    
                    // Sauvegarde des revêtements de sol (Strate basse)
                    if (p.getStrateDown() != null) {
                        for (Revetement r : p.getStrateDown().getRevsSup()) {
                            writer.println("REV_SOL;" + r.getIdRev());
                        }
                    }
                        
                    // Sauvegarde des revêtements de plafond (Strate haute)
                    if (p.getStrateUp() != null) {
                        for (Revetement r : p.getStrateUp().getRevsInf()) {
                            writer.println("REV_PLAFOND;" + r.getIdRev());
                        }
                    }

                    for (Mur m : p.getMurs()) {
                        double[] c = m.getCoords();
                        writer.println("MUR;" + m.getIdMur() + ";" + 
                                       c[0] + ";" + c[1] + ";" + 
                                       c[2] + ";" + c[3] + ";" + 
                                       (m.isMurExt() ? "1" : "0"));
                        
                        // Sauvegarde des revêtements de ce mur
                        for (Revetement r : m.getRevetements()) {
                                writer.println("REV_MUR;" + r.getIdRev());
                        }
                    }
                }
            }
        }
        System.out.println("Sauvegarde du bâtiment terminée : " + nomFichier);
    } catch (java.io.IOException e) {
        System.out.println("Erreur de sauvegarde : " + e.getMessage());
    }
}
}
    
package blablabli.devispeinture2026;

import java.util.List;

public class Batiment {
    private int idBatiment;
    private boolean type;
    private List<Niveau> niveaux;

    public Batiment(int idBatiment, boolean type, List<Niveau> niveaux) {
        this.idBatiment = idBatiment;
        this.type = type;
        this.niveaux = niveaux;
    }

    public int getIdBatiment() {
        return idBatiment;
    }
    public boolean isType() {
        return type;
    }
    public List<Niveau> getNiveaux() {
        return niveaux;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public void ajouterNiveau(Niveau n) {
        if (n != null) this.niveaux.add(n);
    }
}
