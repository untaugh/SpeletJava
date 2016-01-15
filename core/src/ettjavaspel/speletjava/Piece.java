package ettjavaspel.speletjava;

public class Piece {

    public enum PieceColor {
        BLUE,
        GREEN,
        ICE,
        PURPINK,
        YELLOW,
    }

    PieceColor piececolor;

    public Piece (PieceColor pieceColor) {
        this.piececolor = pieceColor;
    }

}
