package com.vip.vipagents.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vip.vipagents.R;
import com.vip.vipagents.ui.tools.Event;
import com.vip.vipagents.ui.tools.EventActivity;

import java.util.ArrayList;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private ArrayList<Event> events;
    private Context context;

    private FirebaseStorage storage;
    private StorageReference storageRef;

    public EventListAdapter(ArrayList<Event> events, Context context) {
        this.events = events;
        this.context = context;
        storage = FirebaseStorage.getInstance("gs://vip-agents.appspot.com");
        storageRef = storage.getReference();
    }

    @NonNull
    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_main_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventListAdapter.ViewHolder holder, int position) {
        String title = events.get(position).getTitle();
        String start_date = events.get(position).getStart();
        String end_date = events.get(position).getEnd();

        holder.txtTitle.setText(title);
        holder.txtDate.setText(start_date+" ~ "+end_date);
        holder.setClickAction(events.get(position));

        storageRef.child("Events/event"+events.get(position).getNumber()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                 Glide.with(context).load(uri).into(holder.imgEvent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.imgEvent.setImageResource(R.drawable.sample);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle, txtDate;
        public ImageView imgEvent;
        public Event event = null;

        public ViewHolder(View itemVIew) {
            super(itemVIew);

            txtTitle = itemVIew.findViewById(R.id.txtTitle);
            txtDate = itemVIew.findViewById(R.id.txtDate);
            imgEvent = itemVIew.findViewById(R.id.imgEvent);

            imgEvent.setClipToOutline(true);

            itemVIew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (event == null) return;
                    Intent intent = new Intent(context, EventActivity.class);
                    intent.putExtra("Event", event);
                    context.startActivity(intent);
                }
            });
        }

        public void setClickAction(Event event) {
            this.event = event;
        }
    }
}
