package com.vip.vipagents.ui.slideshow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vip.vipagents.R;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.Map;

public class MissionActivity extends AppCompatActivity {
    private Spinner sprMissions, sprDifficulty, sprMode;
    private ListView listMission;
    private TextView txtInfo;
    private Button btnAdd;

    private List<String> arrDifficulty, arrMissions, arrMode;
    private ArrayAdapter difficultyAdapter, missionsAdapter, modeAdapter;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private MissionAdapter missionAdapter;
    private ArrayList<Mission> missions;
    private AlertDialog.Builder builder = null;
    private AlertDialog alertDialog = null;

    private int grade = 0;
    private boolean isClan = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("?????? ???????????? ????????????");

        sprMissions = findViewById(R.id.sprMissions);
        sprDifficulty = findViewById(R.id.sprDifficulty);
        listMission = findViewById(R.id.listMission);
        sprMode = findViewById(R.id.sprMode);
        txtInfo = findViewById(R.id.txtInfo);
        btnAdd = findViewById(R.id.btnAdd);

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

        missions = new ArrayList<Mission>();
        missionAdapter = new MissionAdapter(MissionActivity.this, missions);
        listMission.setAdapter(missionAdapter);

        refreshData((String)sprMissions.getSelectedItem(), getDifficulty((String)sprDifficulty.getSelectedItem()), getMode((String)sprMode.getSelectedItem()));

