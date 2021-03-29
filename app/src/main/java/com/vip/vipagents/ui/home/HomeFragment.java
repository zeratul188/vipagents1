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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vip.vipagents.R;
import com.vip.vipagents.ui.share.Notice;
import com.vip.vipagents.ui.share.NoticeActivity;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference, darkReference, ironhorseReference;

    private ArrayList<Notice> notices;
    private MainNoticeAdapter noticeAdapter;
    private ArrayList<Temp_Member> members;
    private TempMemberAdapter memberAdapter;

    private ListView listNotice, listMember;
    private TextView txtNotice, txtAll, txtMemberInfo, txtDark, txtIronHorse;
    private FrameLayout layoutNotice;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        /*final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        listNotice = root.findViewById(R.id.listNotice);
        txtNotice = root.findViewById(R.id.txtNotice);
        layoutNotice = root.findViewById(R.id.layoutNotice);
        listMember = root.findViewById(R.id.listMember);
        txtAll = root.findViewById(R.id.txtAll);
        txtMemberInfo = root.findViewById(R.id.txtMemberInfo);
        txtDark = root.findViewById(R.id.txtDark);
        txtIronHorse = root.findViewById(R.id.txtIronHorse);

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

        members = new ArrayList<Temp_Member>();
        memberAdapter = new TempMemberAdapter(getActivity(), members);
        listMember.setAdapter(memberAdapter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mReference = mDatabase.getReference("Members");
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    txtMemberInfo.setVisibility(View.GONE);
                    listMember.setVisibility(View.VISIBLE);
                } else {
                    txtMemberInfo.setVisibility(View.VISIBLE);
                    listMember.setVisibility(View.GONE);
                }
                int max = 10, now = 0, all = 0;
                members.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (now < max) {
                        Temp_Member member = new Temp_Member(data.child("id").getValue().toString(), Integer.parseInt(data.child("grade").getValue().toString()),
                                Boolean.parseBoolean(data.child("clan").getValue().toString()));
                        members.add(member);
                    }
                    if (Boolean.parseBoolean(data.child("clan").getValue().toString())) all++;
                    now++;
                }
                txtAll.setText(Integer.toString(all));
                memberAdapter.notifyDataSetChanged();
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