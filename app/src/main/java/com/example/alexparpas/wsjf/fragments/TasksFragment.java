package com.example.alexparpas.wsjf.fragments;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
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
import com.example.alexparpas.wsjf.model.DividerItemDecoration;
import com.example.alexparpas.wsjf.model.EmptyRecyclerView;
import com.example.alexparpas.wsjf.model.Job;
import com.example.alexparpas.wsjf.model.JobLab;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.text.SimpleDateFormat;
import java.util.List;

public class TasksFragment extends Fragment {

    public static final int NOT_SORTED = 0;
    public static final int SORT_WSJF = 1;
    public static final int SORT_ALPHABETICALLY = 2;
    public static final int SORT_DATE = 3;

    private int isSorted; //0 when not sorted, 1 when sorted Alphabetically,
    // 2 when sorted by wsjf, 3 when sorted by date

    private EmptyRecyclerView mJobsRecyclerView;
    private JobAdapter mAdapter;
    private ActionMode mActionMode;
    private int itemPosition;
    private Callbacks mCallbacks;
    private InterstitialAd mInterstitialAd;

    public interface Callbacks {
        void onJobSelected(Job job);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

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
            MenuItem menuItem = menu.findItem(R.id.action_complete);
            modifyActionMode(menuItem);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            List<Job> jobs = getSortedJobs(getIsSorted());
            switch (item.getItemId()) {
                case R.id.action_delete:
                    //if statement avoids crash when action item is clicked while the list is empty
                    if (!JobLab.get(getActivity()).getJobs(NOT_SORTED).isEmpty()) {
                        JobLab.get(getActivity()).deleteJob(jobs.get(itemPosition));
                        updateUI(getIsSorted());
                        mode.finish();
                    }
                    return true;
//                case R.id.action_archive:
//                    mode.finish();
//                    return true;
                case R.id.action_complete:
                    if (!jobs.get(itemPosition).isCompleted()) {
                        jobs.get(itemPosition).setCompleted(true);
                        item.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_undo_white_24dp)); //Handle action item icon
                    } else {
                        jobs.get(itemPosition).setCompleted(false);
                        item.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_done_white_24dp));
                    }
                    JobLab.get(getActivity()).updateJob(jobs.get(itemPosition));
                    updateUI(getIsSorted());
                    mode.finish();
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

    private void modifyActionMode(MenuItem menuItem) {
        List<Job> jobs = JobLab.get(getActivity()).getJobs(getIsSorted());
        if (jobs.get(itemPosition).isCompleted())
            menuItem.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_undo_white_24dp));
        else {
            menuItem.setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_done_white_24dp));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        updateUI(getIsSorted());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort) {
            popupWindow();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tasks, container, false);

        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.tasks_ad_unit_id));

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);

        mJobsRecyclerView = (EmptyRecyclerView) v.findViewById(R.id.job_recycler_view);
        mJobsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mJobsRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity()));

        View emptyView = v.findViewById(R.id.todo_list_empty_view);
        mJobsRecyclerView.setEmptyView(emptyView);
        updateUI(NOT_SORTED);
        return v;
    }

    public void updateUI(int sortType) {
        List<Job> jobs = getSortedJobs(sortType);
        if (mAdapter == null) {
            mAdapter = new JobAdapter(jobs);
            mJobsRecyclerView.setAdapter(mAdapter);
        } else {
            refreshAdapter(jobs);
        }
    }

    private void refreshAdapter(List<Job> jobs) {
        mAdapter.setJobs(jobs);
        mAdapter.notifyDataSetChanged();
    }

    public void popupWindow() {
        View sortView = getActivity().findViewById(R.id.action_sort);
        PopupMenu popup = new PopupMenu(getActivity(), sortView);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                List<Job> sortedJobs;
                switch (item.getItemId()) {
                    case R.id.popup_alphabetically:
                        isSorted = SORT_ALPHABETICALLY;
                        sortedJobs = getSortedJobs(SORT_ALPHABETICALLY);
                        break;
                    case R.id.popup_date:
                        isSorted = SORT_DATE;
                        sortedJobs = getSortedJobs(SORT_DATE);
                        break;
                    case R.id.popup_wsjf:
                        isSorted = SORT_WSJF;
                        sortedJobs = getSortedJobs(SORT_WSJF);
                        break;
                    default:
                        isSorted = NOT_SORTED;
                        sortedJobs = getSortedJobs(NOT_SORTED);
                        break;
                }
                refreshAdapter(sortedJobs);
                return true;
            }
        });
        popup.show();
    }

    public List<Job> getSortedJobs(int sortType) {
        return JobLab.get(getActivity()).getJobs(sortType);
    }

    public int getIsSorted() {
        return isSorted;
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
            mDateTextView.setText(new SimpleDateFormat("dd/MM/yy").format(mJob.getDate()));
            setStrikeThroughText();
        }

        private void setStrikeThroughText() {
            if (mJob.isCompleted()) {
                mTitleTextView.setPaintFlags(mTitleTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mJobDescription.setPaintFlags(mJobDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mScore.setPaintFlags(mScore.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mDateTextView.setPaintFlags(mDateTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                mTitleTextView.setPaintFlags(mTitleTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                mJobDescription.setPaintFlags(mJobDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                mScore.setPaintFlags(mScore.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                mDateTextView.setPaintFlags(mDateTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        }

        @Override
        public void onClick(View view) {
            mCallbacks.onJobSelected(mJob);
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