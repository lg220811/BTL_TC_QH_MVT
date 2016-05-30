package com.example.thiem.tcqhmvt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

/**
 * Created by Thiem on 14-Mar-16.
 */
public class OptionActivity extends AppCompatActivity{
    SeekBar sbN, sbPc, sbA, sbR;
    EditText edtN, edtPc, edtA, edtR;
    Button btnOk;

    private int numberNode, Rn;
    private double Pc, A;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        init();
        custom();
    }

    private void init(){
        sbN = (SeekBar)findViewById(R.id.seekBarN);
        sbPc = (SeekBar) findViewById(R.id.seekBarPc);
        sbA = (SeekBar) findViewById(R.id.seekBarA);
        sbR = (SeekBar) findViewById(R.id.seekBarR);

        edtN = (EditText) findViewById(R.id.editTextN);
        edtN.setText("10");
        edtPc = (EditText) findViewById(R.id.editTextPc);
        edtPc.setText("0.10");
        edtA = (EditText) findViewById(R.id.editTextA);
        edtA.setText("0.10");
        edtR = (EditText) findViewById(R.id.editTextR);
        edtR.setText("10");

        btnOk = (Button) findViewById(R.id.buttonOk);
    }

    private void custom(){
        sbN.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                edtN.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbPc.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                edtPc.setText(""+progress*0.01);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbA.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                edtA.setText("" + progress * 0.01);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbR.setMax(90);
        sbR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                edtR.setText(""+(10+progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParam();
                if (!(numberNode<0||Pc<0||A<0||Rn<0))
                    next();
            }
        });
    }

    private void getParam(){
        numberNode = (int) getNumber(edtN, "số nút",getApplicationContext());
        Pc = getNumber(edtPc, "Pc",getApplicationContext());
        A = getNumber(edtA, "A",getApplicationContext());
        Rn = (int)getNumber(edtR, "R",getApplicationContext());
    }

    public static double getNumber(EditText e, String s, Context context){
        double re = -1;
        try {
            re = Double.parseDouble(""+e.getText());
        } catch (Exception e1){
            Toast.makeText(context,"Xảy ra lỗi khi nhận vào "+s,Toast.LENGTH_SHORT);
        }
        return re;
    }

    private void next(){
        Intent intent = new Intent(this,DisplayActivity.class);
        intent.putExtra("N",numberNode);
        intent.putExtra("Pc", Pc);
        intent.putExtra("A",A);
        intent.putExtra("R",Rn);

        startActivity(intent);
    }
}
