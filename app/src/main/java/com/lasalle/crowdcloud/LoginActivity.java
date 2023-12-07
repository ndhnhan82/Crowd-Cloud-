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
    ViewPager mVpLogin;
    TabLayout tabLayout;
    TextView tvLogin;
    TextView tvPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login );
        initialize();
    }

    private void initialize() {
        languagePrefer = getIntent().getStringExtra("languagePrefer");

        mVpLogin = findViewById(R.id.vpgLogin);
        mVpLogin.setAdapter(new LoginAdapter(getSupportFragmentManager()));
        tabLayout = findViewById(R.id.tlLogin );
        tabLayout.setupWithViewPager(mVpLogin);
        tvLogin = findViewById(R.id.tvLogin);
        tvPolicy = findViewById(R.id.tvPolicy);

        // Load LANGUAGE

        context = LocaleHelper.setLocale(LoginActivity.this, languagePrefer);
        loadLanguage();
    }

    private void loadLanguage() {
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
