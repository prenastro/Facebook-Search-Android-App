package com.example.prena.myapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link placesTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class placesTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static List<items> itemsList = null;
    public static List<items> favList = null;
    public static  String pagingDataNext = null;
    public static String pagingDataPrevious = null;
    public static String PLACE_DATA = "pagesPagingData";
    public static boolean isFav;

    private OnFragmentInteractionListener4 mListener;

    public placesTab(String place_data) {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment placesTab.
     */
    // TODO: Rename and change types and number of parameters
    public static placesTab newInstance(String place_data) {
        placesTab fragment = new placesTab(place_data);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, place_data);
        isFav = false;

        itemsList = new ArrayList<>();
        if(place_data!= null){
            try {
                JSONObject placeObject = new JSONObject(place_data);
                JSONArray myArray = placeObject.getJSONArray("data");
                for (int i = 0 ; i < myArray.length(); i++) {
                    JSONObject obj = myArray.getJSONObject(i);
                    items item = new items();
                    item.setId(obj.getString("id"));
                    item.setType("places");
                    item.setName(obj.getString("name"));
                    String pictureUrl = null;
                    if(((JSONObject)obj.get("picture")).has("data")){
                        pictureUrl = ((JSONObject)((JSONObject)obj.get("picture")).get("data")).get("url").toString();
                    } else {
                        pictureUrl = ((JSONObject)obj.get("picture")).get("url").toString();
                    }
                    JSONObject picture = obj.getJSONObject("picture");
                    item.setUrl(pictureUrl);
                    itemsList.add(item);
                }
                if(placeObject.has("pager")) {
                    if (((JSONObject) placeObject.get("pager")).has("paging")){
                        if (((JSONObject)((JSONObject) placeObject.get("pager")).get("paging")).has("next")) {
                            fragment.pagingDataNext = ((JSONObject)((JSONObject) placeObject.get("pager")).get("paging")).get("next").toString();
                        } else {
                            fragment.pagingDataNext = null;
                        }
                        if (((JSONObject)((JSONObject) placeObject.get("pager")).get("previous")).has("previous")) {
                            fragment.pagingDataPrevious = ((JSONObject)((JSONObject) placeObject.get("pager")).get("paging")).get("previous").toString();
                        } else {
                            fragment.pagingDataPrevious = null;
                        }
                    } else {
                        if (((JSONObject) placeObject.get("paging")).has("next")) {
                            pagingDataNext = ((JSONObject) placeObject.get("paging")).get("next").toString();
                        } else {
                            pagingDataNext = null;
                        }
                        if (((JSONObject) placeObject.get("paging")).has("previous")) {
                            pagingDataPrevious = ((JSONObject) placeObject.get("paging")).get("previous").toString();
                        } else {
                            pagingDataPrevious = null;
                        }
                    }
                } else {
                    if (((JSONObject) placeObject.get("paging")).has("next")) {
                        pagingDataNext = ((JSONObject) placeObject.get("paging")).get("next").toString();
                    } else {
                        pagingDataNext = null;
                    }
                    if (((JSONObject) placeObject.get("paging")).has("previous")) {
                        pagingDataPrevious = ((JSONObject) placeObject.get("paging")).get("previous").toString();
                    } else {
                        pagingDataPrevious = null;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        fragment.setArguments(args);
        return fragment;
    }

    public static placesTab newInstance(List<items> place_data) {
        placesTab fragment = new placesTab(new JSONArray(place_data).toString());
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, new JSONArray(place_data).toString());
        isFav = true;
        favList = new ArrayList< >();
        if(place_data!= null){
            favList = new ArrayList<items>(place_data);
            isFav = true;
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
        View view = inflater.inflate(R.layout.fragment_places_tab, container, false);
        ListView placeView = (ListView) view.findViewById(R.id.placeView);
        tableAdapter adapter;
        Button previous = (Button) view.findViewById(R.id.placeprev);
        Button next = (Button) view.findViewById(R.id.placenext);

        if(pagingDataNext == null){
            next.setClickable(false);
        } else {
            next.setClickable(true);
            try {
                next.setOnClickListener(new placesTab.MyListener(this.getContext(),this.pagingDataNext, "next"));
            } catch (Exception e) {

            }
        }

        if(pagingDataPrevious == null){
            previous.setClickable(false);
        } else {
            previous.setClickable(true);
            try {
                previous.setOnClickListener(new placesTab.MyListener(this.getContext(),pagingDataPrevious,"previous"));
            } catch (Exception e) {

            }
        }

        if(isFav == false) {
            adapter = new tableAdapter(this.getContext(), R.layout.tablerow, itemsList);
        } else{
            adapter = new tableAdapter(this.getContext(), R.layout.tablerow, favList);
            //isFav = false;
        }
        placeView.setAdapter(adapter);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    private class MyListener implements View.OnClickListener{
        private Context mContext;
        private String pagingNext;
        private String identifier;
        public MyListener(Context context, String pagingData, String identifier){
            mContext = context;
            this.pagingNext = pagingData;
            this.identifier = identifier;
        }

        @Override
        public void onClick(View v){
            Intent intent = new Intent(mContext, TabbedActivity.class);

            if(this.identifier.equalsIgnoreCase("next")) {
                intent.putExtra(PLACE_DATA, pagingDataNext);
            } else if(this.identifier.equalsIgnoreCase("previous")){
                intent.putExtra(PLACE_DATA, pagingDataPrevious);
            }
            mContext.startActivity(intent);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener4) {
            mListener = (OnFragmentInteractionListener4) context;
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
    public interface OnFragmentInteractionListener4 {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}