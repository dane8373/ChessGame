package com.example.chessgame;

public class ChessBoard {
	private Boolean[][] pieceAt=new Boolean[8][8];
	int[] blackKing=new int[2];
	int[] whiteKing=new int[2];
	
	public ChessBoard() {
		for (int i=0; i<8; i++)
			for (int j=0; j<8; j++)
				pieceAt[i][j]=false;
		blackKing[0]=0;
		blackKing[1]=0;
		whiteKing[0]=0;
		whiteKing[1]=0;
	}
	
	public Boolean pieceAt(int row, int column){
		return  pieceAt[row][column];
	}
	
	public int[] whiteKing(){
		return  whiteKing;
	}
	public int[] blackKing(){
		return  blackKing;
	}
	
	public void whiteKing(int row, int column){
		whiteKing[0]=row;
		whiteKing[1]=column;
	}
	public void blackKing(int row, int column){
		blackKing[0]=row;
		blackKing[1]=column;
	}
	
	public void placePiece(int row, int column) {
		pieceAt[row][column]=true;
	}
	
	public void removePiece(int row, int column) {
		pieceAt[row][column]=false;
	}
	
}
