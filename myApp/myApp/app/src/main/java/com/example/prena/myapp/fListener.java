package com.example.prena.myapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prena on 4/17/2017.
 */

public class fListener implements View.OnClickListener {
    private Context fContext;
    private items favData;

    public fListener(Context context, items data){
        fContext = context;
        favData = data;
    }

    @Override
    public void onClick(View v){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(fContext);

        boolean exists = false;
        int location= -1;
        Gson gson = new Gson();
        Type type = new TypeToken<List<items>>(){}.getType();
        String json = appSharedPrefs.getString("favItems", "");
        List<items> favList = gson.fromJson(json, type);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        if(favList == null){
            favList = new ArrayList<items>();
            favList.add(this.favData);
            String storeValue = gson.toJson(favList);
            prefsEditor.putString("favItems", storeValue);
            prefsEditor.commit();
        } else {
            exists = false;
            int i = -1;
            for(items favData : favList){
                i++;
                if(favData.getId().equalsIgnoreCase(this.favData.getId())){
                    exists = true;
                    location=i;
                }
            }
            if(exists){
                favList.remove(location);
            } else {
                favList.add(this.favData);
            }
            String storeValue = gson.toJson(favList);
            prefsEditor.putString("favItems", storeValue);
            prefsEditor.commit();
        }

        ImageView favImage = (ImageView) v.findViewById(R.id.favourite);

        if (favImage.getTag().toString().equalsIgnoreCase("1")) {
            favImage.setImageResource(R.mipmap.fav_on);

            favImage.setTag(2);

        } else {
            favImage.setImageResource(R.mipmap.fav_off);
            favImage.setTag(1);

        }
    }

}
