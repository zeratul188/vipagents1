package com.vip.vipagents.ui.tools;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vip.vipagents.R;

import java.util.ArrayList;

public class EventAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Event> events;

    private FirebaseStorage storage;
    private StorageReference storageRef;

    public EventAdapter(Context context, ArrayList<Event> events) {
        this.context = context;
        this.events = events;
        storage = FirebaseStorage.getInstance("gs://vip-agents.appspot.com");
        storageRef = storage.getReference();
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = View.inflate(context, R.layout.list_event, null);

        final ImageView imgEvent = convertView.findViewById(R.id.imgEvent);
        final TextView txtTitle = convertView.findViewById(R.id.txtTitle);
        final TextView txtStartDate = convertView.findViewById(R.id.txtStartDate);
        final TextView txtEndDate = convertView.findViewById(R.id.txtEndDate);

        txtTitle.setText(events.get(position).getTitle());
        txtStartDate.setText(events.get(position).getStart());
        txtEndDate.setText(events.get(position).getEnd());

        storageRef.child("Events/event"+events.get(position).getNumber()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(imgEvent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imgEvent.setImageResource(R.drawable.sample);
            }
        });

        return convertView;
    }
}
