package blablabli.devispeinture2026.model;

public abstract class Ouverture {
    protected double largeur;
    protected double hauteur;

    public Ouverture(double largeur, double hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
    }

    public double getLargeur() {
        return largeur;
    }
    public double getHauteur() {
        return hauteur;
    }

    public double calculerSurface(){
        return this.largeur*this.hauteur;
    }
    
}
