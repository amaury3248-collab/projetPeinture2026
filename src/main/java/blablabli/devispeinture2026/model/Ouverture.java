package blablabli.devispeinture2026.model;

public abstract class Ouverture {

    protected double largeur;   // largeur de l’ouverture
    protected double hauteur;   // hauteur de l’ouverture
    protected double hBas;      // hauteur du bas de l’ouverture par rapport au sol

    public Ouverture(double largeur, double hauteur, double hBas) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.hBas = hBas;
    }

    public double calculerSurface() {
        return largeur * hauteur;
    }

    public double getLargeur() {
        return largeur;
    }

    public double getHauteur() {
        return hauteur;
    }

    public double getHauteurBas() {
        return hBas;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
               " (" + largeur + "m x " + hauteur + "m, hBas=" + hBas + "m)";
    }
}
