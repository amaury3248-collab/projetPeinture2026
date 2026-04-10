import java.util.ArrayList;
import java.util.List;

public class Niveau {
    private String idNiveau;
    private double h;
    private int nbrAppart;

    public Niveau(String idNiveau, double h, int nbrAppart) {
        this.idNiveau = idNiveau;
        this.h = h;
        this.nbrAppart = nbrApart;
    }
}

    public int getNbrAppart() {
        return this.nbrAppart;
    }

    public void setNbrAppart(int nbrAppart) {
        this.nbrAppart = nbrAppart;
    }
}
