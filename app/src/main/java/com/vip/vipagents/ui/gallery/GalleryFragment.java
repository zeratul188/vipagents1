package com.vip.vipagents.ui.gallery;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vip.vipagents.Member;
import com.vip.vipagents.MyPageActivity;
import com.vip.vipagents.R;
import com.vip.vipagents.ui.slideshow.PokerAdapter;
import com.vip.vipagents.ui.slideshow.PokerResult;

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
    private AlertDialog.Builder builder = null;
    private AlertDialog alertDialog = null;

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
                    int exp = Integer.parseInt(data.child("exp").getValue().toString());
                    Member member = new Member(id, pwd, grade, isClan, exp);
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

        listMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = members.get(position).getId();
                int exp = members.get(position).getExp();
                int level = (exp/200)+1;
                View main = getLayoutInflater().inflate(R.layout.memberdialog, null);

                final ImageView imgGrade = main.findViewById(R.id.imgGrade);
                final TextView txtID = main.findViewById(R.id.txtID);
                final TextView txtGrade = main.findViewById(R.id.txtGrade);
                final TextView txtLevel = main.findViewById(R.id.txtLevel);
                final TextView txtLevelValue = main.findViewById(R.id.txtLevelValue);
                final TextView txtTowerPower = main.findViewById(R.id.txtTowerPower);
                final TextView txtPokerPower = main.findViewById(R.id.txtPokerPower);
                final TextView txtLegend = main.findViewById(R.id.txtLegend);
                final TextView txtElite = main.findViewById(R.id.txtElite);
                final TextView txtEpic = main.findViewById(R.id.txtEpic);
                final TextView txtRare = main.findViewById(R.id.txtRare);
                final TextView txtNormal = main.findViewById(R.id.txtNormal);
                final ProgressBar progressLevel = main.findViewById(R.id.progressLevel);
                final ListView listPoker = main.findViewById(R.id.listView);
                final Button btnOK = main.findViewById(R.id.btnOK);

                DatabaseReference towerRef = mDatabase.getReference("Members/"+name+"/Tower");
                DatabaseReference pokerRef = mDatabase.getReference("Members/"+name+"/Poker");

                txtID.setText(name);

                switch (members.get(position).getGrade()) {
                    case 3:
                        imgGrade.setImageResource(R.drawable.diff4);
                        txtGrade.setText("지휘관");
                        break;
                    case 2:
                        imgGrade.setImageResource(R.drawable.diff3);
                        txtGrade.setText("부관");
                        break;
                    case 1:
                        imgGrade.setImageResource(R.drawable.diff2);
                        txtGrade.setText("요원");
                        break;
                    default:
                        imgGrade.setImageResource(R.drawable.diff1);
                        txtGrade.setText("수습 요원");
                }

                if (members.get(position).isClan()) {
                    imgGrade.setVisibility(View.VISIBLE);
                    txtGrade.setVisibility(View.VISIBLE);
                } else {
                    imgGrade.setVisibility(View.GONE);
                    txtGrade.setVisibility(View.GONE);
                }

                txtLevel.setText(Integer.toString(level));
                txtLevelValue.setText("("+exp+"/"+level*200+")");
                progressLevel.setMax(200);
                progressLevel.setProgress(exp%200);

                towerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int legend = 0, elite = 0, epic = 0, rare = 0, normal = 0, bonus = 0;
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (data.getKey().equals("money") || data.getKey().equals("date")) continue;
                            int type = 0, grade = 0;
                            for (DataSnapshot data2 : data.getChildren()) {
                                if (data2.getKey().equals("type")) type = Integer.parseInt(data2.getValue().toString());
                                else if (data2.getKey().equals("grade")) grade = Integer.parseInt(data2.getValue().toString());
                            }
                            switch (grade) {
                                case 1:
                                    normal++;
                                    if (type == 7) bonus += 23;
                                    else if (type == 8) bonus += 8;
                                    break;
                                case 2:
                                    rare++;
                                    if (type == 7) bonus += 68;
                                    else if (type == 8) bonus += 23;
                                    break;
                                case 3:
                                    epic++;
                                    if (type == 7) bonus += 203;
                                    else if (type == 8) bonus += 68;
                                    break;
                                case 4:
                                    elite++;
                                    if (type == 7) bonus += 608;
                                    else if (type == 8) bonus += 203;
                                    break;
                                case 5:
                                    legend++;
                                    if (type == 7) bonus += 1823;
                                    else if (type == 8) bonus += 608;
                                    break;
                            }
                        }
                        int result = (normal*75) + (rare*225) + (epic*675) + (elite*2025) + (legend*6075);
                        result += bonus;
                        txtTowerPower.setText(Integer.toString(result));
                        txtLegend.setText(Integer.toString(legend));
                        txtElite.setText(Integer.toString(elite));
                        txtEpic.setText(Integer.toString(epic));
                        txtRare.setText(Integer.toString(rare));
                        txtNormal.setText(Integer.toString(normal));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                final ArrayList<PokerResult> pokerResults = new ArrayList<PokerResult>();
                final PokerAdapter pokerAdapter = new PokerAdapter(getActivity(), pokerResults);
                listPoker.setAdapter(pokerAdapter);
                pokerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int demage = 0;
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (data.getKey().equals("date") || data.getKey().equals("pokers") || data.getKey().equals("ticket")) continue;
                            int score = 0, number = 0;
                            String name = "null";
                            for (DataSnapshot data2 : data.getChildren()) {
                                if (data2.getKey().equals("score")) {
                                    demage += Integer.parseInt(data2.getValue().toString());
                                    score = Integer.parseInt(data2.getValue().toString());
                                } else if (data2.getKey().equals("name")) {
                                    name = data2.getValue().toString();
                                } else if (data2.getKey().equals("number")) {
                                    number = Integer.parseInt(data2.getValue().toString());
                                }
                            }
                            PokerResult pokerResult = new PokerResult(score, name);
                            pokerResult.setNumber(number);
                            pokerResults.add(pokerResult);
                        }
                        txtPokerPower.setText(Integer.toString(demage));
                        Collections.sort(pokerResults);
                        pokerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        alertDialog.dismiss();
                    }
                });

                builder = new AlertDialog.Builder(getActivity());
                builder.setView(main);

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

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
                            int exp = Integer.parseInt(data.child("exp").getValue().toString());
                            Member member = null;
                            switch (checkedId) {
                                case R.id.rdoFilter1:
                                    member = new Member(id, pwd, grade, isClan, exp);
                                    members.add(member);
                                    break;
                                case R.id.rdoFilter2:
                                    if (!isClan) {
                                        member = new Member(id, pwd, grade, isClan, exp);
                                        members.add(member);
                                    }
                                    break;
                                case R.id.rdoFilter3:
                                    if (isClan && grade == 0) {
                                        member = new Member(id, pwd, grade, isClan, exp);
                                        members.add(member);
                                    }
                                    break;
                                case R.id.rdoFilter4:
                                    if (isClan && grade == 1) {
                                        member = new Member(id, pwd, grade, isClan, exp);
                                        members.add(member);
                                    }
                                    break;
                                case R.id.rdoFilter5:
                                    if (isClan && grade == 2) {
                                        member = new Member(id, pwd, grade, isClan, exp);
                                        members.add(member);
                                    }
                                    break;
                                case R.id.rdoFilter6:
                                    if (isClan && grade == 3) {
                                        member = new Member(id, pwd, grade, isClan, exp);
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