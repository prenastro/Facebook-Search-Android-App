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
 * Created by prena on 4/13/2017.
 */
public class HttpGetRequestPaging extends AsyncTask<String, Void, JSONObject> {
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    public String type;
    public String fragment;
    public asyncresponse delegate = null;

    //http request to get table data
    public HttpGetRequestPaging(asyncresponse delegate, String type,String fragment){
        this.delegate = delegate;
        this.type = type;
        this.fragment = fragment;
    }
    protected JSONObject  doInBackground(String... params) {
        String keyword = params[0];
        //String Url = "http://sample-env-1.mmrai338xg.us-west-2.elasticbeanstalk.com/index.php?keyword="+keyword;
        JSONObject jsonData = null;
        String inputLine;
        try {
            URL myUrl = new URL(params[0]);
            HttpURLConnection connection =(HttpURLConnection)myUrl.openConnection();

            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            connection.connect();

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }
            reader.close();
            streamReader.close();
            jsonData = new JSONObject( stringBuilder.toString());
        }catch (IOException e){
            e.printStackTrace();
            jsonData = null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonData.put("type", fragment);
        } catch(Exception e){

        }
        return jsonData;
    }
    @Override
    protected void onPostExecute(JSONObject jsonData){
        if(fragment!="none"){
            ((TabbedActivity) delegate).displayPagination(jsonData.toString(),fragment);
            fragment = "none";
        }
    }

}
