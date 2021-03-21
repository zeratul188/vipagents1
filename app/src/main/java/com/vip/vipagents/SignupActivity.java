package com.vip.vipagents;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    private EditText edtID, edtPassword, edtPasswordRetry;
    private Button btnCheck, btnManagement, btnSignup;
    private RadioGroup rgGrade;
    private RadioButton[] rdoGrade = new RadioButton[4];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signuplayout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("회원가입");

        edtID = findViewById(R.id.edtID);
        edtPassword = findViewById(R.id.edtPassword);
        edtPasswordRetry = findViewById(R.id.edtPasswordRetry);
        btnCheck = findViewById(R.id.btnCheck);
        btnManagement = findViewById(R.id.btnManagement);
        btnSignup = findViewById(R.id.btnSignup);
        rgGrade = findViewById(R.id.rgGrade);

        for (int i = 0; i < rdoGrade.length; i++) {
            int resource = getResources().getIdentifier("rdoGrade"+(i+1), "id", getPackageName());
            rdoGrade[i] = findViewById(resource);
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
