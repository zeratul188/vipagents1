package com.vip.vipagents.ui.gallery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vip.vipagents.Member;
import com.vip.vipagents.R;

import java.util.ArrayList;

public class MemberAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Member> members;

    public MemberAdapter(Context context, ArrayList<Member> members) {
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
        if (convertView == null) convertView = View.inflate(context, R.layout.list_member, null);

        ImageView imgGrade = convertView.findViewById(R.id.imgGrade);
        TextView txtID = convertView.findViewById(R.id.txtID);
        TextView txtGrade = convertView.findViewById(R.id.txtGrade);

        txtID.setText(members.get(position).getId());
        switch (members.get(position).getGrade()) {
            case 0:
                txtGrade.setText("수습 요원");
                imgGrade.setImageResource(R.drawable.diff1);
                break;
            case 1:
                txtGrade.setText("요원");
                imgGrade.setImageResource(R.drawable.diff2);
                break;
            case 2:
                txtGrade.setText("부관");
                imgGrade.setImageResource(R.drawable.diff3);
                break;
            case 3:
                txtGrade.setText("지휘관");
                imgGrade.setImageResource(R.drawable.diff4);
                break;
        }
        if (!members.get(position).isClan()) {
            imgGrade.setVisibility(View.GONE);
            txtGrade.setText("클랜 없음");
        } else imgGrade.setVisibility(View.VISIBLE);

        return convertView;
    }
}
