package com.example.prena.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.prena.myapp.interfaces.asyncresponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TabbedActivity extends AppCompatActivity implements asyncresponse, usersTab.OnFragmentInteractionListener1,pagesTab.OnFragmentInteractionListener2,eventsTab.OnFragmentInteractionListener3,placesTab.OnFragmentInteractionListener4,groupsTab.OnFragmentInteractionListener5 {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    public static SectionsPagerAdapter mSectionsPagerAdapter;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String userPagingMessage = intent.getStringExtra(usersTab.USER_DATA);
        String pagesPagingMessage = intent.getStringExtra(pagesTab.PAGE_DATA);
        String placesPagingMessage = intent.getStringExtra(placesTab.PLACE_DATA);
        String groupsPagingMessage = intent.getStringExtra(groupsTab.GROUP_DATA);
       String eventsPagingMessage = intent.getStringExtra(eventsTab.EVENT_DATA);
        //if the message is null then favourites needs to be opened else normal user tab
        if(message!=null) {
            //clear all shared preferences
            SharedPreferences appSharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this.getApplicationContext());
            appSharedPrefs.edit().clear().commit();
            new HttpGetRequest(this).execute(message);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            }
            else if(userPagingMessage != null) {
                try {
                    new HttpGetRequestPaging(this, "TabbedActivity", "users").execute(userPagingMessage);
                } catch (Exception e) {

                }

            } else if(pagesPagingMessage != null) {
                try {
                    new HttpGetRequestPaging(this, "TabbedActivity", "pages").execute(pagesPagingMessage);
                } catch (Exception e) {

                }
            } else if(eventsPagingMessage != null) {
                try {
                    new HttpGetRequestPaging(this, "TabbedActivity", "events").execute(eventsPagingMessage);
                } catch (Exception e) {

                }
            }  else if(placesPagingMessage != null) {
                try {
                    new HttpGetRequestPaging(this, "TabbedActivity", "places").execute(placesPagingMessage);
                } catch (Exception e) {

                }
            } else if(groupsPagingMessage != null) {
                try {
                    new HttpGetRequestPaging(this, "TabbedActivity", "groups").execute(groupsPagingMessage);
                } catch (Exception e) {

                }
            }
            else {
            //fetching the favourites data stored in shared preferences
            SharedPreferences appSharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this.getApplicationContext());

            Gson gson = new Gson();
            Type type = new TypeToken<List<items>>(){}.getType();
            String jsonData = appSharedPrefs.getString("favItems", "");
            List<items> itemsList = null;
            if(jsonData.length() > 0){
                itemsList = gson.fromJson(jsonData, type);
            } else {
                itemsList = new ArrayList<items>();
            }
            ((Toolbar) findViewById(R.id.toolbar)).setTitle("Favourites");
            displayFavourites(itemsList);
            }
        }

    //set up for favourites
    private void displayFavourites(List<items> itemsList) {
        this.setTitle("FAVOURITES");
        setUpSection(mSectionsPagerAdapter,itemsList);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        setupTabIcons(tabLayout);
    }
    public void displayPagination(String data, String type) {
        // mSectionsPagerAdapter.notifyDataSetChanged();
        SectionsPagerAdapter mSectionsPagerAdapterData = new SectionsPagerAdapter(getSupportFragmentManager());

        switch(type){
            case "users":
                try {
                    mSectionsPagerAdapterData.addFrag(usersTab.newInstance(data), "Users");
                } catch(Exception e) {
                    Log.e("TabbedActivity","Exception occourred while adding fragment to adaptor :"+e.getMessage());
                }
                mSectionsPagerAdapterData.addFrag(pagesTab.newInstance(mSectionsPagerAdapter.getItem(1).getArguments().getString("param1").toString()),"Pages");
                mSectionsPagerAdapterData.addFrag(eventsTab.newInstance(mSectionsPagerAdapter.getItem(2).getArguments().getString("param1").toString()),"Events");
                mSectionsPagerAdapterData.addFrag(placesTab.newInstance(mSectionsPagerAdapter.getItem(3).getArguments().getString("param1").toString()),"Places");
                mSectionsPagerAdapterData.addFrag(groupsTab.newInstance(mSectionsPagerAdapter.getItem(4).getArguments().getString("param1").toString()),"Groups");
                break;
            case "pages":

                mSectionsPagerAdapterData.addFrag(usersTab.newInstance(mSectionsPagerAdapter.getItem(0).getArguments().getString("param1").toString()),"Users");

                try {
                    mSectionsPagerAdapterData.addFrag(pagesTab.newInstance(data), "Pages");
                } catch(Exception e) {
                    Log.e("TabbedActivity","Exception occourred while adding fragment to adaptor :"+e.getMessage());
                }
                mSectionsPagerAdapterData.addFrag(eventsTab.newInstance(mSectionsPagerAdapter.getItem(2).getArguments().getString("param1").toString()),"Events");
                mSectionsPagerAdapterData.addFrag(placesTab.newInstance(mSectionsPagerAdapter.getItem(3).getArguments().getString("param1").toString()),"Places");
                mSectionsPagerAdapterData.addFrag(groupsTab.newInstance(mSectionsPagerAdapter.getItem(4).getArguments().getString("param1").toString()),"Groups");
                break;
            case "events":

                mSectionsPagerAdapterData.addFrag(usersTab.newInstance(mSectionsPagerAdapter.getItem(0).getArguments().getString("param1").toString()),"Users");
                mSectionsPagerAdapterData.addFrag(pagesTab.newInstance(mSectionsPagerAdapter.getItem(1).getArguments().getString("param1").toString()),"Pages");
                try {
                    mSectionsPagerAdapterData.addFrag(eventsTab.newInstance(data), "Events");
                } catch(Exception e) {
                    Log.e("TabbedActivity","Exception occourred while adding fragment to adaptor :"+e.getMessage());
                }
                mSectionsPagerAdapterData.addFrag(placesTab.newInstance(mSectionsPagerAdapter.getItem(3).getArguments().getString("param1").toString()),"Places");
                mSectionsPagerAdapterData.addFrag(groupsTab.newInstance(mSectionsPagerAdapter.getItem(4).getArguments().getString("param1").toString()),"Groups");
                break;

            case "places":

                mSectionsPagerAdapterData.addFrag(usersTab.newInstance(mSectionsPagerAdapter.getItem(0).getArguments().getString("param1").toString()),"Users");
                mSectionsPagerAdapterData.addFrag(pagesTab.newInstance(mSectionsPagerAdapter.getItem(1).getArguments().getString("param1").toString()),"Pages");
                mSectionsPagerAdapterData.addFrag(eventsTab.newInstance(mSectionsPagerAdapter.getItem(2).getArguments().getString("param1").toString()),"Events");

                try {
                    mSectionsPagerAdapterData.addFrag(placesTab.newInstance(data), "Places");
                } catch(Exception e) {
                    Log.e("TabbedActivity","Exception occourred while adding fragment to adaptor :"+e.getMessage());
                }
                mSectionsPagerAdapterData.addFrag(groupsTab.newInstance(mSectionsPagerAdapter.getItem(4).getArguments().getString("param1").toString()),"Groups");
                break;

            case "groups":

                mSectionsPagerAdapterData.addFrag(usersTab.newInstance(mSectionsPagerAdapter.getItem(0).getArguments().getString("param1").toString()),"Users");
                mSectionsPagerAdapterData.addFrag(pagesTab.newInstance(mSectionsPagerAdapter.getItem(1).getArguments().getString("param1").toString()),"Pages");
                mSectionsPagerAdapterData.addFrag(eventsTab.newInstance(mSectionsPagerAdapter.getItem(2).getArguments().getString("param1").toString()),"Events");
                mSectionsPagerAdapterData.addFrag(placesTab.newInstance(mSectionsPagerAdapter.getItem(3).getArguments().getString("param1").toString()),"Places");
                try {
                    mSectionsPagerAdapterData.addFrag(groupsTab.newInstance(data), "Groups");
                } catch(Exception e) {
                    Log.e("TabbedActivity","Exception occourred while adding fragment to adaptor :"+e.getMessage());
                }
                break;
        }

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapterData);
        try {
            mSectionsPagerAdapter =  mSectionsPagerAdapterData;
        } catch(Exception e){

        }
        // mSectionsPagerAdapter.notifyDataSetChanged();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

      /*  tabLayout.getTabAt(0).setIcon(R.mipmap.ic_users);
        tabLayout.getTabAt(1).setIcon(R.mipmap.ic_users);
        tabLayout.getTabAt(2).setIcon(R.mipmap.ic_events);
        tabLayout.getTabAt(3).setIcon(R.mipmap.ic_places);
        tabLayout.getTabAt(4).setIcon(R.mipmap.ic_groups);*/

        // mViewPager.getAdapter().notifyDataSetChanged();
    }

    public void processFinish(JSONObject data) {
        // Set up the ViewPager with the sections adapter.
        setUpSectionPager(mSectionsPagerAdapter,data);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.mipmap.user);
        tabLayout.getTabAt(1).setIcon(R.mipmap.pages);
        tabLayout.getTabAt(2).setIcon(R.mipmap.events);
        tabLayout.getTabAt(3).setIcon(R.mipmap.places);
        tabLayout.getTabAt(4).setIcon(R.mipmap.groups);
    }
    //setting up adapter for favourites
    private void setUpSection(SectionsPagerAdapter sectionsPagerAdapter, List<items> data) {
        List<items> user_data = new ArrayList<items>();
        List<items> page_data = new ArrayList<items>();
        List<items> event_data = new ArrayList<items>();
        List<items> place_data = new ArrayList<items>();
        List<items> group_data = new ArrayList<items>();
        for(items item: data){
            if(item.getType().equalsIgnoreCase("users")){
                user_data.add(item);
            }
            if(item.getType().equalsIgnoreCase("pages")){
                page_data.add(item);
            }
            if(item.getType().equalsIgnoreCase("events")){
                event_data.add(item);
            }
            if(item.getType().equalsIgnoreCase("places")){
                place_data.add(item);
            }
            if(item.getType().equalsIgnoreCase("groups")){
                group_data.add(item);
            }
        }

        mViewPager = (ViewPager) findViewById(R.id.container);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFrag(usersTab.newInstance(user_data), "USERS");
        mSectionsPagerAdapter.addFrag(pagesTab.newInstance(page_data), "PAGES");
        mSectionsPagerAdapter.addFrag(eventsTab.newInstance(event_data), "EVENTS");
        mSectionsPagerAdapter.addFrag(placesTab.newInstance(place_data), "PLACES");
        mSectionsPagerAdapter.addFrag(groupsTab.newInstance(group_data), "GROUPS");

        mViewPager.setAdapter(mSectionsPagerAdapter);
    }
    //setting up pager for normal table view
    private void setUpSectionPager(SectionsPagerAdapter sectionsPagerAdapter, JSONObject data) {
        String user_data = null;
        String page_data = null;
        String event_data = null;
        String place_data = null;
        String group_data = null;
        try {
                user_data = data.get("user").toString();
                page_data = data.get("page").toString();
                event_data = data.get("event").toString();
                place_data = data.get("place").toString();
                group_data = data.get("group").toString();

        } catch (Exception e) {

        }
        // Create the adapter that will return a fragment
        mViewPager = (ViewPager) findViewById(R.id.container);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFrag(usersTab.newInstance(user_data), "USERS");
        mSectionsPagerAdapter.addFrag(pagesTab.newInstance(page_data), "PAGES");
        mSectionsPagerAdapter.addFrag(eventsTab.newInstance(event_data), "EVENTS");
        mSectionsPagerAdapter.addFrag(placesTab.newInstance(place_data), "PLACES");
        mSectionsPagerAdapter.addFrag(groupsTab.newInstance(group_data), "GROUPS");

        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    private void setupTabIcons(TabLayout tabLayout){
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.mipmap.user);
        tabLayout.getTabAt(1).setIcon(R.mipmap.pages);
        tabLayout.getTabAt(2).setIcon(R.mipmap.events);
        tabLayout.getTabAt(3).setIcon(R.mipmap.places);
        tabLayout.getTabAt(4).setIcon(R.mipmap.groups);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

