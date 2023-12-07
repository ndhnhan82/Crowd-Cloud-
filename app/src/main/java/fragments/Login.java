package fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lasalle.crowdcloud.LoginActivity;
import com.lasalle.crowdcloud.MainActivity;
import com.lasalle.crowdcloud.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import model.LocaleHelper;

//import model.DatabaseManagement;

public class Login extends Fragment implements View.OnClickListener {
    private TextInputEditText tieEmailAddress, tiePassword;
    TextView tvForgetPassword;
    private View mRootView;
    private String languagePrefer;
    private Context context;
    private Resources resources;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate( R.layout.fragment_login, container, false );
        LoginActivity activity = (LoginActivity) getActivity();
        languagePrefer = activity.getLanguage();
        initialize();
        return mRootView;
    }

    private void initialize() {
        tieEmailAddress = (TextInputEditText) mRootView.findViewById( R.id.tieEmailAddress );
        tiePassword = (TextInputEditText) mRootView.findViewById( R.id.tiePassword );
        Button btnLogin = (Button) mRootView.findViewById( R.id.btnLogin );
        tvForgetPassword = (TextView) mRootView.findViewById( R.id.tvForgetPass );
        btnLogin.setOnClickListener( this );
        tvForgetPassword.setOnClickListener( this );

        // Set LANGUAGE
        context = LocaleHelper.setLocale(mRootView.getContext(), languagePrefer);
        resources = context.getResources();
//        tieEmailAddress.setHint(resources.getString(R.string.email_address));
//        tiePassword.setHint(resources.getString(R.string.password));
        btnLogin.setText(resources.getString(R.string.login));
        tvForgetPassword.setText(resources.getString(R.string.forget_password));

    }

    @Override
    public void onClick(View view) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String strEmail = tieEmailAddress.getText().toString().trim();
        String strPassword = tiePassword.getText().toString();
        String safeEmail = strEmail.replace( "@", "-" )
                .replace( ".", "-" );



        if (strEmail.isEmpty() || strPassword.isEmpty()) {
            showAlert(resources.getString(R.string.emailEmpty));
            return;
        }
        int id = view.getId();
        if (id == R.id.tvForgetPass) {
            showAlert(resources.getString(R.string.emailPass));

            mAuth.sendPasswordResetEmail( strEmail );
        } else if (id == R.id.btnLogin) {
            mAuth.signInWithEmailAndPassword( strEmail, strPassword )
                    .addOnCompleteListener( getActivity(), task -> {
                        if (task.isSuccessful()) {
                            gotoMainActivity(strEmail);

                        } else
                            showAlert(resources.getString(R.string.passwordWrong) );
                    } );
        }
    }


    private void gotoMainActivity(String email) {
        Intent intent = new Intent( getContext(), MainActivity.class );
        intent.putExtra("email",email);
        startActivity( intent );
        getActivity().finishAffinity();
    }

    private void showAlert(String message) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder( getContext() );
        alertDialog.setTitle( "Notification " )
                .setMessage( message )
                .setPositiveButton( "OK", null )
                .show();

    }


}