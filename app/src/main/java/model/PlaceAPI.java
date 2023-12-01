package model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PlaceAPI {
    public ArrayList<String> autoComplete(String input){
        ArrayList<String> arrayList = new ArrayList<String>();
        HttpURLConnection connection = null;
        StringBuilder jsonResult = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder("http://api.positionstack.com/v1/forward?");
            sb.append("access_key=e61d6ade5f3c6a251406b8aa9dfbcef7");
            sb.append("&query="+input);
            URL url = new URL(sb.toString());
            connection = (HttpURLConnection) url.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader(
                    connection.getInputStream());

            int read;

            char[] buff = new char[1024];
            while((read = inputStreamReader.read(buff))!=-1){
                jsonResult.append(buff,0,read);
            }

        } catch (MalformedURLException e){
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if(connection!=null){
                connection.disconnect();
            }
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonResult.toString());
            JSONArray data = jsonObject.getJSONArray("data");
            for (int i = 0;i<data.length();i++) {
                arrayList.add(data.getJSONObject(i).getString("label"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

}
