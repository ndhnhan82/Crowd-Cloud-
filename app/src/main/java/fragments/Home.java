package fragments;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lasalle.crowdcloud.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Fragmented Assets
    private AutoCompleteTextView edLocation;
    private Button btnFinish;
    private ScrollView svResults;
    private TextView tvCityResult, tvTemperatureResult, tvHumidityResult, tvDescriptionResult, tvWindSpeedResult, tvCloudinessResult, tvPressureResult;

    //API

    private static final String api = "a2efb35666c4f4327b66b2a93870e256";
    private static final String url = "https://api.openweathermap.org/data/2.5/weather";

    DecimalFormat df = new DecimalFormat("#.##");

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString( ARG_PARAM1, param1 );
        args.putString( ARG_PARAM2, param2 );
        fragment.setArguments( args );
        return fragment;
    }

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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize UI components
        edLocation = view.findViewById(R.id.edLocation);
        btnFinish = view.findViewById(R.id.btnFinish);
        svResults = view.findViewById(R.id.svResults);
        tvCityResult = view.findViewById(R.id.tvCityResult);
        tvTemperatureResult = view.findViewById(R.id.tvTemperatureResult);
        tvHumidityResult = view.findViewById(R.id.tvHumidityResult);
        tvDescriptionResult = view.findViewById(R.id.tvDescriptionResult);
        tvWindSpeedResult = view.findViewById(R.id.tvWindSpeedResult);
        tvCloudinessResult = view.findViewById(R.id.tvCloudinessResult);
        tvPressureResult = view.findViewById(R.id.tvPressureResult);


        // Set onClickListener for the Finish button
        btnFinish.setOnClickListener(v -> {
            // Get the entered location from the AutoCompleteTextView
            String location = edLocation.getText().toString().toLowerCase().trim();

            // Make API request using Volley
            fetchWeatherData(location);
        });

        return view;
    }

    private void fetchWeatherData(String location) {
        // Construct the API URL
        String apiUrl = url + "?q=" + location + "&appid=" + api;

        // Create a StringRequest to make the API request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the JSON response
                        handleWeatherResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Log.e("Volley Error", error.toString());
                        Toast.makeText(getContext(), "Error fetching weather data", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(requireContext()).add(stringRequest);
    }

    private void handleWeatherResponse(String response) {
        try {
            // Parsing the JSON response
            JSONObject jsonObject = new JSONObject(response);

            // Extracting relevant information
            String cityName = jsonObject.getString("name");
            double temperature = jsonObject.getJSONObject("main").getDouble("temp");
            int humidity = jsonObject.getJSONObject("main").getInt("humidity");
            JSONArray weatherArray = jsonObject.getJSONArray("weather");
            double windSpeed = jsonObject.getJSONObject("wind").getDouble("speed");
            int cloudiness = jsonObject.getJSONObject("clouds").getInt("all");
            int pressure = jsonObject.getJSONObject("main").getInt("pressure");



            double tempCelsius = Math.round(temperature - 273.15);


            // Updating UI with the extracted information
            tvCityResult.setText(cityName);
            tvTemperatureResult.setText(String.valueOf((int)tempCelsius + " °C"));
            tvHumidityResult.setText(String.valueOf(humidity));
            tvWindSpeedResult.setText(String.valueOf(windSpeed) + " kmph");
            tvCloudinessResult.setText(String.valueOf(cloudiness) + " %");
            tvPressureResult.setText(String.valueOf(pressure) + " mb");

            try {
                //extracting the description
                if (weatherArray.length() > 0) {
                    JSONObject weatherObject = weatherArray.getJSONObject(0);
                    String description = weatherObject.getString("description");
                    String main = weatherObject.getString("main");

                    tvDescriptionResult.setText(main + ", " + description);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }





        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}