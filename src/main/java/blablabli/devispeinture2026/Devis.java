package com.mycompany.projetpeinture2026;

import java.util.HashMap;
import java.util.Map;

public class Devis {
    private Batiment batiment;
    private double montantTotal;
    
    // Ces listes vont associer un Revêtement à sa surface totale et à son prix total
    private Map<Revetement, Double> surfaceParRevetement;
    private Map<Revetement, Double> prixParRevetement;

    public Devis(Batiment batiment) {
        this.batiment = batiment;
        this.surfaceParRevetement = new HashMap<>();
        this.prixParRevetement = new HashMap<>();
        this.montantTotal = 0.0;
    }

    public void calculer() {
        // compteurs à zéro (utile si on recalcule après une modif)
        surfaceParRevetement.clear();
        prixParRevetement.clear();
        montantTotal = 0.0;

        for (Niveau niveau : batiment.getNiveaux()) {
            double hauteurNiveau = niveau.getH(); // Nécessaire pour les murs
            
            for (Appartement appart : niveau.getApparts()) {
                for (Piece piece : appart.getPieces()) {
                    
                    for (Mur mur : piece.getMurs()) {
                        double surfaceMur = mur.calculerSurfaceNette(hauteurNiveau);
                        for (Revetement rev : mur.getRevetements()) {
                            ajouterSurface(rev, surfaceMur);
                        }
                    }
                    
                    if (piece.getStrateUp() != null) {
                        double surfacePlafond = piece.getStrateUp().calculerSurfaceNette();
                        for (Revetement rev : piece.getStrateUp().getRevsInf()) {
                            ajouterSurface(rev, surfacePlafond);
                        }
                    }

                    if (piece.getStrateDown() != null) {
                        double surfaceSol = piece.getStrateDown().calculerSurfaceNette();
                        for (Revetement rev : piece.getStrateDown().getRevsSup()) {
                            ajouterSurface(rev, surfaceSol);
                        }
                    }
                }
            }
        }

        for (Map.Entry<Revetement, Double> entry : surfaceParRevetement.entrySet()) {
            Revetement rev = entry.getKey();
            double surfaceTotale = entry.getValue();
            
            double prixTotalRevetement = surfaceTotale * rev.getPrix();
            
            // sauvegarde du prix du revêtement actuel et ajout au total final
            prixParRevetement.put(rev, prixTotalRevetement);
            montantTotal += prixTotalRevetement;
        }
    }

    // Méthode utilitaire privée pour additionner les surfaces dans la HashMap
    private void ajouterSurface(Revetement rev, double surface) {
        if (rev != null) {
            // getOrDefault : si le revêtement est déjà dans la liste, on prend son ancienne valeur. Sinon on commence à 0.
            double ancienneSurface = surfaceParRevetement.getOrDefault(rev, 0.0);
            surfaceParRevetement.put(rev, ancienneSurface + surface);
        }
    }

    public void afficherDetail() {
        System.out.println("======================================");
        System.out.println("         DEVIS DU BÂTIMENT " + batiment.getIdBatiment());
        System.out.println("======================================");
        
        for (Revetement rev : surfaceParRevetement.keySet()) {
            double surface = surfaceParRevetement.get(rev);
            double prix = prixParRevetement.get(rev);
            
            // String.format permet d'arrondir à 2 chiffres après la virgule
            System.out.println(String.format("- %s (Réf %d) : %.2f m2 -> %.2f euros", 
                    rev.getType(), rev.getIdRev(), surface, prix));
        }
        
        System.out.println("--------------------------------------");
        System.out.println(String.format("TOTAL GLOBAL : %.2f euros", montantTotal));
        System.out.println("======================================");
    }
    
    // si besoin de récupérer les infos plus tard
    public double getMontantTotal() { return montantTotal; }
    public Map<Revetement, Double> getSurfaceParRevetement() { return surfaceParRevetement; }
    public Map<Revetement, Double> getPrixParRevetement() { return prixParRevetement; }
}
