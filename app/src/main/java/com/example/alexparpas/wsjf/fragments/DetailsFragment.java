package com.example.alexparpas.wsjf.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.alexparpas.wsjf.model.Job;
import com.example.alexparpas.wsjf.R;

import java.util.ArrayList;

/**
 * Created by Alex on 06/08/2016.
 */
public class DetailsFragment extends Fragment {
    private Job mJob;
    private Button mStartDateButton;
    private Button mEndDateButton;
    private CheckBox mCompletedCheckBox;

    public DetailsFragment() {
        mJob = new Job();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details, container, false);
        mStartDateButton = (Button) v.findViewById(R.id.job_start_date);
        mStartDateButton.setText(mJob.getStartDate().toString());
        mStartDateButton.setEnabled(false);

        mEndDateButton = (Button) v.findViewById(R.id.job_end_date);
        mEndDateButton.setText(mJob.getEndDate().toString());
        mEndDateButton.setEnabled(false);

        mCompletedCheckBox = (CheckBox) v.findViewById(R.id.job_completed);
        mCompletedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mJob.setCompleted(isChecked);
            }
        });
        return v;
    }
}
