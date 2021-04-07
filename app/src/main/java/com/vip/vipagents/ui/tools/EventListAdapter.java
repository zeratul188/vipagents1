package com.vip.vipagents.ui.tools;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vip.vipagents.R;

import java.util.ArrayList;

public class EventListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> members;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    public EventListAdapter(Context context, ArrayList<String> members) {
        this.context = context;
        this.members = members;
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Members");
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int position) {
        return members.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = View.inflate(context, R.layout.list_event_member, null);

        final ImageView imgGrade = convertView.findViewById(R.id.imgGrade);
        TextView txtID = convertView.findViewById(R.id.txtID);

        txtID.setText(members.get(position));

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.child("id").getValue().toString().equals(members.get(position))) {
                        switch (Integer.parseInt(data.child("grade").getValue().toString())) {
                            case 1:
                                imgGrade.setImageResource(R.drawable.diff2);
                                break;
                            case 2:
                                imgGrade.setImageResource(R.drawable.diff3);
                                break;
                            case 3:
                                imgGrade.setImageResource(R.drawable.diff4);
                                break;
                            default:
                                imgGrade.setImageResource(R.drawable.diff1);
                        }

                        if (!Boolean.parseBoolean(data.child("clan").getValue().toString())) imgGrade.setVisibility(View.GONE);
                        else imgGrade.setVisibility(View.VISIBLE);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return convertView;
    }
}
