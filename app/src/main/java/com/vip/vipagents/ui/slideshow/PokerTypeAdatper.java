package com.vip.vipagents.ui.slideshow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vip.vipagents.R;

import java.util.ArrayList;

public class PokerTypeAdatper extends BaseAdapter {
    private Context context;
    private ArrayList<PokerType> pokers;

    public PokerTypeAdatper(Context context, ArrayList<PokerType> pokers) {
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
        if (convertView == null) convertView = View.inflate(context, R.layout.list_poker_type, null);

        final TextView txtContent = convertView.findViewById(R.id.txtContent);
        final TextView txtCount = convertView.findViewById(R.id.txtCount);

        txtContent.setText(pokers.get(position).getContent());
        txtCount.setText(Integer.toString(pokers.get(position).getCount()));

        return convertView;
    }
}
