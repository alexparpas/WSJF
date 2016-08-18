package com.example.alexparpas.wsjf.fragments;

import android.app.Activity;
import android.content.Intent;
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

import com.example.alexparpas.wsjf.activities.MainActivity;
import com.example.alexparpas.wsjf.model.Job;
import com.example.alexparpas.wsjf.R;
import com.example.alexparpas.wsjf.model.JobLab;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Alex on 06/08/2016.
 */
public class DetailsFragment extends Fragment {
    private static final String ARG_JOB_ID = "job_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;

    private Job mJob;
    private Button mDateButton, mSaveButton;
    private CheckBox mCompletedCheckBox;
    private EditText mTitleField, mDescriptionField;

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
        mTitleField = (EditText) v.findViewById(R.id.job_title);
        mDescriptionField = (EditText) v.findViewById(R.id.job_description);

        mSaveButton = (Button) v.findViewById(R.id.btn_save);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mJob.setJobName(mTitleField.getText().toString().trim());
                mJob.setJobDescription(mDescriptionField.getText().toString().trim());
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        mDateButton = (Button) v.findViewById(R.id.job_date);
        updateDate();

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mJob.getDate());
                dialog.setTargetFragment(DetailsFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mJob.setDate(date);
            updateDate();
        }
    }

    @Override
    public void onResume() {
        updateValues();
        super.onResume();
    }

    private void updateDate() {
        mDateButton.setText(mJob.getDate().toString());
    }

    private void updateValues(){
        mTitleField.setText(mJob.getJobName());
        mDescriptionField.setText(mJob.getJobDescription());
    }
}
