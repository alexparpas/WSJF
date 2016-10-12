package com.ap.alexparpas.wsjf.activities;

import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ap.alexparpas.wsjf.R;
import com.ap.alexparpas.wsjf.billing.IabHelper;
import com.ap.alexparpas.wsjf.billing.IabResult;
import com.ap.alexparpas.wsjf.fragments.AboutFragment;
import com.ap.alexparpas.wsjf.fragments.ArchiveFragment;
import com.ap.alexparpas.wsjf.fragments.DetailsFragment;
import com.ap.alexparpas.wsjf.fragments.TasksFragment;
import com.ap.alexparpas.wsjf.model.Job;
import com.ap.alexparpas.wsjf.model.JobLab;
import com.ap.alexparpas.wsjf.welcome.MyWelcomeActivity;
import com.google.android.gms.ads.MobileAds;
import com.stephentuso.welcome.WelcomeScreenHelper;

import java.util.Stack;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TasksFragment.Callbacks, DetailsFragment.Callbacks {

    IabHelper mHelper;
    public static final String TAG = MainActivity.class.getSimpleName();
    private Stack<Fragment> fragmentStack;
    FloatingActionButton fab;
    NavigationView navigationView;
    MenuItem navAbout, navTasks;
    WelcomeScreenHelper welcomeScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        init();

        welcomeScreen = new WelcomeScreenHelper(this, MyWelcomeActivity.class);
        welcomeScreen.show(savedInstanceState);
    }

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onJobSelected(Job job) {
        if (findViewById(R.id.details_content_frame) == null) { //Check for tablet
            Intent intent = JobPagerActivity.newIntent(this, job.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = DetailsFragment.newInstance(job.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_content_frame, newDetail)
                    .commit();
        }
    }

    @Override
    public void onJobUpdated(Job job) {
        TasksFragment fragment = (TasksFragment) getSupportFragmentManager().findFragmentByTag("TASKS_FRAGMENT");
        fragment.updateUI(fragment.getSortValue());
    }

    private void init() {
        fragmentStack = new Stack<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MobileAds.initialize(getApplicationContext(), getString(R.string.app_ad_unit_id));

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Job job = new Job();
                JobLab.get(getBaseContext()).addJob(job);
                onJobSelected(job);
            }
        });

        //In App Billing Setup //TODO
        mHelper = new IabHelper(this, getString(R.string.billing_license));
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.d(TAG, "In app billing setup failed: " + result);
                }
                else{
                    Log.d(TAG, "In app billing is set up OK");
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Initialize navigation menu items to set as checked/not checked
        Menu menuNav = navigationView.getMenu();
        navAbout = menuNav.findItem(R.id.nav_about);
        navTasks = menuNav.findItem(R.id.nav_tasks);
        navTasks.setChecked(true);

        //Add first fragment and push it on the stack
        TasksFragment tf = new TasksFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, tf, "TASKS_FRAGMENT");
        fragmentStack.push(tf);
        ft.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        welcomeScreen.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentStack.size() == 2) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            fragmentStack.lastElement().onPause();
            ft.remove(fragmentStack.pop());
            fragmentStack.lastElement().onResume();
            ft.show(fragmentStack.lastElement());

            if (fragmentStack.lastElement() instanceof TasksFragment) {
                fab.show();
                navTasks.setChecked(true);
                navAbout.setChecked(false);
            } else if (fragmentStack.lastElement() instanceof AboutFragment) {
                fab.hide();
                navAbout.setChecked(true);
                navTasks.setChecked(false);
            }
            ft.commit();
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_tasks) {
            if (!item.isChecked()) {
                item.setChecked(true);
                navAbout.setChecked(false);
                populateFragment(new TasksFragment(), "TASKS_FRAGMENT");
                fab.show();
            }
//        } else if (id == R.id.nav_archive) {
//            if (!item.isChecked()) {
//                populateFragment(new ArchiveFragment(), "");
//                fab.hide();
//            }
//        } else if (id == R.id.nav_settings) {
//            if (!item.isChecked()) {
//                startActivity(new Intent(this, SettingsActivity.class));
//                Toast.makeText(getApplicationContext(), "Settings Selected", Toast.LENGTH_SHORT).show();
//            }
        } else if (id == R.id.nav_about) {
            if (!item.isChecked()) {
                item.setChecked(true);
                navTasks.setChecked(false);
                populateFragment(new AboutFragment(), "");
                fab.hide();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void populateFragment(Fragment f, String tag) {
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (f instanceof TasksFragment) {
            fragment = new TasksFragment();
        } else if (f instanceof AboutFragment) {
            fragment = new AboutFragment();
        } else if (f instanceof ArchiveFragment) {
            fragment = new ArchiveFragment();
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.content_frame, fragment, tag);
        fragmentStack.lastElement().onPause();
        //Hide the last fragment
        ft.hide(fragmentStack.lastElement());
        //Push the new fragment into stack
        fragmentStack.push(fragment);
        ft.commit();
    }
}
