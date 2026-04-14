package com.mycompany.projetpeinture2026;

import java.util.*; // Je ne sais pas si il faut mettre List ou bien * suffit : ca n'avait pas l'air de marcher sur NetBeans :( 

public class Niveau {
    private int idNiveau;
    private double h;
    private int nbrApparts;
    private List<Appartement> apparts;

    public Niveau(int idNiveau, double h, int nbrApparts, List<Appartement> apparts) {
        this.idNiveau = idNiveau;
        this.h = h;
        this.nbrApparts = nbrApparts;
        this.apparts = apparts;
    }

    public int getIdNiveau() {
        return idNiveau;
    }
    public double getH() {
        return h;
    }
    public int getNbrApparts() {
        return nbrApparts;
    }
    public List<Appartement> getApparts() {
        return apparts;
    }

    public void setH(double h) {
        this.h = h;
    }
    public void setNbrApparts(int nbrApparts) {
        this.nbrApparts = nbrApparts;
    }
    
    public void ajouterAppartement(Appartement a){
        if (a != null) this.apparts.add(a);
    }

}
