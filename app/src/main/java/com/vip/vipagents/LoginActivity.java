package com.vip.vipagents;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends BaseActivity {
    private EditText edtID, edtPassword;
    private Button btnLogin, btnSignup;
    private Member member = null;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginlayout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("로그인");

        edtID = findViewById(R.id.edtID);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);

        mDatabase = FirebaseDatabase.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

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
                mReference = mDatabase.getReference("Members");
                mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String message = "아이디가 존재하지 않습니다.";
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (data.child("id").getValue().toString().equals(edtID.getText().toString())) {
                                if (data.child("pwd").getValue().toString().equals(edtPassword.getText().toString())) {
                                    member = new Member(data.child("id").getValue().toString(), data.child("pwd").getValue().toString(), Integer.parseInt(data.child("grade").getValue().toString())
                                            , Boolean.parseBoolean(data.child("clan").getValue().toString()), Integer.parseInt(data.child("exp").getValue().toString()));
                                    message = "로그인하셨습니다.";
                                    Intent intent = new Intent();
                                    intent.putExtra("logined_member", member);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                    break;
                                } else {
                                    message = "비밀번호가 일치하지 않습니다.";
                                    break;
                                }
                            }
                        }
                        toast(message);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
