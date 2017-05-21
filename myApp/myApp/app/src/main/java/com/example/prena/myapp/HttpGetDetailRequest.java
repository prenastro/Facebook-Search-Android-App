package com.example.prena.myapp;

import android.os.AsyncTask;

import com.example.prena.myapp.interfaces.asyncresponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Prena on 4/16/2017.
 */

public class HttpGetDetailRequest extends AsyncTask<String, Void, JSONObject> {
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    public asyncresponse delegate = null;

    public HttpGetDetailRequest(asyncresponse delegate) {
        this.delegate = delegate;
    }

    protected JSONObject doInBackground(String... params) {
        String id = params[0];
        String Url = "http://sample-env-1.mmrai338xg.us-west-2.elasticbeanstalk.com/index.php?action=" + id;
        JSONObject jsonData = null;
        String inputLine;
        try {
            URL myUrl = new URL(Url);
            HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();

            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            connection.connect();

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            reader.close();
            streamReader.close();
            jsonData = new JSONObject(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
            jsonData = null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonData;
    }

    @Override
    protected void onPostExecute(JSONObject jsonData) {
        ((DetailsTabActivity) delegate).processFinish(jsonData);
    }
}
