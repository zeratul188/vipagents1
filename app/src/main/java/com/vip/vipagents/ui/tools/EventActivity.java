package com.vip.vipagents.ui.tools;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vip.vipagents.R;

public class EventActivity extends AppCompatActivity {
    private ImageView imgEvent;
    private FloatingActionButton fabAdd;
    private TextView txtPlay, txtMax, txtStart, txtEnd, txtDate, txtContent;
    private LinearLayout layoutPlay;

    private Event event = null;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    private int now_play = 0;

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

        if (event.getLimit() > 9999) layoutPlay.setVisibility(View.GONE);
        else {
            layoutPlay.setVisibility(View.VISIBLE);
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
                now_play++;
                txtPlay.setText(Integer.toString(now_play));
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
}
