package fragments;

import android.content.Intent;
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

import com.lasalle.crowdcloud.MainActivity;
import com.lasalle.crowdcloud.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

//import model.DatabaseManagement;

public class Login extends Fragment implements View.OnClickListener {
    private TextInputEditText tieEmailAddress, tiePassword;
    TextView tvForgetPassword;
    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate( R.layout.fragment_login, container, false );
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
    }

    @Override
    public void onClick(View view) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String strEmail = tieEmailAddress.getText().toString().trim();
        String strPassword = tiePassword.getText().toString();
        String safeEmail = strEmail.replace( "@", "-" )
                .replace( ".", "-" );



        if (strEmail.isEmpty() || strPassword.isEmpty()) {
            showAlert( "Your email and password cannot be empty. Please reenter and try again!" );
            return;
        }
        int id = view.getId();
        if (id == R.id.tvForgetPass) {
            showAlert( "An email will be sent to you shortly!" );

            mAuth.sendPasswordResetEmail( strEmail );
        } else if (id == R.id.btnLogin) {
            mAuth.signInWithEmailAndPassword( strEmail, strPassword )
                    .addOnCompleteListener( getActivity(), task -> {
                        if (task.isSuccessful()) {
                            gotoMainActivity();

                        } else
                            showAlert( "Your email or password is not correct. PLease try again or register a new user!" );
                    } );
        }
    }


    private void gotoMainActivity() {
        Intent intent = new Intent( getContext(), MainActivity.class );
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