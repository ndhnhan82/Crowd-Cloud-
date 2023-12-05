package fragments;

//import static fragments.Login.loggedInUser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.lasalle.crowdcloud.LoginActivity;
import com.lasalle.crowdcloud.MainActivity;
import com.lasalle.crowdcloud.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import model.LocaleHelper;
import model.User;

public class Register extends Fragment implements View.OnClickListener {
    private View mRootView;
    private TextInputEditText tieEmailAddress, tiePassword, tiePassword2;
    private Button btnRegister;
    // Initialize Firebase Auth
    private FirebaseAuth mAuth;
    AlertDialog.Builder builder;
    private ProgressDialog progressDialog;
    private DatabaseReference usersDatabase;
    private String languagePrefer;
    private Context context;
    private Resources resources;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate( R.layout.fragment_register, container, false );

        LoginActivity activity = (LoginActivity) getActivity();
        languagePrefer = activity.getLanguage();

        initialize();
        return mRootView;

    }

    private void initialize() {
        progressDialog = new ProgressDialog( getContext() );

        tieEmailAddress = mRootView.findViewById( R.id.tieEmailAddress );
        tiePassword = mRootView.findViewById( R.id.tiePassword );
        tiePassword2 = mRootView.findViewById( R.id.tiePassword2 );
        btnRegister = mRootView.findViewById( R.id.btnRegister );

        context = LocaleHelper.setLocale(mRootView.getContext(), languagePrefer);
        resources = context.getResources();
        //tieEmailAddress.setHint(resources.getString(R.string.email_address));
        //tiePassword.setHint(resources.getString(R.string.password));
        //tiePassword2.setHint(resources.getString(R.string.confirm_password));
        btnRegister.setText(resources.getString(R.string.signup));

        btnRegister.setOnClickListener( this );
        usersDatabase = FirebaseDatabase.getInstance().getReference( "Users" );
    }

    @Override
    public void onClick(View view) {
        mAuth = FirebaseAuth.getInstance();
        String strEmail = tieEmailAddress.getText().toString();
        String strPassword = tiePassword.getText().toString();
        String strPassword2 = tiePassword2.getText().toString();
        if (strEmail.isEmpty() || strPassword.isEmpty()) {
            showAlert(resources.getString(R.string.emailEmpty));
            return;
        }
        if (!strPassword.equals(strPassword2)) {
            showAlert(resources.getString(R.string.notsamepass));
        }
        else {
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword( strEmail, strPassword)
                    .addOnCompleteListener( getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                addUpdateUser( new User(strEmail,languagePrefer) );
                                Intent intent = new Intent( getActivity(), MainActivity.class );
                                intent.putExtra("languagePrefer",languagePrefer);
                                startActivity( intent );
                                getActivity().finishAffinity();
                            } else {
                                showAlert(resources.getString(R.string.somethingwrong));
                            }
                        }
                    } );
        }
    }

    private void addUpdateUser(User user) {
        String safeEmail = user.getEmailAddress()
                .replace("@", "-")
                .replace(".", "-");
        usersDatabase.child(safeEmail).setValue(user);
    }


    private void showAlert(String message) {

        builder = new AlertDialog.Builder( getContext() );

        builder.setTitle( "Message" )
                .setMessage( message )
                .setCancelable( true )
                .setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent( getActivity(), LoginActivity.class );
                        startActivity( intent );
                    }
                } )
                .show();

    }
}
