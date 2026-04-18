package com.mycompany.projetpeinture2026; /////////// ATTENTION AUX PACKAGES DIFFERENTS ENTRE NOUS 

import java.util.ArrayList;
import java.util.List;

public class Mur {
    private int idMur;
    private float[] coords; // [xDebut, yDebut, xFin; yFin]
    private boolean murExt;
    private List<Revetement> revetements;
    private List<Ouverture> ouvertures;

    public Mur(int idMur, float[] coords, boolean murExt, List<Revetement> revetements, List<Ouverture> ouvertures) {
        this.idMur = idMur;
        this.coords = coords;
        this.murExt = murExt;
        this.revetements = revetements;
        this.ouvertures = ouvertures;
    }

    public int getIdMur() {
        return idMur;
    }
    public float[] getCoords() {
        return coords;
    }
    public boolean isMurExt() {
        return murExt;
    }
    public List<Revetement> getRevetements() {
        return revetements;
    }
    public List<Ouverture> getOuvertures() {
        return ouvertures;
    }

    public void setMurExt(boolean murExt) { // Setter uniquement pour murExt pour modifier 
        this.murExt = murExt;
    }
// Pas de setters pour idMur, coords (on détruit et on reconstruit), jamais pour les listes
 
    public void ajouterRevetement(Revetement r){
        if (r != null) this.revetements.add(r);
    }
    public void ajouterOuverture(Ouverture o){
        if (o != null) this.ouvertures.add(o);
    }
    public float calculerLongueur(){
        return (float) Math.sqrt(Math.pow(coords[2]-coords[0], 2) + Math.pow(coords[3]-coords[1], 2));
    }
    
    public double calculerSurfaceNette(double hauteurNiveau) {        
        double surfaceBrute = this.calculerLongueur() * hauteurNiveau; // Surface burte      
        double surfaceOuvertures = 0; // surface à déduire
        for (Ouverture o : this.ouvertures) {
            surfaceOuvertures += o.calculerSurface();
        }
                
        return surfaceBrute - surfaceOuvertures; // Surface réelle
    }
    public double calculerPrixMur(double hauteurNiveau) {
        double surface = calculerSurfaceNette(hauteurNiveau);
        double prixTotal = 0;              
        for (Revetement r : this.revetements) {
            prixTotal += (surface * r.getPrix());
        }
        
        return prixTotal;
    }
}
