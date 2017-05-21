package com.example.prena.myapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link posts.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link posts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class posts extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static List<postLists> postsList = null;
    public static String name;
    public static String url;

    private OnFragmentInteractionListenerposts mListener;

    public posts() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment posts.
     */
    // TODO: Rename and change types and number of parameters
    public static posts newInstance(String post_data) {
        posts fragment = new posts();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, post_data);
        postsList = new ArrayList<>();
        if(post_data!= null){
            try {
                JSONObject userObject = new JSONObject(post_data);
//                name = userObject.getString("name");
//                JSONObject picture = userObject.getJSONObject("picture");
//                url = picture.getString("url");
                JSONArray myArray = userObject.getJSONArray("posts");
                for (int i = 0 ; i < myArray.length(); i++) {
                    JSONObject obj = myArray.getJSONObject(i);
                    postLists item = new postLists();
                    item.setName(userObject.getString("name"));
                    JSONObject picture = userObject.getJSONObject("picture");
                    item.setUrl(picture.getString("url"));
                    if(obj.getString("message")!=null){
                        item.setMessage(obj.getString("message"));
                    }
                    JSONObject createdTime = obj.getJSONObject("created_time");
                    item.setDate(createdTime.getString("date"));
                    postsList.add(item);
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
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        if(postsList.size()>0) {
            ListView postView = (ListView) view.findViewById(R.id.postsView);
            postAdapter adapter = new postAdapter(this.getContext(), R.layout.postrow, postsList);
            postView.setAdapter(adapter);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.noPosts);
            textView.setText("No Posts Found");
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
        if (context instanceof OnFragmentInteractionListenerposts) {
            mListener = (OnFragmentInteractionListenerposts) context;
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
    public interface OnFragmentInteractionListenerposts {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
