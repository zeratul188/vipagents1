package com.vip.vipagents;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CharactorLevel {
    private Context context = null;
    private String id = "null";
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private int now_exp = 0;

    public CharactorLevel(Context context, String id) {
        this.context = context;
        this.id = id;
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Members/"+id);
    }

    public void toast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public int outputExp() {
        return now_exp;
    }

    public void getExp(final int exp_value) {
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getKey().equals("exp")) {
                        now_exp = Integer.parseInt(data.getValue().toString());
                    }
                }
                int undo_level = now_exp/200;
                now_exp += exp_value;
                Map<String, Object> taskMap = new HashMap<String, Object>();
                taskMap.put("exp", now_exp);
                mReference.updateChildren(taskMap);
                if (undo_level < now_exp/200) toast("레벨업 하였습니다.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
