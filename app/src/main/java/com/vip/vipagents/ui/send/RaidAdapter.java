package com.vip.vipagents.ui.send;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vip.vipagents.R;

import java.util.ArrayList;

public class RaidAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<RaidMember> members;

    public RaidAdapter(Context context, ArrayList<RaidMember> members) {
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
        if (convertView == null) convertView = View.inflate(context, R.layout.list_raid, null);

        ImageView imgGrade = convertView.findViewById(R.id.imgGrade);
        TextView txtID = convertView.findViewById(R.id.txtID);
        LinearLayout layoutCommander = convertView.findViewById(R.id.layoutCommander);

        txtID.setText(members.get(position).getId());
        if (members.get(position).isCommander()) layoutCommander.setVisibility(View.VISIBLE);
        else layoutCommander.setVisibility(View.GONE);
        switch (members.get(position).getGrade()) {
            case 0:
                imgGrade.setImageResource(R.drawable.diff1);
                break;
            case 1:
                imgGrade.setImageResource(R.drawable.diff2);
                break;
            case 2:
                imgGrade.setImageResource(R.drawable.diff3);
                break;
            case 3:
                imgGrade.setImageResource(R.drawable.diff4);
                break;
        }
        if (!members.get(position).isClan()) imgGrade.setVisibility(View.GONE);
        else imgGrade.setVisibility(View.VISIBLE);

        return convertView;
    }
}
