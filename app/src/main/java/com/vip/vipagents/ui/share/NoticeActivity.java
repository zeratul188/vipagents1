package com.vip.vipagents.ui.share;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vip.vipagents.BaseActivity;
import com.vip.vipagents.R;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NoticeActivity extends BaseActivity {
    private TextView txtWriter, txtDate, txtView, txtContent;
    private Notice notice = null;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private AlertDialog.Builder builder = null;
    private AlertDialog alertDialog = null;

    private boolean isDeveloper = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtWriter = findViewById(R.id.txtWriter);
        txtDate = findViewById(R.id.txtDate);
        txtView = findViewById(R.id.txtView);
        txtContent = findViewById(R.id.txtContent);

        Intent intent = getIntent();
        notice = (Notice)intent.getSerializableExtra("Notice");

        setTitle(notice.getTitle());
        txtWriter.setText(notice.getWriter());
        txtDate.setText(notice.getDate());
        txtContent.setText(notice.getContent());

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReference("Members");
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.child("id").getValue().toString().equals(loadProfile()) && Integer.parseInt(data.child("grade").getValue().toString()) > 1) {
                        isDeveloper = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mReference = mDatabase.getReference("Notice");
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int view = 0;
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (Integer.parseInt(data.child("number").getValue().toString()) == notice.getNumber()) {
                        view = Integer.parseInt(data.child("view").getValue().toString());
                        break;
                    }
                }
                view++;
                Map<String, Object> taskMap = new HashMap<String, Object>();
                taskMap.put("view", Integer.toString(view));
                mReference.child(Integer.toString(notice.getNumber())).updateChildren(taskMap);
                notice.setView(view);
                txtView.setText(Integer.toString(notice.getView()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*FloatingActionButton fabGood = findViewById(R.id.fabGood);
        fabGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                txtView.setText("HelloWorld!!");
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notice_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
            case R.id.action_btn1:
                if (!isDeveloper) {
                    toast("관리자만 게시물을 수정할 수 있습니다.");
                    return true;
                }
                Intent intent = new Intent(NoticeActivity.this, WriteNoticeActivity.class);
                intent.putExtra("Edit_Notice", notice);
                intent.putExtra("Edit", true);
                startActivity(intent);
                finish();
                return true;
            case R.id.action_btn2:
                if (!isDeveloper) {
                    toast("관리자만 게시물을 삭제할 수 있습니다.");
                    return true;
                }
                View view = getLayoutInflater().inflate(R.layout.answerdialog, null);

                final TextView txtView = view.findViewById(R.id.txtView);
                final Button btnCancel = view.findViewById(R.id.btnCancel);
                final Button btnOK = view.findViewById(R.id.btnOK);

                txtView.setText("게시물을 삭제하시겠습니까?");
                btnOK.setText("삭제");

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mReference = mDatabase.getReference("Notice");
                        mReference.child(Integer.toString(notice.getNumber())).removeValue();
                        toast("해당 게시물을 삭제하였습니다.");
                        finish();
                    }
                });

                builder = new AlertDialog.Builder(NoticeActivity.this);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toast(String message) {
        Toast.makeText(NoticeActivity.this, message, Toast.LENGTH_SHORT).show();
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
