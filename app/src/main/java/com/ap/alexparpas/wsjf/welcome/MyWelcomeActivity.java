package com.ap.alexparpas.wsjf.welcome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ap.alexparpas.wsjf.R;
import com.stephentuso.welcome.ui.WelcomeActivity;
import com.stephentuso.welcome.util.WelcomeScreenConfiguration;

public class MyWelcomeActivity extends WelcomeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_welcome);
    }

    @Override
    protected WelcomeScreenConfiguration configuration() {
        return null;
    }
}
