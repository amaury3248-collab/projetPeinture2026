package com.mycompany.projetpeinture2026;

public class Revetement {
    private double prix;
    private int idRev;
    private String type;
    private boolean pourMur; //     
    private boolean pourSol; //     en référence au document texte
    private boolean pourPlafond; //

    public Revetement(double prix, int idRev, String type, boolean pourMur, boolean pourSol, boolean pourPlafond) {
        this.prix = prix;
        this.idRev = idRev;
        this.type = type;
        this.pourMur = pourMur;
        this.pourSol = pourSol;
        this.pourPlafond = pourPlafond;
    }

    public double getPrix() {
        return prix;
    }
    public int getIdRev() {
        return idRev;
    }
    public String getType() {
        return type;
    }

    public boolean isPourMur() {
        return pourMur;
    }

    public boolean isPourSol() {
        return pourSol;
    }

    public boolean isPourPlafond() {
        return pourPlafond;
    }
// On ne met pas de setter pour l'id --> pas de modif après création
    public void setPrix(double prix) {
        this.prix = prix;
    }
    public void setType(String type) {
        this.type = type;
    }

    public void setPourMur(boolean pourMur) {
        this.pourMur = pourMur;
    }

    public void setPourSol(boolean pourSol) {
        this.pourSol = pourSol;
    }

    public void setPourPlafond(boolean pourPlafond) {
        this.pourPlafond = pourPlafond;
    }
      
}
