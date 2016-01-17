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

    // return true if specified move is legal.
    private boolean MoveTest (int col, int row, Direction dir) {

        Piece.PieceColor pieceColor= pieces[col][row].piececolor;

        int colD = 0;
        int rowD = 0;

        switch (dir) {
            case UP:
                rowD = 1;
                break;
            case DOWN:
                rowD = -1;
                break;
            case LEFT:
                colD = -1;
                break;
            case RIGHT:
                colD = 1;
                break;
            default:
                return false;
        }

        // move to next piece
        col = col + colD;
        row = row + rowD;

        // edge reached
        if (row < 0 || row >= rows || col < 0 || col >=cols) {
            return false;
        }

        // cannot move if piece next to is same color
        if ( pieces[col][row].piececolor == pieceColor) {
            return false;
        }

        while (col >= 0 && col < cols && row >= 0 && row < rows) {

            // legal move exists
            if ( pieces[col][row].piececolor == pieceColor) {
                return true;
            }

            col = col + colD;
            row = row + rowD;
        }

        // edge reached and no legal moves found
        return false;
    }

    // Move a piece to a specific direction
    public boolean Move (int col, int row, Direction dir) {

        if (!MoveTest(col, row, dir)) {
            return false;
        }

        int colD = 0;
        int rowD = 0;

        switch (dir) {
            case UP:
                rowD = 1;
                break;
            case DOWN:
                rowD = -1;
                break;
            case LEFT:
                colD = -1;
                break;
            case RIGHT:
                colD = 1;
                break;
            default:
                return false;
        }

        while (pieces[col][row].piececolor != pieces[col+colD][row+rowD].piececolor) {
            Step(col,row,dir);
            col = col + colD;
            row = row + rowD;
        }

        return true;
    }

    public boolean Step (int col, int row, Direction dir) {

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
                return false;
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

    // deselected all pieces
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

    // Get piece at this coordinate
    public Piece GetPiece(int col, int row) {
        // check that selected piece exists
        if (row < 0 || row >= rows || col < 0 || col >=cols) {
            return null;
        }

        return pieces[col][row];
    }

    // Get pieces next to a piece
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

    // Piece position
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

    public boolean contains(Piece pieceArray[], Piece piece) {

        for (Piece p: pieceArray) {
            if (p == piece) {
                return true;
            }
        }
        return false;
    }

    public Piece[] group(Piece piece) {

        Piece pieceArray[] = {piece};
        int length = 0;

        while ( pieceArray.length > length) {
            length = pieceArray.length;
            pieceArray = addGroup(pieceArray);
            System.out.println("Lengths" + length + " " + pieceArray.length);
        }

        return pieceArray;
    }

    public Piece[] addGroup(Piece piece) {
        Piece pieceArray[] = {piece};
        return pieceArray;
    }

    public Piece[] addGroup(Piece pieceArray[]) {

        Piece group[] = new Piece[pieceArray.length + pieceArray.length*4];

        int groupCount = 0;

        for (Piece p : pieceArray) {
            if (!contains(group, p)) {
                group[groupCount++] = p;
            }

            Piece next[] = NextPiece(p);

            for (Piece np : next) {
                //System.out.println("next " + next.length);
                if (!contains(group, np)) {
                    group[groupCount++] = np;
                    System.out.println("adding");
                }
            }

        }

        return Arrays.copyOf(group, groupCount);
    }


}
