package model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DatabaseManagement {


    public static void getUserHistoryList(String safeEmail, final HistoryListCallback callback) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference( "Users" )
                .child( safeEmail ).child( "History" );
        ArrayList<History> arrListUserHistory = new ArrayList<>();

        userRef.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                History history = snapshot.getValue( History.class );
                    arrListUserHistory.add( history );
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle changes if needed
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Handle removal if needed
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle move if needed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
                callback.onFailure( error.toException() );
            }
        } );

        // After all child events are processed, notify the callback
        userRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                callback.onHistoryListUpdated( arrListUserHistory );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
                callback.onFailure( error.toException() );
            }
        } );
    }


    public interface HistoryListCallback {
        void onHistoryListUpdated(ArrayList<History> userList);

        void onFailure(Exception e);

        void onFavoriteListUpdated(ArrayList<Favorite> arrListUserFavorite);
    }

    public void showAlert(Context context, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder( context );

        builder.setTitle( "Message" )
                .setMessage( message )
                .setCancelable( true )
                .setPositiveButton( "OK", null )
                .show();

    }
    public static String getSafeEmailOfCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser().getEmail()
                .replace( "@","-" )
                .replace( ".","-" );
    }

    public static void getUserFavoriteList(String safeEmail, final FavoriteListCallback callback) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference( "Users" )
                .child( safeEmail ).child( "favorite" );
        ArrayList <Favorite> arrListUserFavorite = new ArrayList<Favorite>();

        userRef.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Favorite favorite = snapshot.getValue( Favorite.class );
                arrListUserFavorite.add( favorite );
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle changes if needed
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Handle removal if needed
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle move if needed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
                callback.onFailure( error.toException() );
            }
        } );

        // After all child events are processed, notify the callback
        userRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                callback.onFavoriteListUpdated( arrListUserFavorite );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
                callback.onFailure( error.toException() );
            }
        } );
    }
    public interface FavoriteListCallback {
        void onFavoriteListUpdated(ArrayList<Favorite> userList);


        void onFailure(Exception e);
    }
}