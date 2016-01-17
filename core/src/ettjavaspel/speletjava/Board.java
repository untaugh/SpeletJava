package ettjavaspel.speletjava;

import java.util.Random;

public class Board {

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        NONE,
    }

    Piece[][] pieces;
    public int rows;
    public int cols;

    public Board (int cols, int rows) {
        this.rows = rows;
        this.cols = cols;

        pieces = new Piece[cols][rows];

        Random rand = new Random();
        for (int i=0; i<cols; i++) {
            for (int j=0; j<rows; j++) {
                int a = rand.nextInt(Piece.PieceColor.values().length);
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

    // deselected all pieces
    public void DeselectAll() {
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                pieces[col][row].selected = false;
            }
        }
    }

}
