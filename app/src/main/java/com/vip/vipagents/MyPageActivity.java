package com.vip.vipagents;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;

public class MyPageActivity extends AppCompatActivity {
    private Button btnEdit, btnEditPassword, btnReset;
    private TextView txtGrade;
    private ImageView imgGrade;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(loadProfile());

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        btnEdit = findViewById(R.id.btnEdit);
        btnEditPassword = findViewById(R.id.btnEditPassword);
        btnReset = findViewById(R.id.btnReset);
        txtGrade = findViewById(R.id.txtGrade);
        imgGrade = findViewById(R.id.imgGrade);

        mDatabase = FirebaseDatabase.getInstance();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageActivity.this, ProfileEditActivity.class);
                startActivity(intent);
            }
        });

        btnEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageActivity.this, EditPasswordActivity.class);
                startActivity(intent);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(loadProfile());
        mReference = mDatabase.getReference("Members");
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.child("id").getValue().toString().equals(loadProfile())) {
                        switch (Integer.parseInt(data.child("grade").getValue().toString())) {
                            case 0:
                                imgGrade.setImageResource(R.drawable.diff1);
                                txtGrade.setText("수습 요원");
                                break;
                            case 1:
                                imgGrade.setImageResource(R.drawable.diff2);
                                txtGrade.setText("요원");
                                break;
                            case 2:
                                imgGrade.setImageResource(R.drawable.diff3);
                                txtGrade.setText("부관");
                                break;
                            case 3:
                                imgGrade.setImageResource(R.drawable.diff4);
                                txtGrade.setText("지휘관");
                                break;
                        }
                        if (!Boolean.parseBoolean(data.child("clan").getValue().toString())) {
                            imgGrade.setVisibility(View.GONE);
                            txtGrade.setText("클랜 없음");
                        } else {
                            imgGrade.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
