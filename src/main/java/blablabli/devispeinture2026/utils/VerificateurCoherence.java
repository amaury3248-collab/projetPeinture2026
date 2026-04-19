package blablabli.devispeinture2026.ui.utils;

import blablabli.devispeinture2026.model.Mur;
import blablabli.devispeinture2026.model.Ouverture;
import blablabli.devispeinture2026.model.Porte;
import blablabli.devispeinture2026.model.exceptions.OuvertureInvalideException;
import blablabli.devispeinture2026.model.exceptions.PorteInterditeSurMurExterneException;

public class VerificateurCoherence {

    /**
     * Vérifie si une ouverture est cohérente avec le mur et la hauteur du niveau.
     */
    public static void verifierOuverture(Mur mur, Ouverture o, double hauteurNiveau) {

        // 1) L’ouverture ne doit pas dépasser la hauteur du mur
        if (o.getHauteurBas() + o.getHauteur() > hauteurNiveau) {
            throw new OuvertureInvalideException(
                "L'ouverture dépasse la hauteur du mur (" + hauteurNiveau + " m)."
            );
        }

        // 2) Une porte ne peut pas être en hauteur sur un mur extérieur
        if (o instanceof Porte && mur.isMurExt() && o.getHauteurBas() > 0.01) {
            throw new PorteInterditeSurMurExterneException(
                "Impossible de placer une porte en hauteur sur un mur extérieur."
            );
        }
    }
}
