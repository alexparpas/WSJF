package com.example.alexparpas.wsjf.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
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
    private EditText mTitleField;
    private TextView mJobDescriptionField, mDateValueField, mUserValueField, mTimeValueField, mRroeValueField, mJobSizeField;
    private RelativeLayout setJobDescription, setDateValue, setUserValue, setTimeValue, setRrroeValue, setJobSizeValue;

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

        //Values section
        setUserValue = (RelativeLayout) v.findViewById(R.id.set_user_value);
        setTimeValue = (RelativeLayout) v.findViewById(R.id.set_time_value);
        setRrroeValue = (RelativeLayout) v.findViewById(R.id.set_rroe_value);
        setJobSizeValue = (RelativeLayout) v.findViewById(R.id.set_job_size_value);

        mUserValueField = (TextView) v.findViewById(R.id.user_value);
        mTimeValueField = (TextView) v.findViewById(R.id.time_value);
        mRroeValueField = (TextView) v.findViewById(R.id.rroe_value);
        mJobSizeField = (TextView) v.findViewById(R.id.job_size_value);

        setUserValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(NP_USER_VALUE);
            }
        });

        setTimeValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(NP_TIME_VALUE);
            }
        });

        setRrroeValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(NP_RROE_VALUE);
            }
        });

        setJobSizeValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(NP_JOBSIZE_VALUE);
            }
        });

        setDateValue = (RelativeLayout) v.findViewById(R.id.set_date_value);
        mDateValueField = (TextView) v.findViewById(R.id.date_value);
        updateDate();

        setDateValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mJob.getDate());
                dialog.setTargetFragment(DetailsFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        setJobDescription = (RelativeLayout) v.findViewById(R.id.set_description);
        mJobDescriptionField = (EditText) v.findViewById(R.id.job_description);
        setJobDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mJobDescriptionField.requestFocus();
                InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imgr.showSoftInput(mJobDescriptionField, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        FloatingActionButton detailsFAB = (FloatingActionButton) v.findViewById(R.id.add_job_fab);
        detailsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mJob.setJobName(mTitleField.getText().toString().trim());
                mJob.setJobDescription(mJobDescriptionField.getText().toString().trim());
                mJob.calculateWSJF();

                System.out.println("WSJF value on DetailsFragment is: " + mJob.getWsjfScore());
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

//        mCompletedCheckBox = (CheckBox) v.findViewById(R.id.job_completed);
//        mCompletedCheckBox.setChecked(mJob.isCompleted());
//        mCompletedCheckBox.setOnCheckedChangeListener(
//                new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        mJob.setCompleted(isChecked);
//                    }
//                }
//        );
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
        mDateValueField.setText(new SimpleDateFormat("dd-MM-yyyy").format(mJob.getDate()));
    }

    private void updateValues() {
        mTitleField.setText(mJob.getJobName());
        mJobDescriptionField.setText(mJob.getJobDescription());
        mUserValueField.setText(String.valueOf(mJob.getUserValue()));
        mTimeValueField.setText(String.valueOf(mJob.getTimeValue()));
        mRroeValueField.setText(String.valueOf(mJob.getRroeValue()));
        mJobSizeField.setText(String.valueOf(mJob.getJobSize()));
    }

    public void showDialog(final String value) {
        RelativeLayout linearLayout = new RelativeLayout(getActivity());
        final NumberPicker numberPicker = new NumberPicker(getActivity());
        numberPicker.setMaxValue(13);
        numberPicker.setMinValue(0);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
        RelativeLayout.LayoutParams numPickerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        numPickerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        linearLayout.setLayoutParams(params);
        linearLayout.addView(numberPicker, numPickerParams);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        switch (value){
            case NP_USER_VALUE:
                numberPicker.setValue(Integer.valueOf(mUserValueField.getText().toString()));
                alertDialogBuilder.setTitle("Select User Value");
                break;
            case NP_TIME_VALUE:
                numberPicker.setValue(Integer.valueOf(mTimeValueField.getText().toString()));
                alertDialogBuilder.setTitle("Select Time Value");
                break;
            case NP_RROE_VALUE:
                numberPicker.setValue(Integer.valueOf(mRroeValueField.getText().toString()));
                alertDialogBuilder.setTitle("Select RROE Value");
                break;
            case NP_JOBSIZE_VALUE:
                numberPicker.setValue(Integer.valueOf(mJobSizeField.getText().toString()));
                alertDialogBuilder.setTitle("Select Job Size Value");
                break;
        }
        alertDialogBuilder.setView(linearLayout);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                switch (value) {
                                    case NP_USER_VALUE:
                                        mUserValueField.setText(String.valueOf(numberPicker.getValue()));
                                        mJob.setUserValue(numberPicker.getValue());
                                        break;
                                    case NP_TIME_VALUE:
                                        mTimeValueField.setText(String.valueOf(numberPicker.getValue()));
                                        mJob.setTimeValue(numberPicker.getValue());
                                        break;
                                    case NP_RROE_VALUE:
                                        mRroeValueField.setText(String.valueOf(numberPicker.getValue()));
                                        mJob.setRroeValue(numberPicker.getValue());
                                        break;
                                    case NP_JOBSIZE_VALUE:
                                        mJobSizeField.setText(String.valueOf(numberPicker.getValue()));
                                        mJob.setJobSize(numberPicker.getValue());
                                        break;
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
