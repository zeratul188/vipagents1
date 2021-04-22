package com.vip.vipagents.ui.slideshow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vip.vipagents.R;

import java.util.ArrayList;

public class PokerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PokerResult> pokers;

    public PokerAdapter(Context context, ArrayList<PokerResult> pokers) {
        this.context = context;
        this.pokers = pokers;
    }

    @Override
    public int getCount() {
        return pokers.size();
    }

    @Override
    public Object getItem(int position) {
        return pokers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = View.inflate(context, R.layout.list_poker, null);

        final TextView txtContent = convertView.findViewById(R.id.txtContent);
        final TextView txtScore = convertView.findViewById(R.id.txtScore);

        txtContent.setText(pokers.get(position).getName());
        txtScore.setText("(+"+Integer.toString(pokers.get(position).getScore())+")");

        return convertView;
    }
}
