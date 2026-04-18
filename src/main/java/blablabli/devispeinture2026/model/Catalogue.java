package blablabli.devispeinture2026.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Catalogue {

    private List<Revetement> liste;

    public Catalogue() {
        this.liste = new ArrayList<>();
    }

    public void ajouter(Revetement r) {
        liste.add(r);
    }

    public List<Revetement> getTous() {
        return liste;
    }

    public List<Revetement> getPourMur() {
        return liste.stream().filter(Revetement::isPourMur).collect(Collectors.toList());
    }

    public List<Revetement> getPourSol() {
        return liste.stream().filter(Revetement::isPourSol).collect(Collectors.toList());
    }

    public List<Revetement> getPourPlafond() {
        return liste.stream().filter(Revetement::isPourPlafond).collect(Collectors.toList());
    }
}
