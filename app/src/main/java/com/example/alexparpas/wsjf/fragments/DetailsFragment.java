package com.example.alexparpas.wsjf.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.alexparpas.wsjf.model.Job;
import com.example.alexparpas.wsjf.R;
import com.example.alexparpas.wsjf.model.JobLab;

import java.util.UUID;

/**
 * Created by Alex on 06/08/2016.
 */
public class DetailsFragment extends Fragment {
    private static final String ARG_JOB_ID = "job_id";
    private static final String DIALOG_START_DATE = "DialogStartDate";
    private static final String DIALOG_END_DATE = "DialogEndDate";

    private static final int REQUEST_DATE = 0;

    private Job mJob;
    private Button mStartDateButton, mEndDateButton;
    private CheckBox mCompletedCheckBox;

    public static DetailsFragment newInstance(UUID jobId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_JOB_ID, jobId);

        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID jobId = (UUID) getArguments().getSerializable(ARG_JOB_ID);
        mJob = JobLab.get(getActivity()).getJob(jobId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details, container, false);
        EditText mTitleField = (EditText) v.findViewById(R.id.job_title);
        mTitleField.setText(mJob.getJobName());

        mStartDateButton = (Button) v.findViewById(R.id.job_start_date);
        mStartDateButton.setText(mJob.getStartDate().toString());
        mStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDateDialog(DIALOG_START_DATE, mJob.getStartDate());
            }
        });

        mEndDateButton = (Button) v.findViewById(R.id.job_end_date);
        mEndDateButton.setText(mJob.getEndDate().toString());
        mEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDateDialog(DIALOG_END_DATE, mJob.getEndDate());
            }
        });

        mCompletedCheckBox = (CheckBox) v.findViewById(R.id.job_completed);
        mCompletedCheckBox.setChecked(mJob.isCompleted());
        mCompletedCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mJob.setCompleted(isChecked);
                    }
                }

        );
        return v;
    }

    public void createDateDialog(String dateType, java.util.Date date) {
        FragmentManager manager = getFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(date);
        dialog.setTargetFragment(DetailsFragment.this, REQUEST_DATE);
        dialog.show(manager, dateType);
    }
}
