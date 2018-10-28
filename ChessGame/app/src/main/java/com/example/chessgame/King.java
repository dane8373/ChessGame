package com.example.chessgame;

import android.widget.ImageView;

public class King extends Piece {
    public King(int color, int row, int column, ImageView square, ChessBoard board) {
        super(color, row, column, square, board);
        this.specialMove(1);
        setPieceImage();
    }

    @Override
    public void setPieceImage() {
        if (mColor == WHITE) {
            if(ChessBoard.getSquareColor(row, col) == WHITE) {
                squareImage.setImageResource(R.drawable.white_king_white);
            }
            else {
                squareImage.setImageResource(R.drawable.white_king_grey);
            }
        }
        else {
            if(ChessBoard.getSquareColor(row, col) == WHITE) {
                squareImage.setImageResource(R.drawable.black_king_white);
            }
            else {
                squareImage.setImageResource(R.drawable.black_king_grey);
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
            squareImage.setImageResource(R.drawable.white_king_red);
        }
        else {
            squareImage.setImageResource(R.drawable.black_king_red);
        }
    }

    @Override
    public void unRed() {
        setPieceImage();
    }

    @Override
    public void makeGreen() {
        if (mColor == WHITE) {
            squareImage.setImageResource(R.drawable.white_king_green);
        }
        else {
            squareImage.setImageResource(R.drawable.black_king_green);
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
        if ((endRow-1)<=row && (endRow+1)>=row && (endColumn-1)<=col
                && (endColumn+1)>=col && !(row==endRow && col==endColumn))
            if (theBoard.pieceAt(endRow, endColumn) && theBoard.getPieceAt(endRow, endColumn).getColor() == mColor)
                return false;
            else
                return true;
        else return false;
    }

    public Boolean castleHandler(int endRow, int endCol)
    {
        Piece rook = theBoard.getPieceAt(endRow, endCol);
        if (rook == null || !(rook instanceof Rook) || rook.specialMove() == 0 || this.specialMove() == 0
                || this.getColor() != rook.getColor() || theBoard.isCheck(rook.getColor())) {
            return false;
        }
        Piece king = this;
        if (king.getCol()>rook.getCol()) //queenside castling
        {
            for (int i=rook.getCol()+1; i<king.getCol(); i++)
            {
                if (theBoard.pieceAt(king.getRow(), i)) //if there are pieces int the way we can't castle :(
                    return false;
                if (i>rook.getCol()+1)
                    if (theBoard.moveIntoCheck(king, king.getRow(), i))
                    {
                        return false;
                    }
            }
            return true;
        }
        else
        {
            for (int i=king.getCol()+1; i<rook.getCol(); i++)
            {
                if (theBoard.pieceAt(king.getRow(), i)) //if there are pieces int the way we can't castle :(
                    return false;
                if (i<rook.getCol())
                    if (theBoard.moveIntoCheck(king, king.getRow(), i))
                    {
                        return false;
                    }
            }
            return true;
        }
    }
}
