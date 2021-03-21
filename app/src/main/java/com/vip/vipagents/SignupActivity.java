package com.vip.vipagents;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {
    private EditText edtID, edtPassword, edtPasswordRetry;
    private Button btnCheck, btnManagement, btnSignup;
    private RadioGroup rgGrade;
    private RadioButton[] rdoGrade = new RadioButton[4];

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private boolean isDoubleCheck = false;

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

        mDatabase = FirebaseDatabase.getInstance();

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtID.getText().toString().equals("")) {
                    toast("아이디를 입력하십시오.");
                    return;
                }
                mReference = mDatabase.getReference("Members");
                mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean isDouble = false;
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (data.child("id").getValue().toString().equals(edtID.getText().toString())) {
                                isDouble = true;
                            }
                        }
                        if (!isDouble) {
                            edtID.setEnabled(false);
                            isDoubleCheck = true;
                            toast("사용가능한 아이디입니다.");
                        } else toast("이미 존재한 아이디입니다.");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDoubleCheck) {
                    toast("중복 체크를 하십시오.");
                    return;
                }
                if (!edtPassword.getText().toString().equals(edtPasswordRetry.getText().toString())) {
                    toast("비밀번호를 동일하게 입력하지 않았습니다.");
                    return;
                }
                mReference = mDatabase.getReference("Members");
                int grade = 0;
                switch (rgGrade.getCheckedRadioButtonId()) {
                    case R.id.rdoGrade1:
                        grade = 0;
                        break;
                    case R.id.rdoGrade2:
                        grade = 1;
                        break;
                    case R.id.rdoGrade3:
                        grade = 2;
                        break;
                    case R.id.rdoGrade4:
                        grade = 3;
                        break;
                }
                Member member = new Member(edtID.getText().toString(), edtPassword.getText().toString(), grade);
                mReference.child(edtID.getText().toString()).setValue(member);
                toast(edtID.getText().toString()+"님 환영합니다!!");
                finish();
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
