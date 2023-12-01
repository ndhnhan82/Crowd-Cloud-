package fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.lasalle.crowdcloud.MainActivity;
import com.lasalle.crowdcloud.R;
import com.lasalle.crowdcloud.adapter.PlaceAutoSuggestAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Preference#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Preference extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;

    public Preference() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Preference.
     */
    // TODO: Rename and change types and number of parameters
    public static Preference newInstance(String param1, String param2) {
        Preference fragment = new Preference();
        Bundle args = new Bundle();
        args.putString( ARG_PARAM1, param1 );
        args.putString( ARG_PARAM2, param2 );
        fragment.setArguments( args );
        return fragment;
    }

    Switch sbTheme;
    AutoCompleteTextView edLocation;
    Button btnFinish;
    Boolean switchChecked = false;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (getArguments() != null) {
            mParam1 = getArguments().getString( ARG_PARAM1 );
            mParam2 = getArguments().getString( ARG_PARAM2 );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();

        view = inflater.inflate( R.layout.fragment_preference, container, false );
        edLocation = view.findViewById(R.id.edLocation);
        btnFinish = view.findViewById(R.id.btnFinish);

        edLocation.setAdapter(new PlaceAutoSuggestAdapter(context,
                android.R.layout.simple_list_item_1));

        // Inflate the layout for this fragment
        return view;
    }

}