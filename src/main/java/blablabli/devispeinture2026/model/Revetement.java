package com.mycompany.projetpeinture2026;

import java.util.Objects;

public class Revetement {

    private final double prix;
    private final int idRev;
    private final String type;
    private final boolean pourMur;
    private final boolean pourSol;
    private final boolean pourPlafond;

    public Revetement(double prix, int idRev, String type, boolean pourMur, boolean pourSol, boolean pourPlafond) {
        this.prix = prix;
        this.idRev = idRev;
        this.type = type;
        this.pourMur = pourMur;
        this.pourSol = pourSol;
        this.pourPlafond = pourPlafond;
    }

    public double getPrix() { return prix; }
    public int getIdRev() { return idRev; }
    public String getType() { return type; }
    public boolean isPourMur() { return pourMur; }
    public boolean isPourSol() { return pourSol; }
    public boolean isPourPlafond() { return pourPlafond; }

    @Override
    public String toString() {
        return type + " (id=" + idRev + ", " + prix + " €/m²)";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Revetement)) return false;
        Revetement that = (Revetement) o;
        return idRev == that.idRev;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRev);
    }
}
