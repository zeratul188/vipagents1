package com.vip.vipagents.ui.slideshow;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vip.vipagents.R;

import java.util.ArrayList;

public class TowerMemberAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MemberTower> members;
    private String myName;

    public TowerMemberAdapter(Context context, ArrayList<MemberTower> members, String myName) {
        this.context = context;
        this.members = members;
        this.myName = myName;
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
        if (convertView == null) convertView = View.inflate(context, R.layout.list_tower_members, null);

        ImageView imgGrade = convertView.findViewById(R.id.imgGrade);
        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtDemage = convertView.findViewById(R.id.txtDemage);
        TextView txtLegend = convertView.findViewById(R.id.txtLegend);
        TextView txtElite = convertView.findViewById(R.id.txtElite);
        TextView txtEpic = convertView.findViewById(R.id.txtEpic);
        TextView txtRare = convertView.findViewById(R.id.txtRare);
        TextView txtNormal = convertView.findViewById(R.id.txtNormal);

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

        txtName.setText(members.get(position).getName());
        if (members.get(position).getName().equals(myName)) txtName.setTextColor(Color.parseColor("#FE6E0E"));
        else txtName.setTextColor(Color.parseColor("#000000"));
        txtLegend.setText(Integer.toString(members.get(position).getLegend()));
        txtElite.setText(Integer.toString(members.get(position).getElite()));
        txtEpic.setText(Integer.toString(members.get(position).getEpic()));
        txtRare.setText(Integer.toString(members.get(position).getRare()));
        txtNormal.setText(Integer.toString(members.get(position).getNormal()));
        txtDemage.setText(Integer.toString(members.get(position).getDemage()));

        return convertView;
    }
}
