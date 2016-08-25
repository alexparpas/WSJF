package com.example.alexparpas.wsjf.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.alexparpas.wsjf.R;
import com.example.alexparpas.wsjf.activities.JobPagerActivity;
import com.example.alexparpas.wsjf.model.DividerItemDecoration;
import com.example.alexparpas.wsjf.model.EmptyRecyclerView;
import com.example.alexparpas.wsjf.model.Job;
import com.example.alexparpas.wsjf.model.JobLab;
import java.text.SimpleDateFormat;
import java.util.List;

public class TasksFragment extends Fragment {

    private EmptyRecyclerView mJobsRecyclerView;
    private JobAdapter mAdapter;
    private ActionMode mActionMode;
    private int itemPosition;
    private int statusBarColor;

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.add_task_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    List<Job> jobs = JobLab.get(getActivity()).getJobs();
                    JobLab.get(getActivity()).deleteJob(jobs.get(itemPosition));
                    updateUI();
                    return true;
                case R.id.action_archive:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
                mActionMode = null;
        }
    };

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
        mJobsRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity()));

        View emptyView = v.findViewById(R.id.todo_list_empty_view);
        mJobsRecyclerView.setEmptyView(emptyView);
        updateUI();
        return v;
    }

    private void updateUI() {
        List<Job> jobs = JobLab.get(getActivity()).getJobs();
        if (mAdapter == null) {
            mAdapter = new JobAdapter(jobs);
            mJobsRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setJobs(jobs);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class JobsHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private Job mJob;
        public TextView mTitleTextView, mJobDescription, mDateTextView, mScore;

        public JobsHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_job_title_text_view);
            mJobDescription = (TextView) itemView.findViewById(R.id.list_item_description_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_view_dates_text_view);
            mScore = (TextView) itemView.findViewById(R.id.list_item_description_wsjf_view);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bindJob(Job job) {
            mJob = job;
            mTitleTextView.setText(mJob.getJobName());
            mJobDescription.setText(mJob.getJobDescription());
            mScore.setText(String.valueOf(mJob.getWsjfScore()));
            mDateTextView.setText(new SimpleDateFormat("dd/MM").format(mJob.getDate()));
        }

        @Override
        public void onClick(View view) {
            Intent intent = JobPagerActivity.newIntent(getActivity(), mJob.getId());
            startActivity(intent);
        }

        @Override
        public boolean onLongClick(View view) {
            itemPosition = getAdapterPosition();

            // if actionmode is null "not started"
            if (mActionMode != null) {
                return false;
            }

            // Start the CAB
            mActionMode = getActivity().startActionMode(mActionModeCallback);
            view.setSelected(true);
            return true;
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