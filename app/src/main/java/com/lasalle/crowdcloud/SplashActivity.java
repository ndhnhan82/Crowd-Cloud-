package com.lasalle.crowdcloud;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.LocaleHelper;
import model.User;

public class SplashActivity extends AppCompatActivity {

    private TextView tvSlash;
    private String languagePrefer;
    private DatabaseReference userDatabase;
    private Context context;
    private Resources resources;
    private FirebaseUser currentUser;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        getLanguage(new languageCallBack() {
            @Override
            public void onLanguageUpdate(String language) {
                Log.d("check",language);
                if (currentUser==null){
                    languagePrefer = "en";
                }
                else{
                    languagePrefer = language;
                }
                context = LocaleHelper.setLocale(SplashActivity.this, languagePrefer);
                resources = context.getResources();
                tvSlash.setText(resources.getString(R.string.app_name));

                Handler handler = new Handler();
                handler.postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        nextActivity();
                    }
                } ,1000);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        //languagePrefer = "fr";
        setContentView( R.layout.activity_splash );

        initialize();
    }

    private void initialize() {
        tvSlash = findViewById(R.id.tvSlash);

        userDatabase = FirebaseDatabase.getInstance().getReference("Users");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        // Set LANGUAGE

    }

    private void nextActivity() {
        if (currentUser == null){
            intent = new Intent( this, LoginActivity.class );
        }else{
            intent = new Intent( this, MainActivity.class );
        }
        startActivity( intent );
    }
    public interface languageCallBack{
        void onLanguageUpdate(String language);
        void onFailure(Exception e);
    }
    public void getLanguage(final languageCallBack callBack){
        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("Users");
        String key = FirebaseAuth.getInstance().getCurrentUser().getEmail()
                .replace("@","-")
                .replace(".","-");
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