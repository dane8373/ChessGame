package com.example.chessgame;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

public class SquareListener implements View.OnClickListener {
    private static Piece lastPiece;
    private static Piece movePiece;
    private static int endRow;
    private static int endCol;
    private static ChessBoard theBoard;

    public static Piece getMovePiece() {
        return movePiece;
    }

    public static Piece getLastPiece() {
        return lastPiece;
    }

    public static int getEndRow() {
        return endRow;
    }

    public static int getEndCol() {
        return endCol;
    }

    public static void clearMove() {
        endRow = endCol = -1;
        movePiece = null;
        theBoard.resetBoardColors();
    }

    public static void setBoard(ChessBoard b) {
        theBoard = b;
    }
    public SquareListener() {
        super();
        movePiece=null;
        endCol=-1;
        endRow=-1;
    }
    @Override
    public void onClick(View v) {
        if (SubmitListener.getCheckmate()) {
            return;
        }
        LinearLayout parent=(LinearLayout) v.getParent();
        LinearLayout grandParent=(LinearLayout) parent.getParent();
        int column=parent.indexOfChild(v);
        int row=grandParent.indexOfChild((View) parent);
        if (movePiece == null) {
            if (theBoard.pieceAt(row, column)) {
                Piece p = theBoard.getPieceAt(row, column);
                if (theBoard.getTurn() == p.getColor()) {
                    lastPiece = movePiece;
                    movePiece = p;
                    theBoard.displayPossibleMoves(p);
                    theBoard.makeRed(row, column);
                }
            }
        }
        else if (endRow == -1) {
            endRow = row;
            endCol = column;
            theBoard.makeRed(row, column);
        }
        else {
            clearMove();
            theBoard.resetBoardColors();
        }
    }
}
