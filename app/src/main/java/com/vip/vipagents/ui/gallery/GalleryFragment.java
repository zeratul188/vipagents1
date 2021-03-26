package com.vip.vipagents.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vip.vipagents.Member;
import com.vip.vipagents.R;

import java.util.ArrayList;
import java.util.Collections;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    private TextView txtClan, txtAll;
    private LinearLayout layoutAll;
    private TableLayout tableGrade;
    private TextView[] txtGrade = new TextView[5];
    private RadioGroup rgFilter;
    private RadioButton[] rdoFilter = new RadioButton[6];
    private ListView listMembers;
    private ImageView imgMenu;

    private MemberAdapter memberAdapter;
    private ArrayList<Member> members;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        txtClan = root.findViewById(R.id.txtClan);
        txtAll = root.findViewById(R.id.txtAll);
        layoutAll = root.findViewById(R.id.layoutAll);
        tableGrade = root.findViewById(R.id.tableGrade);
        rgFilter = root.findViewById(R.id.rgFilter);
        listMembers = root.findViewById(R.id.listMembers);
        imgMenu = root.findViewById(R.id.imgMenu);

        for (int i = 0; i < txtGrade.length; i++) {
            int resource = getResources().getIdentifier("txtGrade"+(i+1), "id", getActivity().getPackageName());
            txtGrade[i] = root.findViewById(resource);
        }
        for (int i = 0; i < rdoFilter.length; i++) {
            int resource = getResources().getIdentifier("rdoFilter"+(i+1), "id", getActivity().getPackageName());
            rdoFilter[i] = root.findViewById(resource);
        }

        layoutAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tableGrade.getVisibility() == View.VISIBLE) {
                    tableGrade.setVisibility(View.GONE);
                    imgMenu.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                } else {
                    tableGrade.setVisibility(View.VISIBLE);
                    imgMenu.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                }
            }
        });

        mDatabase = FirebaseDatabase.getInstance();

        members = new ArrayList<Member>();
        mReference = mDatabase.getReference("Members");
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                members.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String id = data.child("id").getValue().toString();
                    String pwd = data.child("pwd").getValue().toString();
                    int grade = Integer.parseInt(data.child("grade").getValue().toString());
                    boolean isClan = Boolean.parseBoolean(data.child("clan").getValue().toString());
                    Member member = new Member(id, pwd, grade, isClan);
                    members.add(member);
                    Collections.sort(members);
                }
                memberAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        memberAdapter = new MemberAdapter(getActivity(), members);
        listMembers.setAdapter(memberAdapter);

        rgFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, final int checkedId) {
                mReference = mDatabase.getReference("Members");
                mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        members.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            String id = data.child("id").getValue().toString();
                            String pwd = data.child("pwd").getValue().toString();
                            int grade = Integer.parseInt(data.child("grade").getValue().toString());
                            boolean isClan = Boolean.parseBoolean(data.child("clan").getValue().toString());
                            Member member = null;
                            switch (checkedId) {
                                case R.id.rdoFilter1:
                                    member = new Member(id, pwd, grade, isClan);
                                    members.add(member);
                                    break;
                                case R.id.rdoFilter2:
                                    if (!isClan) {
                                        member = new Member(id, pwd, grade, isClan);
                                        members.add(member);
                                    }
                                    break;
                                case R.id.rdoFilter3:
                                    if (isClan && grade == 0) {
                                        member = new Member(id, pwd, grade, isClan);
                                        members.add(member);
                                    }
                                    break;
                                case R.id.rdoFilter4:
                                    if (isClan && grade == 1) {
                                        member = new Member(id, pwd, grade, isClan);
                                        members.add(member);
                                    }
                                    break;
                                case R.id.rdoFilter5:
                                    if (isClan && grade == 2) {
                                        member = new Member(id, pwd, grade, isClan);
                                        members.add(member);
                                    }
                                    break;
                                case R.id.rdoFilter6:
                                    if (isClan && grade == 3) {
                                        member = new Member(id, pwd, grade, isClan);
                                        members.add(member);
                                    }
                                    break;
                            }
                        }
                        Collections.sort(members);
                        memberAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mReference = mDatabase.getReference("Members");
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int no = 0, gd1 = 0, gd2 = 0, gd3 = 0, gd4 = 0, all = 0, clan = 0;
                rdoFilter[0].setChecked(true);
                members.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    all++;
                    if (Boolean.parseBoolean(data.child("clan").getValue().toString())) {
                        clan++;
                        switch (Integer.parseInt(data.child("grade").getValue().toString())) {
                            case 0:
                                gd1++;
                                break;
                            case 1:
                                gd2++;
                                break;
                            case 2:
                                gd3++;
                                break;
                            case 3:
                                gd4++;
                                break;
                        }
                    } else {
                        no++;
                    }
                }
                txtGrade[0].setText(Integer.toString(gd1));
                txtGrade[1].setText(Integer.toString(gd2));
                txtGrade[2].setText(Integer.toString(gd3));
                txtGrade[3].setText(Integer.toString(gd4));
                txtGrade[4].setText(Integer.toString(no));
                txtAll.setText(Integer.toString(all));
                txtClan.setText(Integer.toString(clan));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}