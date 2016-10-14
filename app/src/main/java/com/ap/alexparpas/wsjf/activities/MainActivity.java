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
import android.widget.Toast;

import com.ap.alexparpas.wsjf.R;
import com.ap.alexparpas.wsjf.billing.IabHelper;
import com.ap.alexparpas.wsjf.billing.IabResult;
import com.ap.alexparpas.wsjf.billing.Purchase;
import com.ap.alexparpas.wsjf.fragments.AboutFragment;
import com.ap.alexparpas.wsjf.fragments.ArchiveFragment;
import com.ap.alexparpas.wsjf.fragments.DetailsFragment;
import com.ap.alexparpas.wsjf.fragments.TasksFragment;
import com.ap.alexparpas.wsjf.model.Job;
import com.ap.alexparpas.wsjf.model.JobLab;
import com.ap.alexparpas.wsjf.preferences.SettingsActivity;
import com.ap.alexparpas.wsjf.welcome.MyWelcomeActivity;
import com.google.android.gms.ads.MobileAds;
import com.stephentuso.welcome.WelcomeScreenHelper;

import java.util.Stack;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TasksFragment.Callbacks, DetailsFragment.Callbacks {

    private static boolean removeAds = false;
    public static final String TAG = MainActivity.class.getSimpleName();
    static final String SKU_REMOVE_ADS = "ap.alexparpas.removeads";
    private Stack<Fragment> fragmentStack;
    FloatingActionButton fab;
    NavigationView navigationView;
    MenuItem navTasks, navArchive, navSettings, navAbout, navUpgrade;
    WelcomeScreenHelper welcomeScreen;
    IabHelper mHelper;
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        init();

        //In App Billing Setup //TODO
        String base64EncodedPublicKey = getString(R.string.billing_license);
        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.d(TAG, "In app billing setup failed: " + result);
                }
            }
        });

        mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
            @Override
            public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
                if (result.isFailure()) {
                    Toast.makeText(MainActivity.this, "Error Purchasing: " + result, Toast.LENGTH_SHORT).show();
                    return;
                } else if (purchase.getSku().equals(SKU_REMOVE_ADS)) {
                    Toast.makeText(MainActivity.this, "Thanks for your purchase", Toast.LENGTH_SHORT).show();
                    removeAds = true;
                }
            }
        };
        welcomeScreen = new WelcomeScreenHelper(this, MyWelcomeActivity.class);
        welcomeScreen.show(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
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
        if (!removeAds)
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Initialize navigation menu items to set as checked/not checked
        Menu menuNav = navigationView.getMenu();
        navTasks = menuNav.findItem(R.id.nav_tasks);
        navArchive = menuNav.findItem(R.id.nav_archive);
        navSettings = menuNav.findItem(R.id.nav_settings);
        navAbout = menuNav.findItem(R.id.nav_about);
        navUpgrade = menuNav.findItem(R.id.nav_upgrade);
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
                handleChecked(true, false, false, false, false);
            } else if (fragmentStack.lastElement() instanceof ArchiveFragment) {
                fab.hide();
                handleChecked(false, true, false, false, false);
            } else if (fragmentStack.lastElement() instanceof AboutFragment) {
                fab.hide();
                handleChecked(false, false, false, true, false);
                ft.commit();
            } else {
                super.onBackPressed();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_tasks) {
            if (!item.isChecked()) {
                handleChecked(true, false, false, false, false);
                populateFragment(new TasksFragment(), "TASKS_FRAGMENT");
                fab.show();
            }
        } else if (id == R.id.nav_archive) {
            if (!item.isChecked()) {
                handleChecked(false, true, false, false, false);
                populateFragment(new ArchiveFragment(), "");
                fab.hide();
            }
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            Toast.makeText(getApplicationContext(), "Settings Selected", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_about) {
            if (!item.isChecked()) {
                handleChecked(false, false, false, true, false);
                populateFragment(new AboutFragment(), "");
                fab.hide();
            }
        } else if (id == R.id.nav_upgrade) {
            buyClick();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void handleChecked(boolean tasks, boolean archive, boolean settings, boolean about, boolean upgrade) {
        navTasks.setChecked(tasks);
        navArchive.setChecked(archive);
        navSettings.setChecked(settings);
        navAbout.setChecked(about);
        navUpgrade.setChecked(upgrade);
    }

    public void buyClick() {
        mHelper.launchPurchaseFlow(this, SKU_REMOVE_ADS, 10001, mPurchaseFinishedListener, "removeads");
    }

    public static boolean isRemoveAds() {
        return removeAds;
    }

}
