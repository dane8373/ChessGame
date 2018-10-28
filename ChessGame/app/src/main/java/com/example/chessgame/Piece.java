package com.example.chessgame;


import android.widget.ImageView;

import java.util.ArrayList;

public abstract class Piece {

	static final int BLACK=2;
	static final int WHITE=1;
	static final int RED=0;
    /*
	static final int PAWN=0;
	static final int KNIGHT=1;
	static final int BISHOP=2;
	static final int ROOK=3;
	static final int QUEEN=4;
	static final int KING=5;*/
    static final int BOARDSIZE=8;

    protected ChessBoard theBoard;
	protected int mColor;
	protected int row;
	protected int col;
	protected int specialMove=1;
	protected ImageView squareImage;

	public void setBoard(ChessBoard b) {
	    theBoard = b;
    }

	public Piece(Piece p){
		mColor=p.getColor();
		row=p.row;
		col=p.col;
		specialMove=p.specialMove;
	}

	public Piece(int color, int row, int column, ImageView square, ChessBoard board) {
		mColor=color;
		this.row=row;
		this.col=column;
		theBoard = board;
		squareImage = square;
		specialMove=1;
	}

	public void setPosition(int row, int column, ImageView square) {
		this.row=row;
		this.col=column;
		squareImage = square;
	}

	public void setColor(int color) {
		mColor=color;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
	    return col;
    }

	public int getColor() {
		return mColor;
	}
	
	public int specialMove() {
		return specialMove;
	}
	
	public void specialMove(int set) {
		specialMove=set;
	}

    public ArrayList<ArrayList<Integer>> allValidMoves() {
        ArrayList<ArrayList<Integer>> ret = new ArrayList<ArrayList<Integer>>();
        for (int i=0; i<BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                ArrayList<Integer> temp = new ArrayList<Integer>();
                if (isValidMove(i, j)) {
                    temp.add(i);
                    temp.add(j);
                    ret.add(temp);
                }
            }
        }
        return ret;
    }

	public abstract boolean isValidMove(int row, int col);
	public abstract void makeRed();
	public abstract void unRed();
	public abstract void makeGreen();
    public abstract void unGreen();
    public abstract void setPieceImage();
    public abstract void removePieceImage();

}
