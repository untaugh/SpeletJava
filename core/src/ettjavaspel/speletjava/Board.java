package ettjavaspel.speletjava;

/**
 * Created by Oskar Rundgren on 1/14/16.
 */

public class Board {

    Piece[][] pieces = new Piece[10][10];

    public Board () {
        for (int i=0; i<10; i++) {
            for (int j=0; j<10; j++) {
                pieces[i][j] = new Piece(Piece.PieceColor.ICE);
            }
        }
    }

}
