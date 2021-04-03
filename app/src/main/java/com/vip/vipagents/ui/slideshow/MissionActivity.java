package com.vip.vipagents.ui.slideshow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vip.vipagents.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class MissionActivity extends AppCompatActivity {
    private Spinner sprMissions, sprDifficulty, sprMode;
    private ListView listMission;

    private List<String> arrDifficulty, arrMissions, arrMode;
    private ArrayAdapter difficultyAdapter, missionsAdapter, modeAdapter;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("임무 타임어택");

        sprMissions = findViewById(R.id.sprMissions);
        sprDifficulty = findViewById(R.id.sprDifficulty);
        listMission = findViewById(R.id.listMission);
        sprMode = findViewById(R.id.sprMode);

        arrDifficulty = Arrays.asList(getResources().getStringArray(R.array.difficulty));
        difficultyAdapter = new ArrayAdapter(MissionActivity.this, R.layout.spinner_sub_item, arrDifficulty);
        difficultyAdapter.setDropDownViewResource(R.layout.spinner_sub_item);
        sprDifficulty.setAdapter(difficultyAdapter);

        arrMode = Arrays.asList(getResources().getStringArray(R.array.mission_mode));
        modeAdapter = new ArrayAdapter(MissionActivity.this, R.layout.spinner_sub_item, arrMode);
        modeAdapter.setDropDownViewResource(R.layout.spinner_sub_item);
        sprMode.setAdapter(modeAdapter);

        arrMissions = new ArrayList<String>();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Contents/Missions");
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    arrMissions.add(data.child("name").getValue().toString());
                }
                missionsAdapter = new ArrayAdapter(MissionActivity.this, R.layout.spinner_mission_item, arrMissions);
                missionsAdapter.setDropDownViewResource(R.layout.spinner_mission_item);
                sprMissions.setAdapter(missionsAdapter);
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

    private void toast(String message) {
        Toast.makeText(MissionActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
