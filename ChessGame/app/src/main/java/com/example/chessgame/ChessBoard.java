package com.example.chessgame;

import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChessBoard {
    private static final int BOARDSIZE = 8;
	private Piece[][] pieceAt=new Piece[BOARDSIZE][BOARDSIZE];
	Piece blackKing;
	Piece whiteKing;
	private static ImageView square[][];
	static final int BLACK=2;
	static final int WHITE=1;
	private ArrayList<Piece> whitePieces;
	private ArrayList<Piece> blackPieces;
	private int turn = WHITE;

	public ImageView getSquareImage(int row, int col) {
		return square[row][col];
	}

	public void setUpBoard(ImageView square[][]) {
	    blackPieces = new ArrayList<Piece>();
	    whitePieces = new ArrayList<Piece>();
	    this.square = square;
        //set up back row for black pieces
        pieceAt[0][0] = new Rook(BLACK, 0,0, square[0][0], this);
        pieceAt[0][1] = new Knight(BLACK, 0,1, square[0][1], this);
        pieceAt[0][2] = new Bishop(BLACK, 0,2, square[0][2], this);
        pieceAt[0][3] = new Queen(BLACK, 0,3, square[0][3], this);
        blackKing = new King(BLACK, 0,4, square[0][4], this);
        pieceAt[0][4] = blackKing;
        pieceAt[0][5] = new Bishop(BLACK, 0,5, square[0][5], this);
        pieceAt[0][6] = new Knight(BLACK, 0,6, square[0][6], this);
        pieceAt[0][7] = new Rook(BLACK, 0,7, square[0][7], this);

        //set up back row for white pieces
        pieceAt[7][0] = new Rook(WHITE, 7,0, square[7][0], this);
        pieceAt[7][1] = new Knight(WHITE, 7,1, square[7][1], this);
        pieceAt[7][2] = new Bishop(WHITE, 7,2, square[7][2], this);
        pieceAt[7][3] = new Queen(WHITE, 7,3, square[7][3], this);
        whiteKing = new King(WHITE, 7,4, square[7][4], this);
        pieceAt[7][4] = whiteKing;
        pieceAt[7][5] = new Bishop(WHITE, 7,5, square[7][5], this);
        pieceAt[7][6] = new Knight(WHITE, 7,6, square[7][6], this);
        pieceAt[7][7] = new Rook(WHITE, 7,7, square[7][7], this);

        //set up pawns and tell the board which spaces currently have pieces on them
        for (int i=0; i<8; i++)
        {
            pieceAt[1][i] = new Pawn(BLACK, 1, i, square[1][i], this);
            pieceAt[6][i] = new Pawn(WHITE, 6, i, square[6][i], this);
        }
        for (int i=0; i<8; i++)
        {
            blackPieces.add(pieceAt[0][i]);
            blackPieces.add(pieceAt[1][i]);
            whitePieces.add(pieceAt[6][i]);
            whitePieces.add(pieceAt[7][i]);
        }
	}

	public void makeRed(int row, int col) {
        if (pieceAt(row, col)) {
            pieceAt[row][col].makeRed();
        }
        else {
            square[row][col].setImageResource(R.drawable.red_square);
        }
    }

    public void removePieceAt(int row, int col) {
	    int color = pieceAt[row][col].getColor();
	    if (color == BLACK) {
	        blackPieces.remove(findListIndex(row,col));
            pieceAt[row][col] = null;
        }
        else {
            whitePieces.remove(findListIndex(row,col));
            pieceAt[row][col] = null;
        }
        resetBoardColors();
    }

    public int findListIndex(int row, int col) {
	    for (int i=0; i<whitePieces.size(); i++) {
	        if (whitePieces.get(i).getRow() == row && whitePieces.get(i).getCol() == col) {
	            return i;
            }
            if (blackPieces.get(i).getRow() == row && blackPieces.get(i).getCol() == col) {
                return i;
            }
        }
        return -1;
    }

    public Boolean isCheck(int color) //checks to see if king of color kingColor is in check
    {
        if (color==WHITE)
        {
            for (Piece p:blackPieces)
            {
                if (p.isValidMove(whiteKing.getRow() ,whiteKing.getCol()))
                    return true;
            }
        }
        else
        {
            for (Piece p:whitePieces)
            {
                if (p.isValidMove(blackKing.getRow() ,blackKing.getCol()))
                    return true;
            }
        }
        return false;
    }

    public void pawnChecker(int color) { /*fixes enpassant captures*/
        if (color==BLACK)
            for (Piece p:whitePieces)
            {
                if (p.specialMove()==2)
                    p.specialMove(0);
            }
        else
            for (Piece p:blackPieces)
            {
                if (p.specialMove()==2)
                    p.specialMove(0);
            }
    }

    public Boolean weakCheckmate(int kingColor) //checks to see if king can move itself out of check
    {
        if (kingColor==BLACK)
        {
            for (int i=blackKing.getRow()-1; i<blackKing.getRow()+2; i++)
                for (int j=blackKing.getCol()-1; j<blackKing.getCol()+2; j++)
                {
                    if (j<0 || i<0 || j>7 || i>7)
                        continue;
                    if (blackKing.isValidMove(i,j))
                        if (!moveIntoCheck(blackKing, i, j))
                        {
                            return false;
                        }
                }
        }
        else {
            for (int i = whiteKing.getRow() - 1; i < whiteKing.getRow() + 2; i++)
                for (int j = whiteKing.getCol() - 1; j < whiteKing.getCol() + 2; j++) {
                    if (j < 0 || i < 0 || j > 7 || i > 7)
                        continue;
                    if (whiteKing.isValidMove(i, j))
                        if (!moveIntoCheck(whiteKing, i, j)) {
                            return false;
                        }
                }
        }
                    return true;
    }

    public Boolean isCheckmate(int kingColor) //seperate from weak checkmate so we don't iterate over the list everytime we are in check
    {
        List<Piece> safeBlackPieces = Collections.synchronizedList(blackPieces);
        if (kingColor==BLACK)
            for (Piece p:safeBlackPieces)
                for (int i=0; i<8; i++)
                    for (int j=0; j<8; j++)
                    {
                        if (p.isValidMove(i, j))
                            if (!moveIntoCheck(p, i, j))
                            {
                                return false;
                            }
                    }
        else {
            List<Piece> safeWhitePieces = Collections.synchronizedList(whitePieces);
            for (Piece p : safeWhitePieces)
                for (int i = 0; i < 8; i++)
                    for (int j = 0; j < 8; j++) {
                        if (p.isValidMove(i, j))
                            if (!moveIntoCheck(p, i, j)) {
                                return false;
                            }
                    }
        }
        return true;
    }


    public void unred(int row, int col) {
        if (pieceAt(row, col)) {
            pieceAt[row][col].unRed();
        }
        else {
            if (getSquareColor(row, col) == WHITE)
                square[row][col].setImageResource(R.drawable.white_square);
            else
                square[row][col].setImageResource(R.drawable.grey_square);
        }
    }

    public void makeGreen(int row, int col) {
        if (pieceAt(row, col)) {
            pieceAt[row][col].makeGreen();
        }
        else {
            square[row][col].setImageResource(R.drawable.green_square);
        }
    }

    public void unGreen(int row, int col) {
        if (pieceAt(row, col)) {
            pieceAt[row][col].unGreen();
        }
        else {
            if (getSquareColor(row, col) == WHITE)
                square[row][col].setImageResource(R.drawable.white_square);
            else
                square[row][col].setImageResource(R.drawable.grey_square);
        }
    }

    public void movePiece(Piece p, int endRow, int endColumn) {
	    if (pieceAt(endRow, endColumn)) //if there is a piece where we are tryign to move, we have to consider what will happen if it gets taken. We suppress this piece and check for check, then restor it later
        {
            Piece tempPiece=getPieceAt(endRow, endColumn);
            if (tempPiece.getColor() == p.getColor()) {
                handleCasting(p, tempPiece);
                return;
            }
            if (tempPiece.getColor() == WHITE) {
                whitePieces.remove(tempPiece);
            }
            else {
                blackPieces.remove(tempPiece);
            }
        }
	    p.removePieceImage();
        pieceAt[p.getRow()][p.getCol()] = null;
        p.setPosition(endRow, endColumn, square[endRow][endColumn]);
        p.setPieceImage();
        pieceAt[p.getRow()][p.getCol()] = p;
        if (turn == WHITE) {
            turn = BLACK;
        }
        else {
            turn = WHITE;
        }
        Log.i("Piece list sizes", " " + whitePieces.size() + " " + blackPieces.size());
    }

    public void handleCasting(Piece p1, Piece p2) {
	    Piece king, rook;
	    if (p1 instanceof King) {
	        king = p1;
	        rook = p2;
        }
        else {
	        king = p2;
	        rook = p1;
        }
        if (rook.getCol() < king.getCol()) {
            king.removePieceImage();
            rook.removePieceImage();
            pieceAt[king.getRow()][king.getCol()] = null;
            pieceAt[rook.getRow()][rook.getCol()] = null;
            pieceAt[king.getRow()][king.getCol()-2] = king;
            pieceAt[rook.getRow()][rook.getCol()+3] = rook;
            king.setPosition(king.getRow(), king.getCol()-2, square[king.getRow()][king.getCol()-2]);
            rook.setPosition(rook.getRow(), rook.getCol()+3, square[rook.getRow()][rook.getCol()+3]);
            king.setPieceImage();
            rook.setPieceImage();
        }
        else {
            king.removePieceImage();
            rook.removePieceImage();
            pieceAt[king.getRow()][king.getCol()] = null;
            pieceAt[rook.getRow()][rook.getCol()] = null;
            pieceAt[king.getRow()][king.getCol()+2] = king;
            pieceAt[rook.getRow()][rook.getCol()-2] = rook;
            king.setPosition(king.getRow(), king.getCol()+2, square[king.getRow()][king.getCol()+2]);
            rook.setPosition(rook.getRow(), rook.getCol()-2, square[rook.getRow()][rook.getCol()-2]);
            king.setPieceImage();
            rook.setPieceImage();
        }
        if (turn == WHITE) {
            turn = BLACK;
        }
        else {
            turn = WHITE;
        }
    }

    public int getTurn() {
	    return turn;
    }

    public void displayPossibleMoves(Piece p) {
	    ArrayList<ArrayList<Integer>> possibleMoves = p.allValidMoves();
	    if (possibleMoves.size() == 0)
	        return;
	    for (ArrayList<Integer> move : possibleMoves) {
	        int row = move.get(0);
	        int col = move.get(1);
	        if (moveIntoCheck(p, row, col)) {
	            continue;
            }
	        if (pieceAt(row, col)) {
	            getPieceAt(row,col).makeGreen();
            }
            else {
	            makeGreen(row,col);
            }
	    }
    }

    public void resetBoardColors() {
        for (int i=0; i<BOARDSIZE; i++) {
            for (int j=0; j<BOARDSIZE; j++) {
                if (pieceAt(i, j)) {
                    getPieceAt(i,j).setPieceImage();
                }
                else {
                    if(ChessBoard.getSquareColor(i, j) == WHITE) {
                        square[i][j].setImageResource(R.drawable.white_square);
                    }
                    else {
                        square[i][j].setImageResource(R.drawable.grey_square);
                    }
                }
            }
        }
    }

    public Boolean moveIntoCheck(Piece p, int endRow, int endCol)
    {
        Piece tempPiece = null;
        Boolean ret = false;
        if (pieceAt(endRow, endCol)) //if there is a piece where we are tryign to move, we have to consider what will happen if it gets taken. We suppress this piece and check for check, then restor it later
        {
            tempPiece=getPieceAt(endRow, endCol);
            pieceAt[endRow][endCol] = null;
            if (tempPiece.getColor() == WHITE) {
                whitePieces.remove(tempPiece);
            }
            else {
                blackPieces.remove(tempPiece);
            }
        }
        int prevRow = p.getRow();
        int prevCol = p.getCol();
        p.setPosition(endRow, endCol, null);
        pieceAt[prevRow][prevCol] = null;
        pieceAt[endRow][endCol] = p;
        if (isCheck(p.getColor())) {
            ret = true;
        }
        pieceAt[prevRow][prevCol] = p;
        pieceAt[endRow][endCol] = null;
        p.setPosition(prevRow, prevCol, square[prevRow][prevCol]);
        if (tempPiece != null) {
           pieceAt[endRow][endCol] = tempPiece;
            if (tempPiece.getColor() == WHITE) {
                whitePieces.add(tempPiece);
            }
            else {
                blackPieces.add(tempPiece);
            }
        }
        return ret;
    }

	public static int getSquareColor(int row, int col) {
		if ((row + col) % 2 == 0) {
			return WHITE;
		}
		return BLACK;
	}

	public ChessBoard() {
		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++)
				pieceAt[i][j]=null;
		blackKing=null;
		whiteKing=null;
	}

	public void changePieceAt(String type) {
	    int row = 0;
	    int col = 0;
	    for (int i=0; i<BOARDSIZE; i++) {
	        if (pieceAt[0][i] instanceof Pawn) {
	            row = 0;
	            col = i;
            }
        }
        for (int i=0; i<BOARDSIZE; i++) {
            if (pieceAt[7][i] instanceof Pawn) {
                row = 7;
                col = i;
            }
        }
        Piece p = pieceAt[row][col];
	    int color = p.getColor();
        removePieceAt(SquareListener.getEndRow(), SquareListener.getEndCol());
        Piece newPiece = null;
        if (type.equalsIgnoreCase("Knight")) {
            newPiece = new Knight(color, row, col, square[row][col], this);
        }
        else if (type.equalsIgnoreCase("Queen")) {
            newPiece = new Queen(color, row, col, square[row][col], this);
        }
        else if (type.equalsIgnoreCase("Bishop")) {
            newPiece = new Bishop(color, row, col, square[row][col], this);
        }
        else if (type.equalsIgnoreCase("Rook")) {
            newPiece = new Rook(color, row, col, square[row][col], this);
        }
        placePiece(SquareListener.getEndRow(), SquareListener.getEndCol(), newPiece);
    }
	
	public Boolean pieceAt(int row, int column){
		return  pieceAt[row][column]!=null;
	}

	public Piece getPieceAt(int row, int column){
		return  pieceAt[row][column];
	}

	public void placePiece(int row, int column, Piece p) {
		pieceAt[row][column]=p;
		if (p.getColor() == WHITE) {
		    whitePieces.add(p);
        }
        else {
		    blackPieces.add(p);
        }
	}

	
}
