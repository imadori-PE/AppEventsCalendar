package com.example.appeventscalendar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


public class ResultNewEventFragment extends Fragment {

    TextView txtResultEvent;

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
        } else {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_left);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_result_new_event, container, false);
        txtResultEvent=rootView.findViewById(R.id.txtResultEvent);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String data = bundle.getString("DATA");
            txtResultEvent.setText(data);
        }
        return rootView;

    }
}