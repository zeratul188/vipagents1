package com.vip.vipagents.ui.share;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vip.vipagents.R;

import java.util.ArrayList;

public class NoticeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Notice> notices;

    public NoticeAdapter(Context context, ArrayList<Notice> notices) {
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
        if (convertView == null) convertView = View.inflate(context, R.layout.list_notice, null);

        TextView txtTitle = convertView.findViewById(R.id.txtTitle);
        TextView txtWriter = convertView.findViewById(R.id.txtWriter);
        TextView txtDate = convertView.findViewById(R.id.txtDate);
        TextView txtView = convertView.findViewById(R.id.txtView);

        txtTitle.setText(notices.get(position).getTitle());
        txtWriter.setText(notices.get(position).getWriter());
        txtDate.setText(notices.get(position).getDate());
        txtView.setText(Integer.toString(notices.get(position).getView()));

        return convertView;
    }
}
