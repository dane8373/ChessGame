package com.example.chessgame;

import android.view.View;

public class CancelListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        SquareListener.clearMove();
    }
}
