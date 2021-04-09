package com.vip.vipagents.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vip.vipagents.R;
import com.vip.vipagents.ui.share.Notice;
import com.vip.vipagents.ui.share.NoticeActivity;
import com.vip.vipagents.ui.tools.Event;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference, darkReference, ironhorseReference, eventReference;

    private ArrayList<Notice> notices;
    private ArrayList<Event> events;
    private MainNoticeAdapter noticeAdapter;

    private ListView listNotice;
    private TextView txtNotice, txtAll, txtDark, txtIronHorse, txtEvent;
    private FrameLayout layoutNotice, layoutDark, layoutIronHorse;
    private RecyclerView listEvent;
    private EventListAdapter eventAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        listNotice = root.findViewById(R.id.listNotice);
        txtNotice = root.findViewById(R.id.txtNotice);
        layoutNotice = root.findViewById(R.id.layoutNotice);
        txtAll = root.findViewById(R.id.txtAll);
        txtDark = root.findViewById(R.id.txtDark);
        txtIronHorse = root.findViewById(R.id.txtIronHorse);
        layoutDark = root.findViewById(R.id.layoutDark);
        layoutIronHorse = root.findViewById(R.id.layoutIronHorse);

        layoutDark.setClipToOutline(true);
        layoutIronHorse.setClipToOutline(true);

        mDatabase = FirebaseDatabase.getInstance();

        notices = new ArrayList<Notice>();
        noticeAdapter = new MainNoticeAdapter(getActivity(), notices);
        listNotice.setAdapter(noticeAdapter);
        listNotice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NoticeActivity.class);
                intent.putExtra("Notice", notices.get(position));
                startActivity(intent);
            }
        });

        listEvent = root.findViewById(R.id.listEvent);
        txtEvent = root.findViewById(R.id.txtEvent);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        listEvent.setLayoutManager(layoutManager);
        events = new ArrayList<Event>();
        eventAdapter = new EventListAdapter(events, getActivity());
        listEvent.setAdapter(eventAdapter);
        EventDecoration decoration = new EventDecoration();
        listEvent.addItemDecoration(decoration);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        eventReference = mDatabase.getReference("Contents/Events");
        eventReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    txtEvent.setVisibility(View.GONE);
                    listEvent.setVisibility(View.VISIBLE);
                } else {
                    txtEvent.setVisibility(View.VISIBLE);
                    listEvent.setVisibility(View.GONE);
                }
                events.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String title = data.child("title").getValue().toString();
                    String date = data.child("date").getValue().toString();
                    String start = data.child("start").getValue().toString();
                    String end = data.child("end").getValue().toString();
                    String content = data.child("content").getValue().toString();
                    int number = Integer.parseInt(data.child("number").getValue().toString());
                    int limit = Integer.parseInt(data.child("limit").getValue().toString());
                    int play = Integer.parseInt(data.child("play").getValue().toString());

                    Event event = new Event(number, title, date, start, end, content, limit, play);
                    events.add(event);
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mReference = mDatabase.getReference("Notice");
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    /*layoutNotice.getLayoutParams().height = 400;
                    layoutNotice.setLayoutParams(layoutNotice.getLayoutParams());
                    layoutNotice.requestLayout();*/
                    txtNotice.setVisibility(View.GONE);
                    listNotice.setVisibility(View.VISIBLE);
                } else {
                    /*layoutNotice.getLayoutParams().height = 200;
                    layoutNotice.setLayoutParams(layoutNotice.getLayoutParams());
                    layoutNotice.requestLayout();*/
                    txtNotice.setVisibility(View.VISIBLE);
                    listNotice.setVisibility(View.GONE);
                }
                int max = 5, now = 0;
                notices.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Notice notice = new Notice(Integer.parseInt(data.child("number").getValue().toString()), data.child("title").getValue().toString(), data.child("writer").getValue().toString(),
                            data.child("content").getValue().toString(), Integer.parseInt(data.child("view").getValue().toString()), data.child("date").getValue().toString());
                    notices.add(notice);
                    now++;
                    if (now == max) break;
                }
                noticeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        darkReference = mDatabase.getReference("Raid/Dark");
        darkReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int dark = 0;
                for (DataSnapshot data : snapshot.getChildren()) {
                    dark++;
                }
                txtDark.setText(Integer.toString(dark));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ironhorseReference = mDatabase.getReference("Raid/IronHorse");
        ironhorseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int ironhorse = 0;
                for (DataSnapshot data : snapshot.getChildren()) {
                    ironhorse++;
                }
                txtIronHorse.setText(Integer.toString(ironhorse));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}