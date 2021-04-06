package com.vip.vipagents.ui.tools;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vip.vipagents.R;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ToolsFragment extends Fragment {

    private ToolsViewModel toolsViewModel;

    private TextView txtInfo;
    private ListView listEvent;
    private RadioGroup rgEvent;
    private RadioButton[] rdoEvent = new RadioButton[2];
    private FloatingActionButton fabAdd;

    private FirebaseDatabase mDatabase;
    private DatabaseReference membersReference, eventReference;

    private EventAdapter eventAdapter;
    private ArrayList<Event> events;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);

        txtInfo = root.findViewById(R.id.txtInfo);
        listEvent = root.findViewById(R.id.listEvent);
        rgEvent = root.findViewById(R.id.rgEvent);
        fabAdd = root.findViewById(R.id.fabAdd);
        for (int i = 0; i < rdoEvent.length; i++) {
            int resource = root.getResources().getIdentifier("rdoEvent"+(i+1), "id", getActivity().getPackageName());
            rdoEvent[i] = root.findViewById(resource);
        }

        mDatabase = FirebaseDatabase.getInstance();

        events = new ArrayList<Event>();
        eventAdapter = new EventAdapter(getActivity(), events);
        listEvent.setAdapter(eventAdapter);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WrtieEventActivity.class);
                startActivity(intent);
            }
        });

        rgEvent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdoEvent1:
                        txtInfo.setText("진행 중인 이벤트가 없습니다.");
                        rdoEvent[0].setTextColor(Color.parseColor("#FFFFFF"));
                        rdoEvent[1].setTextColor(Color.parseColor("#000000"));
                        break;
                    case R.id.rdoEvent2:
                        txtInfo.setText("종료된 이벤트가 없습니다.");
                        rdoEvent[1].setTextColor(Color.parseColor("#FFFFFF"));
                        rdoEvent[0].setTextColor(Color.parseColor("#000000"));
                        break;
                }
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        membersReference = mDatabase.getReference("Members");
        membersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.child("id").getValue().toString().equals(loadProfile()) && Integer.parseInt(data.child("grade").getValue().toString()) >= 2) {
                        fabAdd.setVisibility(View.VISIBLE);
                        return;
                    }
                }
                fabAdd.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        events.clear();
        eventReference = mDatabase.getReference("Contents/Events");
        eventReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    String title = data.child("title").getValue().toString();
                    String content = data.child("content").getValue().toString();
                    String date = data.child("date").getValue().toString();
                    String end = data.child("end").getValue().toString();
                    int limit = Integer.parseInt(data.child("limit").getValue().toString());
                    int number = Integer.parseInt(data.child("number").getValue().toString());
                    String start = data.child("start").getValue().toString();

                    Event event = new Event(number, title, date, start, end, content, limit);
                    events.add(event);
                }
                if (!events.isEmpty()) {
                    listEvent.setVisibility(View.VISIBLE);
                    txtInfo.setVisibility(View.GONE);
                } else {
                    listEvent.setVisibility(View.GONE);
                    txtInfo.setVisibility(View.VISIBLE);
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String loadProfile() {
        FileInputStream fis = null;
        try {
            fis = getActivity().openFileInput("id.txt");
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