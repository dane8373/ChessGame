/*package com.example.chessgame;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ChessActivity extends Activity {

	private LinearLayout rows[]=new LinearLayout[8];
	private LinearLayout board;
	private ImageView square[][]=new ImageView[8][8];
	int rowIds[]={R.id.row_0, R.id.row_1, R.id.row_2, R.id.row_3, R.id.row_4, R.id.row_5, R.id.row_6, R.id.row_7};
	int squareIds[]={
			R.id.row0_sq0, R.id.row0_sq1, R.id.row0_sq2, R.id.row0_sq3, R.id.row0_sq4, R.id.row0_sq5, R.id.row0_sq6, R.id.row0_sq7, 
			R.id.row1_sq0, R.id.row1_sq1, R.id.row1_sq2, R.id.row1_sq3, R.id.row1_sq4, R.id.row1_sq5, R.id.row1_sq6, R.id.row1_sq7, 
			R.id.row2_sq0, R.id.row2_sq1, R.id.row2_sq2, R.id.row2_sq3, R.id.row2_sq4, R.id.row2_sq5, R.id.row2_sq6, R.id.row2_sq7, 
			R.id.row3_sq0, R.id.row3_sq1, R.id.row3_sq2, R.id.row3_sq3, R.id.row3_sq4, R.id.row3_sq5, R.id.row3_sq6, R.id.row3_sq7, 
			R.id.row4_sq0, R.id.row4_sq1, R.id.row4_sq2, R.id.row4_sq3, R.id.row4_sq4, R.id.row4_sq5, R.id.row4_sq6, R.id.row4_sq7, 
			R.id.row5_sq0, R.id.row5_sq1, R.id.row5_sq2, R.id.row5_sq3, R.id.row5_sq4, R.id.row5_sq5, R.id.row5_sq6, R.id.row5_sq7, 
			R.id.row6_sq0, R.id.row6_sq1, R.id.row6_sq2, R.id.row6_sq3, R.id.row6_sq4, R.id.row6_sq5, R.id.row6_sq6, R.id.row6_sq7, 
			R.id.row7_sq0, R.id.row7_sq1, R.id.row7_sq2, R.id.row7_sq3, R.id.row7_sq4, R.id.row7_sq5, R.id.row7_sq6, R.id.row7_sq7, };
	private TextView turn;
	private Button submit;

    static final int BLACK=2;
	static final int WHITE=1;
	int TURN=WHITE;
	static final int RED=0;
	static final int PAWN=0;
	static final int KNIGHT=1;
	static final int BISHOP=2;
	static final int ROOK=3;
	static final int QUEEN=4;
	static final int KING=5;
	
	ChessBoard theBoard=new ChessBoard();
	Vector<Piece> whitePieces=new Vector<Piece>();
	Vector<Piece> blackPieces=new Vector<Piece>();
	Stack<int[]> redSpaces=new Stack<int[]>(); 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chess);
		RelativeLayout frame= (RelativeLayout) findViewById(R.id.frame);
		submit=(Button) findViewById(R.id.submit);
		turn=(TextView) findViewById(R.id.turn);
		board=(LinearLayout) findViewById(R.id.table_1);
		Log.i("board width", ""+frame.getWidth());
		int j=0;
		for (int i:rowIds){
			//Log.i("i checker", ""+i);
				rows[j]=(LinearLayout) findViewById(i);
				j++;
		}
		int n=0;
		int m=0;
				for (int z:squareIds)
				{
					square[n][m]=(ImageView) findViewById(z);
					m++;
					if (m>7)
					{
						m=0;
						n++;
					}
				}
		
			setUpBoard();	

			View.OnClickListener listener = new View.OnClickListener() {
				@Override
				public void onClick(View v){
					LinearLayout parent=(LinearLayout) v.getParent();
					LinearLayout grandParent=(LinearLayout) parent.getParent();
					int column=parent.indexOfChild(v);
					int row=grandParent.indexOfChild((View) parent);
					int [] loc={row,column};
					Piece p=getPieceAt(row,column);
					if (redSpaces.size()==0)
					{
						if (p!=null && p.getColor()==TURN)
						{
							redSpaces.push(loc);
							makeRed(loc[0],loc[1]);
							Log.i("go clicked checker", ""+row+""+column);
						}
					}
					else if (redSpaces.size()==1)
					{
						redSpaces.push(loc);
						makeRed(loc[0],loc[1]);
					}
					else
					{
						int [] redLoc;
						while (!redSpaces.empty())
						{
							redLoc=redSpaces.pop();
							unRed(redLoc[0],redLoc[1]);
						}
						if (p!=null && p.getColor()==TURN)
						{
							redSpaces.push(loc);
							makeRed(loc[0],loc[1]);
						}
					}
				;
				}
			};
			for (int ii=0; ii<8; ii++)
				for (int jj=0; jj<8; jj++)
					square[ii][jj].setOnClickListener(listener);
			
			submit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v){
					if (TURN==WHITE)
					{
						if (submitMove())
						{
							TURN=BLACK;
							turn.setText("Black\'s turn");
						}
					}
					else
					{
						if (submitMove())
						{
							TURN=WHITE;
							turn.setText("White\'s turn");	
						}
					}
				}
			});
}

	public void setUpBoard() {
		//set up back row for black pieces
		blackPieces.add(new Piece(ROOK, BLACK, 0,0));
		blackPieces.add(new Piece(KNIGHT, BLACK, 0,1));
		blackPieces.add(new Piece(BISHOP, BLACK, 0,2));
		blackPieces.add(new Piece(QUEEN, BLACK, 0,3));
		blackPieces.add(new Piece(KING, BLACK, 0,4));
		theBoard.blackKing(0, 4);
		blackPieces.add(new Piece(BISHOP, BLACK, 0,5));
		blackPieces.add(new Piece(KNIGHT, BLACK, 0,6));
		blackPieces.add(new Piece(ROOK, BLACK, 0,7));
		
		//set up back row for white pieces
		whitePieces.add(new Piece(ROOK, WHITE, 7,0));
		whitePieces.add(new Piece(KNIGHT, WHITE, 7,1));
		whitePieces.add(new Piece(BISHOP, WHITE, 7,2));
		whitePieces.add(new Piece(QUEEN, WHITE, 7,3));
		whitePieces.add(new Piece(KING, WHITE, 7,4));
		theBoard.whiteKing(7, 4);
		whitePieces.add(new Piece(BISHOP, WHITE, 7,5));
		whitePieces.add(new Piece(KNIGHT, WHITE, 7,6));
		whitePieces.add(new Piece(ROOK, WHITE, 7,7));
		
		//set up pawns and tell the board which spaces currently have pieces on them
		for (int i=0; i<7; i++)
		{
			blackPieces.add(new Piece(PAWN, BLACK, 1, i));
			whitePieces.add(new Piece(PAWN, WHITE, 6, i));
			theBoard.placePiece(0, i);
			theBoard.placePiece(1, i);
			theBoard.placePiece(6, i);
			theBoard.placePiece(7, i);
		}
	}
	
	public void makeRed(int row, int column) {

		Piece p=getPieceAt(row,column);
		if (p!=null)
		{
			switch (p.getType()) 
			{
				case PAWN:
				{ 
					if (p.getColor()==BLACK)
						square[row][column].setImageResource(R.drawable.black_pawn_red);
					else
						square[row][column].setImageResource(R.drawable.white_pawn_red);
					break;
				}
				case ROOK:
				{ 
					if (p.getColor()==BLACK)
						square[row][column].setImageResource(R.drawable.black_rook_red);
					else
						square[row][column].setImageResource(R.drawable.white_rook_red);
					break;
				}
				
				case KNIGHT:
				{ 
					if (p.getColor()==BLACK)
						square[row][column].setImageResource(R.drawable.black_knight_red);
					else
						square[row][column].setImageResource(R.drawable.white_knight_red);
					break;
				}
				case BISHOP:
				{ 
					if (p.getColor()==BLACK)
						square[row][column].setImageResource(R.drawable.black_bishop_red);
					else
						square[row][column].setImageResource(R.drawable.white_bishop_red);
					break;
				}
				case QUEEN:
				{ 
					if (p.getColor()==BLACK)
						square[row][column].setImageResource(R.drawable.black_queen_red);
					else
						square[row][column].setImageResource(R.drawable.white_queen_red);
					break;
				}
				case KING:
				{ 
					if (p.getColor()==BLACK)
						square[row][column].setImageResource(R.drawable.black_king_red);
					else
						square[row][column].setImageResource(R.drawable.white_king_red);
					break;
				}
			}
		}
		else
			square[row][column].setImageResource(R.drawable.red_square);
	}
	
	public Piece getPieceAt(int row, int column) {
		Piece ret=null;
		for (Piece i:whitePieces)
		{
			if ((i.getPosition())[0]==row && (i.getPosition())[1]==column)
				ret=i;
		}
		for (Piece i:blackPieces)
		{
			if ((i.getPosition())[0]==row && (i.getPosition())[1]==column)
				ret=i;
		}
			return ret;
	}
	
	public void unRed(int row, int column) {
		int spaceColor;
		if ((row+column)%2==0)
			spaceColor=WHITE;
		else
			spaceColor=BLACK;
		Piece p=getPieceAt(row,column);
		if (p!=null)
		{
			if (spaceColor==WHITE)
			{
				switch (p.getType()) 
				{
					case PAWN:
					{ 
						if (p.getColor()==BLACK)
							square[row][column].setImageResource(R.drawable.black_pawn_white);
						else
							square[row][column].setImageResource(R.drawable.white_pawn_white);
						break;
					}
					case ROOK:
					{ 
						if (p.getColor()==BLACK)
							square[row][column].setImageResource(R.drawable.black_rook_white);
						else
							square[row][column].setImageResource(R.drawable.white_rook_white);
						break;
					}
				
					case KNIGHT:
					{ 
						if (p.getColor()==BLACK)
							square[row][column].setImageResource(R.drawable.black_knight_white);
						else
							square[row][column].setImageResource(R.drawable.white_knight_white);
						break;
					}
					case BISHOP:
					{ 
						if (p.getColor()==BLACK)
							square[row][column].setImageResource(R.drawable.black_bishop_white);
						else
							square[row][column].setImageResource(R.drawable.white_bishop_white);
						break;
					}
					case QUEEN:
					{ 
						if (p.getColor()==BLACK)
							square[row][column].setImageResource(R.drawable.black_queen_white);
						else
							square[row][column].setImageResource(R.drawable.white_queen_white);
						break;
					}
					case KING:
					{ 
						if (p.getColor()==BLACK)
							square[row][column].setImageResource(R.drawable.black_king_white);
						else
							square[row][column].setImageResource(R.drawable.white_king_white);
						break;
					}
				}
			}
			else
			{
				switch (p.getType()) 
				{
					case PAWN:
					{ 
						if (p.getColor()==BLACK)
							square[row][column].setImageResource(R.drawable.black_pawn_grey);
						else
							square[row][column].setImageResource(R.drawable.white_pawn_grey);
						break;
					}
					case ROOK:
					{ 
						if (p.getColor()==BLACK)
							square[row][column].setImageResource(R.drawable.black_rook_grey);
						else
							square[row][column].setImageResource(R.drawable.white_rook_grey);
						break;
					}
				
					case KNIGHT:
					{ 
						if (p.getColor()==BLACK)
							square[row][column].setImageResource(R.drawable.black_knight_grey);
						else
							square[row][column].setImageResource(R.drawable.white_knight_grey);
						break;
					}
					case BISHOP:
					{ 
						if (p.getColor()==BLACK)
							square[row][column].setImageResource(R.drawable.black_bishop_grey);
						else
							square[row][column].setImageResource(R.drawable.white_bishop_grey);
						break;
					}
					case QUEEN:
					{ 
						if (p.getColor()==BLACK)
							square[row][column].setImageResource(R.drawable.black_queen_grey);
						else
							square[row][column].setImageResource(R.drawable.white_queen_grey);
						break;
					}
					case KING:
					{ 
						if (p.getColor()==BLACK)
							square[row][column].setImageResource(R.drawable.black_king_grey);
						else
							square[row][column].setImageResource(R.drawable.white_king_grey);
						break;
					}
				}
			}
		}
		else if (spaceColor==WHITE)
			square[row][column].setImageResource(R.drawable.white_square);
		else
			square[row][column].setImageResource(R.drawable.grey_square);
	}
	
	public Boolean isValidMove(Piece p, int endRow, int endColumn) {
		if (p!=null)
		{
			int row=(p.getPosition())[0];
			int col=(p.getPosition())[1];
			if (row==endRow && col==endColumn)
				return false;
			switch (p.getType()) 
			{
				case PAWN: //need to somehow add enpassant capture
				{ 
					if (p.getColor()==BLACK)
					{
						if (endRow==row+1 && col==endColumn && !(theBoard.pieceAt(endRow,endColumn)))
							return true;
						else if (row==1)
							if (endRow==row+2 && col==endColumn && !(theBoard.pieceAt(endRow,endColumn)))
								return true;
							else ;
						else if (endRow==row+1 && (col==endColumn+1 || col==endColumn-1) && (theBoard.pieceAt(endRow,endColumn)))
							return true;
						else ;
					}
					else 
					{
						if (endRow==row-1 && col==endColumn && !(theBoard.pieceAt(endRow,endColumn)))
							return true;
						else if (row==6)
							if (endRow==row-2 && col==endColumn && !(theBoard.pieceAt(endRow,endColumn)))
								return true;
							else ;
						else if (endRow==row-1 && (col==endColumn+1 || col==endColumn-1) && (theBoard.pieceAt(endRow,endColumn)))
							return true;
						else ;
						return false;
					}
				}
				case KNIGHT:
				{ 
					if ((row==endRow+1 || row==endRow-1) && (col==endColumn+2 || col==endColumn-2))
						return true;
					else if ((row==endRow+2 || row==endRow-2) && (col==endColumn+1 || col==endColumn-1))
						return true;
					else return false;
				}
				case ROOK: // need to add castling
				{ 
					Boolean ret=false;
					if (row==endRow)
					{
						if (col>endColumn)
							for (int i=col; i>endColumn; i--)
								ret=!(theBoard.pieceAt(row, i));
						else
							for (int i=col; i<endColumn; i++)
								ret=!(theBoard.pieceAt(row, i));
					return ret;
					}
					else if (col==endColumn)
					{
						if (row>endRow)
							for (int i=row; i>endRow; i--)
								ret=!(theBoard.pieceAt(i, col));
						else
							for (int i=row; i<endRow; i++)
								ret=!(theBoard.pieceAt(row, i));
					return ret;
					}
					else return false;
				}
				case BISHOP:
				{
					Boolean ret=false;
					if (row>endRow)
					{
						if (col>endColumn)
							for (int i=1; i<=(col-endColumn); i++)
								ret=!(theBoard.pieceAt(row+i, col-i));
						else
							for (int i=1; i<=(endColumn-col); i++)
								ret=!(theBoard.pieceAt(row+i, col+i));
					return ret;
					}
					else if (row<endRow)
					{
						if (col>endColumn)
							for (int i=1; i<=(col-endColumn); i++)
								ret=!(theBoard.pieceAt(row-i, col-i));
						else
							for (int i=1; i<=(endColumn-col); i++)
								ret=!(theBoard.pieceAt(row-i, col+i));
					return ret;
					}
					else return false;
				}
					
				case QUEEN:
				{ 
					Boolean ret=false;
					if (row>endRow)
					{
						if (col>endColumn)
							for (int i=1; i<=(col-endColumn); i++)
								ret=!(theBoard.pieceAt(row+i, col-i));
						else
							for (int i=1; i<=(endColumn-col); i++)
								ret=!(theBoard.pieceAt(row+i, col+i));
					return ret;
					}
					else if (row<endRow)
					{
						if (col>endColumn)
							for (int i=1; i<=(col-endColumn); i++)
								ret=!(theBoard.pieceAt(row-i, col-i));
						else
							for (int i=1; i<=(endColumn-col); i++)
								ret=!(theBoard.pieceAt(row-i, col+i));
					return ret;
					}
					else if (row==endRow)
					{
						if (col>endColumn)
							for (int i=col; i>endColumn; i--)
								ret=!(theBoard.pieceAt(row, i));
						else
							for (int i=col; i<endColumn; i++)
								ret=!(theBoard.pieceAt(row, i));
					return ret;
					}
					else if (col==endColumn)
					{
						if (row>endRow)
							for (int i=row; i>endRow; i--)
								ret=!(theBoard.pieceAt(i, col));
						else
							for (int i=row; i<endRow; i++)
								ret=!(theBoard.pieceAt(row, i));
					return ret;
					}
					else return false;
				}
				case KING: //need to add checks for check
				{ 
					if ((endRow-1)<=row && (endRow+1)>=row && (endColumn-1)<=col && (endColumn+1)>=col)
						return true;
					else return false;
				}
			}
		}
		return false;
	}
	
	public Boolean submitMove() {
		if (redSpaces.size()<2)
		{
			int [] space ;
			Toast.makeText(getApplicationContext(), "Destination space not selected", Toast.LENGTH_LONG).show();
			while (!redSpaces.empty())
			{
				space=redSpaces.pop();
				unRed(space[0],space[1]);
			}
			return false;
			
		}
		else
		{
			int [] endSpace=redSpaces.pop();
			int [] startSpace=redSpaces.pop();
			Piece p=getPieceAt(startSpace[0],startSpace[1]);
			if (isValidMove(p, endSpace[0], endSpace[1]))
				{
				movePiece(p,endSpace[0], endSpace[1]);
				return true;
				}
			else 
			{
				Toast.makeText(getApplicationContext(), "Invalid Move", Toast.LENGTH_LONG).show();
				unRed(startSpace[0], startSpace[1]);
				unRed(endSpace[0], endSpace[1]);
				return false;
			}
		}
	}


	public int [] getPieceIndexAt(int row, int column) {
		for (Piece i:whitePieces)
		{
		if ((i.getPosition())[0]==row && (i.getPosition())[1]==column)
			whitePieces.remove(i);
		}
		for (Piece i:blackPieces)
		{
		if ((i.getPosition())[0]==row && (i.getPosition())[1]==column)
			blackPieces.remove(i);
		}
	}
	public void movePiece(Piece p, int endRow, int endColumn) {
		int [] loc=p.getPosition();
		int row=loc[0];
		int col=loc[1];
		for (Piece i:whitePieces)
		{
			if ((i.getPosition())[0]==row && (i.getPosition())[1]==col)
				i.setPosition(endRow, endColumn);

		}
		for (Piece i:blackPieces)
		{
			if ((i.getPosition())[0]==row && (i.getPosition())[1]==col)
				i.setPosition(endRow, endColumn);
		}
		theBoard.removePiece(row, col);
		theBoard.placePiece(endRow, endColumn);
		if (p.getType()==KING)
		{
		if (p.getColor()==BLACK)
			theBoard.blackKing(endRow, endColumn);
		else 
			theBoard.whiteKing(endRow, endColumn);
		}
		unRed(endRow, endColumn);
		unRed(row, col);
	}
	
}*/
