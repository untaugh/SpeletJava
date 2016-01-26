package ettjavaspel.speletjava;

public class Piece {

    public enum PieceColor {
        BLUE,
        GREEN,
        ICE,
        PURPINK,
        YELLOW,
    }

    public PieceColor piececolor;
    public boolean selected = false;
    public boolean moving = false;

    public Piece (PieceColor pieceColor) {
        this.piececolor = pieceColor;
    }

    public boolean SameColor(Piece piece) {
        return piece != null && piece.piececolor == piececolor;
    }

}
