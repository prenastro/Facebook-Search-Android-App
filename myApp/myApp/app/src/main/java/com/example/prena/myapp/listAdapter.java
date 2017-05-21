package com.example.prena.myapp;

/**
 * Created by Prena on 4/16/2017.
 */

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class listAdapter extends BaseExpandableListAdapter{
    private Context context;
    private List<String> listTitle;
    private HashMap<String, List<String>> listDetail;

    public listAdapter(Context context, List<String> listTitle,
                                       HashMap<String, List<String>> listDetail) {
        this.context = context;
        this.listTitle = listTitle;
        this.listDetail = listDetail;
    }

    @Override
    public Object getChild(int listPosition, int expListPosition) {
        return this.listDetail.get(this.listTitle.get(listPosition))
                .get(expListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expListPosition) {
        return expListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expListText = (String) getChild(listPosition, expListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.album_item, null);
        }
        ImageView expandedListImageView = (ImageView) convertView
                .findViewById(R.id.child_img);
        Picasso.with(context).load(expListText).into(expandedListImageView);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.listDetail.get(this.listTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.listTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.album_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.parent_txt);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
