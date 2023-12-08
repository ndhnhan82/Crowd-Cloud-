package com.lasalle.crowdcloud.adapter;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lasalle.crowdcloud.R;

import java.util.ArrayList;

import model.DatabaseManagement;
import model.History;

public class HistoryAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<History> histories;
    private History history;
    public static final int USER_HISTORY = 1;
    private int fragment;

    public HistoryAdapter() {
    }

    public HistoryAdapter(Context context, ArrayList<History> histories, int fragment) {
        this.context = context;
        this.histories = histories;
        this.fragment = fragment;


    }

    @Override
    public int getCount() {
        return histories.size();
    }

    @Override
    public Object getItem(int i) {
        return histories.get( i );
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        //1- perform the required declaration
        View oneItem = null;
        ImageView imDelete;
        TextView tvLocation, tvInfo;
        //2- convert the layout one_item.xml to the corresponding java view objects
        //in order to manipulate different widgets -This conversion is called  Layout inflation
        LayoutInflater layoutInf = LayoutInflater.from( context );
        // change xml to java objects
        oneItem = layoutInf.inflate( R.layout.one_history_item, viewGroup, false );

        //3- to reference the widgets using findViewById inside to one_item.xml
        tvLocation = oneItem.findViewById( R.id.tvHistoryLocationItem );
        tvInfo = oneItem.findViewById( R.id.tvLatLongItem );
        imDelete = oneItem.findViewById( R.id.btnDelete );

        history = (History) getItem( position );
        tvLocation.setText( history.getLocation() );
        tvInfo.setText( String.format( "Lat: %.2f - Long: %.2f", history.getLatitude(), history.getLongitude() ) );

        View finalOneItem = oneItem;
        imDelete.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                if (id == R.id.btnDelete) {
                    String safeEmail = DatabaseManagement.getSafeEmailOfCurrentUser();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder( finalOneItem.getContext() );
                    alertDialog.setTitle( "Deletion" );
                    alertDialog.setMessage( "Do you want to delete this user?" );
                    alertDialog.setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == Dialog.BUTTON_POSITIVE) {
                                DatabaseReference historyRef = FirebaseDatabase.getInstance()
                                        .getReference( "Users" )
                                        .child( safeEmail ).child( "History" );
                                histories.remove( position );
                                notifyDataSetChanged();
                                historyRef.child( tvLocation.getText().toString() ).removeValue();
                            }
                        }
                    } );
                    alertDialog.setNegativeButton( "No", null);
                    alertDialog.create().show();
                    
                }
            }
        } );

        return oneItem;
    }
}
