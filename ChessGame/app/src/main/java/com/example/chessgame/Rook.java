package com.example.chessgame;

import android.util.Log;
import android.widget.ImageView;

public class Rook extends Piece {
    public Rook(int color, int row, int column, ImageView square, ChessBoard board) {
        super(color, row, column, square, board);
        setPieceImage();
        this.specialMove(1);
    }

    @Override
    public void setPieceImage() {
        if (mColor == WHITE) {
            if(ChessBoard.getSquareColor(row, col) == WHITE) {
                squareImage.setImageResource(R.drawable.white_rook_white);
            }
            else {
                squareImage.setImageResource(R.drawable.white_rook_grey);
            }
        }
        else {
            if(ChessBoard.getSquareColor(row, col) == WHITE) {
                squareImage.setImageResource(R.drawable.black_rook_white);
            }
            else {
                squareImage.setImageResource(R.drawable.black_rook_grey);
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
            squareImage.setImageResource(R.drawable.white_rook_red);
        }
        else {
            squareImage.setImageResource(R.drawable.black_rook_red);
        }
    }

    @Override
    public void unRed() {
        setPieceImage();
    }

    @Override
    public void makeGreen() {
        if (mColor == WHITE) {
            squareImage.setImageResource(R.drawable.white_rook_green);
        }
        else {
            squareImage.setImageResource(R.drawable.black_rook_green);
        }
    }

    @Override
    public void unGreen() {
        setPieceImage();
    }

    @Override
    public boolean isValidMove(int endRow, int endColumn) {
        if (castleHandler(endRow, endColumn)) {
            return true;
        }
        if (row==endRow)
        {
            if (col>endColumn)
                for (int i=col-1; i>endColumn; i--)
                {
                    if (theBoard.pieceAt(row, i)==true)
                        return false;
                }
            else
                for (int i=col+1; i<endColumn; i++)
                {
                    if (theBoard.pieceAt(row, i)==true)
                        return false;
                }
            if (theBoard.pieceAt(endRow, endColumn) && theBoard.getPieceAt(endRow, endColumn).getColor() == mColor)
                return false;
            else
                return true;
        }
        else if (col==endColumn) {
            if (row > endRow)
                for (int i = row - 1; i > endRow; i--) {
                    if (theBoard.pieceAt(i, col) == true)
                        return false;
                }

            else
                for (int i = row + 1; i < endRow; i++) {
                    if (theBoard.pieceAt(i, col) == true)
                        return false;
                }
            if (theBoard.pieceAt(endRow, endColumn) && theBoard.getPieceAt(endRow, endColumn).getColor() == mColor)
                return false;
            else
                return true;
        }
        else return false;
    }

    public Boolean castleHandler(int endRow, int endCol)
    {
        Piece king = theBoard.getPieceAt(endRow, endCol);
        if (king == null || !(king instanceof King) || king.specialMove() == 0 || this.specialMove() == 0
                || this.getColor() != king.getColor() || theBoard.isCheck(king.getColor())) {
            return false;
        }
        Piece rook = this;
        if (king.getCol()>rook.getCol()) //queenside castling
        {
            for (int i=rook.getCol()+1; i<king.getCol(); i++)
            {
                if (theBoard.pieceAt(king.getRow(), i)) //if there are pieces int the way we can't castle :(
                {
                    System.out.println("2 error");
                    return false;
                }
                if (i>rook.getCol()+1) {
                    System.out.println("3 error");
                    if (theBoard.moveIntoCheck(king, king.getRow(), i)) {
                        return false;
                    }
                }
            }
            return true;
        }
        else
        {
            for (int i=king.getCol()+1; i<rook.getCol(); i++)
            {
                if (theBoard.pieceAt(king.getRow(), i)) //if there are pieces int the way we can't castle :(
                {
                    System.out.println("4  error");
                    return false;
                }
                if (i<rook.getCol()) {
                    if (theBoard.moveIntoCheck(king, king.getRow(), i)) {
                        System.out.println("5 error");
                        return false;
                    }
                }
            }
            return true;
        }
    }

}
