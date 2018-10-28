package com.example.chessgame;

import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class Pawn extends Piece {
    public Pawn(int color, int row, int column, ImageView square, ChessBoard board) {
        super(color, row, column, square, board);
        setPieceImage();
    }

    @Override
    public void setPieceImage() {
        if (mColor == WHITE) {
            if(ChessBoard.getSquareColor(row, col) == WHITE) {
                squareImage.setImageResource(R.drawable.white_pawn_white);
            }
            else {
                squareImage.setImageResource(R.drawable.white_pawn_grey);
            }
        }
        else {
            if(ChessBoard.getSquareColor(row, col) == WHITE) {
                squareImage.setImageResource(R.drawable.black_pawn_white);
            }
            else {
                squareImage.setImageResource(R.drawable.black_pawn_grey);
            }
        }
    }

    @Override
    public void removePieceImage() {
        if(ChessBoard.getSquareColor(row, col) == WHITE) {
            squareImage.setImageResource(R.drawable.white_square);
        }
        else {
            squareImage.setImageResource(R.drawable.grey_square);
        }
    }

    @Override
    public void makeRed() {
        if (mColor == WHITE) {
            squareImage.setImageResource(R.drawable.white_pawn_red);
        }
        else {
            squareImage.setImageResource(R.drawable.black_pawn_red);
        }
    }

    @Override
    public void unRed() {
        setPieceImage();
    }

    @Override
    public void makeGreen() {
        if (mColor == WHITE) {
            squareImage.setImageResource(R.drawable.white_pawn_green);
        }
        else {
            squareImage.setImageResource(R.drawable.black_pawn_green);
        }
    }

    @Override
    public void unGreen() {
        setPieceImage();
    }

    @Override
    public boolean isValidMove(int endRow, int endColumn) {
        if (mColor==BLACK)
        {
            if (endRow==row+1 && col==endColumn && !(theBoard.pieceAt(endRow,endColumn)))
                return true;
            if (row==1)
            {
                if (endRow==row+2 && col==endColumn && !(theBoard.pieceAt(endRow-1,endColumn)) && !(theBoard.pieceAt(endRow,endColumn)))
                    return true;
            }
            if (endRow==row+1 && (col==endColumn+1 || col==endColumn-1) && (theBoard.pieceAt(endRow,endColumn))
                    && theBoard.getPieceAt(endRow, endColumn).getColor() != mColor)
                return true;
        }
        else
        {
            if (endRow==row-1 && col==endColumn  && !(theBoard.pieceAt(endRow,endColumn)))
                return true;
            if (row==6)
                if (endRow==row-2 && col==endColumn && !(theBoard.pieceAt(endRow+1,endColumn)) && !(theBoard.pieceAt(endRow,endColumn)))
                    return true;
                else ;
            if (endRow==row-1 && (col==endColumn+1 || col==endColumn-1) && (theBoard.pieceAt(endRow,endColumn))
                    && theBoard.getPieceAt(endRow, endColumn).getColor() != mColor)
                return true;
        }
        if (enPassantHandler(endRow, endColumn)) {
            //this.specialMove(3);
            return true;
        }
        return false;
    }

    public Boolean enPassantHandler(int endRow, int endCol)
    {
        if (endRow!=5 && endRow !=2 || (endCol != col+1 && endCol !=col-1)
                || (endRow != row+1 && endRow !=row-1) || theBoard.pieceAt(endRow, endCol)) {
            return false;
        }
        if (mColor==BLACK)
        {
            Piece p=theBoard.getPieceAt(endRow-1, endCol);
            if (p==null  || p.getColor()==mColor || p.specialMove()!=2)
                return false;
            else
            {
                return true;
            }
        }
        else
        {
            Piece p=theBoard.getPieceAt(endRow+1, endCol);
            if (p==null  || p.getColor()==mColor || p.specialMove()!=2)
                return false;
            else
            {
                return true;
            }
        }
    }
}
