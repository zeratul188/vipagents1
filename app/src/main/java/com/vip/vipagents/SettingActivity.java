package com.vip.vipagents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class SettingActivity extends BaseActivity {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private AlertDialog.Builder builder = null;
    private AlertDialog alertDialog = null;

    private Button btnInfo, btnLogout, btnDelete;
    private TextView txtNetwork, txtVersion;
    private ImageView imgNetwork;

    private AppDataPackage adp;

    private String id = "null";
    private boolean logined = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("설정");

        btnInfo = findViewById(R.id.btnInfo);
        btnLogout = findViewById(R.id.btnLogout);
        btnDelete = findViewById(R.id.btnDelete);
        txtNetwork = findViewById(R.id.txtNetwork);
        txtVersion = findViewById(R.id.txtVersion);
        imgNetwork = findViewById(R.id.imgNetwork);

        mDatabase = FirebaseDatabase.getInstance();
        adp = new AppDataPackage();

        txtVersion.setText(adp.getVersionInfo(SettingActivity.this));

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        logined = intent.getBooleanExtra("logined", false);

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!logined) {
                    toast("로그인 상태가 아닙니다.");
                    return;
                }
                Intent intent = new Intent(SettingActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!logined) {
                    toast("로그인 상태가 아닙니다.");
                    return;
                }
                View view = getLayoutInflater().inflate(R.layout.answerdialog, null);

                final TextView txtView = view.findViewById(R.id.txtView);
                final Button btnCancel = view.findViewById(R.id.btnCancel);
                final Button btnOK = view.findViewById(R.id.btnOK);

                txtView.setText("정말로 로그아웃하시겠습니까?");

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("logouted", true);
                        setResult(RESULT_OK, intent);
                        toast("로그아웃되었습니다.");
                        saveProfile("null");
                        finish();
                    }
                });

                builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!logined) {
                    toast("로그인 상태가 아닙니다.");
                    return;
                }
                View view = getLayoutInflater().inflate(R.layout.editdialog, null);

                final TextView txtView = view.findViewById(R.id.txtView);
                final EditText edtText = view.findViewById(R.id.edtText);
                final Button btnCancel = view.findViewById(R.id.btnCancel);
                final Button btnOK = view.findViewById(R.id.btnOK);

                txtView.setText("회원님의 정보를 삭제하시겠습니까?\n삭제하실려면 비밀번호를 입력해주십시오.");
                txtView.setTextColor(Color.parseColor("#FF2222"));
                edtText.setHint("비밀번호를 입력해주십시오.");
                edtText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                edtText.setTransformationMethod(PasswordTransformationMethod.getInstance());
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
                        mReference = mDatabase.getReference("Members");
                        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    if (data.child("id").getValue().toString().equals(id)) {
                                        if (data.child("pwd").getValue().toString().equals(edtText.getText().toString())) {
                                            deleteReaderBoard(loadProfile());
                                            mReference.child(id).removeValue();
                                            toast("회원님의 계정이 삭제되었습니다.");
                                            Intent intent = new Intent();
                                            intent.putExtra("logouted", true);
                                            setResult(RESULT_OK, intent);
                                            saveProfile("null");
                                            finish();
                                            break;
                                        } else toast("비밀번호가 일치하지 않습니다.");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });
    }

    private void deleteReaderBoard(final String name) {
        final DatabaseReference leaderboardReference = mDatabase.getReference("Contents/Missions");
        leaderboardReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    for (DataSnapshot data2 : data.getChildren()) {
                        if (data2.getKey().equals("name")) continue;
                        if (data2.child("name").getValue().toString().equals(name)) {
                            leaderboardReference.child(data.getKey()).child(data2.getKey()).removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void toast(String message) {
        Toast.makeText(SettingActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    protected void onResume() {
        super.onResume();
        switch (NetworkStatus.getConnecttivityStatus(SettingActivity.this)) {
            case NetworkStatus.TYPE_WIFI:
                txtNetwork.setText("WI-FI");
                txtNetwork.setTextColor(Color.parseColor("#117711"));
                imgNetwork.setImageResource(R.drawable.ic_wifi_black_24dp);
                break;
            case NetworkStatus.TYPE_MOBILE:
                txtNetwork.setText("LTE 또는 3G");
                txtNetwork.setTextColor(Color.parseColor("#FF7700"));
                imgNetwork.setImageResource(R.drawable.ic_import_export_black_24dp);
                break;
            case NetworkStatus.TYPE_NOT_CONNECTED:
                txtNetwork.setText("네트워크에 연결되어 있지 않음");
                txtNetwork.setTextColor(Color.parseColor("#FF4444"));
                imgNetwork.setImageResource(R.drawable.ic_clear_black_24dp);
                break;
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
    }
}
