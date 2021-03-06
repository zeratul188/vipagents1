package com.vip.vipagents.ui.send;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.vip.vipagents.ui.share.NoticeActivity;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SendFragment extends Fragment {

    private SendViewModel sendViewModel;

    private FloatingActionButton fabAddDark, fabAddIronHorse, fabReset, fabRefresh;
    private TextView txtDark, txtIronHorse;
    private ListView listDark, listIronHorse;

    private FirebaseDatabase mDatabase;
    private DatabaseReference darkReference, ironhorseReference, mReference;
    private RaidAdapter darkAdapter, ironhorseAdapter;
    private ArrayList<RaidMember> darkMembers, ironhorseMembers;
    private AlertDialog.Builder builder = null;
    private AlertDialog alertDialog = null;

    private int dark = 0, ironhorse = 0;
    private boolean haveDarkMember = false, haveIronHorseMember = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of(this).get(SendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_send, container, false);

        fabAddDark = root.findViewById(R.id.fabAddDark);
        fabAddIronHorse = root.findViewById(R.id.fabAddIronHorse);
        fabReset = root.findViewById(R.id.fabReset);
        fabRefresh = root.findViewById(R.id.fabRefresh);
        txtDark = root.findViewById(R.id.txtDark);
        txtIronHorse = root.findViewById(R.id.txtIronHorse);
        listDark = root.findViewById(R.id.listDark);
        listIronHorse = root.findViewById(R.id.listIronHorse);

        mDatabase = FirebaseDatabase.getInstance();
        darkMembers = new ArrayList<RaidMember>();
        ironhorseMembers = new ArrayList<RaidMember>();

        darkReference = mDatabase.getReference("Raid/Dark");
        ironhorseReference = mDatabase.getReference("Raid/IronHorse");
        mReference = mDatabase.getReference("Members");

        darkAdapter = new RaidAdapter(getActivity(), darkMembers);
        listDark.setAdapter(darkAdapter);
        ironhorseAdapter = new RaidAdapter(getActivity(), ironhorseMembers);
        listIronHorse.setAdapter(ironhorseAdapter);

        fabAddDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.raiddialog, null);

                final TextView txtView = view.findViewById(R.id.txtView);
                final CheckBox chkCommander = view.findViewById(R.id.chkCommander);
                final Button btnCancel = view.findViewById(R.id.btnCancel);
                final Button btnOK = view.findViewById(R.id.btnOK);

                darkReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (data.child("id").getValue().toString().equals(loadProfile())) {
                                haveDarkMember = true;
                                txtView.setText("?????? ????????? ?????? ???????????? ?????????????????????. ");
                                btnOK.setText("?????? ??????");
                                chkCommander.setVisibility(View.GONE);
                                return;
                            }
                        }
                        haveDarkMember = false;
                        if (dark >= 8) {
                            txtView.setText("?????? ????????? ????????? ??????????????????. ????????? ??????????????????.");
                            btnOK.setEnabled(false);
                            return;
                        }
                        txtView.setText("????????? ?????? ???????????? ?????????????????????????");
                        btnOK.setText("??????");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                darkReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (Boolean.parseBoolean(data.child("commander").getValue().toString())) chkCommander.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                chkCommander.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            darkReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    boolean haveCommander = false;
                                    for (DataSnapshot data : snapshot.getChildren()) {
                                        if (Boolean.parseBoolean(data.child("commander").getValue().toString())) haveCommander = true;
                                    }
                                    if (haveCommander) {
                                        chkCommander.setChecked(false);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (haveDarkMember) {
                            darkReference.child(loadProfile()).removeValue();
                            toast("????????? ?????? ????????? ?????? ?????????????????????.");
                            haveDarkMember = false;
                            onResume();
                            alertDialog.dismiss();
                            return;
                        }
                        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                alertDialog.dismiss();
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    if (data.child("id").getValue().toString().equals(loadProfile())) {
                                        RaidMember member = new RaidMember(data.child("id").getValue().toString(), Integer.parseInt(data.child("grade").getValue().toString()), chkCommander.isChecked(),
                                                Boolean.parseBoolean(data.child("clan").getValue().toString()));
                                        darkReference.child(loadProfile()).setValue(member);
                                        toast("????????? ?????? ???????????? ?????????????????????!");
                                        onResume();
                                        return;
                                    }
                                }
                                toast("????????? ????????? ????????????. ??????????????? ?????????????????????.");
                                return;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });

                builder = new AlertDialog.Builder(getActivity());
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        fabAddIronHorse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.raiddialog, null);

                final TextView txtView = view.findViewById(R.id.txtView);
                final CheckBox chkCommander = view.findViewById(R.id.chkCommander);
                final Button btnCancel = view.findViewById(R.id.btnCancel);
                final Button btnOK = view.findViewById(R.id.btnOK);

                ironhorseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (data.child("id").getValue().toString().equals(loadProfile())) {
                                haveIronHorseMember = true;
                                txtView.setText("?????? ?????? ???????????? ?????????????????????. ");
                                btnOK.setText("?????? ??????");
                                chkCommander.setVisibility(View.GONE);
                                return;
                            }
                        }
                        haveIronHorseMember = false;
                        if (ironhorse >= 8) {
                            txtView.setText("?????? ????????? ????????? ??????????????????. ????????? ??????????????????.");
                            btnOK.setEnabled(false);
                            return;
                        }
                        txtView.setText("?????? ???????????? ?????????????????????????");
                        btnOK.setText("??????");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                ironhorseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (Boolean.parseBoolean(data.child("commander").getValue().toString())) chkCommander.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                chkCommander.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            ironhorseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    boolean haveCommander = false;
                                    for (DataSnapshot data : snapshot.getChildren()) {
                                        if (Boolean.parseBoolean(data.child("commander").getValue().toString())) haveCommander = true;
                                    }
                                    if (haveCommander) {
                                        chkCommander.setChecked(false);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (haveIronHorseMember) {
                            ironhorseReference.child(loadProfile()).removeValue();
                            toast("?????? ????????? ?????? ?????????????????????.");
                            haveDarkMember = false;
                            onResume();
                            alertDialog.dismiss();
                            return;
                        }
                        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                alertDialog.dismiss();
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    if (data.child("id").getValue().toString().equals(loadProfile())) {
                                        RaidMember member = new RaidMember(data.child("id").getValue().toString(), Integer.parseInt(data.child("grade").getValue().toString()), chkCommander.isChecked(),
                                                Boolean.parseBoolean(data.child("clan").getValue().toString()));
                                        ironhorseReference.child(loadProfile()).setValue(member);
                                        toast("?????? ???????????? ?????????????????????!");
                                        onResume();
                                        return;
                                    }
                                }
                                toast("????????? ????????? ????????????. ??????????????? ?????????????????????.");
                                return;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });

                builder = new AlertDialog.Builder(getActivity());
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();
                toast("?????? ?????????????????????.");
            }
        });

        fabReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.answerdialog, null);

                final TextView txtView = view.findViewById(R.id.txtView);
                final Button btnCancel = view.findViewById(R.id.btnCancel);
                final Button btnOK = view.findViewById(R.id.btnOK);

                txtView.setText("???????????? ????????????????????????????");
                btnOK.setText("?????????");

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        darkReference.removeValue();
                        ironhorseReference.removeValue();
                        onResume();
                        toast("?????? ???????????? ?????????????????????.");
                        alertDialog.dismiss();
                    }
                });

                builder = new AlertDialog.Builder(getActivity());
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        return root;
    }

    private void toast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        darkMembers.clear();
        ironhorseMembers.clear();
        darkReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dark = 0;
                for (DataSnapshot data : snapshot.getChildren()) {
                    RaidMember member = new RaidMember(data.child("id").getValue().toString(), Integer.parseInt(data.child("grade").getValue().toString()),
                            Boolean.parseBoolean(data.child("commander").getValue().toString()), Boolean.parseBoolean(data.child("clan").getValue().toString()));
                    darkMembers.add(member);
                    dark++;
                }
                txtDark.setText(Integer.toString(dark));
                darkAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ironhorseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ironhorse = 0;
                for (DataSnapshot data : snapshot.getChildren()) {
                    RaidMember member = new RaidMember(data.child("id").getValue().toString(), Integer.parseInt(data.child("grade").getValue().toString()),
                            Boolean.parseBoolean(data.child("commander").getValue().toString()), Boolean.parseBoolean(data.child("clan").getValue().toString()));
                    ironhorseMembers.add(member);
                    ironhorse++;
                }
                txtIronHorse.setText(Integer.toString(ironhorse));
                ironhorseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.child("id").getValue().toString().equals(loadProfile())) {
                        if (Integer.parseInt(data.child("grade").getValue().toString()) > 1) {
                            fabReset.setVisibility(View.VISIBLE);
                        } else {
                            fabReset.setVisibility(View.GONE);
                        }
                        return;
                    }
                }
                fabReset.setVisibility(View.GONE);
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