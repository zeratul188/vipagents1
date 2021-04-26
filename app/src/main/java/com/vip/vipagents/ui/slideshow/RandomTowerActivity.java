package com.vip.vipagents.ui.slideshow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vip.vipagents.CharactorLevel;
import com.vip.vipagents.R;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RandomTowerActivity extends AppCompatActivity {
    private TextView txtMoney, txtLegend, txtElite, txtEpic, txtRare, txtNormal, txtDemage, txtLevel, txtLevelValue;
    private ProgressBar progressLevel;
    private Button btnReset, btnAdd;
    private CheckBox chkEdit;
    private ImageView[][] btnBox = new ImageView[6][5];

    private Tower[][] towers = new Tower[6][5];
    private int money = 0, exp = 0;
    private String now_date;
    private boolean isDeveloper = false;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference, memberRef;
    private SimpleDateFormat format = null;
    private Date time = null;
    private AlertDialog.Builder builder = null;
    private AlertDialog alertDialog = null;
    private CharactorLevel charactorLevel = null;

    private int[] imageResource = {R.drawable.blacktuskcustom, R.drawable.hyenascustom, R.drawable.truesonscustom, R.drawable.rikercustom, R.drawable.cleanerscustom, R.drawable.outcastcustom, R.drawable.firebirdcustom, R.drawable.darkzonecustom};
    private Drawable[] drawables = new Drawable[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_tower);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("랜덤 타워");

        format = new SimpleDateFormat("yyyy년 MM월 dd일");
        time = new Date();
        now_date = format.format(time);

        for (int i = 0; i < drawables.length; i++) {
            drawables[i] = getResources().getDrawable(imageResource[i]);
        }

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Members/"+loadProfile()+"/Tower");
        memberRef = mDatabase.getReference("Members/"+loadProfile());
        charactorLevel = new CharactorLevel(RandomTowerActivity.this, loadProfile());


        txtMoney = findViewById(R.id.txtMoney);
        txtLegend = findViewById(R.id.txtLegend);
        txtElite = findViewById(R.id.txtElite);
        txtEpic = findViewById(R.id.txtEpic);
        txtRare = findViewById(R.id.txtRare);
        txtNormal = findViewById(R.id.txtNormal);
        btnReset = findViewById(R.id.btnReset);
        chkEdit = findViewById(R.id.chkEdit);
        btnAdd = findViewById(R.id.btnAdd);
        txtDemage = findViewById(R.id.txtDemage);
        txtLevel = findViewById(R.id.txtLevel);
        txtLevelValue = findViewById(R.id.txtLevelValue);
        progressLevel = findViewById(R.id.progressLevel);

        progressLevel.setMax(200);
        uploadExp(0);

        txtMoney.setText(Integer.toString(money));

        if (!checkLogin()) {
            toast("로그인 후 이용해주시기 바랍니다.");
            btnReset.setEnabled(false);
            btnAdd.setEnabled(false);
            chkEdit.setEnabled(false);
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkLogin()) {
                    toast("로그인 후 이용해주십시오.");
                    return;
                }
                money += 300;
                Map<String, Object> taskMap = new HashMap<String, Object>();
                taskMap.put("money", money);
                mReference.updateChildren(taskMap);
                txtMoney.setText(Integer.toString(money));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.answerdialog, null);

                final TextView txtView = view.findViewById(R.id.txtView);
                final Button btnOK = view.findViewById(R.id.btnOK);
                final Button btnCancel = view.findViewById(R.id.btnCancel);

                txtView.setText("랜덤 타워들이 모두 삭제되고 골드는 500으로 초기화됩니다. 초기화시 경험치 100을 소모합니다. 경험치가 100 이하일 경우는 0으로 초기화됩니다.");
                btnOK.setText("초기화");

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String now = format.format(time);
                        money = 500;
                        txtMoney.setText(Integer.toString(money));
                        Map<String, Object> taskMap = new HashMap<String, Object>();
                        taskMap.put("date", now);
                        taskMap.put("money", money);
                        mReference.updateChildren(taskMap);
                        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot data : snapshot.getChildren()) {
                                    if (data.getKey().equals("date") || data.getKey().equals("money")) continue;
                                    for (int i = 0; i < btnBox.length; i++) {
                                        for (int j = 0; j < btnBox[i].length; j++) {
                                            if (data.getKey().equals(Integer.toString(i)+Integer.toString(j))) {
                                                mReference.child(Integer.toString(i)+Integer.toString(j)).removeValue();
                                            }
                                        }
                                    }
                                }
                                uploadExp(-100);
                                loadData();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        alertDialog.dismiss();
                    }
                });
                builder = new AlertDialog.Builder(RandomTowerActivity.this);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        for (int i = 0; i < btnBox.length; i++) {
            for (int j = 0; j < btnBox[i].length; j++) {
                towers[i][j] = new Tower(i, j);
                int resource = getResources().getIdentifier("btnBox"+(i+1)+(j+1), "id", getPackageName());
                btnBox[i][j] = findViewById(resource);
                final int x = i;
                final int y = j;
                btnBox[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (chkEdit.isChecked()) {
                            if (btnBox[x][y].getDrawable() == null) return;
                            sell(x, y);
                            return;
                        }
                        if (btnBox[x][y].getDrawable() == null) {
                            if (money < 100) {
                                toast("골드가 부족합니다.");
                                return;
                            }
                            uploadExp(5);
                            createTower(x, y);
                        } else {
                            for (int i = 0; i < btnBox.length; i++) {
                                for (int j = 0; j < btnBox[i].length; j++) {
                                    if (i == x && j == y) continue;
                                    if (towers[i][j].getGrade() == towers[x][y].getGrade() && towers[i][j].getType() == towers[x][y].getType() && towers[x][y].getGrade() != 5) {
                                        towers[x][y].levelUp();
                                        towers[i][j].reset();
                                        mReference.child(Integer.toString(i)+Integer.toString(j)).removeValue();
                                        Map<String, Object> taskMap = new HashMap<String, Object>();
                                        taskMap.put("grade", towers[x][y].getGrade());
                                        taskMap.put("type", towers[x][y].getType());
                                        mReference.child(Integer.toString(x)+Integer.toString(y)).updateChildren(taskMap);
                                        int up = 0;
                                        switch (towers[x][y].getGrade()) {
                                            case 2:
                                                up = 20;
                                                break;
                                            case 3:
                                                up = 40;
                                                break;
                                            case 4:
                                                up = 80;
                                                break;
                                            case 5:
                                                up = 160;
                                                break;
                                            default:
                                                up = 10;
                                        }
                                        uploadExp(up);
                                        loadData();
                                        return;
                                    }
                                }
                            }
                            toast("동일한 타워가 존재하지 않습니다.");
                            return;
                        }
                    }
                });
            }
        }

        if (checkLogin()) {
            createMoney();
            createDate();
            loadData();
        } else {
            for (int i = 0; i < btnBox.length; i++) {
                for (int j = 0; j < btnBox[i].length; j++) {
                    btnBox[i][j].setEnabled(false);
                }
            }
            toast("로그인 후 이용해주세요.");
        }
    }

    private void uploadExp(final int update) {
        memberRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getKey().equals("exp")) {
                        exp = Integer.parseInt(data.getValue().toString());
                    }
                }
                int undo_level = exp/200;
                exp += update;
                if (exp < 0) exp = 0;
                txtLevel.setText(Integer.toString((exp/200)+1));
                txtLevelValue.setText("("+Integer.toString(exp)+"/"+Integer.toString(((exp/200)+1)*200)+")");
                progressLevel.setProgress(exp%200);
                Map<String, Object> taskMap = new HashMap<String, Object>();
                taskMap.put("exp", exp);
                memberRef.updateChildren(taskMap);
                if (undo_level < exp/200) toast("레벨업 하였습니다. Lv."+(undo_level+1)+" -> Lv."+(exp/200+1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createTower(final int x, final int y) {
        money -= 100;
        final int type = (int)(Math.random()*123456)%8+1;
        towers[x][y].setGrade(1);
        towers[x][y].setType(type);
        mReference.child(Integer.toString(x)+Integer.toString(y)).setValue(towers[x][y]);
        Map<String, Object> taskMap = new HashMap<String, Object>();
        taskMap.put("money", money);
        mReference.updateChildren(taskMap);
        loadData();
    }

    private void sell(final int x, final int y) {
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int grade = 0;
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getKey().equals(Integer.toString(x)+Integer.toString(y))) {
                        for (DataSnapshot data2 : data.getChildren()) {
                            if (data2.getKey().equals("grade")) grade = Integer.parseInt(data2.getValue().toString());
                            break;
                        }
                        break;
                    }
                }
                switch (grade) {
                    case 1:
                        money += 50;
                        uploadExp(2);
                        break;
                    case 2:
                        money += 100;
                        uploadExp(8);
                        break;
                    case 3:
                        money += 200;
                        uploadExp(32);
                        break;
                    case 4:
                        money += 400;
                        uploadExp(128);
                        break;
                    case 5:
                        money += 800;
                        uploadExp(512);
                        break;
                }
                if (grade != 0) {
                    Map<String, Object> taskMap = new HashMap<String, Object>();
                    taskMap.put("money", money);
                    mReference.updateChildren(taskMap);
                    mReference.child(Integer.toString(x)+Integer.toString(y)).removeValue();
                    loadData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createMoney() {
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean have = false;
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getKey().equals("money")) have = true;
                }
                if (!have) mReference.child("money").setValue(0);
                else {
                    money = Integer.parseInt(snapshot.child("money").getValue().toString());
                    txtMoney.setText(Integer.toString(money));
                    /*for (DataSnapshot data : snapshot.getChildren()) {
                        money = Integer.parseInt(data.child("money").getValue().toString());
                        txtMoney.setText(Integer.toString(money));
                    }*/
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
                int normal = 0, rare = 0, epic = 0, elite = 0, legend = 0;
                int bonus = 0;
                for (int i = 0; i < btnBox.length; i++) {
                    for (int j = 0; j < btnBox[i].length; j++) {
                        towers[i][j].reset();
                    }
                }
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getKey().equals("money") || data.getKey().equals("date")) continue;
                    int x = 0;
                    int y = 0;
                    int grade = 0;
                    int type = 0;
                    for (DataSnapshot data2 : data.getChildren()) {
                        if (data2.getKey().equals("x")) x = Integer.parseInt(data2.getValue().toString());
                        else if (data2.getKey().equals("y")) y = Integer.parseInt(data2.getValue().toString());
                        else if (data2.getKey().equals("grade")) grade = Integer.parseInt(data2.getValue().toString());
                        else if (data2.getKey().equals("type")) type = Integer.parseInt(data2.getValue().toString());
                    }
                    towers[x][y].setGrade(grade);
                    towers[x][y].setType(type);
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
                int left = (int)(getResources().getDimension(R.dimen.image_left_top));
                int top = (int)(getResources().getDimension(R.dimen.image_left_top));
                int right = (int)(getResources().getDimension(R.dimen.image_right));
                int bottom = (int)(getResources().getDimension(R.dimen.image_bottom));
                for (int i = 0; i < btnBox.length; i++) {
                    for (int j = 0; j < btnBox[i].length; j++) {
                        switch (towers[i][j].getGrade()) {
                            case 0:
                                btnBox[i][j].setBackgroundResource(R.drawable.towercustom);
                                btnBox[i][j].setImageDrawable(null);
                                break;
                            case 1:
                                btnBox[i][j].setBackgroundResource(R.drawable.normaltower);
                                btnBox[i][j].setImageDrawable(drawables[towers[i][j].getType()-1]);
                                break;
                            case 2:
                                btnBox[i][j].setBackgroundResource(R.drawable.raretower);
                                btnBox[i][j].setImageDrawable(drawables[towers[i][j].getType()-1]);
                                break;
                            case 3:
                                btnBox[i][j].setBackgroundResource(R.drawable.epictower);
                                btnBox[i][j].setImageDrawable(drawables[towers[i][j].getType()-1]);
                                break;
                            case 4:
                                btnBox[i][j].setBackgroundResource(R.drawable.elitetower);
                                btnBox[i][j].setImageDrawable(drawables[towers[i][j].getType()-1]);
                                break;
                            case 5:
                                btnBox[i][j].setBackgroundResource(R.drawable.legendtower);
                                btnBox[i][j].setImageDrawable(drawables[towers[i][j].getType()-1]);
                                break;
                        }
                        btnBox[i][j].setPadding(left, top, right, bottom);
                    }
                }
                txtNormal.setText(Integer.toString(normal));
                txtRare.setText(Integer.toString(rare));
                txtEpic.setText(Integer.toString(epic));
                txtElite.setText(Integer.toString(elite));
                txtLegend.setText(Integer.toString(legend));
                txtMoney.setText(Integer.toString(money));

                int result = (normal*75) + (rare*225) + (epic*675) + (elite*2025) + (legend*6075);
                result += bonus;
                txtDemage.setText(Integer.toString(result));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createDate() {
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean have = false;
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.getKey().equals("date")) have = true;
                }
                if (!have) {
                    String now = format.format(time);
                    mReference.child("date").setValue(now);
                } else {
                    if (!snapshot.child("date").getValue().toString().equals(now_date)) {
                        money += 300;
                        String now = format.format(time);
                        Map<String, Object> taskMap = new HashMap<String, Object>();
                        taskMap.put("money", money);
                        taskMap.put("date", now);
                        mReference.updateChildren(taskMap);
                        txtMoney.setText(Integer.toString(money));
                        toast("오늘 처음 접속하여 300 골드를 획득하셨습니다.");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean checkLogin() {
        if (loadProfile().equals("null") || loadProfile() == null) return false;
        else return true;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tower, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Button btnOK = null;
        View view = null;
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
            case R.id.action_developer:
                if (isDeveloper) {
                    isDeveloper = false;
                    btnAdd.setVisibility(View.GONE);
                    toast("테스트 모드를 비활성화하였습니다.");
                    return true;
                }

                view = getLayoutInflater().inflate(R.layout.editdialog, null);

                final TextView txtView = view.findViewById(R.id.txtView);
                final EditText edtText = view.findViewById(R.id.edtText);
                btnOK = view.findViewById(R.id.btnOK);
                final Button btnCancel = view.findViewById(R.id.btnCancel);

                txtView.setText("관리자 비밀번호를 입력하십시오.");
                edtText.setHint("관리자 비밀번호");
                edtText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                edtText.setTransformationMethod(PasswordTransformationMethod.getInstance());

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtText.getText().toString().equals("")) {
                            toast("비밀번호를 입력하십시오.");
                            return;
                        } else if (edtText.getText().toString().equals("wv2155")) {
                            isDeveloper = true;
                            btnAdd.setVisibility(View.VISIBLE);
                            toast("테스트 모드를 활성화하였습니다.");
                            alertDialog.dismiss();
                        } else toast("비밀번호가 일치하지 않습니다.");
                    }
                });

                builder = new AlertDialog.Builder(RandomTowerActivity.this);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
                return true;
            case R.id.action_tower:
                view = getLayoutInflater().inflate(R.layout.towermembersdialog, null);

                final ListView listView = view.findViewById(R.id.listView);
                btnOK = view.findViewById(R.id.btnOK);

                final ArrayList<MemberTower> towers = new ArrayList<MemberTower>();
                final TowerMemberAdapter tma = new TowerMemberAdapter(RandomTowerActivity.this, towers, loadProfile());
                listView.setAdapter(tma);

                DatabaseReference memberRef = mDatabase.getReference("Members");
                memberRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            int normal = 0, rare = 0, epic = 0, elite = 0, legend = 0, bonus = 0;
                            for (DataSnapshot data2 : data.child("Tower").getChildren()) {
                                if (data2.getKey().equals("date") || data2.getKey().equals("money")) continue;
                                int type = 0, grade = 0;
                                for (DataSnapshot data3 : data2.getChildren()) {
                                    if (data3.getKey().equals("grade")) {
                                        switch (Integer.parseInt(data3.getValue().toString())) {
                                            case 1:
                                                normal++;
                                                grade = 1;
                                                break;
                                            case 2:
                                                rare++;
                                                grade = 2;
                                                break;
                                            case 3:
                                                epic++;
                                                grade = 3;
                                                break;
                                            case 4:
                                                elite++;
                                                grade = 4;
                                                break;
                                            case 5:
                                                legend++;
                                                grade = 5;
                                                break;
                                        }
                                    } else if (data3.getKey().equals("type")) {
                                        type = Integer.parseInt(data3.getValue().toString());
                                    }
                                }
                                if (type == 7) {
                                    switch (grade) {
                                        case 1:
                                            bonus += 23;
                                            break;
                                        case 2:
                                            bonus += 68;
                                            break;
                                        case 3:
                                            bonus += 203;
                                            break;
                                        case 4:
                                            bonus += 608;
                                            break;
                                        case 5:
                                            bonus += 1823;
                                            break;
                                    }
                                } else if (type == 8) {
                                    switch (grade) {
                                        case 1:
                                            bonus += 8;
                                            break;
                                        case 2:
                                            bonus += 23;
                                            break;
                                        case 3:
                                            bonus += 68;
                                            break;
                                        case 4:
                                            bonus += 203;
                                            break;
                                        case 5:
                                            bonus += 608;
                                            break;
                                    }
                                }
                            }
                            if (normal != 0 || rare != 0 || epic != 0 || elite != 0 || legend != 0) {
                                MemberTower mt = new MemberTower(data.child("id").getValue().toString(), Integer.parseInt(data.child("grade").getValue().toString()), normal, rare, epic, elite, legend);
                                mt.addBonus(bonus);
                                towers.add(mt);
                            }
                        }
                        Collections.sort(towers);
                        tma.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                builder = new AlertDialog.Builder(RandomTowerActivity.this);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toast(String message) {
        Toast.makeText(RandomTowerActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
