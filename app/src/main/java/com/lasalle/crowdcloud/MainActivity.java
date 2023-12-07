package com.lasalle.crowdcloud;

import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import fragments.Home;
import fragments.UserHistory;
import model.LocaleHelper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_HISTORY = 1;
    private int mCurrentFragment = FRAGMENT_HOME;
    private NavigationView navigationView;
    private Switch swDark;
    private TextView tvUser;
    private String languagePrefer;
    private Context context;
    private Resources resources;
    private int selectedLanguage;
    private final String[] languageList = {"English","Français"};
    private String email;
    private DatabaseReference userDatabase;


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
        navigationView.getMenu().findItem( R.id.nav_home ).setChecked( true );

        TextClock tcClock = Objects.requireNonNull( navigationView.getMenu().findItem( R.id.tcClock ).getActionView() ).findViewById( R.id.tcClock );
        tvUser = Objects.requireNonNull( navigationView.getMenu().findItem( R.id.tvUser ).getActionView() ).findViewById( R.id.tvUser );

        swDark = Objects.requireNonNull( navigationView.getMenu().findItem( R.id.nav_switch ).getActionView() ).findViewById( R.id.nav_switch );
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            swDark.setChecked( true );
        }
        email =  FirebaseAuth.getInstance().getCurrentUser().getEmail();
        tvUser.setText( String.format(  "Welcome %s !",email) );
        tvUser.setTextAppearance( this, R.style.Title );
        tcClock.setTextAppearance( this, R.style.tcClock );

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

        int defaultBackgroundColor = Color.parseColor( "#408EE1" );

        // Check if night mode is active
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {

            toolbar.setBackgroundColor( Color.BLACK );

        } else {

            toolbar.setBackgroundColor( defaultBackgroundColor );

        }

        // Load Language
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");
        Boolean hasEmailExtra = getIntent().hasExtra("email");
        // No Extra String Email means:
        // - If App goes from SplashActivity -> Old User -> AppLanguage already = ThisUser.LanguagePrefer
        // - If App goes from Register -> New User -> AppLanguage = ThisUser.LanguagePrefer = Nearest Login of Existing User
        if(!hasEmailExtra) {
            languagePrefer = getIntent().getStringExtra("languagePrefer");
            context = LocaleHelper.setLocale(MainActivity.this, languagePrefer);
            if(languagePrefer.equals("en"))
                selectedLanguage = 0;
            else if(languagePrefer.equals("fr"))
                selectedLanguage =1;
            loadLanguage();
        }
        // hasEmailExtra means:
        // - an Existing User coming back in to App -> has their own LanguagePrefer
        else {
            String userEmail = getIntent().getStringExtra("email");
            String safeEmail = userEmail.replace("@","-")
                    .replace(".","-");
            getLanguage(new MainActivity.languageCallBack(){
                @Override
                public void onLanguageUpdate(String language) {

                    languagePrefer = language;

                    context = LocaleHelper.setLocale(MainActivity.this, languagePrefer);
                    if(languagePrefer.equals("en"))
                        selectedLanguage = 0;
                    else if(languagePrefer.equals("fr"))
                        selectedLanguage =1;
                    loadLanguage();
                }
                @Override
                public void onFailure(Exception e) {

                }
            },safeEmail);
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
        } else if (id == R.id.nav_history) {
            if (mCurrentFragment != FRAGMENT_HISTORY) {
                replaceFragment( new UserHistory() );
                mCurrentFragment = FRAGMENT_HISTORY;

            }
            drawerLayout.closeDrawers();
        } else if (id == R.id.nav_SignOut) {
            FirebaseAuth.getInstance().signOut();
            this.finish();
            Intent intent = new Intent( this, LoginActivity.class );
            startActivity( intent );
        } else if (id == R.id.nav_quit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else if (id == R.id.nav_language) {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
            dialogBuilder.setTitle("Select a Language")
                    .setSingleChoiceItems(languageList, selectedLanguage, (dialog, i) -> {
                        if (languageList[i].equals("English")) {
                            languagePrefer = "en";

                            selectedLanguage = 0;

                        } else if (languageList[i].equals("Français")) {
                            languagePrefer = "fr";

                            selectedLanguage = 1;
                        }
                    }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            context = LocaleHelper.setLocale(MainActivity.this, languagePrefer);
                            loadLanguage();
                            if (mCurrentFragment == FRAGMENT_HOME)
                                replaceFragment(new Home());
                            else if (mCurrentFragment == FRAGMENT_HISTORY)
                                replaceFragment(new UserHistory());
                            updateLanguageFirebase();
                            dialog.dismiss();
                        }
                    });
            dialogBuilder.create().show();
        }

        return true;
    }
    private void updateLanguageFirebase() {
        userDatabase.child(email.replace("@","-")
                        .replace(".","-")).child("languagePrefer")
                .setValue(languagePrefer);
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
    public void loadLanguage(){

        resources = context.getResources();
        // Home
        navigationView.getMenu().findItem(R.id.nav_home)
                .setTitle(resources.getString(R.string.home));
        // Darkmode
        navigationView.getMenu().findItem(R.id.nav_switch)
                .setTitle(resources.getString(R.string.light_dark));
        // Language
        navigationView.getMenu().findItem(R.id.nav_language)
                .setTitle(resources.getString(R.string.language));
        // Signout
        navigationView.getMenu().findItem(R.id.nav_SignOut)
                .setTitle(resources.getString(R.string.signout));
        // Quit
        navigationView.getMenu().findItem(R.id.nav_quit)
                .setTitle(resources.getString(R.string.quit));

    }
    public interface languageCallBack{
        void onLanguageUpdate(String language);
        void onFailure(Exception e);
    }
    public void getLanguage(final MainActivity.languageCallBack callBack, String key){
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");
        userDatabase.child(key).child("languagePrefer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                languagePrefer = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userDatabase.child(key).child("languagePrefer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callBack.onLanguageUpdate(languagePrefer);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callBack.onFailure(error.toException());
            }
        });

    }
}
