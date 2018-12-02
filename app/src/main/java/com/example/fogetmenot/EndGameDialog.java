package com.example.fogetmenot;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EndGameDialog extends Dialog implements
        android.view.View.OnClickListener{

    private Activity c;
    private int score;

    public EndGameDialog(Activity activity, int score){
        super(activity);
        this.c = activity;
        this.score = score;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_game_dialog);

        Button endGame_btn = (Button) findViewById(R.id.btn_end_game_score);

        endGame_btn.setOnClickListener(this);

        TextView tx_score = (TextView) findViewById(R.id.txtv_score);
        tx_score.setText(score+"");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_end_game_score:
                c.finish();
                break;
            default:
                break;
        }
        dismiss();
    }
}

