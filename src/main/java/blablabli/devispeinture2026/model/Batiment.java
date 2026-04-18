package blablabli.devispeinture2026;

import java.util.List;

public class Batiment {
    private int idBatiment;
    private boolean type;
    private List<Niveau> niveaux;

    public Batiment(int idBatiment, boolean type, List<Niveau> niveaux) {
        this.idBatiment = idBatiment;
        this.type = type;
        this.niveaux = niveaux;
    }

    public int getIdBatiment() {
        return idBatiment;
    }
    public boolean isType() {
        return type;
    }
    public List<Niveau> getNiveaux() {
        return niveaux;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public void ajouterNiveau(Niveau n) {
        if (n != null) this.niveaux.add(n);
    }
}
