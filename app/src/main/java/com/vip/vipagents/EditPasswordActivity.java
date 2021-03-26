package com.vip.vipagents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditPasswordActivity extends AppCompatActivity {
    private EditText edtUndoPassword, edtNewPassword, edtNewRetryPassword;
    private Button btnEdit;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("비밀번호 변경");

        edtUndoPassword = findViewById(R.id.edtUndoPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtNewRetryPassword = findViewById(R.id.edtNewRetryPassword);
        btnEdit = findViewById(R.id.btnEdit);

        mDatabase = FirebaseDatabase.getInstance();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtUndoPassword.getText().toString().equals("") || edtNewPassword.getText().toString().equals("") || edtNewRetryPassword.getText().toString().equals("")) {
                    toast("빈 항목이 있습니다. 모두 입력해주시기 바랍니다.");
                    return;
                } else if (!edtNewPassword.getText().toString().equals(edtNewRetryPassword.getText().toString())) {
                    toast("새 비밀번호가 서로 다릅니다. 동일하게 입력해주세요.");
                    return;
                }
                mReference = mDatabase.getReference("Members");
                mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (data.child("id").getValue().toString().equals(loadProfile())) {
                                if (!data.child("pwd").getValue().toString().equals(edtUndoPassword.getText().toString())) {
                                    toast("기존 비밀번호가 일치하지 않습니다.");
                                    return;
                                }
                                Map<String, Object> taskMap = new HashMap<String, Object>();
                                taskMap.put("pwd", edtNewPassword.getText().toString());
                                mReference.child(loadProfile()).updateChildren(taskMap);
                                toast("비밀번호가 변경되었습니다.");
                                finish();
                                return;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
        Toast.makeText(EditPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
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
}
