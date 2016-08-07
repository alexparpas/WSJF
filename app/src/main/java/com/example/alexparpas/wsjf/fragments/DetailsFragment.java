package com.example.alexparpas.wsjf.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alexparpas.wsjf.model.Job;
import com.example.alexparpas.wsjf.R;

import java.util.ArrayList;

/**
 * Created by Alex on 06/08/2016.
 */
public class DetailsFragment extends Fragment {
    ArrayList<Job> pendingJobs;
    private Job mJob;
    private TextView tv;

    public DetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pendingJobs = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_job, container, false);
        tv = (TextView) v.findViewById(R.id.job_title);
        return v;
    }
}
