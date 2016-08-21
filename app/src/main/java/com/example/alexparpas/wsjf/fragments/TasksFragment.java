package com.example.alexparpas.wsjf.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alexparpas.wsjf.R;
import com.example.alexparpas.wsjf.activities.JobPagerActivity;
import com.example.alexparpas.wsjf.activities.MainActivity;
import com.example.alexparpas.wsjf.model.EmptyRecyclerView;
import com.example.alexparpas.wsjf.model.Job;
import com.example.alexparpas.wsjf.model.JobLab;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Alex on 07/08/2016.
 */
public class TasksFragment extends Fragment {

    private EmptyRecyclerView mJobsRecyclerView;
    private JobAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tasks, container, false);
        mJobsRecyclerView = (EmptyRecyclerView) v.findViewById(R.id.job_recycler_view);
        mJobsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        View emptyView = v.findViewById(R.id.todo_list_empty_view);
        mJobsRecyclerView.setEmptyView(emptyView);

        updateUI();
        return v;
    }

    private void updateUI() {
        JobLab jobLab = JobLab.get(getActivity());
        List<Job> jobs = jobLab.getJobs();

        if (mAdapter == null) {
            mAdapter = new JobAdapter(jobs);
            mJobsRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setJobs(jobs);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class JobsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Job mJob;
        public TextView mTitleTextView, mJobDescription, mDateTextView, mScore;

        public JobsHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_job_title_text_view);
            mJobDescription = (TextView) itemView.findViewById(R.id.list_item_description_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_view_dates_text_view);
            mScore = (TextView) itemView.findViewById(R.id.list_item_description_wsjf_view);
            itemView.setOnClickListener(this);
        }

        public void bindJob(Job job) {
            mJob = job;
            mTitleTextView.setText(mJob.getJobName().toString());
            mJobDescription.setText(mJob.getJobDescription().toString());
            mScore.setText(String.valueOf(mJob.getWsjfScore()));
            mDateTextView.setText(mJob.getDate().toString());
        }

        @Override
        public void onClick(View view) {
            Intent intent = JobPagerActivity.newIntent(getActivity(), mJob.getId());
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
            System.out.println("WSJF value on the RecyclerView is: " + job.getWsjfScore());
            holder.bindJob(job); //Connecting the adapter with the ViewHolder
        }

        @Override
        public int getItemCount() {
            return mJobs.size();
        }

        public void setJobs(List<Job> jobs) {
            mJobs = jobs;
        }
    }

}
