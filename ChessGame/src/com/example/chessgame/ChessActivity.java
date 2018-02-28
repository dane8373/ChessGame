package com.example.chessgame;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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
	private PopupMenu promotion;
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
	
	Vector<Piece> tempWhites=new Vector<Piece>();
	Vector<Piece> tempBlacks=new Vector<Piece>();
	ChessBoard tempBoard=new ChessBoard();
	ChessBoard theBoard=new ChessBoard();
	Vector<Piece> whitePieces=new Vector<Piece>();
	Vector<Piece> blackPieces=new Vector<Piece>();
	Stack<int[]> redSpaces=new Stack<int[]>(); 
	
	int bro=0; //used to display secret bro message
	int pawnPro=-1;
	int [] promoteSpace=new int[2];
	boolean promote=false;
	
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
			 promotion = new PopupMenu(this, turn);
			 promotion.getMenu().add(Menu.NONE, QUEEN, Menu.NONE, "Queen");
			 promotion.getMenu().add(Menu.NONE, ROOK, Menu.NONE, "Rook");
			 promotion.getMenu().add(Menu.NONE, BISHOP, Menu.NONE, "Bishop");
			 promotion.getMenu().add(Menu.NONE, KNIGHT, Menu.NONE, "Knight");
			 promotion.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
				 @Override
					public boolean onMenuItemClick(MenuItem item) {
					       switch (item.getItemId()) {
					       case KNIGHT:
					              pawnPro=KNIGHT;
					              break;
					       case BISHOP:
					    	   	  pawnPro=BISHOP;
					              break;
					       case ROOK:
					    	      pawnPro=ROOK;
					              break;
					       case QUEEN:
					    	   	  pawnPro=QUEEN;
					              break;
					       }
					       return false;
					}
				 
			 });
			View.OnClickListener listener = new View.OnClickListener() {
				@Override
				public void onClick(View v){
					LinearLayout parent=(LinearLayout) v.getParent();
					LinearLayout grandParent=(LinearLayout) parent.getParent();
					int column=parent.indexOfChild(v);
					int row=grandParent.indexOfChild((View) parent);
					int [] loc={row,column};
					int [] colorAndIndex;
					Piece p=null;
					colorAndIndex=getPieceAt(row,column);
					if (colorAndIndex!=null)
					{
						int pieceColor=colorAndIndex[0];
						int pieceIndex=colorAndIndex[1];
						if (pieceColor==BLACK)
							p=blackPieces.get(pieceIndex);
						else
							p=whitePieces.get(pieceIndex);
					}
					else
						p=null;
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
			
			final View.OnClickListener submitListener= new View.OnClickListener() {
				@Override
				public void onClick(View v){
					if (TURN==WHITE)
					{
						if (promote)
						{
							if (pawnPro==-1)
							{
								Toast.makeText(getApplicationContext(), "Please select promotion for pawn", Toast.LENGTH_LONG).show();
								promotion.show();
							}
							else
								{
								int [] colorAndIndex=getPieceAt(promoteSpace[0],promoteSpace[1]);
								whitePieces.get(colorAndIndex[1]).setType(pawnPro);
								unRed(promoteSpace[0],promoteSpace[1]);
								submit.setText("Submit Move");
								pawnPro=-1;
								promote=false;
								TURN=BLACK;
								if (isCheck(BLACK))
								{
									if (weakCheckmate(BLACK)|| TURN==BLACK)
									{
										if (isCheckmate(BLACK))
										{
											Toast.makeText(getApplicationContext(), "Checkmate, White wins", Toast.LENGTH_LONG).show();
											turn.setText("White has won");
										}
										else
										{
											turn.setText("Black\'s turn (Black in check)");
											Toast.makeText(getApplicationContext(), "Black is in Check", Toast.LENGTH_LONG).show();
										}
									}
									else
									{
										Toast.makeText(getApplicationContext(), "Black is in Check", Toast.LENGTH_LONG).show();
										turn.setText("Black\'s turn (Black in check)");
									}
								}
								else
								turn.setText("Black\'s turn");
								}
						}
						else if (submitMove())
						{
							if (promote)
								submit.setText("Promote Pawn");
							else
							{
								TURN=BLACK;
								if (isCheck(BLACK))
								{
									if (weakCheckmate(BLACK)|| TURN==BLACK)
									{
										if (isCheckmate(BLACK))
										{
											Toast.makeText(getApplicationContext(), "Checkmate, White wins", Toast.LENGTH_LONG).show();
											turn.setText("White has won");
										}
										else
										{
											turn.setText("Black\'s turn (Black in check)");
											Toast.makeText(getApplicationContext(), "Black is in Check", Toast.LENGTH_LONG).show();
										}
									}
									else
									{
										Toast.makeText(getApplicationContext(), "Black is in Check", Toast.LENGTH_LONG).show();
										turn.setText("Black\'s turn (Black in check)");
									}
								}
								else
									turn.setText("Black\'s turn");
							}
						}
					}
					else
					{
						if (promote)
						{
							if (pawnPro==-1)
							{
								Toast.makeText(getApplicationContext(), "Please select promotion for pawn", Toast.LENGTH_LONG).show();
								promotion.show();
							}
							else
							{
								int [] colorAndIndex=getPieceAt(promoteSpace[0],promoteSpace[1]);
								blackPieces.get(colorAndIndex[1]).setType(pawnPro);
								unRed(promoteSpace[0],promoteSpace[1]);
								submit.setText("Submit Move");
								pawnPro=-1;
								promote=false;
								TURN=BLACK;
								if (isCheck(WHITE))
								{
									if (weakCheckmate(WHITE))
									{
										if (isCheckmate(WHITE))
										{
											Toast.makeText(getApplicationContext(), "Checkmate, Black wins", Toast.LENGTH_LONG).show();
											turn.setText("Black has won");
										}
										else
										{
											turn.setText("White\'s turn (White in check)");
											Toast.makeText(getApplicationContext(), "White is in Check", Toast.LENGTH_LONG).show();
										}
									}
									else
									{
										turn.setText("White\'s turn (White in check)");
										Toast.makeText(getApplicationContext(), "White is in Check", Toast.LENGTH_LONG).show();
									}
								}
								else
								turn.setText("White\'s turn");
							}
						}
						else if (submitMove())
						{
							if (promote)
								submit.setText("Promote Pawn");
							else
							{
								TURN=WHITE;
								if (isCheck(WHITE))
								{
									if (weakCheckmate(WHITE))
									{
										if (isCheckmate(WHITE))
										{
											Toast.makeText(getApplicationContext(), "Checkmate, Black wins", Toast.LENGTH_LONG).show();
											turn.setText("Black has won");
										}
										else
										{
											turn.setText("White\'s turn (White in check)");
											Toast.makeText(getApplicationContext(), "White is in Check", Toast.LENGTH_LONG).show();
										}
									}
									else
									{
										turn.setText("White\'s turn (White in check)");
										Toast.makeText(getApplicationContext(), "White is in Check", Toast.LENGTH_LONG).show();
									}
								}
								else
									turn.setText("White\'s turn");	
							}
						}
					}
				}
			};
			submit.setOnClickListener(submitListener);
							
}
	@Override
	public void onResume()
	{
		super.onResume();
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
		for (int i=0; i<8; i++)
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
		int [] colorAndIndex;
		colorAndIndex=getPieceAt(row,column);
		int pieceColor;
		int pieceIndex;
		Piece p=null;
		if (colorAndIndex!=null)
		{
			pieceColor=colorAndIndex[0];
			pieceIndex=colorAndIndex[1];
			if (pieceColor==BLACK)
				p=blackPieces.get(pieceIndex);
			else
				p=whitePieces.get(pieceIndex);
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
	
	public int [] getPieceAt(int row, int column) {
		int [] colorAndIndex=new int[2];
		for (int i=0;i<whitePieces.size(); i++)
		{
			if ((whitePieces.get(i).getPosition())[0]==row && (whitePieces.get(i).getPosition())[1]==column)
				{
				colorAndIndex[0]=WHITE;
				colorAndIndex[1]=i;
				return colorAndIndex;
				}
		}
		for (int i=0;i<blackPieces.size(); i++)
		{
			if ((blackPieces.get(i).getPosition())[0]==row && (blackPieces.get(i).getPosition())[1]==column)
				{
				colorAndIndex[0]=BLACK;
				colorAndIndex[1]=i;
				return colorAndIndex;
				}
		}
			return null;
	}
	
	public void unRed(int row, int column) {
		int spaceColor;
		if ((row+column)%2==0)
			spaceColor=WHITE;
		else
			spaceColor=BLACK;
		int [] colorAndIndex;
		colorAndIndex=getPieceAt(row,column);
		int pieceColor;
		int pieceIndex;
		Piece p=null;
		if (colorAndIndex!=null)
		{
			pieceColor=colorAndIndex[0];
			pieceIndex=colorAndIndex[1];
			if (pieceColor==BLACK)
				p=blackPieces.get(pieceIndex);
			else
				p=whitePieces.get(pieceIndex);
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
			int [] colorAndIndex;
			int pieceColor;
			int pieceIndex;
			Piece p2;
			if ((colorAndIndex=getPieceAt(endRow,endColumn))!=null)
			{
				pieceColor=colorAndIndex[0];
				pieceIndex=colorAndIndex[1];
				if (pieceColor==BLACK)
					p2=blackPieces.get(pieceIndex);
				else
					p2=whitePieces.get(pieceIndex);
				if (p.getColor()==p2.getColor() && p2.getType()!=-1) //makes it so you cant take your own pieces, exceptiong for type =-1 is for case when we have a piece suppressed and are checking for check or checkmate
						return false;
			}
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
						{
							if (endRow==row+2 && col==endColumn && !(theBoard.pieceAt(endRow-1,endColumn)) && !(theBoard.pieceAt(endRow,endColumn)))
								return true;
						}
						else if (endRow==row+1 && (col==endColumn+1 || col==endColumn-1) && (theBoard.pieceAt(endRow,endColumn)))
							return true;
					}
					else 
					{
						if (endRow==row-1 && col==endColumn  && !(theBoard.pieceAt(endRow,endColumn)))
							return true;
						else if (row==6)
							if (endRow==row-2 && col==endColumn && !(theBoard.pieceAt(endRow+1,endColumn)) && !(theBoard.pieceAt(endRow,endColumn)))
								return true;
							else ;
						else if (endRow==row-1 && (col==endColumn+1 || col==endColumn-1) && (theBoard.pieceAt(endRow,endColumn)))
							return true;
					}
					return false;
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
					return true;
					}
					else if (col==endColumn)
					{
						if (row>endRow)
							for (int i=row-1; i>endRow; i--)
							{
								if (theBoard.pieceAt(i, col)==true)
									return false;
							}
								
						else
							for (int i=row+1; i<endRow; i++)
							{
								if (theBoard.pieceAt(i, col)==true)
									return false;
							}
					return true;
					}
					else return false;
				}
				case BISHOP:
				{
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
								return true;
							}
							else
							{
								for (int i=1; i<distX; i++)
								{
									if (theBoard.pieceAt(row-i,col+i))
										return false;
								}
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
								return true;
							}
							else
							{
								for (int i=1; i<(-1*distX); i++)
								{
									if (theBoard.pieceAt(row-i,col-i))
										return false;
								}
								return true;
							}
						}
					}
				}
					
				case QUEEN:
				{ 
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
					return true;
					}
					else if (col==endColumn)
					{
						if (row>endRow)
							for (int i=row-1; i>endRow; i--)
							{
								if (theBoard.pieceAt(i, col)==true)
									return false;
							}
								
						else
							for (int i=row+1; i<endRow; i++)
							{
								if (theBoard.pieceAt(i, col)==true)
									return false;
							}
					return true;
					}
					else
					{
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
									return true;
								}
								else
								{
									for (int i=1; i<distX; i++)
									{
										if (theBoard.pieceAt(row-i,col+i))
											return false;
									}
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
									return true;
								}
								else
								{
									for (int i=1; i<(-1*distX); i++)
									{
										if (theBoard.pieceAt(row-i,col-i))
											return false;
									}
									return true;
								}
							}
						}	
					}
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
			int [] endSpace=redSpaces.pop();// get our pieces, start of piece getting
			int [] startSpace=redSpaces.pop();
			int [] colorAndIndex,colorAndIndex2;
			Piece p, p2;
			colorAndIndex=getPieceAt(startSpace[0],startSpace[1]);
			int pieceColor=colorAndIndex[0];
			int pieceIndex=colorAndIndex[1];
			if (pieceColor==BLACK)
				p=blackPieces.get(pieceIndex);
			else
				p=whitePieces.get(pieceIndex);
			if (theBoard.pieceAt(endSpace[0], endSpace[1])) //get a piece if theres one in the space we're moving to.
			{
				colorAndIndex2=getPieceAt(endSpace[0], endSpace[1]);
				int pieceColor2=colorAndIndex2[0];
				int pieceIndex2=colorAndIndex2[1];
				if (pieceColor2==BLACK)
					p2=blackPieces.get(pieceIndex2);
				else
					p2=whitePieces.get(pieceIndex2); //end of piece getting
			}
			else
			{
				p2=null;
				colorAndIndex2=null;
			}
			if (isValidMove(p, endSpace[0], endSpace[1]))
				{
					if (moveIntoCheck(p, endSpace[0], endSpace[1]))
					{
						Toast.makeText(getApplicationContext(), "Invalid move: Cannot move into check", Toast.LENGTH_LONG).show();
						unRed(startSpace[0], startSpace[1]);
						unRed(endSpace[0], endSpace[1]);
						return false;
					}
					if (theBoard.pieceAt(endSpace[0], endSpace[1]))
					{
						/// getrid of a piece if we are attacking one
						removePieceAt(colorAndIndex2[0], colorAndIndex2[1], endSpace[0], endSpace[1]);
					}
					colorAndIndex=getPieceAt(startSpace[0],startSpace[1]); //need to update now that piece has been deleted
					pieceColor=colorAndIndex[0];
					pieceIndex=colorAndIndex[1];
					movePiece(pieceColor, pieceIndex ,endSpace[0], endSpace[1]);
					if (p.getType()!=PAWN) //pawns are loose cannons that don't play by the rules. They need to be handled differently due to enpassant captures
						p.specialMove(0);
					else
						if (endSpace[0]-startSpace[0]==2 || endSpace[0]-startSpace[0]==-2)
							p.specialMove(2);
						else
							p.specialMove(0);
					pawnChecker(TURN);
					bro=0; //resets bro so secret message doesn't appear
					return true;
				}
			Boolean castled=false;
			if (p2!=null && ((p2.getType()==KING && p.getType()==ROOK) || (p.getType()==KING && p2.getType()==ROOK)) && p.getColor()==p2.getColor()) // special case for castling
			{
			castled=castleHandler(p,p2);
			}
			if (castled)
			{
				p2.specialMove(0);
				p.specialMove(0);
				pawnChecker(TURN);
				return true;
			}
			Boolean enPassant=false;
			if (p.getType()==PAWN && (startSpace[0]==3 || startSpace[0]==4))
				enPassant=enPassantHandler(colorAndIndex[0], colorAndIndex[1], endSpace[0], endSpace[1]);
			if (enPassant)
			{
				pawnChecker(TURN);
				return true;
			}
			else
			{
				bro++;
				if (bro>=10) // if you have 10 consecutive invalid moves then the secret message appears
					Toast.makeText(getApplicationContext(), "Bro do you even chess?", Toast.LENGTH_LONG).show();
				Toast.makeText(getApplicationContext(), "Invalid Move", Toast.LENGTH_LONG).show();
				unRed(startSpace[0], startSpace[1]);
				unRed(endSpace[0], endSpace[1]);
				return false;
			}
		}
	}

	public void pawnChecker(int color) { /*fixes enpassant captures*/
		if (color==BLACK)
			for (Piece p:whitePieces)
			{
				if (p.specialMove()==2)
					p.specialMove(0);
			}
		else
			for (Piece p:blackPieces)
			{
				if (p.specialMove()==2)
					p.specialMove(0);
			}
	}
	
	public void removePieceAt(int color, int index, int endRow, int endColumn) {
		int [] loc;
		if (color==WHITE)
			loc=whitePieces.get(index).getPosition();
		else
			loc=blackPieces.get(index).getPosition();
		int row=loc[0];
		int col=loc[1];
		if (color==WHITE)
			{
			whitePieces.remove(index);
			theBoard.removePiece(row, col);
			}
		else
		{
			blackPieces.remove(index);
			theBoard.removePiece(row, col);
		}
	}
	public void movePiece(int color, int index, int endRow, int endColumn) {
		int [] loc;
		if (color==WHITE)
			loc=whitePieces.get(index).getPosition();
		else
			loc=blackPieces.get(index).getPosition();
		int row=loc[0];
		int col=loc[1];
		if (color==WHITE)
			whitePieces.get(index).setPosition(endRow, endColumn);
		else
			blackPieces.get(index).setPosition(endRow, endColumn);
		if (color==BLACK)
		{
			if (blackPieces.get(index).getType()==KING) //moves holder of kings position
				theBoard.blackKing(endRow, endColumn);
			if (blackPieces.get(index).getType()==PAWN) //gives you a queen if you get the pawn to the end
				if (loc[0]==7)
				{
					promotion.show();
					promoteSpace=loc;
					turn.setText("Promote Pawn");
					promote=true;
				}
		}
		else
		{
			if (whitePieces.get(index).getType()==KING)
				theBoard.whiteKing(endRow, endColumn);
			if (whitePieces.get(index).getType()==PAWN)
				if (loc[0]==0)
				{
					promotion.show();
					promoteSpace=loc;
					turn.setText("Promote Pawn");
					promote=true;
				}
		}
		theBoard.removePiece(row, col);
		theBoard.placePiece(endRow, endColumn);
		unRed(endRow, endColumn);
		unRed(row, col);
	}
	public Boolean isCheck(int color) //checks to see if king of color kingColor is in check
	{
		if (color==WHITE)
		{
			for (Piece p:blackPieces)
			{
				if (isValidMove(p, theBoard.whiteKing()[0],theBoard.whiteKing()[1]))
					return true;
			}
		}
		else
		{
			for (Piece p:whitePieces)
			{
				if (isValidMove(p, theBoard.blackKing()[0],theBoard.blackKing()[1]))
					return true;
			}
		}
		return false;
	}
	
	public Boolean weakCheckmate(int kingColor) //checks to see if king can move itself out of check
	{
		Piece King=null;
		if (kingColor==BLACK)
		{
			King=blackPieces.get((getPieceAt(theBoard.blackKing()[0],theBoard.blackKing()[1])[1]));
			for (int i=theBoard.blackKing()[0]-1; i<theBoard.blackKing()[0]+2; i++)
				for (int j=theBoard.blackKing()[1]-1; j<theBoard.blackKing()[1]+2; j++)
				{
					if (j<0 || i<0 || j>7 || i>7)
						continue;
					if (isValidMove(King,i,j))
						if (!moveIntoCheck(King, i, j))
						{
							return false;
						}
				}
		}
		else
		{
			King=whitePieces.get((getPieceAt(theBoard.whiteKing()[0],theBoard.whiteKing()[1])[1]));
			for (int i=theBoard.whiteKing()[0]-1; i<theBoard.whiteKing()[0]+2; i++)
				for (int j=theBoard.whiteKing()[1]-1; j<theBoard.whiteKing()[1]+2; j++)
				{
					if (j<0 || i<0 || j>7 || i>7)
						continue;
					if (isValidMove(King,i,j))
						if (!moveIntoCheck(King, i, j))
						{
							return false;
						}
				}
		}
		return true;
	}
	
	public Boolean isCheckmate(int kingColor) //seperate from weak checkmate so we don't iterate over the list everytime we are in check
	{
		if (kingColor==BLACK)
			for (Piece p:blackPieces)
				for (int i=0; i<8; i++)
					for (int j=0; j<8; j++)
					{
						if (isValidMove(p, i, j))
							if (!moveIntoCheck(p, i, j))
							{
								return false;
							}
					}
		else
			for (Piece p:whitePieces)
				for (int i=0; i<8; i++)
					for (int j=0; j<8; j++)
					{
						if (isValidMove(p, i, j))
							if (!moveIntoCheck(p, i, j))
							{
								return false;
							}
					}
		return true;
	}
	
	public Boolean moveIntoCheck(Piece p, int endRow, int endCol)
	{
		Boolean undo=theBoard.pieceAt(endRow, endCol);
		int tempType=0;
		int [] colorAndIndexOther;
		if (undo) //if there is a piece where we are tryign to move, we have to consider what will happen if it gets taken. We suppress this piece and check for check, then restor it later
		{
			colorAndIndexOther=getPieceAt(endRow, endCol);
			if (colorAndIndexOther[0]==BLACK)
			{
				tempType= blackPieces.get(colorAndIndexOther[1]).getType();
				blackPieces.get(colorAndIndexOther[1]).setType(-1);
			}
			else
			{
				tempType= whitePieces.get(colorAndIndexOther[1]).getType();
				whitePieces.get(colorAndIndexOther[1]).setType(-1);
			}
		}
		else
		{
			colorAndIndexOther=null;
		}
		Boolean ret=false;
		int [] loc;
		int [] colorAndIndex=getPieceAt(p.getPosition()[0],p.getPosition()[1]);
		if (p.getColor()==WHITE)
			loc=whitePieces.get(colorAndIndex[1]).getPosition();
		else
			loc=blackPieces.get(colorAndIndex[1]).getPosition();
		int row=loc[0];
		int col=loc[1];
		if (p.getColor()==WHITE)
			whitePieces.get(colorAndIndex[1]).setPosition(endRow, endCol);
		else
			blackPieces.get(colorAndIndex[1]).setPosition(endRow, endCol);
		if (p.getColor()==BLACK)
		{
			if (blackPieces.get(colorAndIndex[1]).getType()==KING)
				theBoard.blackKing(endRow, endCol);
		}
		else
		{
			if (whitePieces.get(colorAndIndex[1]).getType()==KING)
				theBoard.whiteKing(endRow, endCol);
		}
		theBoard.removePiece(row, col);
		theBoard.placePiece(endRow, endCol);
		if (isCheck(p.getColor())) //see if we would be in check if we make this move
			ret=true;
		if (p.getColor()==WHITE)
			whitePieces.get(colorAndIndex[1]).setPosition(row, col);
		else
			blackPieces.get(colorAndIndex[1]).setPosition(row, col);
		theBoard.removePiece(endRow, endCol);
		theBoard.placePiece(row, col);
		if (p.getColor()==BLACK)
		{
			if (blackPieces.get(colorAndIndex[1]).getType()==KING)
				theBoard.blackKing(row, col);
		}
		else
		{
			if (whitePieces.get(colorAndIndex[1]).getType()==KING)
				theBoard.whiteKing(row, col);
		}
		if (undo) //restoring the piece at the end position;\
		{
			theBoard.placePiece(endRow, endCol);
			if (colorAndIndexOther[0]==BLACK)
			{
				blackPieces.get(colorAndIndexOther[1]).setType(tempType);
			}
			else
			{
				whitePieces.get(colorAndIndexOther[1]).setType(tempType);
			}
		}
		
		
		return ret;
	}
	
	public Boolean castleHandler(Piece p1, Piece p2) // need to add case to check if king moves through check 
	{
		if (p1.specialMove()==0 || p2.specialMove()==0)
			return false;
		Piece king, rook;
		if (p1.getType()==KING)
		{
			king=p1;
			rook=p2;
		}
		else
		{
			king=p2;
			rook=p1;
		}
		if (king.getPosition()[1]>rook.getPosition()[1]) //queenside castling
		{
			for (int i=rook.getPosition()[1]+1; i<king.getPosition()[1]; i++)
			{
				if (theBoard.pieceAt(king.getPosition()[0], i)) //if there are pieces int the way we can't castle :(
					return false;
				if (i>rook.getPosition()[1]+1)
				if (moveIntoCheck(king, king.getPosition()[0], i))
				{
					Toast.makeText(getApplicationContext(), "Cannot Castle Through Check", Toast.LENGTH_LONG).show();
					return false;
				}
			}
				int [] kingColorIndex=getPieceAt(king.getPosition()[0], king.getPosition()[1]); //need to get references to the real pieces
				int [] rookColorIndex=getPieceAt(rook.getPosition()[0], rook.getPosition()[1]); //need to get references to the real pieces
				movePiece(kingColorIndex[0], kingColorIndex[1], king.getPosition()[0], (king.getPosition()[1]-2));
				movePiece(rookColorIndex[0], rookColorIndex[1], rook.getPosition()[0], (rook.getPosition()[1]+3));
				return true;
		}
		else
		{
			for (int i=king.getPosition()[1]+1; i<rook.getPosition()[1]; i++)
			{
				if (theBoard.pieceAt(king.getPosition()[0], i)) //if there are pieces int the way we can't castle :(
					return false;
				if (i<rook.getPosition()[1])
				if (moveIntoCheck(king, king.getPosition()[0], i))
				{
					Toast.makeText(getApplicationContext(), "Cannot Castle Through Check", Toast.LENGTH_LONG).show();
					return false;
				}
			}
				int [] kingColorIndex=getPieceAt(king.getPosition()[0], king.getPosition()[1]); //need to get references to the real pieces
				int [] rookColorIndex=getPieceAt(rook.getPosition()[0], rook.getPosition()[1]); //need to get references to the real pieces
				movePiece(kingColorIndex[0], kingColorIndex[1], king.getPosition()[0], (king.getPosition()[1]+2));
				movePiece(rookColorIndex[0], rookColorIndex[1], rook.getPosition()[0], (rook.getPosition()[1]-2));
				return true;
		}
	}
	
	
	public Boolean enPassantHandler(int color, int index, int endRow, int endCol)
	{
		int [] colorAndIndex=null;
		if (color==BLACK)
		{
			colorAndIndex=getPieceAt(endRow-1, endCol);
			if (colorAndIndex==null || colorAndIndex[0]==color || whitePieces.get(colorAndIndex[1]).specialMove()!=2)
					return false;
			else
			{
				movePiece(color, index, endRow, endCol);
				removePieceAt(colorAndIndex[0], colorAndIndex[1], endRow-1, endCol);
				unRed(endRow-1,endCol);
				return true;
			}
		}
		else
		{
			colorAndIndex=getPieceAt(endRow+1, endCol);
			if (colorAndIndex==null || colorAndIndex[0]==color || blackPieces.get(colorAndIndex[1]).specialMove()!=2)
					return false;
			else
			{
				movePiece(color, index, endRow, endCol);
				removePieceAt(colorAndIndex[0], colorAndIndex[1], endRow+1, endCol);
				unRed(endRow+1,endCol);
				return true;
			}
		}
	}
}
