package com.example.alexparpas.wsjf.fragments;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexparpas.wsjf.activities.MainActivity;
import com.example.alexparpas.wsjf.model.Job;
import com.example.alexparpas.wsjf.R;
import com.example.alexparpas.wsjf.model.JobLab;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class DetailsFragment extends Fragment implements NumberPicker.OnValueChangeListener {
    private static final String ARG_JOB_ID = "job_id";
    private static final String DIALOG_DATE = "dialogDate";
    private static final String DIALOG_TIME = "dialogTime";

    private static final String NP_USER_VALUE = "UserValue";
    private static final String NP_TIME_VALUE = "TimeValue";
    private static final String NP_RROE_VALUE = "RroeValue";
    private static final String NP_JOBSIZE_VALUE = "JobSizeValue";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    private Job mJob;
    private EditText mTitleField;
    private TextView mJobDescriptionField, mDateField, mDateTimeField, mUserValueField, mTimeValueField, mRroeValueField, mJobSizeField;
    private RelativeLayout setJobDescription, setDate, setDateTime, setUserValue, setTimeValue, setRrroeValue, setJobSizeValue;
    ImageButton userValueInfo, timeValueInfo, rroeValueInfo, jobSizeInfo;

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
        setup(v);
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
        if (requestCode == REQUEST_TIME) {
            Date time = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mJob.setDateTime(time);
            updateTime();
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

    private void setup(View v) {
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
                showNumberPickerDialog(NP_USER_VALUE);
            }
        });

        setTimeValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPickerDialog(NP_TIME_VALUE);
            }
        });

        setRrroeValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPickerDialog(NP_RROE_VALUE);
            }
        });

        setJobSizeValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPickerDialog(NP_JOBSIZE_VALUE);
            }
        });

        //Date
        setDate = (RelativeLayout) v.findViewById(R.id.set_date_value);
        mDateField = (TextView) v.findViewById(R.id.date_value);
        updateDate();

        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment datePickerDialog = DatePickerFragment.newInstance(mJob.getDate());
                datePickerDialog.setTargetFragment(DetailsFragment.this, REQUEST_DATE);
                datePickerDialog.show(manager, DIALOG_DATE);
            }
        });

        //Time
        // TODO sort out functionality
        setDateTime = (RelativeLayout) v.findViewById(R.id.set_date_time);
        mDateTimeField = (TextView) v.findViewById(R.id.date_time_value);
        updateTime();

        setDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mJob.getDateTime());
                timePickerFragment.setTargetFragment(DetailsFragment.this, REQUEST_TIME);
                timePickerFragment.show(manager, DIALOG_TIME);
            }
        });

        //Job Description
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

        //Info
        userValueInfo = (ImageButton) v.findViewById(R.id.user_value_info);
        timeValueInfo = (ImageButton) v.findViewById(R.id.time_value_info);
        rroeValueInfo = (ImageButton) v.findViewById(R.id.rroe_value_info);
        jobSizeInfo = (ImageButton) v.findViewById(R.id.job_size_value_info);

        userValueInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog(NP_USER_VALUE);
            }
        });

        timeValueInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog(NP_TIME_VALUE);
            }
        });

        rroeValueInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog(NP_RROE_VALUE);
            }
        });

        jobSizeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog(NP_JOBSIZE_VALUE);
            }
        });

        //FAB
        FloatingActionButton detailsFAB = (FloatingActionButton) v.findViewById(R.id.add_job_fab);
        detailsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jobName = mTitleField.getText().toString().trim();
                if (jobName.matches("")) {
                    Toast.makeText(getActivity(), "Title cannot be blank ", Toast.LENGTH_SHORT).show();
                }
                else{
                    mJob.setJobName(mTitleField.getText().toString().trim());
                    mJob.setJobDescription(mJobDescriptionField.getText().toString().trim());
                    mJob.calculateWSJF();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
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
    }

    private void updateDate() {
        mDateField.setText(new SimpleDateFormat("dd-MM-yyyy").format(mJob.getDate()));
    }

    private void updateTime() {
        //TODO format time appropriately
        mDateTimeField.setText(new SimpleDateFormat("HH:mm").format(mJob.getDateTime()));
    }

    private void updateValues() {
        mTitleField.setText(mJob.getJobName());
        mJobDescriptionField.setText(mJob.getJobDescription());
        mUserValueField.setText(String.valueOf(mJob.getUserValue()));
        mTimeValueField.setText(String.valueOf(mJob.getTimeValue()));
        mRroeValueField.setText(String.valueOf(mJob.getRroeValue()));
        mJobSizeField.setText(String.valueOf(mJob.getJobSize()));
    }

    public void showNumberPickerDialog(final String value) {
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
        switch (value) {
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

    public void showInfoDialog(final String value) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        switch (value) {
            case NP_USER_VALUE:
                alertDialog.setTitle("User Value");
                alertDialog.setMessage("Alert message to be shown");
                break;
            case NP_TIME_VALUE:
                alertDialog.setTitle("Time Value");
                alertDialog.setMessage("Alert message to be shown");
                break;
            case NP_RROE_VALUE:
                alertDialog.setTitle("Risk Reduction Value");
                alertDialog.setMessage("Alert message to be shown");
                break;
            case NP_JOBSIZE_VALUE:
                alertDialog.setTitle("Job Size Value");
                alertDialog.setMessage("Alert message to be shown");
                break;
        }

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }
}
