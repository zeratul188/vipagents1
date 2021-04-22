package com.vip.vipagents.ui.slideshow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vip.vipagents.R;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PokerActivity extends AppCompatActivity {
    private TextView txtDemage, txtTicket;
    private Button btnAdd, btnOK, btnReset;
    private TextView[] txtCard = new TextView[5];
    private ImageView[] imgChange = new ImageView[5];
    private ListView listView;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private PokerSet pokerSet;
    private SimpleDateFormat format = null;
    private Date time = null;
    private ArrayList<PokerResult> pokerResults;
    private PokerAdapter pokerAdapter;

    private int ticket = 0, number = 0, demage = 0;
    private boolean isStart = false;
    private Poker[] pokers = new Poker[5];

    private String[] contents = {"7", "8", "9", "10", "J", "Q", "K", "A"};
    private String[] backgrounds = {"RA", "RB", "BA", "BB"};

    private int[] backgroundResource = {R.drawable.nulltype, R.drawable.redtype_a, R.drawable.redtype_b, R.drawable.blacktype_a, R.drawable.blacktype_b};
    private Drawable[] drawables = new Drawable[backgroundResource.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("포커");

        format = new SimpleDateFormat("yyyy년 MM월 dd일");
        time = new Date();

        txtDemage = findViewById(R.id.txtDemage);
        txtTicket = findViewById(R.id.txtTicket);
        btnAdd = findViewById(R.id.btnAdd);
        btnOK = findViewById(R.id.btnOK);
        btnReset = findViewById(R.id.btnReset);
        listView = findViewById(R.id.listView);

        int resource;
        for (int i = 0; i < txtCard.length; i++) {
            resource = getResources().getIdentifier("txtCard"+(i+1), "id", getPackageName());
            txtCard[i] = findViewById(resource);
            resource = getResources().getIdentifier("imgChange"+(i+1), "id", getPackageName());
            imgChange[i] = findViewById(resource);

            final int index = i;
            imgChange[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isStart) {
                        pokers[index] = new Poker(contents[random(contents.length, 0)], backgrounds[random(backgrounds.length, 0)]);
                        uploadPoker(pokers[index], index);
                        imgChange[index].setVisibility(View.INVISIBLE);
                    }
                }
            });
        }

        for (int i = 0; i < drawables.length; i++) {
            drawables[i] = getResources().getDrawable(backgroundResource[i]);
        }

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Members/"+loadProfile()+"/Poker");

        pokerSet = new PokerSet();

        pokerResults = new ArrayList<PokerResult>();
        pokerAdapter = new PokerAdapter(PokerActivity.this, pokerResults);
        listView.setAdapter(pokerAdapter);

        createProfile();
        loadData();

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStart) {
                    btnOK.setText("티켓 사용하기");
                    isStart = false;
                    for (int i = 0; i < imgChange.length; i++) {
                        imgChange[i].setVisibility(View.INVISIBLE);
                        txtCard[i].setText("-");
                        txtCard[i].setBackgroundDrawable(drawables[0]);
                    }
                    PokerResult pokerResult = pokerSet.checkOption(pokers);
                    toast(pokerResult.getName()+"("+pokerResult.getScore()+")");
                    uploadPokerResult(pokerResult);
                    for (int i = 0; i < pokers.length; i++) pokers[i] = null;
                    loadData();
                } else {
                    if (ticket < 1) {
                        toast("티켓이 부족합니다.");
                        return;
                    } else {
                        ticket--;
                        Map<String, Object> taskMap = new HashMap<String, Object>();
                        taskMap.put("ticket", ticket);
                        mReference.updateChildren(taskMap);
                        txtTicket.setText(Integer.toString(ticket));
                    }
                    btnOK.setText("결정하기");
                    isStart = true;
                    for (int i = 0; i < imgChange.length; i++) {
                        imgChange[i].setVisibility(View.VISIBLE);
                    }
                    for (int i = 0; i < pokers.length; i++) {
                        pokers[i] = new Poker(contents[random(contents.length, 0)], backgrounds[random(backgrounds.length, 0)]);
                        uploadPoker(pokers[i], i);
                    }
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticket = 2;
                Map<String, Object> taskMap = new HashMap<String, Object>();
                taskMap.put("date", format.format(time));
                taskMap.put("pokers", 0);
                taskMap.put("ticket", 2);
                mReference.updateChildren(taskMap);
                mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (data.getKey().equals("date") || data.getKey().equals("pokers") || data.getKey().equals("ticket")) continue;
                            mReference.child(data.getKey()).removeValue();
                        }
                        toast("초기화하였습니다.");
                        loadData();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticket++;
                Map<String, Object> taskMap = new HashMap<String, Object>();
                taskMap.put("ticket", ticket);
                mReference.updateChildren(taskMap);
                txtTicket.setText(Integer.toString(ticket));
            }
        });
    }

    private void uploadPokerResult(PokerResult pokerResult) {
        number++;
        pokerResult.setNumber(number);
        Map<String, Object> taskMap = new HashMap<String, Object>();
        taskMap.put("pokers", number);
        mReference.updateChildren(taskMap);
        mReference.child(Integer.toString(number)).setValue(pokerResult);
    }

    private int random(int length, int min) {
        return (int)(Math.random()*123456)%length + min;
    }

    private void uploadPoker(Poker poker, int position) {
        txtCard[position].setText(poker.getContent());

        int back = 0;
        switch (poker.getBackground()) {
            case "RA":
                back = 1;
                break;
            case "RB":
                back = 2;
                break;
            case "BA":
                back = 3;
                break;
            case "BB":
                back = 4;
                break;
            default:
                back = 0;
        }
        txtCard[position].setBackgroundDrawable(drawables[back]);
    }

    private void createProfile() {
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isHave = false;
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getKey().equals("date")) {
                        isHave = true;
                    }
                }
                if (!isHave) {
                    mReference.child("ticket").setValue(2);
                    mReference.child("date").setValue(format.format(time));
                    mReference.child("pokers").setValue(0);
                    ticket = 2;
                    txtTicket.setText(Integer.toString(ticket));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadData() {
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getKey().equals("ticket")) {
                        ticket = Integer.parseInt(data.getValue().toString());
                        txtTicket.setText(Integer.toString(ticket));
                    } else if (data.getKey().equals("pokers")) {
                        number = Integer.parseInt(data.getValue().toString());
                    }
                }
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getKey().equals("date")) {
                        if (!format.format(time).equals(data.getValue().toString())) {
                            ticket++;
                            toast("오늘 처음 접속하여 티켓을 하나 지급됩니다.");
                            Map<String, Object> taskMap = new HashMap<String, Object>();
                            taskMap.put("ticket", ticket);
                            taskMap.put("date", format.format(time));
                            mReference.updateChildren(taskMap);
                            txtTicket.setText(Integer.toString(ticket));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        pokerResults.clear();
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                demage = 0;
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
                txtDemage.setText(Integer.toString(demage));
                pokerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private String loadProfile() {
        FileInputStream fis = null;
        try {
            fis = openFileInput("id.txt");
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
        return "null";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void toast(String message) {
        Toast.makeText(PokerActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
