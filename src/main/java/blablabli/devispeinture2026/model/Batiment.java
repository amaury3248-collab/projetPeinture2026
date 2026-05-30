package com.mycompany.projetpeinture2026;

import java.util.ArrayList;
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
    
        // Méthode pour charger un bâtiment à partir d'un fichier de sauvegarde texte
    public static Batiment chargerBatiment(String nomFichier, List<Revetement> catalogue) {
        Batiment batimentCharge = null;
        Niveau niveauCourant = null;
        Appartement appartCourant = null;
        Piece pieceCourante = null;
        Mur murCourant = null; // on garde la memoire sur le dernier mur lu 

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(nomFichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] d = ligne.split(";");
                if (d.length == 0) continue;

                switch (d[0]) {
                    case "BATIMENT":
                        int idBat = Integer.parseInt(d[1]);
                        boolean isImmeuble = d[2].equalsIgnoreCase("Immeuble");
                        batimentCharge = new Batiment(idBat, isImmeuble, new ArrayList<>());
                        break;

                    case "NIVEAU":
                        if (batimentCharge != null) {
                            int idNiv = Integer.parseInt(d[1]);
                            double hauteur = Double.parseDouble(d[2]);
                            niveauCourant = new Niveau(idNiv, hauteur, 0, new ArrayList<>());
                            batimentCharge.ajouterNiveau(niveauCourant);
                        }
                        break;

                    case "APPART":
                        if (niveauCourant != null) {
                            int idAppart = Integer.parseInt(d[1]);
                            appartCourant = new Appartement(idAppart, 0, new ArrayList<>());
                            niveauCourant.ajouterAppartement(appartCourant);
                        }
                        break;

                    case "PIECE":
                        if (appartCourant != null) {
                            int idPiece = Integer.parseInt(d[1]);
                            String usage = d[2];
                            // Initialisation de strates vides (Sol et Plafond) pour la pièce
                            Strate sol = new Strate(1, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                            Strate plafond = new Strate(2, 0, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                            
                            pieceCourante = new Piece(idPiece, usage, new ArrayList<>(), plafond, sol);
                            appartCourant.ajouterPiece(pieceCourante);
                        }
                        break;
                        
                    case "REV_SOL":
                        if (pieceCourante != null && pieceCourante.getStrateDown() != null) {
                            int idRev = Integer.parseInt(d[1]);
                            Revetement rev = trouverRevetementParId(idRev, catalogue);
                            if (rev != null) pieceCourante.getStrateDown().ajouterRevSup(rev);
                        }
                        break;
                        
                    case "REV_PLAFOND":
                        if (pieceCourante != null && pieceCourante.getStrateUp() != null) {
                            int idRev = Integer.parseInt(d[1]);
                            Revetement rev = trouverRevetementParId(idRev, catalogue);
                            if (rev != null) pieceCourante.getStrateUp().ajouterRevInf(rev);
                        }
                        break;

                    case "MUR":
                        if (pieceCourante != null) {
                            int idMur = Integer.parseInt(d[1]);
                            double[] coords = new double[]{
                                Double.parseDouble(d[2]), Double.parseDouble(d[3]),
                                Double.parseDouble(d[4]), Double.parseDouble(d[5])
                            };
                            boolean isExt = d[6].equals("1");
                            
                            murCourant = new Mur(idMur, coords, isExt, new ArrayList<>(), new ArrayList<>());
                            pieceCourante.ajouterMur(murCourant);
                        }
                        break;
                    
                    case "REV_MUR":
                        if (murCourant != null) {
                            int idRev = Integer.parseInt(d[1]);
                            Revetement rev = trouverRevetementParId(idRev, catalogue);
                            if (rev != null) murCourant.ajouterRevetement(rev);
                        }
                        break;
                }
            }
            
            // Recalcul mathématique des surfaces après chargement
            if (batimentCharge != null) {
                for (Niveau n : batimentCharge.getNiveaux()) {
                    for (Appartement a : n.getApparts()) {
                        for (Piece p : a.getPieces()) {
                            // Si la pièce a bien ses murs (au moins la longueur et la largeur)
                            if (p.getMurs().size() >= 2) {
                                double longueur = p.getMurs().get(0).calculerLongueur();
                                double largeur = p.getMurs().get(1).calculerLongueur();
                                double vraieSurface = longueur * largeur;
                                
                                // On met à jour les surfaces à 0 avec les vraies valeurs
                                if (p.getStrateDown() != null) p.getStrateDown().setSurfaceBrute(vraieSurface);
                                if (p.getStrateUp() != null) p.getStrateUp().setSurfaceBrute(vraieSurface);
                            }
                        }
                    }
                }
            }

            System.out.println("Chargement réussi depuis " + nomFichier);
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement du fichier : " + e.getMessage());
        }
        return batimentCharge;
    }
    
        // Méthode pour retrouver un revêtement par son ID
    private static Revetement trouverRevetementParId(int id, List<Revetement> catalogue) {
        for (Revetement r : catalogue) {
            if (r.getIdRev() == id) {
                return r;
            }
        }
        return null;
    }
}
