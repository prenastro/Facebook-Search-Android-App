package com.example.prena.myapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link albums.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link albums#newInstance} factory method to
 * create an instance of this fragment.
 */
public class albums extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";


    // TODO: Rename and change types of parameters
    private String mParam1;
    static HashMap<String,List<String>> albumDetails;
    static List<String> albumList;
    static ExpandableListView expList;
    static ExpandableListAdapter Adapter;
    static ExpandableListView ListView;
    static List<String> Title;

    private OnFragmentInteractionListeneralbums mListener;

    public albums() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment albums.
     */
    // TODO: Rename and change types and number of parameters
    //albums new instance
    public static albums newInstance(String album_data) {
        albums fragment = new albums();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, album_data);
        albumDetails = new HashMap<String,List<String>>();
        albumList = new ArrayList< >();
        if(album_data!= null){
            try {
                JSONObject userObject = new JSONObject(album_data);
                JSONArray myArray = userObject.getJSONArray("albums");
                lists list =new lists();
                String title;
                String image;
                for (int i = 0 ; i < myArray.length(); i++) {
                    JSONObject obj = myArray.getJSONObject(i);
                    list.setTitle(obj.getString("name"));

                    JSONArray photoArray = obj.getJSONArray("photos");
                    for (int j=0; j< photoArray.length(); j++){
                        JSONObject obj1 = photoArray.getJSONObject(j);
                        //list.setPicture(obj1.getString("picture"));
                        albumList.add("https://graph.facebook.com/v2.8/"+obj1.getString("id")+"/picture?type(large)&access_token=EAABbEqHnnuwBALrluaZCrOhgJZCjV5DfcJLrmrbaczS4BBEzAUUOaHjxHvoZAZAPOcF2tazFqADaEMZAmRt5tYxmRiqSawCxC8eEZCSBRw7i94nYk5GgqSURjLPuMmZCaUFOYFl5d5TET1Vg8bGKK5w");
                    }
                    list.setPhotoList(albumList);
                    albumDetails.put(list.getTitle(),albumList);
                    albumList = new ArrayList<>();
                }
//                tableAdapter adapter = new tableAdapter(getApplicationContext(),R.layout.tablerow,itemList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_albums, container, false);
        if(albumDetails.size()>0) {
            Title = new ArrayList<String>(albumDetails.keySet());
            Adapter = new listAdapter(this.getContext(), Title, albumDetails);
            ListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
            ListView.setAdapter(Adapter);
        }else {
            TextView textView = (TextView) view.findViewById(R.id.noAlbums);
            textView.setText("No Albums Found");
            textView.setPadding(40,20,0,0);
            textView.setTextSize(30);
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListeneralbums) {
            mListener = (OnFragmentInteractionListeneralbums) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListeneralbums {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
