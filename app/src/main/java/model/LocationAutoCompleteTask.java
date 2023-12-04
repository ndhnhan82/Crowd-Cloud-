package model;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class LocationAutoCompleteTask extends ArrayAdapter<String> implements Filterable {

    private static final String GEOLOCATE_API_KEY = "bd5e378503939ddaee76f12ad7a97608";
    private static final String GEOLOCATE_API_URL = "https://api.openweathermap.org/geo/1.0/direct";

    private List<String> suggestions;

    public LocationAutoCompleteTask(AutoCompleteTextView autoCompleteTextView) {
        super(autoCompleteTextView.getContext(), android.R.layout.simple_dropdown_item_1line);
        this.suggestions = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public String getItem(int position) {
        return suggestions.get(position);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    suggestions = autoComplete(constraint.toString());
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    private List<String> autoComplete(String input) {
        List<String> suggestions = new ArrayList<>();
        HttpURLConnection connection = null;
        try {
            // Construct the URL for the geolocation API
            String query = URLEncoder.encode(input, "utf-8");
            URL url = new URL(GEOLOCATE_API_URL + "?q=" + query + "&limit=5&appid=" + GEOLOCATE_API_KEY);

            // Establish the connection
            connection = (HttpURLConnection) url.openConnection();

            // Read the response
            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            int data = reader.read();
            StringBuilder response = new StringBuilder();
            while (data != -1) {
                char current = (char) data;
                response.append(current);
                data = reader.read();
            }

            // Parse the JSON response
            JSONArray jsonArray = new JSONArray(response.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject locationObject = jsonArray.getJSONObject(i);
                String cityName = locationObject.getString("name");
                String stateName = locationObject.getString("state");
                String countryName = locationObject.getString("country");

                suggestions.add(cityName + "," + stateName + "," + countryName);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return suggestions;
    }
}
