package com.vip.vipagents.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.vip.vipagents.R;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;

    private LinearLayout layoutTimeAttack, layoutPoker, layoutRandomTower, layoutAbilityStone;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        layoutTimeAttack = root.findViewById(R.id.layoutTimeAttack);
        layoutPoker = root.findViewById(R.id.layoutPoker);
        layoutRandomTower = root.findViewById(R.id.layoutRandomTower);
        layoutAbilityStone = root.findViewById(R.id.layoutAbilityStone);

        layoutTimeAttack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MissionActivity.class);
                startActivity(intent);
            }
        });

        layoutRandomTower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RandomTowerActivity.class);
                startActivity(intent);
            }
        });

        layoutPoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PokerActivity.class);
                startActivity(intent);
            }
        });

        layoutAbilityStone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StoneActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}