package ettjavaspel.speletjava;

import java.util.Random;

public class Board {

    Piece[][] pieces = new Piece[10][10];

    public Board () {
        Random rand = new Random();
        for (int i=0; i<10; i++) {
            for (int j=0; j<10; j++) {
                int a = rand.nextInt(4);
                if(a == 0) {
                    pieces[i][j] = new Piece(Piece.PieceColor.BLUE);
                }
                else if (a == 1){
                    pieces[i][j] = new Piece(Piece.PieceColor.ICE);
                }
                else if (a == 2){
                    pieces[i][j] = new Piece(Piece.PieceColor.GREEN);
                }
                else if (a == 3){
                    pieces[i][j] = new Piece(Piece.PieceColor.PURPINK);
                }
                else if (a == 4){
                    pieces[i][j] = new Piece(Piece.PieceColor.YELLOW);
                }
            }
        }
    }

}
