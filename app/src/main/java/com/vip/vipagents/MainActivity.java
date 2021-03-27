package com.vip.vipagents;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private TextView txtName = null;
    private LinearLayout layoutGrade = null;
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

        View view = navigationView.getHeaderView(0);

        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        toolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));

        txtName = view.findViewById(R.id.txtName);
        layoutGrade = view.findViewById(R.id.layoutGrade);
        imgGrade = view.findViewById(R.id.imgGrade);
        txtGrade = view.findViewById(R.id.txtGrade);

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
    protected void onResume() {
        super.onResume();
        if (loadProfile() != null && !loadProfile().equals("null")) {
            mReference = mDatabase.getReference("Members");
            mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        if (data.child("id").getValue().toString().equals(loadProfile())) {
                            logined_member = new Member(data.child("id").getValue().toString(), data.child("pwd").getValue().toString(),
                                    Integer.parseInt(data.child("grade").getValue().toString()), Boolean.parseBoolean(data.child("clan").getValue().toString()));
                        }
                    }
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
                    boolean logouted = data.getBooleanExtra("logouted", false);
                    if (logouted) {
                        logined_member = null;
                        txtName.setText("로그인해주세요");
                        logined = false;
                        layoutGrade.setVisibility(View.GONE);
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
        return "/*null*/";
    }
}
