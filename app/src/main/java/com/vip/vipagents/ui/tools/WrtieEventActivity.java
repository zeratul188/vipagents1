package com.vip.vipagents.ui.tools;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vip.vipagents.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class WrtieEventActivity extends AppCompatActivity {
    private EditText edtTitle, edtLimit, edtContent;
    private TextView txtImage;
    private Button btnImage, btnStart, btnEnd;
    private CheckBox chkInfinity;

    private final static int FROM_ALBUM = 1;

    private Bitmap image_bitmap = null;
    private Uri photoURI = null;
    private int number = 0, edit_number = 99999999;
    private boolean isEdit = false;
    private Event event = null;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private AlertDialog.Builder builder = null;
    private AlertDialog alertDialog = null;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrtie_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("이벤트 작성");

        edtTitle = findViewById(R.id.edtTitle);
        edtLimit = findViewById(R.id.edtLimit);
        edtContent = findViewById(R.id.edtContent);
        txtImage = findViewById(R.id.txtImage);
        btnImage = findViewById(R.id.btnImage);
        btnStart = findViewById(R.id.btnStart);
        btnEnd = findViewById(R.id.btnEnd);
        chkInfinity = findViewById(R.id.chkInfinity);

        storage = FirebaseStorage.getInstance("gs://vip-agents.appspot.com");
        storageRef = storage.getReference();
        mDatabase = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        isEdit = intent.getBooleanExtra("isEdit", false);
        if (isEdit) {
            event = (Event)intent.getSerializableExtra("Edit_Event");
            if (event != null) {
                edtTitle.setText(event.getTitle());
                edtContent.setText(event.getContent());
                btnImage.setEnabled(false);
                btnStart.setText(event.getStart());
                btnEnd.setText(event.getEnd());
                if (event.getLimit() >= 10000) {
                    chkInfinity.setChecked(true);
                    edtLimit.setEnabled(false);
                }
                else {
                    chkInfinity.setChecked(false);
                    edtLimit.setText(Integer.toString(event.getLimit()));
                }
                edit_number = event.getNumber();
            }
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일");
            Date time = new Date();
            String now = format.format(time);
            btnStart.setText(now);
            btnEnd.setText(now);
        }

        chkInfinity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtLimit.setText("");
                    edtLimit.setEnabled(false);
                } else edtLimit.setEnabled(true);
            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setType("image/*");
                startActivityForResult(intent, FROM_ALBUM);
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.date_dialog, null);

                final Button btnCancel = view.findViewById(R.id.btnCancel);
                final Button btnOK = view.findViewById(R.id.btnOK);
                final DatePicker datePicker = view.findViewById(R.id.datePicker);

                Calendar calendar = new GregorianCalendar();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int mYear = datePicker.getYear();
                        int mMonth = datePicker.getMonth()+1;
                        int mDay = datePicker.getDayOfMonth();
                        btnStart.setText(mYear+"년 "+mMonth+"월 "+mDay+"일");
                        alertDialog.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                builder = new AlertDialog.Builder(WrtieEventActivity.this);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.date_dialog, null);

                final Button btnCancel = view.findViewById(R.id.btnCancel);
                final Button btnOK = view.findViewById(R.id.btnOK);
                final DatePicker datePicker = view.findViewById(R.id.datePicker);

                Calendar calendar = new GregorianCalendar();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int mYear = datePicker.getYear();
                        int mMonth = datePicker.getMonth()+1;
                        int mDay = datePicker.getDayOfMonth();
                        btnEnd.setText(mYear+"년 "+mMonth+"월 "+mDay+"일");
                        alertDialog.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                builder = new AlertDialog.Builder(WrtieEventActivity.this);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });
    }

    private void uploadFireStorage(String filename) {
        StorageReference ref = storage.getReferenceFromUrl("gs://vip-agents.appspot.com").child("Events/"+filename);
        UploadTask uploadTask = ref.putFile(photoURI);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                toast("사진 업로드에 실패하였습니다.");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                toast("사진 업로드에 성공하였습니다.");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.write_notice_menu, menu);
        return true;
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case FROM_ALBUM:
                if (data.getData() != null) {
                    try {
                        photoURI = data.getData();
                        image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                        txtImage.setText("이미지 업로드 준비를 마쳤습니다.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        View view = getLayoutInflater().inflate(R.layout.answerdialog, null);

        final TextView txtView = view.findViewById(R.id.txtView);
        final Button btnCancel = view.findViewById(R.id.btnCancel);
        final Button btnOK = view.findViewById(R.id.btnOK);

        txtView.setText("작성 중인 내용이 게시되지 않았습니다. 그래도 종료하시겠습니까?");
        btnOK.setText("종료");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                alertDialog.dismiss();
            }
        });

        builder = new AlertDialog.Builder(WrtieEventActivity.this);
        builder.setView(view);

        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                View view = getLayoutInflater().inflate(R.layout.answerdialog, null);

                final TextView txtView = view.findViewById(R.id.txtView);
                final Button btnCancel = view.findViewById(R.id.btnCancel);
                final Button btnOK = view.findViewById(R.id.btnOK);

                txtView.setText("작성 중인 내용이 게시되지 않았습니다. 그래도 종료하시겠습니까?");
                btnOK.setText("종료");

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        alertDialog.dismiss();
                    }
                });

                builder = new AlertDialog.Builder(WrtieEventActivity.this);
                builder.setView(view);

                alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
                return true;
            }
            case R.id.action_btn1:
                if (edtTitle.getText().toString().equals("")) {
                    toast("제목을 입력해야 합니다.");
                    return true;
                } else if (edtContent.getText().toString().equals("")) {
                    toast("내용을 입력해야 합니다.");
                    return true;
                } else if (!chkInfinity.isChecked() && edtLimit.getText().toString().equals("")) {
                    toast("제한 인원 수를 입력해야 합니다.");
                    return true;
                }
                mReference = mDatabase.getReference("Contents/Events");
                mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일");
                        Date time = new Date();

                        if (isEdit) {
                            String title = edtTitle.getText().toString();
                            String content = edtContent.getText().toString();
                            String start_date = btnStart.getText().toString();
                            String end_date = btnEnd.getText().toString();
                            int limit;
                            if (!chkInfinity.isChecked()) limit = Integer.parseInt(edtLimit.getText().toString());
                            else limit = 10000;
                            String date = format.format(time);

                            Map<String, Object> taskMap = new HashMap<String, Object>();
                            taskMap.put("title", title);
                            taskMap.put("content", content);
                            taskMap.put("start", start_date);
                            taskMap.put("end", end_date);
                            taskMap.put("limit", limit);
                            taskMap.put("date", date);

                            mReference.child("ev"+edit_number).updateChildren(taskMap);
                            toast("게시물을 수정하였습니다.");
                            finish();
                            return;
                        }

                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (Integer.parseInt(data.child("number").getValue().toString()) > number) number = Integer.parseInt(data.child("number").getValue().toString());
                        }
                        number++;

                        String title = edtTitle.getText().toString();
                        String content = edtContent.getText().toString();
                        String start_date = btnStart.getText().toString();
                        String end_date = btnEnd.getText().toString();
                        int limit;
                        if (!chkInfinity.isChecked()) limit = Integer.parseInt(edtLimit.getText().toString());
                        else limit = 10000;
                        String date = format.format(time);

                        Event event = new Event(number, title, date, start_date, end_date, content, limit, 0);
                        mReference.child("ev"+number).setValue(event);
                        if (photoURI != null) uploadFireStorage("event"+number);
                        toast("이벤트 게시물을 게시했습니다.");
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
