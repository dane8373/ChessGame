package com.example.chessgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainMenu extends Activity {
	
	Button singlePlayer;
	Button localMP;
	Button onlineMP;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		singlePlayer= (Button) findViewById(R.id.mode1);
		localMP= (Button) findViewById(R.id.mode2);
		onlineMP= (Button) findViewById(R.id.mode3);
		
		View.OnClickListener unAvail = new View.OnClickListener() {
			@Override
			public void onClick(View v){
				Toast.makeText(getApplicationContext(), "Mode Currently Unavailable", Toast.LENGTH_LONG).show();
			}
		};
		singlePlayer.setOnClickListener(unAvail);
		onlineMP.setOnClickListener(unAvail);
		
		View.OnClickListener local = new View.OnClickListener() {
			@Override
			public void onClick(View v){
				Intent startLocal=new Intent(MainMenu.this, ChessActivity.class);
				startActivity(startLocal);
			}
		};
		localMP.setOnClickListener(local);
	}

}
