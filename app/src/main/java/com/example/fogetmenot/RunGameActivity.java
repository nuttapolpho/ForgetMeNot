package com.example.fogetmenot;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class RunGameActivity extends AppCompatActivity {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView mVoiceInputTv;
    private ImageButton mSpeakBtn;
    private ArrayList<Person> familyList;
    private ImageView propic;
    private int cIndex;
    private Person cPerson;
    private int score = 0;
    private static boolean isFirst = true;
    private boolean isBlank = true;
    private static int totalSize;
    private static int countRound = 0;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_game);
        propic = (ImageView) findViewById(R.id.imageView2);
        mVoiceInputTv = (TextView) findViewById(R.id.voiceInput);
        mSpeakBtn = (ImageButton) findViewById(R.id.btnSpeak);

        familyList = new ArrayList<>();
        cPerson = new Person("null");

        db = new Database(RunGameActivity.this);
        mSpeakBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });

        try{
            familyList = db.getPersonList();
            runGame();
            isBlank = false;
        }catch (DatabaseException ex){
            new AlertDialog.Builder(this)
                    .setTitle("ไม่มีข้อมูล")
                    .setMessage("กรุณาเพิ่มข้อมูลก่อนเล่น")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("เพิ่มข้อมูล", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent intent = new  Intent(RunGameActivity.this,AddMemberActivity.class);
                            startActivity(intent);
                        }})
                    .setNegativeButton("กลับหน้าหลัก", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();
                        }}).show();

        }
        totalSize = familyList.size();
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "คนนี้คือใครเอ่ย?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String regText = result.get(0);
                    mVoiceInputTv.setText(regText);
                    checkResult(result, cPerson.getName());
                }
                break;
            }

        }
    }

    private void runGame(){
        countRound++;
        if(isFirst){
            cIndex = 0;
            isFirst = false;
            setNextTurn(familyList.get(cIndex));
        }else{
            cIndex++;
            if(cIndex < familyList.size()){
                setNextTurn(familyList.get(cIndex));
            }else{
                cIndex = 0;
                isFirst = true;
                Collections.shuffle(familyList);
                EndGameDialog end = new EndGameDialog(this, score, this);
                end.show();
            }
        }
    }

    private void setNextTurn(Person person){
        cPerson = person;
        propic.setImageBitmap(Bitmap.createScaledBitmap(person.getImage(),
                (int) getResources().getDimension(R.dimen.imageview_width),
                (int) getResources().getDimension(R.dimen.imageview_height), false));
        Toast.makeText(getApplicationContext(), cPerson.getName(),Toast.LENGTH_SHORT).show();
    }

    private void checkResult(ArrayList<String> regconStr, String name){
        ResultDialog rd;
        boolean isCorrect = false;
        String a = "REGC: ";
        for(String x: regconStr){
            if(x.equals(name))
                isCorrect = true;
            a += " " + x;
        }
        Toast.makeText(getApplicationContext(), a + " Name:" + name,Toast.LENGTH_SHORT).show();

        if(isCorrect){
            score++;
            rd = new ResultDialog(RunGameActivity.this, true);
        }else{
            rd = new ResultDialog(RunGameActivity.this, false);
        }
        if(countRound < totalSize){
            rd.show();
        }
        runGame();
    }

}