        sprMissions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshData((String)sprMissions.getSelectedItem(), getDifficulty((String)sprDifficulty.getSelectedItem()), getMode((String)sprMode.getSelectedItem()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sprDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshData((String)sprMissions.getSelectedItem(), getDifficulty((String)sprDifficulty.getSelectedItem()), getMode((String)sprMode.getSelectedItem()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sprMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshData((String)sprMissions.getSelectedItem(), getDifficulty((String)sprDifficulty.getSelectedItem()), getMode((String)sprMode.getSelectedItem()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loadProfile().equals("null") || loadProfile() == null) {
                    toast("????????? ????????? ????????????. ????????? ??? ?????????????????????.");
                    return;
                }
                View view = getLayoutInflater().inflate(R.layout.mission_add_dialog, null);

                final Spinner sprDifficulty2 = view.findViewById(R.id.sprDifficulty2);
                final Spinner sprMode2 = view.findViewById(R.id.sprMode2);
                final EditText edtHour = view.findViewById(R.id.edtHour);
                final EditText edtMin = view.findViewById(R.id.edtMin);
                final EditText edtSecond = view.findViewById(R.id.edtSecond);
                final Button btnCancel = view.findViewById(R.id.btnCancel);
                final Button btnOK = view.findViewById(R.id.btnOK);
                final Button btnDelete = view.findViewById(R.id.btnDelete);

                sprDifficulty2.setAdapter(difficultyAdapter);
                sprMode2.setAdapter(modeAdapter);

                edtMin.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (edtMin.getText().toString().equals("")) return;
                        int value = Integer.parseInt(edtMin.getText().toString());
                        if (value < 0 || value >= 60) {
                            edtMin.setText("0");
                            toast("60?????? ???????????? ??? ????????????.");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                edtSecond.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (edtSecond.getText().toString().equals("")) return;
                        int value = Integer.parseInt(edtSecond.getText().toString());
                        if (value < 0 || value >= 60) {
                            edtSecond.setText("0");
                            toast("60?????? ???????????? ??? ????????????.");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toast("?????? ?????? ??????????????????.");
                    }
                });

                btnDelete.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mReference = mDatabase.getReference("Contents/Missions");
                        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    if (data.child("name").getValue().toString().equals((String)sprMissions.getSelectedItem())) {
                                        for (DataSnapshot data2 : data.getChildren()) {
                                            if (data2.getKey().equals("name")) continue;
                                            if (data2.child("name").getValue().toString().equals(loadProfile())) {
                                                int difficulty = getDifficulty((String)sprDifficulty2.getSelectedItem());
                                                int mode = getMode((String)sprMode2.getSelectedItem());
                                                mReference.child(data.getKey()).child(loadProfile()+difficulty+mode).removeValue();
                                                toast("???????????? ????????? ?????????????????????.");
                                                refreshData((String)sprMissions.getSelectedItem(), getDifficulty((String)sprDifficulty.getSelectedItem()), getMode((String)sprMode.getSelectedItem()));
                                                alertDialog.dismiss();
                                                return;
                                            }
                                        }
                                    }
                                }
                                toast("????????? ??????????????? ????????????.");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        return true;
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtHour.getText().toString().equals("")) edtHour.setText("0");
                        if (edtMin.getText().toString().equals("")) edtMin.setText("0");
                        if (edtSecond.getText().toString().equals("")) edtSecond.setText("0");

                        int hour = Integer.parseInt(edtHour.getText().toString());
                        int min = Integer.parseInt(edtMin.getText().toString());
                        int second = Integer.parseInt(edtSecond.getText().toString());

                        final int time = hour*3600 + min*60 + second;

                        mReference = mDatabase.getReference("Contents/Missions");
                        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    if (data.child("name").getValue().toString().equals((String)sprMissions.getSelectedItem())) {
                                        String name = loadProfile();
                                        int difficulty = getDifficulty((String)sprDifficulty2.getSelectedItem());
                                        int mode = getMode((String)sprMode2.getSelectedItem());

                                        for (DataSnapshot data2 : data.getChildren()) {
                                            if (data2.getKey().equals("name")) continue;
                                            if (data2.child("name").getValue().toString().equals(loadProfile()) && Integer.parseInt(data2.child("difficulty").getValue().toString()) == getDifficulty((String)sprDifficulty2.getSelectedItem()) && Integer.parseInt(data2.child("mode").getValue().toString()) == getMode((String)sprMode2.getSelectedItem())) {
                                                Map<String, Object> taskMap = new HashMap<String, Object>();
                                                taskMap.put("time", time);
                                                mReference.child(data.getKey()).child(loadProfile()+difficulty+mode).updateChildren(taskMap);
                                                toast("??????????????? ???????????? ????????????.");
                                                refreshData((String)sprMissions.getSelectedItem(), getDifficulty((String)sprDifficulty.getSelectedItem()), getMode((String)sprMode.getSelectedItem()));
                                                alertDialog.dismiss();
                                                return;
                                            }
                                        }
                                        /*if (data.child(loadProfile()).child("name").getValue().toString().equals(loadProfile()) && Integer.parseInt(data.child(loadProfile()).child("difficulty").getValue().toString()) == getDifficulty((String)sprDifficulty2.getSelectedItem())
                                        && Integer.parseInt(data.child(loadProfile()).child("mode").getValue().toString()) == getMode((String)sprMode2.getSelectedItem())) {
                                            Map<String, Object> taskMap = new HashMap<String, Object>();
                                            taskMap.put("time", time);
                                            mReference.child(data.getKey()).child(loadProfile()).updateChildren(taskMap);
                                            toast("??????????????? ???????????? ????????????.");
                                            refreshData((String)sprMissions.getSelectedItem(), getDifficulty((String)sprDifficulty.getSelectedItem()), getMode((String)sprMode.getSelectedItem()));
                                            alertDialog.dismiss();
                                            return;
                                        }*/

                                        Mission mission = new Mission(loadProfile(), time, difficulty, grade, mode, isClan);
                                        mReference.child(data.getKey()).child(loadProfile()+difficulty+mode).setValue(mission);
                                        refreshData((String)sprMissions.getSelectedItem(), getDifficulty((String)sprDifficulty.getSelectedItem()), getMode((String)sprMode.getSelectedItem()));
                                        toast("??????????????? ??????????????????.");
                                        alertDialog.dismiss();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                builder = new AlertDialog.Builder(MissionActivity.this);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseReference memberReference = mDatabase.getReference("Members");
        memberReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.child("id").getValue().toString().equals(loadProfile())) {
                        grade = Integer.parseInt(data.child("grade").getValue().toString());
                        isClan = Boolean.parseBoolean(data.child("clan").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private int getDifficulty(String difficulty) {
        switch (difficulty) {
            case "?????????":
                return 1;
            case "?????? ?????????":
                return 2;
            case "??????":
                return 3;
            case "??????":
                return 4;
                default:
                    return 0;
        }
    }

    private int getMode(String mode) {
        switch (mode) {
            case "??????":
                return 1;
            case "?????????":
                return 2;
                default:
                    return 0;
        }
    }

    private void refreshData(final String mission_name, final int difficulty, final int mode) {
        missions.clear();
        mReference = mDatabase.getReference("Contents/Missions");
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.child("name").getValue().toString().equals(mission_name)) {
                        for (DataSnapshot data2 : data.getChildren()) {
                            if (data2.getKey().equals("name")) continue;
                            if (Integer.parseInt(data2.child("difficulty").getValue().toString()) == difficulty && Integer.parseInt(data2.child("mode").getValue().toString()) == mode) {
                                String name = data2.child("name").getValue().toString();
                                int time = Integer.parseInt(data2.child("time").getValue().toString());
                                int difficulty = Integer.parseInt(data2.child("difficulty").getValue().toString());
                                int grade = Integer.parseInt(data2.child("grade").getValue().toString());
                                int mode = Integer.parseInt(data2.child("mode").getValue().toString());
                                boolean isClan = Boolean.parseBoolean(data2.child("clan").getValue().toString());

                                Mission mission = new Mission(name, time, difficulty, grade, mode, isClan);
                                missions.add(mission);
                            }
                        }
                    }
                }

                if (missions.isEmpty()) {
                    listMission.setVisibility(View.GONE);
                    txtInfo.setVisibility(View.VISIBLE);
                } else {
                    listMission.setVisibility(View.VISIBLE);
                    txtInfo.setVisibility(View.GONE);
                }

                Collections.sort(missions);

                missionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar??? back??? ????????? ??? ??????
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
