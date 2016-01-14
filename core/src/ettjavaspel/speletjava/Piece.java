package ettjavaspel.speletjava;

/**
 * Created by Oskar Rundgren on 1/14/16.
 */

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
