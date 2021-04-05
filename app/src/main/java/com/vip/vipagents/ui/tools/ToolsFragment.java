package com.vip.vipagents.ui.tools;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.vip.vipagents.R;

public class ToolsFragment extends Fragment {

    private ToolsViewModel toolsViewModel;

    private TextView txtInfo;
    private ListView listEvent;
    private RadioGroup rgEvent;
    private RadioButton[] rdoEvent = new RadioButton[2];

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);

        txtInfo = root.findViewById(R.id.txtInfo);
        listEvent = root.findViewById(R.id.listEvent);
        rgEvent = root.findViewById(R.id.rgEvent);
        for (int i = 0; i < rdoEvent.length; i++) {
            int resource = root.getResources().getIdentifier("rdoEvent"+(i+1), "id", getActivity().getPackageName());
            rdoEvent[i] = root.findViewById(resource);
        }

        rgEvent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdoEvent1:
                        txtInfo.setText("진행 중인 이벤트가 없습니다.");
                        rdoEvent[0].setTextColor(Color.parseColor("#FFFFFF"));
                        rdoEvent[1].setTextColor(Color.parseColor("#000000"));
                        break;
                    case R.id.rdoEvent2:
                        txtInfo.setText("종료된 이벤트가 없습니다.");
                        rdoEvent[1].setTextColor(Color.parseColor("#FFFFFF"));
                        rdoEvent[0].setTextColor(Color.parseColor("#000000"));
                        break;
                }
            }
        });

        return root;
    }


}