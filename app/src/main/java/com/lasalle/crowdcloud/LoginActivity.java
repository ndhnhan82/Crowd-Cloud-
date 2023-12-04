package com.lasalle.crowdcloud;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.lasalle.crowdcloud.adapter.LoginAdapter;

import model.LocaleHelper;


public class LoginActivity extends AppCompatActivity {

    private String languagePrefer;
    private Context context;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login );
        initialize();
    }

    private void initialize() {
        languagePrefer = getIntent().getStringExtra("languagePrefer");

        ViewPager mVpLogin = findViewById(R.id.vpgLogin);
        TabLayout tabLayout = findViewById(R.id.tlLogin );
        TextView tvLogin = findViewById(R.id.tvLogin);
        TextView tvPolicy = findViewById(R.id.tvPolicy);

        tabLayout.setupWithViewPager(mVpLogin);
        mVpLogin.setAdapter(new LoginAdapter(getSupportFragmentManager()));


        // Load LANGUAGE
        context = LocaleHelper.setLocale(LoginActivity.this, languagePrefer);
        resources = context.getResources();

        tvLogin.setText(resources.getString(R.string.login));
        tvPolicy.setText(resources.getString(R.string.policy));
        tabLayout.getTabAt(0).setText(resources.getString(R.string.login));
        tabLayout.getTabAt(1).setText(resources.getString(R.string.signup));
    }
    public String getLanguage(){
        return languagePrefer;
    }
}