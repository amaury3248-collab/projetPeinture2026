import java.util.ArrayList;
import java.util.List;

public class Niveau {
    private String idNiveau;
    private double h;
    private int nbrAppart;

    public Niveau(String idNiveau, double h) {
        this.idNiveau = idNiveau;
        this.h = h;
        this.nbrAppart = nbrApart;
    }

    public void ajouterAppartement(Appartement appartement) {
        this.appartements.add(appartement);
    }
}
