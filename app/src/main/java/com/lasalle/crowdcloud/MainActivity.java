package com.lasalle.crowdcloud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import fragments.Home;
import fragments.Preference;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_PREFERENCE = 1;
    private int mCurrentFragment = FRAGMENT_HOME;


    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        initialize();
    }

    private void initialize() {

        drawerLayout = (DrawerLayout) findViewById( R.id.drawer_layout );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawerLayout, toolbar,
                R.string.nav_drawer_open, R.string.nav_drawer_close );
        drawerLayout.addDrawerListener( toggle );
        toggle.syncState();
        navigationView = (NavigationView) findViewById( R.id.navigation_view );
        navigationView.setNavigationItemSelectedListener( this );
        navigationView.getMenu().findItem( R.id.nav_preferences ).setChecked( true );
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
        } else if (id == R.id.nav_preferences) {
            if (mCurrentFragment != FRAGMENT_PREFERENCE) {
                replaceFragment( new Preference() );
                mCurrentFragment = FRAGMENT_PREFERENCE;
            }
        }else if (id == R.id.nav_quit){
            this.finish();
            getApplication().notifyAll();
        }
        drawerLayout.closeDrawers();
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
}
