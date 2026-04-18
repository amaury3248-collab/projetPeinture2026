package blablabli.devispeinture2026.devis;

import blablabli.devispeinture2026.model.Revetement;

public class LigneRevetement {

    private final Revetement revetement;
    private final double surface;
    private final double prixTotal;

    public LigneRevetement(Revetement revetement, double surface, double prixTotal) {
        this.revetement = revetement;
        this.surface = surface;
        this.prixTotal = prixTotal;
    }

    public Revetement getRevetement() {
        return revetement;
    }

    public double getSurface() {
        return surface;
    }

    public double getPrixTotal() {
        return prixTotal;
    }

    public String getDesignation() {
        return revetement.getType();
    }

    public double getPrixUnitaire() {
        return revetement.getPrix();
    }
}
