package ettjavaspel.speletjava;

import java.util.Random;
import java.util.Arrays;

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

        // create random board
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

    public void DeselectAll() {
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                pieces[col][row].selected = false;
            }
        }
    }

    public void select(Piece pieceArray[]) {
        for (Piece p : pieceArray) {
            p.selected = true;
        }
    }

    // get piece object for coordinate
    public Piece GetPiece(int col, int row) {
        if (row < 0 || row >= rows || col < 0 || col >=cols) {
            return null;
        }

        return pieces[col][row];
    }

    // get pieces of same color surrounding a piece
    public Piece[] NextPiece(Piece piece) {

        Position pos = position(piece);

        int nextCount = 0;
        Piece nextPiece[] = new Piece[4];

        if (piece.SameColor(GetPiece(pos.col+1,pos.row))) {
            nextPiece[nextCount++] = GetPiece(pos.col+1,pos.row);
        }

        if (piece.SameColor(GetPiece(pos.col,pos.row+1))) {
            nextPiece[nextCount++] = GetPiece(pos.col,pos.row+1);
        }

        if (piece.SameColor(GetPiece(pos.col-1,pos.row))) {
            nextPiece[nextCount++] = GetPiece(pos.col-1,pos.row);
        }

        if (piece.SameColor(GetPiece(pos.col,pos.row-1))) {
            nextPiece[nextCount++] = GetPiece(pos.col,pos.row-1);
        }

        return Arrays.copyOf(nextPiece, nextCount);
    }

    // position on the board for a piece
    public Position position(Piece piece) {
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                if (pieces[col][row] == piece) {
                    return new Position(col, row);
                }
            }
        }
        return new Position(-1, -1);
    }

    // check if array contains a piece
    public boolean contains(Piece pieceArray[], Piece piece) {

        for (Piece p: pieceArray) {
            if (p == piece) {
                return true;
            }
        }
        return false;
    }

    // get a group of pieces to which this piece belongs
    public Piece[] group(Piece piece) {

        Piece pieceArray[] = {piece};
        int length = 0;

        while ( pieceArray.length > length) {
            length = pieceArray.length;
            pieceArray = addGroup(pieceArray);
        }

        return pieceArray;
    }

    // add more surrounding pieces of same color to a group
    public Piece[] addGroup(Piece pieceArray[]) {

        Piece group[] = new Piece[pieceArray.length + pieceArray.length*4];

        int groupCount = 0;

        for (Piece p : pieceArray) {
            if (!contains(group, p)) {
                group[groupCount++] = p;
            }

            Piece next[] = NextPiece(p);

            for (Piece np : next) {
                if (!contains(group, np)) {
                    group[groupCount++] = np;
                }
            }

        }

        return Arrays.copyOf(group, groupCount);
    }


}
