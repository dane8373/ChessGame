package com.example.chessgame;


public class Piece {

	static final int BLACK=2;
	static final int WHITE=1;
	static final int RED=0;

	static final int PAWN=0;
	static final int KNIGHT=1;
	static final int BISHOP=2;
	static final int ROOK=3;
	static final int QUEEN=4;
	static final int KING=5;

	private int mType;
	private int mColor;
	private int mPosition[]=new int[2];
	private int specialMove=1;
	
	public Piece(Piece p){
		mType=p.getType();
		mColor=p.getColor();
		mPosition[0]=p.getPosition()[0];
		mPosition[1]=p.getPosition()[1];
		specialMove=p.specialMove;
	}

	public Piece(int type, int color, int row, int column) {
		mType=type;
		mColor=color;
		mPosition[0]=row;
		mPosition[1]=column;
	}

	public void setPosition(int row, int column) {
		mPosition[0]=row;
		mPosition[1]=column;
	}

	public void setColor(int color) {
		mColor=color;
	}

	public void setType(int type) {
		mType=type;
	}

	public int getType() {
		return mType;
	}

	public int[] getPosition() {
		return mPosition;
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
}
