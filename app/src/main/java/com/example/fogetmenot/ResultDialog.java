package com.example.fogetmenot;

import android.app.Activity;
import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultDialog extends Dialog implements
        android.view.View.OnClickListener {

    private Activity c;
    private Dialog d;
    private Button end, con;
    private TextView tx_result;
    private ImageView imgv_result;
    private boolean result;


    public ResultDialog(Activity activity, boolean result){
        super(activity);
        this.c = activity;
        this.result = result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_dialog);
        end = (Button) findViewById(R.id.btn_end_game);
        con = (Button) findViewById(R.id.btn_continue_game);

        end.setOnClickListener(this);
        con.setOnClickListener(this);

        tx_result = (TextView) findViewById(R.id.anwser_txtv);
        imgv_result = (ImageView) findViewById(R.id.answer_icon_imgv);

        if(result){
            tx_result.setText("ถูกต้อง");
            imgv_result.setImageResource(R.drawable.correct);
        }else{
            tx_result.setText("ผิด");
            imgv_result.setImageResource(R.drawable.incorrect);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_end_game:
                c.finish();
                break;
            case R.id.btn_continue_game:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

}
