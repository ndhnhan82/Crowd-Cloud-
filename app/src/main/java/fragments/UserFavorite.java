package fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lasalle.crowdcloud.R;
import com.lasalle.crowdcloud.adapter.FavoriteAdapter;

import java.util.ArrayList;

import model.DatabaseManagement;
import model.Favorite;

public class UserFavorite extends Fragment {

    private ListView lvFavorite;
    private View view;
    private Fragment fragment;
    private FavoriteAdapter favoriteAdapter;
    private static final String ARG_PARAM = "FromUserFavoriteList";

    public UserFavorite() {
    }

    private void launchHome(Favorite selectedFavorite) {

        fragment = new Home();
        Bundle args = new Bundle();
        args.putString( ARG_PARAM, selectedFavorite.getKey() );
        fragment.setArguments( args );

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace( R.id.content_frame, fragment ).addToBackStack( null ).commit();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate( R.layout.fragment_favorite, container, false );

        lvFavorite = (ListView) view.findViewById( R.id.lvFavorite );
        String safeEmail = DatabaseManagement.getSafeEmailOfCurrentUser();
        DatabaseManagement.getUserFavoriteList(safeEmail, new DatabaseManagement.FavoriteListCallback() {
            @Override
            public void onFavoriteListUpdated(ArrayList<Favorite> userList) {
                favoriteAdapter = new FavoriteAdapter(view.getContext(), userList , FavoriteAdapter.USER_FAVORITE);
                lvFavorite.setAdapter(favoriteAdapter);
                lvFavorite.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Favorite selectedUser = (Favorite) lvFavorite.getItemAtPosition( position );
                        launchHome( selectedUser );
                    }
                } );
            }
            @Override
            public void onFailure(Exception e) {
                Log.d( "ERROR", e.getMessage() );
            }
        } );

        return view;
    }
}