package com.example.alexparpas.wsjf.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.alexparpas.wsjf.activities.JobPagerActivity;
import com.example.alexparpas.wsjf.activities.MainActivity;
import com.example.alexparpas.wsjf.model.Job;
import com.example.alexparpas.wsjf.R;
import com.example.alexparpas.wsjf.model.JobLab;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Alex on 06/08/2016.
 */
public class DetailsFragment extends Fragment implements NumberPicker.OnValueChangeListener {
    private static final String ARG_JOB_ID = "job_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final String NP_USER_VALUE = "UserValue";
    private static final String NP_TIME_VALUE = "TimeValue";
    private static final String NP_RROE_VALUE = "RroeValue";
    private static final String NP_JOBSIZE_VALUE = "JobSizeValue";


    private static final int REQUEST_DATE = 0;

    private Job mJob;
    private Button mDateButton, mSaveButton,
            mUserValueButton, mTimeValueButton, mRroeValueButton, mJobSizeButton;
    private CheckBox mCompletedCheckBox;
    private EditText mTitleField, mDescriptionField;
    private TextView mUserValueField, mTimeValueField, mRroeValueField, mJobSizeField;

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

        //Title section
        mTitleField = (EditText) v.findViewById(R.id.job_title);

        //Description section
        mDescriptionField = (EditText) v.findViewById(R.id.job_description);

        //Values section
        mUserValueButton = (Button) v.findViewById(R.id.btn_user_value);
        mTimeValueButton = (Button) v.findViewById(R.id.btn_time_value);
        mRroeValueButton = (Button) v.findViewById(R.id.btn_rroe_value);
        mJobSizeButton = (Button) v.findViewById(R.id.btn_job_size);

        mUserValueField = (TextView) v.findViewById(R.id.tf_user_value);
        mTimeValueField = (TextView) v.findViewById(R.id.tf_time_value);
        mRroeValueField = (TextView) v.findViewById(R.id.tf_rroe_value);
        mJobSizeField = (TextView) v.findViewById(R.id.tf_job_size);

        mUserValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(NP_USER_VALUE);
            }
        });

        mTimeValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(NP_TIME_VALUE);
            }
        });

        mRroeValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(NP_RROE_VALUE);
            }
        });

        mJobSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(NP_JOBSIZE_VALUE);
            }
        });

        //Details section
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

        //Save button
        mSaveButton = (Button) v.findViewById(R.id.btn_save);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mJob.setJobName(mTitleField.getText().toString().trim());
                mJob.setJobDescription(mDescriptionField.getText().toString().trim());
                mJob.calculateWSJF();

                System.out.println("WSJF value on DetailsFragment is: " + mJob.getWsjfScore());
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
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


    @Override
    public void onPause() {
        super.onPause();
        JobLab.get(getActivity()).updateJob(mJob);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.i("value is", "" + newVal);
    }

    private void updateDate() {
        mDateButton.setText(new SimpleDateFormat("dd-MM-yyyy").format(mJob.getDate()));
    }

    private void updateValues() {
        mTitleField.setText(mJob.getJobName());
        mDescriptionField.setText(mJob.getJobDescription());
        mUserValueField.setText(String.valueOf(mJob.getUserValue()));
        mTimeValueField.setText(String.valueOf(mJob.getTimeValue()));
        mRroeValueField.setText(String.valueOf(mJob.getRroeValue()));
        mJobSizeField.setText(String.valueOf(mJob.getJobSize()));
    }

    public void showDialog(final String value) {
        final Dialog d = new Dialog(getActivity());
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog_number_picker);
        Button mButtonSet = (Button) d.findViewById(R.id.btn_set);
        Button mButtonCancel = (Button) d.findViewById(R.id.btn_cancel);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
        np.setMaxValue(13);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (value) {
                    case NP_USER_VALUE:
                        mUserValueField.setText(String.valueOf(np.getValue()));
                        mJob.setUserValue(np.getValue());
                        break;
                    case NP_RROE_VALUE:
                        mRroeValueField.setText(String.valueOf(np.getValue()));
                        mJob.setRroeValue(np.getValue());
                        break;
                    case NP_TIME_VALUE:
                        mTimeValueField.setText(String.valueOf(np.getValue()));
                        mJob.setTimeValue(np.getValue());
                        break;
                    case NP_JOBSIZE_VALUE:
                        mJobSizeField.setText(String.valueOf(np.getValue()));
                        mJob.setJobSize(np.getValue());
                        break;
                }
                d.dismiss();
            }
        });
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }
}
