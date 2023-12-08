package com.lasalle.crowdcloud.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import model.Favorite;

public class FavoriteAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Favorite> favorites;
    private Favorite favorite;
    public static final int USER_FAVORITE = 1;
    private int fragment;

    public FavoriteAdapter() {
    }

    public FavoriteAdapter(Context context, ArrayList<Favorite> favorites, int fragment) {
        this.context = context;
        this.favorites = favorites;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return favorites.size();
    }

    @Override
    public Object getItem(int i) {
        return favorites.get( i );
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
        tvLocation = oneItem.findViewById( R.id.tvFavoriteLocationItem);

        imDelete = oneItem.findViewById( R.id.btnDelete );

        favorite = (Favorite) getItem( position );
        tvLocation.setText( favorite.getKey() );
        View finalOneItem = oneItem;
        imDelete.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int id = v.getId();
                if (id == R.id.btnDelete) {
                    String safeEmail = DatabaseManagement.getSafeEmailOfCurrentUser();

                                DatabaseReference historyRef = FirebaseDatabase.getInstance()
                                        .getReference( "Users" )
                                        .child( safeEmail ).child( "favorite" );
                                historyRef.child( tvLocation.getText().toString()).setValue(null);
                            }
                        }
                    } );
                    favorites.remove( position );
                    notifyDataSetChanged();
        return oneItem;
    }
}
