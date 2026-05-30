package com.mycompany.projetpeinture2026;

import java.util.List;

public class Strate {
    private int idStrate;
    private double surfaceBrute;
    private List<Revetement> revsSup; // pour le sol 
    private List<Revetement> revsInf; // pour le plafond
    private List<Ouverture> tremies;

    public Strate(int idStrate, double surfaceBrute, List<Revetement> revsSup, List<Revetement> revsInf, List<Ouverture> tremies) {
        this.idStrate = idStrate;
        this.surfaceBrute = surfaceBrute;
        this.revsSup = revsSup;
        this.revsInf = revsInf;
        this.tremies = tremies;
    }

    public int getIdStrate() {
        return idStrate;
    }
    public double getSurfaceBrute() {
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
    
    // Pour pouvoir mettre à jour la surface après un chargement de fichier : 
    public void setSurfaceBrute(double surfaceBrute){
        this.surfaceBrute  = surfaceBrute;
    }
    
    
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
