package com.vip.vipagents.ui.slideshow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vip.vipagents.R;

public class StoneActivity extends AppCompatActivity {
    private TextView txtPercent, txtStones, txtWarning;
    private ImageView[] imgFirst = new ImageView[10];
    private ImageView[] imgSecond = new ImageView[10];
    private ImageView[] imgDeburf = new ImageView[10];
    private Button btnFirstUpgrade, btnSecondUpgrade, btnDeburfUpgrade, btnOK;

    private int percent = 75, first = 0, second = 0, deburf = 0;
    private int first_index = 0, second_index = 0, deburf_index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stone);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("어빌리티 스톤 만들기");

        txtPercent = findViewById(R.id.txtPercent);
        txtStones = findViewById(R.id.txtStones);
        txtWarning = findViewById(R.id.txtWarning);
        btnFirstUpgrade = findViewById(R.id.btnFirstUpgrade);
        btnSecondUpgrade = findViewById(R.id.btnSecondUpgrade);
        btnDeburfUpgrade = findViewById(R.id.btnDeburfUpgrade);
        btnOK = findViewById(R.id.btnOK);

        for (int i = 0; i < 10; i++) {
            imgFirst[i] = findViewById(getResources().getIdentifier("imgFirst"+(i+1), "id", getPackageName()));
            imgSecond[i] = findViewById(getResources().getIdentifier("imgSecond"+(i+1), "id", getPackageName()));
            imgDeburf[i] = findViewById(getResources().getIdentifier("imgDeburf"+(i+1), "id", getPackageName()));
        }

        btnFirstUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ransu = (int)(Math.random()*123456)%101;
                if (ransu <= percent) {
                    imgFirst[first_index].setImageResource(R.drawable.success_crystal);
                    first++;
                    if (percent > 25) percent -= 10;
                } else {
                    imgFirst[first_index].setImageResource(R.drawable.fail_crystal);
                    if (percent < 75) percent += 10;
                }
                first_index++;
                if (first_index == 10) btnFirstUpgrade.setEnabled(false);
                txtPercent.setText(percent+"%");
                checkFull();
            }
        });

        btnSecondUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ransu = (int)(Math.random()*123456)%101;
                if (ransu <= percent) {
                    imgSecond[second_index].setImageResource(R.drawable.success_crystal);
                    second++;
                    if (percent > 25) percent -= 10;
                } else {
                    imgSecond[second_index].setImageResource(R.drawable.fail_crystal);
                    if (percent < 75) percent += 10;
                }
                second_index++;
                if (second_index == 10) btnSecondUpgrade.setEnabled(false);
                txtPercent.setText(percent+"%");
                checkFull();
            }
        });

        btnDeburfUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ransu = (int)(Math.random()*123456)%101;
                if (ransu <= percent) {
                    imgDeburf[deburf_index].setImageResource(R.drawable.deburf_crystal);
                    deburf++;
                    if (percent > 25) percent -= 10;
                } else {
                    imgDeburf[deburf_index].setImageResource(R.drawable.deburf_fail_crystal);
                    if (percent < 75) percent += 10;
                }
                deburf_index++;
                if (deburf_index == 10) btnDeburfUpgrade.setEnabled(false);
                txtPercent.setText(percent+"%");
                checkFull();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deburf_index = 0;
                deburf = 0;
                first_index = 0;
                first = 0;
                second_index = 0;
                second = 0;
                percent = 75;
                txtPercent.setText(percent+"%");
                if (!btnFirstUpgrade.isEnabled()) btnFirstUpgrade.setEnabled(true);
                if (!btnSecondUpgrade.isEnabled()) btnSecondUpgrade.setEnabled(true);
                if (!btnDeburfUpgrade.isEnabled()) btnDeburfUpgrade.setEnabled(true);
                for (int i = 0; i < 10; i++) {
                    imgFirst[i].setImageResource(R.drawable.none_crystal);
                    imgSecond[i].setImageResource(R.drawable.none_crystal);
                    imgDeburf[i].setImageResource(R.drawable.none_crystal);
                }
                txtWarning.setVisibility(View.VISIBLE);
                btnOK.setEnabled(false);
            }
        });
    }

    public void checkFull() {
        if (first_index == 10 && second_index == 10 && deburf_index == 10) {
            txtWarning.setVisibility(View.GONE);
            btnOK.setEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
