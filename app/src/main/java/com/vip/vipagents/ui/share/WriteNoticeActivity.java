package com.vip.vipagents.ui.share;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.vip.vipagents.BaseActivity;
import com.vip.vipagents.R;

public class WriteNoticeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_notice);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("공지사항 글 작성 중");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.write_notice_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            case R.id.action_btn1:
                toast("저장 제작 중...");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toast(String message) {
        Toast.makeText(WriteNoticeActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
