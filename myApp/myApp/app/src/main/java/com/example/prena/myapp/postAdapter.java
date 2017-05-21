package com.example.prena.myapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Prena on 4/17/2017.
 */

public class postAdapter extends ArrayAdapter<postLists>{
        private List<postLists> postList;
        private int resource;
        private LayoutInflater inflater;
    public postAdapter(Context context, int resource, List<postLists> objects) {
        super(context, resource, objects);
        postList = objects;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = inflater.inflate(resource,null);
        }
        ImageView postImage;
        TextView message;

        postLists itemData = getItem(position);
        postImage = (ImageView) convertView.findViewById(R.id.postimage);
        message = (TextView) convertView.findViewById(R.id.message);

        Picasso.with(this.getContext()).load(itemData.getUrl()).into(postImage);
//        String message1 = itemData.getMessage();
        message.setText(itemData.getName()+"\n"+itemData.getDate()+"\n\n"+itemData.getMessage());

        return convertView;
    }
}
