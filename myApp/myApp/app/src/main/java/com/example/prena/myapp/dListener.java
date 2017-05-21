package com.example.prena.myapp;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.example.prena.myapp.R;

import java.io.Serializable;

//import static android.nfc.NfcAdapter.EXTRA_ID;

/**
 * Created by prena on 4/16/2017.
 */

public class dListener implements View.OnClickListener{
    private Context dContext;
    private String dId;
    private String dName;
    private String dUrl;
    private items dItem;
    public static String EXTRA_ID = "ID";
    public static String EXTRA_NAME = "NAME";
    public static String EXTRA_URL = "URL";
    public static String EXTRA_FAV ="FAV";
    public static String EXTRA_ITEM ="item";

    public dListener(Context context, String id, String name, String url,items item) {
        dContext =context;
        dId = id;
        dName = name;
        dUrl = url;
        dItem = item;
    }
    @Override
    public void onClick(View v){
        Intent intent = new Intent(dContext, DetailsTabActivity.class);
        intent.putExtra(EXTRA_ID, dId);
        intent.putExtra(EXTRA_NAME, dName);
        intent.putExtra(EXTRA_URL, dUrl);
        intent.putExtra(EXTRA_ITEM, (Serializable) this.dItem);
        ImageView image = (ImageView) v.findViewById(R.id.favourite);
        if(image != null){
            if(image.getTag().toString().equalsIgnoreCase("2")){
                intent.putExtra(EXTRA_FAV, true);
            } else {
                intent.putExtra(EXTRA_FAV, false);
            }
        }
        dContext.startActivity(intent);
    }
}
