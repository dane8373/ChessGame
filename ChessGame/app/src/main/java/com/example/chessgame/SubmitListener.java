package com.example.chessgame;

import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class SubmitListener implements View.OnClickListener {
    Context theContext;
    PopupMenu promotion;
    ChessBoard theBoard;
    TextView turn;
    private static Boolean checkmate = false;
    private static int WHITE = 1;
    private static int BLACK = 2;

    public void setPromotionMenu() {
        promotion = new PopupMenu(theContext, turn);
        promotion.getMenu().add(Menu.NONE, 0, Menu.NONE, "Queen");
        promotion.getMenu().add(Menu.NONE, 1, Menu.NONE, "Rook");
        promotion.getMenu().add(Menu.NONE, 2, Menu.NONE, "Bishop");
        promotion.getMenu().add(Menu.NONE, 3, Menu.NONE, "Knight");
        promotion.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 3:
                        theBoard.changePieceAt( "Knight");
                        break;
                    case 2:
                        theBoard.changePieceAt("Bishop");
                        break;
                    case 1:
                        theBoard.changePieceAt( "Rook");
                        break;
                    case 0:
                        theBoard.changePieceAt("Queen");
                        break;
                }
                return false;
            }

        });
    }

    public SubmitListener(Context c, ChessBoard b, TextView t) {
        theContext = c;
        theBoard = b;
        turn = t;
    }

    public static boolean getCheckmate() {
        return checkmate;
    }
    @Override
    public void onClick(View v) {
        if (checkmate) {
            return;
        }
        Piece movePiece = SquareListener.getMovePiece();
        if (movePiece == null) {
            Toast.makeText(theContext, "Please select a piece to move", Toast.LENGTH_SHORT).show();
            return;
        }
        int endRow = SquareListener.getEndRow();
        int endCol = SquareListener.getEndCol();
        int prevRow = movePiece.getRow();
        int prevCol = movePiece.getCol();
        if (endRow == -1) {
            Toast.makeText(theContext, "Please select a destination for the piece", Toast.LENGTH_SHORT).show();
            return;
        }
        if (movePiece.isValidMove(endRow, endCol)) {
            if (!theBoard.moveIntoCheck(movePiece, endRow, endCol)) {
                if (movePiece instanceof Pawn) {
                    if (movePiece.getColor() == WHITE) {
                        if (endRow == 0) {
                            promotion.show();
                        }
                    }
                    else
                    if (endRow == 7) {
                        promotion.show();
                    }
                }
                theBoard.pawnChecker(theBoard.getTurn());
                if (movePiece instanceof Pawn && (endRow - movePiece.getRow()) % 2 == 0) {
                    movePiece.specialMove(2);
                }
                else if (movePiece instanceof King || movePiece instanceof Rook) {
                    movePiece.specialMove(0);
                }
                if (movePiece instanceof Pawn && endCol != prevCol && theBoard.pieceAt(endRow, endCol) == false) {
                    if (movePiece.getColor() == WHITE)
                        theBoard.removePieceAt(endRow+1, endCol);
                    else
                        theBoard.removePieceAt(endRow-1, endCol);
                }
                theBoard.movePiece(movePiece, endRow, endCol);
            }
        }
        if (theBoard.isCheck(theBoard.getTurn())) {
            if (theBoard.weakCheckmate(theBoard.getTurn())) {
                if (theBoard.isCheckmate(theBoard.getTurn())) {
                    checkmate = true;
                    if (theBoard.getTurn() == BLACK) {
                        Toast.makeText(theContext, "Checkmate, White wins", Toast.LENGTH_LONG).show();
                        turn.setText("White has won");
                    }
                    else {
                        Toast.makeText(theContext, "Checkmate, black wins", Toast.LENGTH_LONG).show();
                        turn.setText("Black has won");
                    }
                }
                theBoard.resetBoardColors();
            }
            else if (theBoard.getTurn() == BLACK) {
                Toast.makeText(theContext, "Black is in check", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(theContext, "White is in check", Toast.LENGTH_LONG).show();
            }
        }
        if (checkmate == false){
            theBoard.resetBoardColors();
            SquareListener.clearMove();
            if (theBoard.getTurn() == BLACK) {
                turn.setText("Black's Turn");
            } else {
                turn.setText("White's Turn");
            }
        }
    }
}
