package com.example.alexparpas.wsjf.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexparpas.wsjf.R;
import com.example.alexparpas.wsjf.activities.DetailsActivity;
import com.example.alexparpas.wsjf.model.Job;
import com.example.alexparpas.wsjf.model.JobLab;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Alex on 07/08/2016.
 */
public class TasksFragment extends Fragment {

    private RecyclerView mJobsRecyclerView;
    private JobAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tasks, container, false);
        mJobsRecyclerView = (RecyclerView) v.findViewById(R.id.job_recycler_view);
        mJobsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return v;
    }

    private void updateUI(){
        JobLab jobLab= JobLab.get(getActivity());
        List<Job> jobs = jobLab.getJobs();

        mAdapter = new JobAdapter(jobs);
        mJobsRecyclerView.setAdapter(mAdapter);
    }
    private class JobsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Job mJob;
        public TextView mTitleTextView;
        private TextView mJobDescription;
        private TextView mDateTextView;

        public JobsHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView)itemView.findViewById(R.id.list_item_job_title_text_view);
            mJobDescription = (TextView)itemView.findViewById(R.id.list_item_description_text_view);
            mDateTextView = (TextView)itemView.findViewById(R.id.list_item_view_dates_text_view);
            itemView.setOnClickListener(this);
        }

        public void bindJob(Job job){
            mJob = job;
            mTitleTextView.setText(mJob.getJobName());
            mJobDescription.setText(mJob.getJobDescription());
            String formattedDate = mJob.getStartDate().toString() + " - " + mJob.getEndDate().toString();
            mDateTextView.setText(formattedDate);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            startActivity(intent);
        }
    }

    private class JobAdapter extends RecyclerView.Adapter<JobsHolder> {
        private List<Job> mJobs;

        public JobAdapter(List<Job> jobs) {
            mJobs = jobs;
        }

        //Gets called by the RecyclerView when it needs a new View to display an item
        @Override
        public JobsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_job, parent, false);
            return new JobsHolder(view);
        }

        @Override
        public void onBindViewHolder(JobsHolder holder, int position) {
            Job job = mJobs.get(position);
            holder.bindJob(job);
        }

        @Override
        public int getItemCount() {
            return mJobs.size();
        }
    }

}
