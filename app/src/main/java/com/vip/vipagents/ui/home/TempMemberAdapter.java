package com.vip.vipagents.ui.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vip.vipagents.R;

import java.util.ArrayList;

public class TempMemberAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Temp_Member> members;

    public TempMemberAdapter(Context context, ArrayList<Temp_Member> members) {
        this.context = context;
        this.members = members;
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int position) {
        return members.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = View.inflate(context, R.layout.list_main_member, null);

        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtGrade = convertView.findViewById(R.id.txtGrade);
        ImageView imgGrade = convertView.findViewById(R.id.imgGrade);

        txtName.setText(members.get(position).getId());
        switch (members.get(position).getGrade()) {
            case 0:
                imgGrade.setImageResource(R.drawable.difficulty1);
                txtGrade.setText("수습 요원");
                break;
            case 1:
                imgGrade.setImageResource(R.drawable.difficulty2);
                txtGrade.setText("요원");
                break;
            case 2:
                imgGrade.setImageResource(R.drawable.difficulty3);
                txtGrade.setText("부관");
                break;
            case 3:
                imgGrade.setImageResource(R.drawable.difficulty4);
                txtGrade.setText("지휘관");
                break;
        }

        if (!members.get(position).isClan()) {
            imgGrade.setVisibility(View.GONE);
            txtGrade.setText("클랜 없음");
        }

        return convertView;
    }
}
