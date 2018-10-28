package com.example.chessgame;

import android.widget.ImageView;

public class Knight extends Piece {
    public Knight(int color, int row, int column, ImageView square, ChessBoard board) {
        super(color, row, column, square, board);
        setPieceImage();
    }

    @Override
    public void setPieceImage() {
        if (mColor == WHITE) {
            if(ChessBoard.getSquareColor(row, col) == WHITE) {
                squareImage.setImageResource(R.drawable.white_knight_white);
            }
            else {
                squareImage.setImageResource(R.drawable.white_knight_grey);
            }
        }
        else {
            if(ChessBoard.getSquareColor(row, col) == WHITE) {
                squareImage.setImageResource(R.drawable.black_knight_white);
            }
            else {
                squareImage.setImageResource(R.drawable.black_knight_grey);
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
            squareImage.setImageResource(R.drawable.white_knight_red);
        }
        else {
            squareImage.setImageResource(R.drawable.black_knight_red);
        }
    }

    @Override
    public void unRed() {
        setPieceImage();
    }

    @Override
    public void makeGreen() {
        if (mColor == WHITE) {
            squareImage.setImageResource(R.drawable.white_knight_green);
        }
        else {
            squareImage.setImageResource(R.drawable.black_knight_green);
        }
    }

    @Override
    public void unGreen() {
        setPieceImage();
    }

    @Override
    public boolean isValidMove(int endRow, int endColumn) {
        if ((row == endRow + 1 || row == endRow - 1) && (col == endColumn + 2 || col == endColumn - 2))
            if (theBoard.pieceAt(endRow,endColumn) && theBoard.getPieceAt(endRow, endColumn).getColor() == mColor)
                return false;
            else
                return true;
        else if ((row == endRow + 2 || row == endRow - 2) && (col == endColumn + 1 || col == endColumn - 1))
            if (theBoard.pieceAt(endRow,endColumn) && theBoard.getPieceAt(endRow, endColumn).getColor() == mColor)
                return false;
            else
                return true;
        else return false;
    }
}
