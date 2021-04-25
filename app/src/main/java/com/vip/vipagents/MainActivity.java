package com.vip.vipagents;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends BaseActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Member logined_member = null;
    private boolean logined = false;

    private final static int ACT_RESULT = 0;
    private final static int ACT_SETTING_RESULT = 1;
    private final static int END_TIME = 2000;

    private long backKeyPressedTime = 0;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private AlertDialog.Builder builder = null;
    private AlertDialog alertDialog = null;

    private TextView txtName = null, txtLevel = null;
    private LinearLayout layoutGrade = null, layoutLevel = null;
    private ImageView imgGrade = null;
    private TextView txtGrade = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg/8V95UMG7Fu"));
                startActivity(intent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if (NetworkStatus.getConnecttivityStatus(getApplicationContext()) == NetworkStatus.TYPE_NOT_CONNECTED) {
            View view = getLayoutInflater().inflate(R.layout.warningdialog, null);

            final TextView txtView = view.findViewById(R.id.txtView);
            final Button btnOK = view.findViewById(R.id.btnOK);

            txtView.setText("네트워크가 연결되어 있지 않습니다. 연결 상태를 확인해주십시오.");
            btnOK.setText("종료");

            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            builder = new AlertDialog.Builder(MainActivity.this);
            builder.setView(view);

            alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();
        }

        View view = navigationView.getHeaderView(0);

        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        toolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));

        txtName = view.findViewById(R.id.txtName);
        layoutGrade = view.findViewById(R.id.layoutGrade);
        imgGrade = view.findViewById(R.id.imgGrade);
        txtGrade = view.findViewById(R.id.txtGrade);
        txtLevel = view.findViewById(R.id.txtLevel);
        layoutLevel = view.findViewById(R.id.layoutLevel);

        mDatabase = FirebaseDatabase.getInstance();

        if (logined_member == null) {
            txtName.setText("로그인해주세요");
            logined = false;
            layoutGrade.setVisibility(View.GONE);
        }

        txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!logined) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, ACT_RESULT);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + END_TIME) {
            backKeyPressedTime = System.currentTimeMillis();
            toast("\"뒤로\"버튼을 한번 더 누르시면 앱이 종료됩니다.");
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + END_TIME) {
            finish();
        }

        /*View view = getLayoutInflater().inflate(R.layout.answerdialog, null);

        final TextView txtView = view.findViewById(R.id.txtView);
        final Button btnOK = view.findViewById(R.id.btnOK);
        final Button btnCancel = view.findViewById(R.id.btnCancel);

        txtView.setText("앱을 종료하시겠습니까?");
        btnOK.setText("종료");

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);

        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (loadProfile() != null && !loadProfile().equals("null")) {
            mReference = mDatabase.getReference("Members/"+loadProfile());
            mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String id = "null", pwd = "null";
                    int grade = 0, exp = 0;
                    boolean isClan = false;
                    for (DataSnapshot data : snapshot.getChildren()) {
                        if (data.getKey().equals("id")) id = data.getValue().toString();
                        else if (data.getKey().equals("pwd")) pwd = data.getValue().toString();
                        else if (data.getKey().equals("grade")) grade = Integer.parseInt(data.getValue().toString());
                        else if (data.getKey().equals("clan")) isClan = Boolean.parseBoolean(data.getValue().toString());
                        else if (data.getKey().equals("exp")) exp = Integer.parseInt(data.getValue().toString());
                        /*if (data.child("id").getValue().toString().equals(loadProfile())) {
                            logined_member = new Member(data.child("id").getValue().toString(), data.child("pwd").getValue().toString(),
                                    Integer.parseInt(data.child("grade").getValue().toString()), Boolean.parseBoolean(data.child("clan").getValue().toString()), Integer.parseInt(data.child("exp").getValue().toString()));
                        }*/
                    }
                    logined_member = new Member(id, pwd, grade, isClan, exp);
                    if (logined_member != null) {
                        logined = true;
                        if (logined_member.isClan()) layoutGrade.setVisibility(View.VISIBLE);
                        else layoutGrade.setVisibility(View.GONE);
                        switch (logined_member.getGrade()) {
                            case 0:
                                imgGrade.setImageResource(R.drawable.darkdiff1);
                                txtGrade.setText("수습 요원");
                                break;
                            case 1:
                                imgGrade.setImageResource(R.drawable.darkdiff2);
                                txtGrade.setText("요원");
                                break;
                            case 2:
                                imgGrade.setImageResource(R.drawable.darkdiff3);
                                txtGrade.setText("부관");
                                break;
                            case 3:
                                imgGrade.setImageResource(R.drawable.darkdiff4);
                                txtGrade.setText("지휘관");
                                break;
                        }
                        txtName.setText(logined_member.getId());
                        layoutLevel.setVisibility(View.VISIBLE);
                        int level = (logined_member.getExp()/200)+1;
                        txtLevel.setText(Integer.toString(level));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void toast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACT_RESULT:
                if (resultCode == RESULT_OK) {
                    logined_member = (Member)data.getSerializableExtra("logined_member");
                    logined = true;
                    if (logined_member.isClan()) layoutGrade.setVisibility(View.VISIBLE);
                    else layoutGrade.setVisibility(View.GONE);
                    switch (logined_member.getGrade()) {
                        case 0:
                            imgGrade.setImageResource(R.drawable.darkdiff1);
                            txtGrade.setText("수습 요원");
                            break;
                        case 1:
                            imgGrade.setImageResource(R.drawable.darkdiff2);
                            txtGrade.setText("요원");
                            break;
                        case 2:
                            imgGrade.setImageResource(R.drawable.darkdiff3);
                            txtGrade.setText("부관");
                            break;
                        case 3:
                            imgGrade.setImageResource(R.drawable.darkdiff4);
                            txtGrade.setText("지휘관");
                            break;
                    }
                    txtName.setText(logined_member.getId());
                    saveProfile(logined_member.getId());
                }
                break;
            case ACT_SETTING_RESULT:
                if (resultCode == RESULT_OK) {
                    boolean logouted = data.getBooleanExtra("logouted", true);
                    if (logouted) {
                        logined_member = null;
                        txtName.setText("로그인해주세요");
                        logined = false;
                        layoutGrade.setVisibility(View.GONE);
                        layoutLevel.setVisibility(View.GONE);
                    } else {
                        if (logined_member.isClan()) layoutGrade.setVisibility(View.VISIBLE);
                        else layoutGrade.setVisibility(View.GONE);
                        switch (logined_member.getGrade()) {
                            case 0:
                                imgGrade.setImageResource(R.drawable.darkdiff1);
                                txtGrade.setText("수습 요원");
                                break;
                            case 1:
                                imgGrade.setImageResource(R.drawable.darkdiff2);
                                txtGrade.setText("요원");
                                break;
                            case 2:
                                imgGrade.setImageResource(R.drawable.darkdiff3);
                                txtGrade.setText("부관");
                                break;
                            case 3:
                                imgGrade.setImageResource(R.drawable.darkdiff4);
                                txtGrade.setText("지휘관");
                                break;
                        }
                        txtName.setText(logined_member.getId());
                    }
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                if (logined_member != null) intent.putExtra("id", logined_member.getId());
                intent.putExtra("logined", logined);
                startActivityForResult(intent, ACT_SETTING_RESULT);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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
        return "null";
    }
}
