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
 * Use the {@link pagesTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class pagesTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    public static  String pagingDataNext = null;
    public static String pagingDataPrevious = null;
    public static String PAGE_DATA = "pagesPagingData";
    public static List<items> itemsList = null;
    public static List<items> favList = null;
    public static boolean isFav;

    private OnFragmentInteractionListener2 mListener;

    public pagesTab(String page_data) {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment pagesTab.
     */
    // TODO: Rename and change types and number of parameters
    public static pagesTab newInstance(String page_data) {
        pagesTab fragment = new pagesTab(page_data);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, page_data);
        isFav = false;

        itemsList = new ArrayList<items>();
        if(page_data!= null){
            try {
                JSONObject pageObject = new JSONObject(page_data);
                JSONArray myArray = pageObject.getJSONArray("data");

                for (int i = 0 ; i < myArray.length(); i++) {
                    JSONObject obj = myArray.getJSONObject(i);
                    items item = new items();
                    item.setId(obj.getString("id"));
                    item.setType("pages");
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
                if(pageObject.has("pager")) {
                    if (((JSONObject) pageObject.get("pager")).has("paging")){
                        if (((JSONObject)((JSONObject) pageObject.get("pager")).get("paging")).has("next")) {
                            fragment.pagingDataNext = ((JSONObject)((JSONObject) pageObject.get("pager")).get("paging")).get("next").toString();
                        } else {
                            fragment.pagingDataNext = null;
                        }
                        if (((JSONObject)((JSONObject) pageObject.get("pager")).get("previous")).has("previous")) {
                            fragment.pagingDataPrevious = ((JSONObject)((JSONObject) pageObject.get("pager")).get("paging")).get("previous").toString();
                        } else {
                            fragment.pagingDataPrevious = null;
                        }
                    } else {
                        if (((JSONObject) pageObject.get("paging")).has("next")) {
                            pagingDataNext = ((JSONObject) pageObject.get("paging")).get("next").toString();
                        } else {
                            pagingDataNext = null;
                        }
                        if (((JSONObject) pageObject.get("paging")).has("previous")) {
                            pagingDataPrevious = ((JSONObject) pageObject.get("paging")).get("previous").toString();
                        } else {
                            pagingDataPrevious = null;
                        }
                    }
                } else {
                    if (((JSONObject) pageObject.get("paging")).has("next")) {
                        pagingDataNext = ((JSONObject) pageObject.get("paging")).get("next").toString();
                    } else {
                        pagingDataNext = null;
                    }
                    if (((JSONObject) pageObject.get("paging")).has("previous")) {
                        pagingDataPrevious = ((JSONObject) pageObject.get("paging")).get("previous").toString();
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

    public static pagesTab newInstance(List<items> page_data) {
        pagesTab fragment = new pagesTab(new JSONArray(page_data).toString());
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, new JSONArray(page_data).toString());
        isFav = true;
        favList = new ArrayList< >();
        if(page_data!= null){
            favList = new ArrayList<items>(page_data);
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
        View view = inflater.inflate(R.layout.fragment_pages_tab, container, false);
        ListView pageView = (ListView) view.findViewById(R.id.pagesView);
        tableAdapter adapter;
        Button previous = (Button) view.findViewById(R.id.pagesprev);
        Button next = (Button) view.findViewById(R.id.pagesnext);

        if(pagingDataNext == null){
            next.setClickable(false);
        } else {
            next.setClickable(true);
            try {
                next.setOnClickListener(new pagesTab.MyListener(this.getContext(),this.pagingDataNext, "next"));
            } catch (Exception e) {

            }
        }

        if(pagingDataPrevious == null){
            previous.setClickable(false);
        } else {
            previous.setClickable(true);
            try {
                previous.setOnClickListener(new pagesTab.MyListener(this.getContext(),pagingDataPrevious,"previous"));
            } catch (Exception e) {

            }
        }

        if(isFav == false) {
            adapter = new tableAdapter(this.getContext(), R.layout.tablerow, itemsList);
        } else{
            adapter = new tableAdapter(this.getContext(), R.layout.tablerow, favList);
            //isFav = false;
        }
        pageView.setAdapter(adapter);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
                intent.putExtra(PAGE_DATA, pagingDataNext);
            } else if(this.identifier.equalsIgnoreCase("previous")){
                intent.putExtra(PAGE_DATA, pagingDataPrevious);
            }
            mContext.startActivity(intent);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener2) {
            mListener = (OnFragmentInteractionListener2) context;
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
    public interface OnFragmentInteractionListener2 {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
