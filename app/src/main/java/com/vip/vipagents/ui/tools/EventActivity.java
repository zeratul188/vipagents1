package com.vip.vipagents.ui.tools;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vip.vipagents.R;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventActivity extends AppCompatActivity {
    private ImageView imgEvent;
    private FloatingActionButton fabAdd;
    private TextView txtPlay, txtMax, txtStart, txtEnd, txtDate, txtContent;
    private LinearLayout layoutPlay;

    private Event event = null;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private AlertDialog.Builder builder = null;
    private AlertDialog alertDialog = null;
    private FirebaseDatabase mDatabase;
    private DatabaseReference memberReference, eventReference;

    private int now_play = 0;
    private boolean isDeveloper = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgEvent = findViewById(R.id.imgEvent);
        fabAdd = findViewById(R.id.fabAdd);
        txtPlay = findViewById(R.id.txtPlay);
        txtMax = findViewById(R.id.txtMax);
        txtStart = findViewById(R.id.txtStart);
        txtEnd = findViewById(R.id.txtEnd);
        txtDate = findViewById(R.id.txtDate);
        txtContent = findViewById(R.id.txtContent);
        layoutPlay = findViewById(R.id.layoutPlay);

        Intent intent = getIntent();
        event = (Event)intent.getSerializableExtra("Event");
        mDatabase = FirebaseDatabase.getInstance();
        memberReference = mDatabase.getReference("Members");
        eventReference = mDatabase.getReference("Contents/Events");
        memberReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.child("id").getValue().toString().equals(loadProfile()) && Integer.parseInt(data.child("grade").getValue().toString()) > 1) {
                        isDeveloper = true;
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        storage = FirebaseStorage.getInstance("gs://vip-agents.appspot.com");
        storageRef = storage.getReference();
        storageRef.child("Events/event"+event.getNumber()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(EventActivity.this).load(uri).into(imgEvent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imgEvent.setImageResource(R.drawable.sample);
            }
        });

        if (event.getLimit() > 9999) {
            layoutPlay.setVisibility(View.GONE);
            fabAdd.setVisibility(View.GONE);
        } else {
            layoutPlay.setVisibility(View.VISIBLE);
            fabAdd.setVisibility(View.VISIBLE);
            txtPlay.setText(Integer.toString(event.getPlay()));
            txtMax.setText(Integer.toString(event.getLimit()));
        }

        txtStart.setText(event.getStart());
        txtEnd.setText(event.getEnd());
        txtDate.setText(event.getDate());
        txtContent.setText(event.getContent());
        setTitle(event.getTitle());

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int play = event.getPlay();
                        if (loadProfile() == null || loadProfile().equals("/*null*/") || loadProfile().equals("null")) {
                            toast("로그인 하신 후 이용해주시기 바랍니다.");
                            return;
                        }
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (!data.getKey().equals("ev"+event.getNumber())) continue;
                            if (data.hasChild("Members")) {
                                for (DataSnapshot data2 : data.child("Members").getChildren()) {
                                    if (data2.getValue().toString().equals(loadProfile())) {
                                        eventReference.child("ev"+event.getNumber()).child("Members").child(loadProfile()).removeValue();
                                        toast("이벤트 참석을 취소하였습니다.");
                                        play--;
                                        event.setPlay(play);
                                        Map<String, Object> taskMap = new HashMap<String, Object>();
                                        taskMap.put("play", play);
                                        eventReference.child("ev"+event.getNumber()).updateChildren(taskMap);
                                        txtPlay.setText(Integer.toString(play));
                                        return;
                                    }
                                }
                            }
                        }
                        if (play >= event.getLimit()) {
                            toast("이미 참석 가능한 인원이 초과되어 참석하실 수 없습니다.");
                            return;
                        }
                        eventReference.child("ev"+event.getNumber()).child("Members").child(loadProfile()).setValue(loadProfile());
                        play++;
                        event.setPlay(play);
                        Map<String, Object> taskMap = new HashMap<String, Object>();
                        taskMap.put("play", play);
                        eventReference.child("ev"+event.getNumber()).updateChildren(taskMap);
                        toast("이벤트에 참석하였습니다.");
                        txtPlay.setText(Integer.toString(play));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View view = null;
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
            case R.id.action_member:
                view = getLayoutInflater().inflate(R.layout.event_list_dialog, null);

                final ListView listMember = view.findViewById(R.id.listMember);
                final TextView txtInfo = view.findViewById(R.id.txtInfo);
                final Button btnClose = view.findViewById(R.id.btnClose);

                final ArrayList<String> members = new ArrayList<String>();
                final EventListAdapter eventListAdapter = new EventListAdapter(EventActivity.this, members);
                listMember.setAdapter(eventListAdapter);

                eventReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        members.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (!data.getKey().equals("ev"+event.getNumber())) continue;
                            if (data.hasChild("Members")) {
                                for (DataSnapshot data2 : data.child("Members").getChildren()) {
                                    members.add(data2.getValue().toString());
                                }
                            }
                        }
                        if (members.isEmpty()) {
                            txtInfo.setVisibility(View.VISIBLE);
                            listMember.setVisibility(View.GONE);
                        } else {
                            txtInfo.setVisibility(View.GONE);
                            listMember.setVisibility(View.VISIBLE);
                        }
                        eventListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                builder = new AlertDialog.Builder(EventActivity.this);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
                return true;
            case R.id.action_edit:
                if (!isDeveloper) {
                    toast("관리자만 게시물을 수정하실 수 있습니다.");
                    return true;
                }
                Intent intent = new Intent(EventActivity.this, WrtieEventActivity.class);
                intent.putExtra("Edit_Event", event);
                intent.putExtra("isEdit", true);
                startActivity(intent);
                finish();
                return true;
            case R.id.action_delete:
                if (!isDeveloper) {
                    toast("관리자만 게시물을 삭제하실 수 있습니다.");
                    return true;
                }
                view = getLayoutInflater().inflate(R.layout.answerdialog, null);

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
                        storageRef.child("Events/event"+event.getNumber()).delete();
                        eventReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    if (Integer.parseInt(data.child("number").getValue().toString()) == event.getNumber()) {
                                        eventReference.child("ev"+event.getNumber()).removeValue();
                                        toast("게시물을 삭제하였습니다.");
                                        alertDialog.dismiss();
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

                builder = new AlertDialog.Builder(EventActivity.this);
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
