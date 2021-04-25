package com.vip.vipagents;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends BaseActivity {
    private EditText edtID, edtPassword, edtPasswordRetry;
    private Button btnCheck, btnManagement, btnSignup;
    private RadioGroup rgGrade;
    private RadioButton[] rdoGrade = new RadioButton[4];
    private LinearLayout layoutGrade;
    private Switch swtClan;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private AlertDialog.Builder builder = null;
    private AlertDialog alertDialog = null;

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
        layoutGrade = findViewById(R.id.layoutGrade);
        swtClan = findViewById(R.id.swtClan);

        for (int i = 0; i < rdoGrade.length; i++) {
            int resource = getResources().getIdentifier("rdoGrade"+(i+1), "id", getPackageName());
            rdoGrade[i] = findViewById(resource);
        }

        swtClan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rdoGrade[0].setChecked(true);
                if (isChecked) {
                    layoutGrade.setVisibility(View.VISIBLE);
                } else {
                    layoutGrade.setVisibility(View.GONE);
                }
            }
        });

        mDatabase = FirebaseDatabase.getInstance();

        btnManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.editdialog, null);

                final TextView txtView = view.findViewById(R.id.txtView);
                final EditText edtText = view.findViewById(R.id.edtText);
                final Button btnCancel = view.findViewById(R.id.btnCancel);
                final Button btnOK = view.findViewById(R.id.btnOK);

                txtView.setText("관리자 비밀번호를 입력해주십시오.");
                edtText.setHint("비밀번호를 입력해주십시오.");
                edtText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                edtText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                btnOK.setText("획득");

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtText.getText().toString().equals("wv2155")) {
                            rdoGrade[2].setEnabled(true);
                            rdoGrade[3].setEnabled(true);
                            rdoGrade[2].setTextColor(Color.parseColor("#000000"));
                            rdoGrade[3].setTextColor(Color.parseColor("#000000"));
                            toast("권한을 획득하셨습니다.");
                            btnManagement.setEnabled(false);
                            alertDialog.dismiss();
                        } else toast("비밀번호가 일치하지 않습니다.");
                    }
                });
                builder = new AlertDialog.Builder(SignupActivity.this);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

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
                            btnCheck.setEnabled(false);
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
                Member member = new Member(edtID.getText().toString(), edtPassword.getText().toString(), grade, swtClan.isChecked(), 0);
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
