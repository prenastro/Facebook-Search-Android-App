package com.example.prena.myapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.List;


/**
 * Created by Prena on 4/15/2017.
 */

public class tableAdapter extends ArrayAdapter<items>{

    private List<items> itemsList;
    private int resource;
    private LayoutInflater inflater;
    public tableAdapter(@NonNull Context context, int resource, List<items> objects) {
        super(context, resource, objects);
        itemsList = objects;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = inflater.inflate(resource,null);
        }
        ImageView dataimage;
        TextView name;
        ImageView favourite;
        ImageView details;

        items itemData = getItem(position);
        dataimage = (ImageView) convertView.findViewById(R.id.dataimage);
        name = (TextView) convertView.findViewById(R.id.name);
        favourite = (ImageView) convertView.findViewById(R.id.favourite);

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getContext());

        Gson gson = new Gson();
        Type type = new TypeToken<List<items>>(){}.getType();
        String json = appSharedPrefs.getString("favItems", "");
        List<items> itemsList = gson.fromJson(json, type);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        if(itemsList != null){

            boolean fav = false;
            for(items item : itemsList){
                if(item.getId().equalsIgnoreCase(itemData.getId())){
                    fav = true;
                }
            }
            if(fav){
                favourite.setImageResource(R.mipmap.fav_on);

                favourite.setTag(2);
            } else {
                favourite.setImageResource(R.mipmap.fav_off);

                favourite.setTag(1);
            }
        }


        favourite.setOnClickListener(new fListener(this.getContext(),itemData));

        details = (ImageView) convertView.findViewById(R.id.details);
        details.setOnClickListener(new dListener(this.getContext(),itemData.getId(),itemData.getName(),itemData.getUrl(),itemData));

        Picasso.with(this.getContext()).load(itemData.getUrl()).into(dataimage);
        name.setText(itemData.getName());

        return convertView;
    }
}


