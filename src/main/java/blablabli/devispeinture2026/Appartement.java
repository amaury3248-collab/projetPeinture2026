package blablabli.devispeinture2026;

import java.util.List;

public class Appartement {
    private int idAppart;
    private int nbrPiece;
    private List<Piece> pieces;

    public Appartement(int idAppart, int nbrPiece, List<Piece> pieces) {
        this.idAppart = idAppart;
        this.nbrPiece = nbrPiece;
        this.pieces = pieces;
    }

    public int getIdAppart() {
        return idAppart;
    }
    public int getNbrPiece() {
        return nbrPiece;
    }
    public List<Piece> getPieces() {
        return pieces;
    }

    public void setNbrPiece(int nbrPiece) {
        this.nbrPiece = nbrPiece;
    }

    public void ajouterPiece(Piece p){
        if(p != null) this.pieces.add(p);
    }
}
