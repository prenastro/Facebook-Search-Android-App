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
 * Use the {@link usersTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class usersTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static List<items> itemsList = null;
    public static List<items> favList = null;
    public static boolean isFav;
    public static  String pagingDataNext = null;
    public static String pagingDataPrevious = null;
    public static String USER_DATA = "usersPagingData";

    private OnFragmentInteractionListener1 mListener;

    public usersTab(String user_data) {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment usersTab.
     */
    // TODO: Rename and change types and number of parameters
    public static usersTab newInstance(String user_data) {
        usersTab fragment = new usersTab(user_data);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, user_data);
        isFav = false;
        itemsList = new ArrayList< >();
        if(user_data!= null){
            try {
                JSONObject userObject = new JSONObject(user_data);
                JSONArray myArray = userObject.getJSONArray("data");
                for (int i = 0 ; i < myArray.length(); i++) {
                    JSONObject obj = myArray.getJSONObject(i);
                    items item = new items();
                    item.setId(obj.getString("id"));
                    item.setType("users");
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
                if(userObject.has("pager")) {
                    if (((JSONObject) userObject.get("pager")).has("paging")){
                        if (((JSONObject)((JSONObject) userObject.get("pager")).get("paging")).has("next")) {
                            fragment.pagingDataNext = ((JSONObject)((JSONObject) userObject.get("pager")).get("paging")).get("next").toString();
                        } else {
                            fragment.pagingDataNext = null;
                        }
                        if (((JSONObject)((JSONObject) userObject.get("pager")).get("previous")).has("previous")) {
                            fragment.pagingDataPrevious = ((JSONObject)((JSONObject) userObject.get("pager")).get("paging")).get("previous").toString();
                        } else {
                            fragment.pagingDataPrevious = null;
                        }
                    } else {
                        if (((JSONObject) userObject.get("paging")).has("next")) {
                            pagingDataNext = ((JSONObject) userObject.get("paging")).get("next").toString();
                        } else {
                            pagingDataNext = null;
                        }
                        if (((JSONObject) userObject.get("paging")).has("previous")) {
                            pagingDataPrevious = ((JSONObject) userObject.get("paging")).get("previous").toString();
                        } else {
                            pagingDataPrevious = null;
                        }
                    }
                } else {
                    if (((JSONObject) userObject.get("paging")).has("next")) {
                        pagingDataNext = ((JSONObject) userObject.get("paging")).get("next").toString();
                    } else {
                        pagingDataNext = null;
                    }
                    if (((JSONObject) userObject.get("paging")).has("previous")) {
                        pagingDataPrevious = ((JSONObject) userObject.get("paging")).get("previous").toString();
                    } else {
                        pagingDataPrevious = null;
                    }
                }
//                tableAdapter adapter = new tableAdapter(getApplicationContext(),R.layout.tablerow,itemList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        fragment.setArguments(args);
        return fragment;
    }
    //for favourites
    public static usersTab newInstance(List<items> user_data) {
        usersTab fragment = new usersTab(new JSONArray(user_data).toString());
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, new JSONArray(user_data).toString());
        isFav = true;
        favList = new ArrayList< >();
        if(user_data!= null){
            favList = new ArrayList<items>(user_data);
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
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_tab, container, false);
        ListView userView = (ListView) view.findViewById(R.id.userView);
        tableAdapter adapter;
        Button previous = (Button) view.findViewById(R.id.userprev);
        Button next = (Button) view.findViewById(R.id.usernext);

        if(pagingDataNext == null){
            next.setClickable(false);
        } else {
            next.setClickable(true);
            try {
                next.setOnClickListener(new MyListener(this.getContext(),this.pagingDataNext, "next"));
            } catch (Exception e) {

            }
        }

        if(pagingDataPrevious == null){
            previous.setClickable(false);
        } else {
            previous.setClickable(true);
            try {
                previous.setOnClickListener(new MyListener(this.getContext(),pagingDataPrevious,"previous"));
            } catch (Exception e) {

            }
        }
        if(isFav == false) {
            adapter = new tableAdapter(this.getContext(), R.layout.tablerow, itemsList);
        } else{
            adapter = new tableAdapter(this.getContext(), R.layout.tablerow, favList);
            //isFav = false;
        }
        userView.setAdapter(adapter);
        return view;
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
                intent.putExtra(USER_DATA, pagingDataNext);
            } else if(this.identifier.equalsIgnoreCase("previous")){
                intent.putExtra(USER_DATA, pagingDataPrevious);
            }
            mContext.startActivity(intent);
        }
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
        if (context instanceof OnFragmentInteractionListener1) {
            mListener = (OnFragmentInteractionListener1) context;
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
    public interface OnFragmentInteractionListener1 {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
