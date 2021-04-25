package com.vip.vipagents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileEditActivity extends AppCompatActivity {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private AlertDialog.Builder builder = null;
    private AlertDialog alertDialog = null;

    private EditText edtID;
    private Button btnCheck, btnManagement, btnEdit;
    private RadioGroup rgGrade;
    private RadioButton[] rdoGrade = new RadioButton[4];
    private LinearLayout layoutGrade;
    private Switch swtClan;

    private boolean isCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(loadProfile()+"님의 정보 수정");

        edtID = findViewById(R.id.edtID);
        btnCheck = findViewById(R.id.btnCheck);
        btnManagement = findViewById(R.id.btnManagement);
        btnEdit = findViewById(R.id.btnEdit);
        rgGrade = findViewById(R.id.rgGrade);
        layoutGrade = findViewById(R.id.layoutGrade);
        swtClan = findViewById(R.id.swtClan);

        for (int i = 0; i < rdoGrade.length; i++) {
            int resource = getResources().getIdentifier("rdoGrade"+(i+1), "id", getPackageName());
            rdoGrade[i] = findViewById(resource);
        }

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReference("Members");
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.child("id").getValue().toString().equals(loadProfile())) {
                        edtID.setText(loadProfile());
                        rdoGrade[Integer.parseInt(data.child("grade").getValue().toString())].setChecked(true);
                        if (Boolean.parseBoolean(data.child("clan").getValue().toString())) swtClan.setChecked(true);
                        else swtClan.setChecked(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        swtClan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) layoutGrade.setVisibility(View.VISIBLE);
                else layoutGrade.setVisibility(View.GONE);
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtID.getText().toString().equals("")) {
                    toast("아이디를 입력해야 합니다.");
                    return;
                } else if (edtID.getText().toString().equals(loadProfile())) {
                    isCheck = true;
                    btnCheck.setEnabled(false);
                    edtID.setEnabled(false);
                    toast("사용 가능한 아이디입니다.");
                    return;
                }
                mReference = mDatabase.getReference("Members");
                mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (data.child("id").getValue().toString().equals(edtID.getText().toString())) {
                                toast("이미 존재한 아이디입니다.");
                                return;
                            }
                        }
                        isCheck = true;
                        btnCheck.setEnabled(false);
                        edtID.setEnabled(false);
                        toast("사용 가능한 아이디입니다.");
                        return;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

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
                builder = new AlertDialog.Builder(ProfileEditActivity.this);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCheck) {
                    toast("중복 체크를 해야 합니다.");
                    return;
                } else if (edtID.getText().toString().equals("")) {
                    toast("아이디를 입력해야 합니다.");
                    return;
                }
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
                Map<String, Object> taskMap = new HashMap<String, Object>();
                taskMap.put("id", edtID.getText().toString());
                taskMap.put("grade", grade);
                taskMap.put("clan", swtClan.isChecked());
                mReference.child(edtID.getText().toString()).updateChildren(taskMap);
                saveProfile(edtID.getText().toString());
                toast("정보가 수정되었습니다.");
                finish();
                return;
            }
        });
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

    private void toast(String message) {
        Toast.makeText(ProfileEditActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private String loadProfile() {
        FileInputStream fis = null;
        try {
            fis = openFileInput("id.txt");
            byte[] memoData = new byte[fis.available()];
            while(fis.read(memoData) != -1) {}
            return new String(memoData);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "/*null*/";
    }

    private void saveProfile(String id) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput("id.txt", MODE_PRIVATE);
            fos.write(id.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
