package com.ap.alexparpas.wsjf.welcome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ap.alexparpas.wsjf.R;
import com.stephentuso.welcome.WelcomeScreenBuilder;
import com.stephentuso.welcome.ui.WelcomeActivity;
import com.stephentuso.welcome.util.WelcomeScreenConfiguration;

public class MyWelcomeActivity extends WelcomeActivity {

    @Override
    protected WelcomeScreenConfiguration configuration() {
        return new WelcomeScreenBuilder(this)
                .theme(R.style.WelcomeScreenTheme_Light)
                .defaultBackgroundColor(R.color.colorPrimary)
                .basicPage(R.drawable.wsjf, "Welcome", getString(R.string.intro_string1), R.color.welcome_color)
                .basicPage(R.drawable.wsjf_grass, "Manage and Prioritise Tasks", getString(R.string.intro_string2), R.color.welcome_color2)
                .basicPage(R.drawable.wsjf_lime, "Work Efficiently", getString(R.string.intro_string3), R.color.welcome_color1)
                .swipeToDismiss(true)
                .build();
    }
}
