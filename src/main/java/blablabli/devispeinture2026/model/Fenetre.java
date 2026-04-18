package com.mycompany.projetpeinture2026;

public class Fenetre extends Ouverture {

    public Fenetre() {
        super(1.20, 1.20); //Dimensions standards comme Porte
    }

    public double getLargeur() {
        return largeur;
    }
    public double getHauteur() {
        return hauteur;
    }
        
}
