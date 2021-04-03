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

public class MissionAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Mission> missions;

    public MissionAdapter(Context context, ArrayList<Mission> missions) {
        this.context = context;
        this.missions = missions;
    }

    @Override
    public int getCount() {
        return missions.size();
    }

    @Override
    public Object getItem(int position) {
        return missions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = View.inflate(context, R.layout.list_mission, null);

        TextView txtTop = convertView.findViewById(R.id.txtTop);
        TextView txtID = convertView.findViewById(R.id.txtID);
        ImageView imgGrade = convertView.findViewById(R.id.imgGrade);
        TextView txtTime = convertView.findViewById(R.id.txtTime);
        View viewLine = convertView.findViewById(R.id.viewLine);

        txtTop.setText(Integer.toString(position+1));
        txtID.setText(missions.get(position).getName());

        switch (position+1) {
            case 1:
                viewLine.setBackgroundResource(R.drawable.firstcustom);
                break;
            case 2:
                viewLine.setBackgroundResource(R.drawable.secondcustom);
                break;
            case 3:
                viewLine.setBackgroundResource(R.drawable.thirdcustom);
                break;
                default:
                    viewLine.setBackgroundResource(R.drawable.othercustom);
        }

        switch (missions.get(position).getGrade()) {
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

        if (missions.get(position).isClan()) imgGrade.setVisibility(View.VISIBLE);
        else imgGrade.setVisibility(View.GONE);

        int hour, min, second;
        second = missions.get(position).getTime();
        hour = second / 3600;
        second %= 3600;
        min = second / 60;
        second %= 60;

        String str = "";
        if (hour != 0) str += hour+"시간 ";
        if (min != 0) str += min+"분 ";
        if (second == 0 && (hour != 0 || min != 0)) str += "";
        else if (second != 0) str += second+"초";
        else str = "0초";

        txtTime.setText(str);

        return convertView;
    }
}
