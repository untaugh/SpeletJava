package ettjavaspel.speletjava;

import java.util.Random;

public class Board {

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
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

    public boolean Move (int row, int col, Direction dir) {

        // check that selected piece exists
        if (row < 0 || row >= rows || col < 0 || col >=cols) {
            return false;
        }

        int toRow = row;
        int toCol = col;

        switch (dir) {
            case UP:
                toRow = row + 1;
                break;
            case DOWN:
                toRow = row - 1;
                break;
            case LEFT:
                toCol = col -1;
                break;
            case RIGHT:
                toCol = col + 1;
                break;
            default:
                break;
        }

        // check that target piece exists
        if (toRow < 0 || toRow >= rows || toCol < 0 || toCol >=cols) {
            return false;
        }

        Piece tempPiece = pieces[col][row];
        pieces[col][row] = pieces[toCol][toRow];
        pieces[toCol][toRow] = tempPiece;
        return true;
    }

}
