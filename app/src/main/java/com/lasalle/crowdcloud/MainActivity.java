package com.lasalle.crowdcloud;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import fragments.Home;
import fragments.Preference;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_PREFERENCE = 1;
    private int mCurrentFragment = FRAGMENT_HOME;

    private boolean isDark = true;
    private Switch swDark;
    private TextView tvUser;
    private TextClock tcClock;
    private Context context;
    private Resources resources;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        initialize();
    }

    @SuppressLint("SetTextI18n")
    private void initialize() {

        drawerLayout = (DrawerLayout) findViewById( R.id.drawer_layout );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawerLayout, toolbar,
                R.string.nav_drawer_open, R.string.nav_drawer_close );
        drawerLayout.addDrawerListener( toggle );
        toggle.syncState();

        // Navigation menu
        navigationView = (NavigationView) findViewById( R.id.navigation_view );
        navigationView.setNavigationItemSelectedListener( this );
        navigationView.getMenu().findItem( R.id.nav_preferences ).setChecked( true );

        tcClock = Objects.requireNonNull( navigationView.getMenu().findItem( R.id.tcClock ).getActionView() ).findViewById( R.id.tcClock );
        tvUser = Objects.requireNonNull( navigationView.getMenu().findItem( R.id.tvUser ).getActionView() ).findViewById( R.id.tvUser );

        swDark = Objects.requireNonNull( navigationView.getMenu().findItem( R.id.nav_switch ).getActionView() ).findViewById( R.id.nav_switch );
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            swDark.setChecked( true );
        }
        String email = Objects.requireNonNull( FirebaseAuth.getInstance().getCurrentUser() ).getEmail();
        tvUser.setText( "Welcome "+ email+" !");
        tvUser.setTextAppearance(this, R.style.Title);
        tcClock.setTextAppearance(this, R.style.tcClock);

        swDark.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode( AppCompatDelegate.MODE_NIGHT_YES );

                    finish();

                } else {
                    AppCompatDelegate.setDefaultNightMode( AppCompatDelegate.MODE_NIGHT_NO );

                    finish();
                }
            }
        } );

        int defaultBackgroundColor = Color.parseColor("#408EE1");

        // Check if night mode is active
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {

            toolbar.setBackgroundColor(Color.BLACK);

        } else {

            toolbar.setBackgroundColor(defaultBackgroundColor);

        }

        View navHeaderView = navigationView.getHeaderView( 0 );
        replaceFragment( new Home() );
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            if (mCurrentFragment != FRAGMENT_HOME) {
                replaceFragment( new Home() );
                mCurrentFragment = FRAGMENT_HOME;

            }
            drawerLayout.closeDrawers();
        } else if (id == R.id.nav_preferences) {
            if (mCurrentFragment != FRAGMENT_PREFERENCE) {
                replaceFragment( new Preference() );
                mCurrentFragment = FRAGMENT_PREFERENCE;

            }
            drawerLayout.closeDrawers();
        }else if (id == R.id.nav_SignOut) {
            FirebaseAuth.getInstance().signOut();
            this.finish();
            Intent intent = new Intent( this, LoginActivity.class );
            startActivity( intent );
        } else if (id == R.id.nav_quit) {
            this.finish();
            getApplication().notifyAll();
            drawerLayout.closeDrawers();
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen( GravityCompat.START ))
            drawerLayout.closeDrawer( GravityCompat.START );
        else
            super.onBackPressed();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace( R.id.content_frame, fragment );
        transaction.commit();
    }

    public void setLanguage(){

        resources = context.getResources();
        // Home
        navigationView.getMenu().findItem(R.id.nav_home)
                .setTitle(resources.getString(R.string.home));
        // Language
        navigationView.getMenu().findItem(R.id.nav_language)
                .setTitle(resources.getString(R.string.language));
        // Light/Dark
        navigationView.getMenu().findItem(R.id.nav_switch)
                .setTitle(resources.getString(R.string.darkmode));
        // Quit
        navigationView.getMenu().findItem(R.id.nav_quit)
                .setTitle(resources.getString(R.string.quit));

    }
}
