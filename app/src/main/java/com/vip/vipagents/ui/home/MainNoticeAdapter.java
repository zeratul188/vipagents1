package com.vip.vipagents.ui.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vip.vipagents.R;
import com.vip.vipagents.ui.share.Notice;

import java.util.ArrayList;

public class MainNoticeAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Notice> notices;

    public MainNoticeAdapter(Context context, ArrayList<Notice> notices) {
        this.context = context;
        this.notices = notices;
    }

    @Override
    public int getCount() {
        return notices.size();
    }

    @Override
    public Object getItem(int position) {
        return notices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = View.inflate(context, R.layout.list_main_notice, null);

        TextView txtTitle = convertView.findViewById(R.id.txtTitle);
        TextView txtDate = convertView.findViewById(R.id.txtDate);

        txtTitle.setText(notices.get(position).getTitle());
        txtDate.setText(notices.get(position).getDate());

        return convertView;
    }
}
