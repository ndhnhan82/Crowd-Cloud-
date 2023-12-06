package fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.lasalle.crowdcloud.R;
import com.lasalle.crowdcloud.adapter.HistoryAdapter;

import java.util.ArrayList;

import model.DatabaseManagement;
import model.History;
import model.User;

public class UserHistory extends Fragment {

    private ListView lvHistory;
    private View view;

    private Fragment fragment;

    private HistoryAdapter historyAdapter;
    private static final String ARG_PARAM = "FromUserHistoryList";

    public UserHistory() {
    }

    private void launchHome(History selectedHistory) {

        fragment = new Home();
        Bundle args = new Bundle();
        args.putString( ARG_PARAM, selectedHistory.getLocation() );
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
        view = inflater.inflate( R.layout.fragment_user_history, container, false );

        lvHistory = (ListView) view.findViewById( R.id.lvCourse );
        String safeEmail = DatabaseManagement.getSafeEmailOfCurrentUser();
        DatabaseManagement.getUserHistoryList(safeEmail, new DatabaseManagement.HistoryListCallback() {
            @Override
            public void onHistoryListUpdated(ArrayList<History> userList) {
                historyAdapter = new HistoryAdapter( view.getContext(), userList ,HistoryAdapter.USER_HISTORY);
                lvHistory.setAdapter( historyAdapter );
                lvHistory.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        History selectedUser = (History) lvHistory.getItemAtPosition( position );
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