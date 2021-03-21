package com.vip.vipagents;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText edtID, edtPassword;
    private Button btnLogin, btnFind, btnSignup;
    private Member member = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginlayout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("로그인");

        edtID = findViewById(R.id.edtID);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnFind = findViewById(R.id.btnFind);
        btnSignup = findViewById(R.id.btnSignup);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtID.getText().toString().equals("")) {
                    toast("아이디를 입력해주십시오.");
                    return;
                } else if (edtPassword.getText().toString().equals("")) {
                    toast("비밀번호를 입력해주십시오.");
                    return;
                }
                member = new Member(edtID.getText().toString(), edtPassword.getText().toString(), 3);
                Intent intent = new Intent();
                intent.putExtra("logined_member", member);
                setResult(RESULT_OK, intent);
                finish();
                return;
            }
        });
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
