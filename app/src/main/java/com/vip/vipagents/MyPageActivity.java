package com.vip.vipagents;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyPageActivity extends AppCompatActivity {
    private Button btnEdit, btnEditPassword, btnReset, btnLevelReset;
    private TextView txtGrade, txtLevel, txtLevelValue, txtLevelMax;
    private ImageView imgGrade;
    private ProgressBar progressLevel;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private AlertDialog.Builder builder = null;
    private AlertDialog alertDialog = null;
    private CharactorLevel charactorLevel = null;

    private int exp = 0;

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
        txtLevel = findViewById(R.id.txtLevel);
        txtLevelValue = findViewById(R.id.txtLevelValue);
        progressLevel = findViewById(R.id.progressLevel);
        btnLevelReset = findViewById(R.id.btnLevelReset);
        txtLevelMax = findViewById(R.id.txtLevelMax);

        charactorLevel = new CharactorLevel(MyPageActivity.this, loadProfile());

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Members/"+loadProfile());
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getKey().equals("exp")) {
                        exp = Integer.parseInt(data.getValue().toString());
                    }
                }
                txtLevel.setText(Integer.toString((exp/200)+1));
                txtLevelValue.setText(Integer.toString(exp));
                txtLevelMax.setText(Integer.toString(((exp/200)+1)*200));
                progressLevel.setMax(200);
                progressLevel.setProgress(exp%200);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnLevelReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = getLayoutInflater().inflate(R.layout.answerdialog, null);

                final TextView txtView = view.findViewById(R.id.txtView);
                final Button btnCancel = view.findViewById(R.id.btnCancel);
                final Button btnOK = view.findViewById(R.id.btnOK);

                txtView.setText("레벨을 1로 초기화하시겠습니까?");
                btnOK.setText("초기화");

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        resetLevel();
                        toast("레벨을 초기화하였습니다.");
                        alertDialog.dismiss();
                    }
                });

                builder = new AlertDialog.Builder(MyPageActivity.this);
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
                final View view = getLayoutInflater().inflate(R.layout.answerdialog, null);

                final TextView txtView = view.findViewById(R.id.txtView);
                final Button btnCancel = view.findViewById(R.id.btnCancel);
                final Button btnOK = view.findViewById(R.id.btnOK);

                txtView.setText("데이터를 삭제하면 복구하실 수 없습니다. 그래도 삭제하시겠습니까?");
                btnOK.setText("삭제");

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        deleteReaderBoard();
                        toast("모든 콘텐츠 데이터를 삭제하였습니다.");
                        alertDialog.dismiss();
                    }
                });

                builder = new AlertDialog.Builder(MyPageActivity.this);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });
    }

    private void deleteReaderBoard() {
        mReference = mDatabase.getReference("Contents/Missions");
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    for (DataSnapshot data2 : data.getChildren()) {
                        if (data2.getKey().equals("name")) continue;
                        if (data2.child("name").getValue().toString().equals(loadProfile())) {
                            mReference.child(data.getKey()).child(data2.getKey()).removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void resetLevel() {
        mReference = mDatabase.getReference("Members/"+loadProfile());
        exp = 0;
        txtLevel.setText("1");
        txtLevelValue.setText("0");
        progressLevel.setMax(200);
        progressLevel.setProgress(0);
        Map<String, Object> taskMap = new HashMap<String, Object>();
        taskMap.put("exp", 0);
        mReference.updateChildren(taskMap);
    }

    private void toast(String message) {
        Toast.makeText(MyPageActivity.this, message, Toast.LENGTH_SHORT).show();
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
