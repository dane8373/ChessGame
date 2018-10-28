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

	private LinearLayout rows[] = new LinearLayout[8];
	private LinearLayout board;
	private ImageView square[][] = new ImageView[8][8];
	int rowIds[] = {R.id.row_0, R.id.row_1, R.id.row_2, R.id.row_3, R.id.row_4, R.id.row_5, R.id.row_6, R.id.row_7};
	int squareIds[] = {
			R.id.row0_sq0, R.id.row0_sq1, R.id.row0_sq2, R.id.row0_sq3, R.id.row0_sq4, R.id.row0_sq5, R.id.row0_sq6, R.id.row0_sq7,
			R.id.row1_sq0, R.id.row1_sq1, R.id.row1_sq2, R.id.row1_sq3, R.id.row1_sq4, R.id.row1_sq5, R.id.row1_sq6, R.id.row1_sq7,
			R.id.row2_sq0, R.id.row2_sq1, R.id.row2_sq2, R.id.row2_sq3, R.id.row2_sq4, R.id.row2_sq5, R.id.row2_sq6, R.id.row2_sq7,
			R.id.row3_sq0, R.id.row3_sq1, R.id.row3_sq2, R.id.row3_sq3, R.id.row3_sq4, R.id.row3_sq5, R.id.row3_sq6, R.id.row3_sq7,
			R.id.row4_sq0, R.id.row4_sq1, R.id.row4_sq2, R.id.row4_sq3, R.id.row4_sq4, R.id.row4_sq5, R.id.row4_sq6, R.id.row4_sq7,
			R.id.row5_sq0, R.id.row5_sq1, R.id.row5_sq2, R.id.row5_sq3, R.id.row5_sq4, R.id.row5_sq5, R.id.row5_sq6, R.id.row5_sq7,
			R.id.row6_sq0, R.id.row6_sq1, R.id.row6_sq2, R.id.row6_sq3, R.id.row6_sq4, R.id.row6_sq5, R.id.row6_sq6, R.id.row6_sq7,
			R.id.row7_sq0, R.id.row7_sq1, R.id.row7_sq2, R.id.row7_sq3, R.id.row7_sq4, R.id.row7_sq5, R.id.row7_sq6, R.id.row7_sq7,};
	private PopupMenu promotion;
	private TextView turn;
	private Button submit;
    private Button cancel;
	ChessBoard theBoard;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chess);
		RelativeLayout frame = (RelativeLayout) findViewById(R.id.frame);
		submit = (Button) findViewById(R.id.submit);
        cancel = (Button) findViewById(R.id.cancel);
		turn = (TextView) findViewById(R.id.turn);
		board = (LinearLayout) findViewById(R.id.table_1);
		int j = 0;
		for (int i : rowIds) {
			rows[j] = (LinearLayout) findViewById(i);
			j++;
		}
		int n = 0;
		int m = 0;
		for (int z : squareIds) {
			square[n][m] = (ImageView) findViewById(z);
			m++;
			if (m > 7) {
				m = 0;
				n++;
			}
		}
		theBoard = new ChessBoard();
		theBoard.setUpBoard(square);
		SquareListener listener = new SquareListener();
		SquareListener.setBoard(theBoard);
		for (int ii = 0; ii < 8; ii++)
			for (int jj = 0; jj < 8; jj++)
				square[ii][jj].setOnClickListener(listener);

		SubmitListener submitListener = new SubmitListener(getApplicationContext(), theBoard, turn);
		submitListener.setPromotionMenu();
		submit.setOnClickListener(submitListener);
		cancel.setOnClickListener(new CancelListener());

	}

	@Override
	public void onResume() {
		super.onResume();
	}
}
