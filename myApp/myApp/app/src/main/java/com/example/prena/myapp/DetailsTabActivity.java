package com.example.prena.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;

import com.example.prena.myapp.interfaces.asyncresponse;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailsTabActivity extends AppCompatActivity implements asyncresponse,albums.OnFragmentInteractionListeneralbums,posts.OnFragmentInteractionListenerposts{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private DetailsTabActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CallbackManager callbackManager;
    private boolean Fav;
    private items dItem;
    static String imgUrl;
    static String title;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_tab);

        Intent intent = getIntent();
        String id = intent.getStringExtra(dListener.EXTRA_ID);
        Fav = intent.getBooleanExtra(dListener.EXTRA_FAV,false);
        dItem = (items)intent.getSerializableExtra(dListener.EXTRA_ITEM);
        new HttpGetDetailRequest(this).execute(id);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void processFinish(JSONObject data) {
        setUpSectionPager(mSectionsPagerAdapter,data);
        try {
            JSONObject pic = data.getJSONObject("picture");
            imgUrl = pic.getString("url");
            title = data.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs1);
        tabLayout.setupWithViewPager(mViewPager);
        setupTabIcons(tabLayout);

    }

    private void setUpSectionPager(SectionsPagerAdapter sectionsPagerAdapter, JSONObject data) {

        // Create the adapter that will return a fragment
        mViewPager = (ViewPager) findViewById(R.id.container1);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFrag(albums.newInstance(data.toString()), "ALBUMS");
        mSectionsPagerAdapter.addFrag(posts.newInstance(data.toString()), "POSTS");

        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    private void setupTabIcons(TabLayout tabLayout){
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.mipmap.albums);
        tabLayout.getTabAt(1).setIcon(R.mipmap.posts);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details_tab, menu);
        if(dItem != null){
            SharedPreferences appSharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this.getApplicationContext());

            Gson gson = new Gson();
            Type gType = new TypeToken<List<items>>(){}.getType();
            String jsonData = appSharedPrefs.getString("favItems", "");
            List<items> itemsList = gson.fromJson(jsonData, gType);
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

            if(itemsList != null){
                boolean fav = false;
                for(items item : itemsList){
                    if(item.getId().equalsIgnoreCase(item.getId())){
                        fav = true;
                    }
                }
                if(fav){
                    menu.findItem(R.id.action_fav).setTitle("Remove from Favourites");
                }
            }
        }
        if(Fav)
            menu.findItem(R.id.action_fav).setTitle("Remove from Favourites");
        return true;
    }

    public boolean addToFav(MenuItem menuItem){
        String title = (String) menuItem.getTitle();
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        Type gType = new TypeToken<List<items>>(){}.getType();
        String jsonData = appSharedPrefs.getString("favItems", "");
        List<items> itemsList = gson.fromJson(jsonData, gType);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

        if(title.contains("Remove")){
            if(itemsList != null){
                int i = -1;
                int indexToRemove = -1;
                for(items item : itemsList){
                    i++;
                    if(item.getId().equalsIgnoreCase(this.dItem.getId())){
                        indexToRemove = i;
                    }
                }
                if(indexToRemove != -1){
                    itemsList.remove(indexToRemove);
                    String jsonToStore = gson.toJson(itemsList);
                    prefsEditor.putString("favItems", jsonToStore);
                    prefsEditor.commit();
                    menuItem.setTitle("Add to Favourites");
                    Toast.makeText(this, "Removed from Favourites", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            if(itemsList == null){
                itemsList = new ArrayList<items>();
            }
            itemsList.add(this.dItem);
            String jsonToStore = gson.toJson(itemsList);
            prefsEditor.putString("favItems", jsonToStore);
            prefsEditor.commit();
            menuItem.setTitle("Remove from Favourites");
            Toast.makeText(this, "Added to Favourites", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public boolean shareToFB(MenuItem menuItem){
        FacebookSdk.setApplicationId("1742952839348527");
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        sharePhotoToFacebook();

        return true;
    }

    private void sharePhotoToFacebook(){
        Bitmap image = null;
        try {
            InputStream is = (InputStream) new URL(this.dItem.getUrl()).getContent();
            image = BitmapFactory.decodeStream(is);

        } catch (Exception e) {
            return ;
        }

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://www-scf.usc.edu/~gumblapu/hw8_sgb_1892/webapp.html#!/"))
                .setImageUrl(Uri.parse(imgUrl))
                .setContentDescription(title)
                .setQuote(title)
                .build();
        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.show(linkContent, ShareDialog.Mode.AUTOMATIC);

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data)
    {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
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

