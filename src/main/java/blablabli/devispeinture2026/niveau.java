package com.mycompany.projetpeinture2026;

import java.util.*;

public class niveau {
    private String idNiveau;
    private double h;
    private int nbrAppart;

    public niveau(String idNiveau, double h, int nbrAppart) {
        this.idNiveau = idNiveau;
        this.h = h;
        this.nbrAppart = nbrAppart;
    }

    public String getIdNiveau() {
        return idNiveau;
    }

    public void setIdNiveau(String idNiveau) {
        this.idNiveau = idNiveau;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public int getNbrAppart() {
        return nbrAppart;
    }

    public void setNbrAppart(int nbrAppart) {
        this.nbrAppart = nbrAppart;
    }


}
