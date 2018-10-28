package com.example.chessgame;

import android.widget.ImageView;

public class Bishop extends Piece {
    public Bishop(int color, int row, int column, ImageView square, ChessBoard board) {
        super(color, row, column, square, board);
        setPieceImage();
    }

    @Override
    public void setPieceImage() {
        if (mColor == WHITE) {
            if(ChessBoard.getSquareColor(row, col) == WHITE) {
                squareImage.setImageResource(R.drawable.white_bishop_white);
            }
            else {
                squareImage.setImageResource(R.drawable.white_bishop_grey);
            }
        }
        else {
            if(ChessBoard.getSquareColor(row, col) == WHITE) {
                squareImage.setImageResource(R.drawable.black_bishop_white);
            }
            else {
                squareImage.setImageResource(R.drawable.black_bishop_grey);
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
            squareImage.setImageResource(R.drawable.white_bishop_red);
        }
        else {
            squareImage.setImageResource(R.drawable.black_bishop_red);
        }
    }

    @Override
    public void unRed() {
        setPieceImage();
    }

    @Override
    public void makeGreen() {
        if (mColor == WHITE) {
            squareImage.setImageResource(R.drawable.white_bishop_green);
        }
        else {
            squareImage.setImageResource(R.drawable.black_bishop_green);
        }
    }

    @Override
    public void unGreen() {
        setPieceImage();
    }

    @Override
    public boolean isValidMove(int endRow, int endColumn) {
        int distX=(endColumn-col);
        int distY=(endRow-row);
        if (distX*distX-distY*distY!=0)
            return false;
        else
        {
            if (distX>0)
            {
                if (distY>0)
                {
                    for (int i=1; i<distY; i++)
                    {
                        if (theBoard.pieceAt(row+i,col+i))
                            return false;
                    }
                    if (theBoard.pieceAt(endRow, endColumn) && theBoard.getPieceAt(endRow, endColumn).getColor() == mColor)
                        return false;
                    else
                        return true;
                }
                else
                {
                    for (int i=1; i<distX; i++)
                    {
                        if (theBoard.pieceAt(row-i,col+i))
                            return false;
                    }
                    if (theBoard.pieceAt(endRow, endColumn) && theBoard.getPieceAt(endRow, endColumn).getColor() == mColor)
                        return false;
                    else
                        return true;
                }
            }
            else
            {
                if (distY>0)
                {
                    for (int i=1; i<distY; i++)
                    {
                        if (theBoard.pieceAt(row+i,col-i))
                            return false;
                    }
                    if (theBoard.pieceAt(endRow, endColumn) && theBoard.getPieceAt(endRow, endColumn).getColor() == mColor)
                        return false;
                    else
                        return true;
                }
                else
                {
                    for (int i=1; i<(-1*distX); i++)
                    {
                        if (theBoard.pieceAt(row-i,col-i))
                            return false;
                    }
                    if (theBoard.pieceAt(endRow, endColumn) && theBoard.getPieceAt(endRow, endColumn).getColor() == mColor)
                    return false;
                        else
                    return true;
                }
            }
        }
    }
}
