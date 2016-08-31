package com.example.alexparpas.wsjf.activities;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import com.example.alexparpas.wsjf.R;
import com.example.alexparpas.wsjf.fragments.AboutFragment;
import com.example.alexparpas.wsjf.fragments.ArchiveFragment;
import com.example.alexparpas.wsjf.fragments.LicencesFragment;
import com.example.alexparpas.wsjf.fragments.TasksFragment;
import com.example.alexparpas.wsjf.model.Job;
import com.example.alexparpas.wsjf.model.JobLab;
import com.example.alexparpas.wsjf.preferences.SettingsActivity;

import java.util.Stack;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Stack<Fragment> fragmentStack;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentStack = new Stack<Fragment>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Job job = new Job();
                JobLab.get(getBaseContext()).addJob(job);
                Intent intent = JobPagerActivity.newIntent(getBaseContext(), job.getId());
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

            //Add first fragment and push it on the stack
            TasksFragment tf = new TasksFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, tf);
            fragmentStack.push(tf);
            ft.commit();
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
            ft.commit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tasks) {
            if (!item.isChecked()) {
                populateFragment(new TasksFragment());
                fab.show();
            }
        } else if (id == R.id.nav_archive) {
            if (!item.isChecked()) {
                populateFragment(new ArchiveFragment());
                fab.hide();
            }
//        } else if (id == R.id.nav_settings) {
//            if (!item.isChecked()) {
//                startActivity(new Intent(this, SettingsActivity.class));
//                Toast.makeText(getApplicationContext(), "Settings Selected", Toast.LENGTH_SHORT).show();
//            }
        } else if (id == R.id.nav_about) {
            if (!item.isChecked()) {
                populateFragment(new AboutFragment());
                fab.hide();
            }
        } else if (id == R.id.nav_licences) {
            if (!item.isChecked()) {
                populateFragment(new LicencesFragment());
                fab.hide();
            }
        } else if (id == R.id.nav_upgrade) {
            if (!item.isChecked()) {
                startActivity(new Intent(this, UpgradeActivity.class));
                Toast.makeText(getApplicationContext(), "Upgrade Selected", Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void populateFragment(Fragment f) {
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (f instanceof TasksFragment) {
            fragment = new TasksFragment();
        } else if (f instanceof AboutFragment) {
            fragment = new AboutFragment();
        } else if (f instanceof LicencesFragment) {
            fragment = new LicencesFragment();
        } else if (f instanceof ArchiveFragment) {
            fragment = new ArchiveFragment();
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.content_frame, fragment);
        fragmentStack.lastElement().onPause();
        //Hide the last fragment
        ft.hide(fragmentStack.lastElement());
        //Push the new fragment into stack
        fragmentStack.push(fragment);
        ft.commit();
    }
}
