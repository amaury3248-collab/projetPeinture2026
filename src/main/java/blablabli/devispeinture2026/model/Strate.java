package com.mycompany.projetpeinture2026;

import java.util.List;

public class Strate {
    private int idStrate;
    private float surfaceBrute;
    private List<Revetement> revsSup; // pour le sol 
    private List<Revetement> revsInf; // pour le plafond
    private List<Ouverture> tremies;  
    //Pour le dernier étage (sans plafond) : Lors de la création de la pièce strate du sol, mais null pour la strate haute :
    // Exemple : Piece maChambreSousLesToits = new Piece(1, "Chambre", monPlancher, null);

    public Strate(int idStrate, float surfaceBrute, List<Revetement> revsSup, List<Revetement> revsInf, List<Ouverture> tremies) {
        this.idStrate = idStrate;
        this.surfaceBrute = surfaceBrute;
        this.revsSup = revsSup;
        this.revsInf = revsInf;
        this.tremies = tremies;
    }

    public int getIdStrate() {
        return idStrate;
    }
    public float getSurfaceBrute() {
        return surfaceBrute;
    }
    public List<Revetement> getRevsSup() {
        return revsSup;
    }
    public List<Revetement> getRevsInf() {
        return revsInf;
    }
    public List<Ouverture> getTremies() {
        return tremies;
    }
// Aucun setters; mais créations de méthodes manuelles (ajouterRevsSup(Revetement r) ou ajouterTremie(Ouverture o)) 

    public void ajouterRevSup(Revetement r){
        if(r != null) this.revsSup.add(r);
    }
    public void ajouterRevInf(Revetement r){
        if(r != null) this.revsInf.add(r);
    }
    public void ajouterTremie(Ouverture o){
        if(o != null) this.tremies.add(o);
    }
    
    public double calculerSurfaceNette(){
        double surfaceTremies = 0;
        for (Ouverture t : tremies) {
            surfaceTremies += t.calculerSurface();        
        }
        return surfaceBrute - surfaceTremies;
    }
    
    public double calculerPrixStrate() {
        double surface = calculerSurfaceNette();
        double prixTotal = 0;
        
       
        for (Revetement r : this.revsSup) {
            prixTotal += (surface * r.getPrix());
        }               
        for (Revetement r : this.revsInf) {
            prixTotal += (surface * r.getPrix());
        }
        
        return prixTotal;
    }
}
