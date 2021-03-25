package com.vip.vipagents.ui.share;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vip.vipagents.BaseActivity;
import com.vip.vipagents.R;
import com.vip.vipagents.SettingActivity;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class WriteNoticeActivity extends BaseActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    /*private AlertDialog.Builder builder = null;
    private AlertDialog alertDialog = null;*/

    private EditText edtTitle, edtContent;

    private int number = 1;
    private Notice notice = null;
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_notice);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("공지사항 글 작성 중");

        mDatabase = FirebaseDatabase.getInstance();
        edtTitle = findViewById(R.id.edtTitle);
        edtContent = findViewById(R.id.edtContent);

        Intent intent = getIntent();
        notice = (Notice)intent.getSerializableExtra("Edit_Notice");
        isEdit = intent.getBooleanExtra("Edit", false);

        if (isEdit) {
            setTitle("게시물 수정 중");
            edtTitle.setText(notice.getTitle());
            edtContent.setText(notice.getContent());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.write_notice_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            case R.id.action_btn1:
                if (isEdit) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일");
                    Map<String, Object> taskMap = new HashMap<String, Object>();
                    taskMap.put("title", edtTitle.getText().toString());
                    taskMap.put("content", edtContent.getText().toString());
                    taskMap.put("date", format.format(System.currentTimeMillis()));
                    mReference = mDatabase.getReference("Notice");
                    mReference.child(Integer.toString(notice.getNumber())).updateChildren(taskMap);
                    toast("게시물이 수정되었습니다.");
                    finish();
                    return true;
                }
                mReference = mDatabase.getReference("Notice");
                mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (Integer.parseInt(data.child("number").getValue().toString()) > number) {
                                number = Integer.parseInt(data.child("number").getValue().toString());
                            }
                        }
                        number++;
                        SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일");
                        Notice notice = new Notice(number, edtTitle.getText().toString(), loadProfile(), edtContent.getText().toString(), 0, format.format(System.currentTimeMillis()));
                        mReference.child(Integer.toString(number)).setValue(notice);
                        toast("게시물을 작성하였습니다.");
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        View view = getLayoutInflater().inflate(R.layout.answerdialog, null);

        final TextView txtView = view.findViewById(R.id.txtView);
        final Button btnCancel = view.findViewById(R.id.btnCancel);
        final Button btnOK = view.findViewById(R.id.btnOK);

        txtView.setText("게시물이 작성중입니다. 그래도 나가시겠습니까?");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        builder = new AlertDialog.Builder(WriteNoticeActivity.this);
        builder.setView(view);

        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }*/

    private void toast(String message) {
        Toast.makeText(WriteNoticeActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
