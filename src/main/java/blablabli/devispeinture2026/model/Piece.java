package com.mycompany.projetpeinture2026;

import java.util.List;

public class Piece {
    private int idPiece; 
    private String usage; 
    private List<Mur> murs;
    private Strate strateUp; // plafond
    private Strate strateDown; // sol

    public Piece(int idPiece, String usage, List<Mur> murs, Strate strateUp, Strate strateDown) {
        this.idPiece = idPiece;
        this.usage = usage;
        this.murs = murs;
        this.strateUp = strateUp;
        this.strateDown = strateDown;
    }

    public int getIdPiece() {
        return idPiece;
    }
    public String getUsage() {
        return usage;
    }
    public List<Mur> getMurs() {
        return murs;
    }
    public Strate getStrateUp() {
        return strateUp;
    }
    public Strate getStrateDown() {
        return strateDown;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
    public void setStrateUp(Strate strateUp) {
        this.strateUp = strateUp;
    }
    public void setStrateDown(Strate strateDown) {
        this.strateDown = strateDown;
    }
// Pas de setter pour la liste murs; mais ajouter une méthode ajouterMur(Mur m)
    
    public void ajouterMur(Mur m){
        if (m != null) this.murs.add(m);
    }
    
    public double calculerPrixPiece(double hauteurNiveau) {
        double coutTotal = 0;
               
        for (Mur m : this.murs) {
            coutTotal += m.calculerPrixMur(hauteurNiveau);
        }
        
        if (this.strateUp != null) {
            coutTotal += this.strateUp.calculerPrixStrate();
        }
        
        if (this.strateDown != null) {
            coutTotal += this.strateDown.calculerPrixStrate();
        }
        
        return coutTotal;
    }

}
