package com.vip.vipagents.ui.share;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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

public class ShareFragment extends Fragment {

    private ShareViewModel shareViewModel;

    private FloatingActionButton fabAdd;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private ListView listNotice;
    private TextView txtInfo;

    private ArrayList<Notice> notices;
    private NoticeAdapter noticeAdapter;

    private int grade = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =
                ViewModelProviders.of(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);

        mDatabase = FirebaseDatabase.getInstance();
        /*mReference = mDatabase.getReference("Members");
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.child("id").getValue().toString().equals(loadProfile())) {
                        grade = Integer.parseInt(data.child("grade").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        fabAdd = root.findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (grade > 1) {
                    Intent intent = new Intent(getActivity(), WriteNoticeActivity.class);
                    startActivity(intent);
                } else toast("관리자만 공지사항을 작성하실 수 있습니다.");
            }
        });

        listNotice = root.findViewById(R.id.listNotice);
        txtInfo = root.findViewById(R.id.txtInfo);

        /*mReference = mDatabase.getReference("Notice");
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    txtInfo.setVisibility(View.GONE);
                    listNotice.setVisibility(View.VISIBLE);
                }
                for (DataSnapshot data : snapshot.getChildren()) {
                    Notice notice = new Notice(Integer.parseInt(data.child("number").getValue().toString()), data.child("title").getValue().toString(), data.child("writer").getValue().toString(),
                            data.child("content").getValue().toString(), Integer.parseInt(data.child("view").getValue().toString()), data.child("date").getValue().toString());
                    notices.add(notice);
                }
                noticeAdapter = new NoticeAdapter(getActivity(), notices);
                listNotice.setAdapter(noticeAdapter);
                listNotice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), NoticeActivity.class);
                        intent.putExtra("Notice", notices.get(position));
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        notices = new ArrayList<Notice>();
        noticeAdapter = new NoticeAdapter(getActivity(), notices);
        listNotice.setAdapter(noticeAdapter);
        listNotice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NoticeActivity.class);
                intent.putExtra("Notice", notices.get(position));
                startActivity(intent);
            }
        });

        return root;
    }

    private void toast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onResume() {
        super.onResume();
        grade = 0;
        mReference = mDatabase.getReference("Members");
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.child("id").getValue().toString().equals(loadProfile())) {
                        grade = Integer.parseInt(data.child("grade").getValue().toString());
                    }
                }
                if (grade > 1) fabAdd.setVisibility(View.VISIBLE);
                else fabAdd.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        notices.clear();
        mReference = mDatabase.getReference("Notice");
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    txtInfo.setVisibility(View.GONE);
                    listNotice.setVisibility(View.VISIBLE);
                } else {
                    txtInfo.setVisibility(View.VISIBLE);
                    listNotice.setVisibility(View.GONE);
                }
                for (DataSnapshot data : snapshot.getChildren()) {
                    Notice notice = new Notice(Integer.parseInt(data.child("number").getValue().toString()), data.child("title").getValue().toString(), data.child("writer").getValue().toString(),
                            data.child("content").getValue().toString(), Integer.parseInt(data.child("view").getValue().toString()), data.child("date").getValue().toString());
                    notices.add(notice);
                }
                noticeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}