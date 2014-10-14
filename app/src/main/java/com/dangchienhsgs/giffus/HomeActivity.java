package com.dangchienhsgs.giffus;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dangchienhsgs.giffus.provider.FriendContract;
import com.dangchienhsgs.giffus.utils.Common;


public class HomeActivity extends ActionBarActivity implements ActionBar.TabListener {
    private ViewPager viewPager;
    private TabAdapter tabAdapter;
    private ActionBar actionBar;

    // Attributes for DrawerLayout
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mDrawerTitle;
    CharSequence mTitle;

    private String username;
    private String password;
    private String registrationId;

    // Title of Tabs
    private String tabsName[] = {"Home", "Friend", "Library"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        createTabs();
        createNavigationDrawer();

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(FriendContract.FRIEND_CONTENT_URI, null, null, null, null);
        if (!cursor.moveToFirst()) {
            Toast.makeText(this, "Can not access", Toast.LENGTH_LONG).show();
        } else {
            String result = cursor.getString(cursor.getColumnIndex(FriendContract.Entry.USERNAME));
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        }


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        username=preferences.getString(Common.USERNAME, "");
        password=preferences.getString(Common.PASSWORD, "");
        registrationId=preferences.getString(Common.REGISTRATION_ID, "");
        Toast.makeText(getApplicationContext(), username+" "+password+" "+registrationId, Toast.LENGTH_LONG).show();

    }

    // Create Home Tabs, Friends Tab and More...
    public void createTabs() {
        // get ViewPager, actionBar
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Add Tabs to actionBar
        for (String title : tabsName) {
            actionBar.addTab(actionBar.newTab().setText(title).setTabListener(this));
        }

        // Add fragments to tabAdapter
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.add(new HomeFragment());
        tabAdapter.add(new FriendFragment());
        tabAdapter.add(new LibraryFragment());

        // Set TabAdapter to viewPager
        viewPager.setAdapter(tabAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int position) {
                /* When Pager select, actionBar will sync with viewPager
                 * Page Change -> Tab Change
                 */
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    // Create DrawerNavigation
    public void createNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_drawer);
        mDrawerTitle = getResources().getStringArray(R.array.nav_drawer_items);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mDrawerTitle));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    /**
     * This class handle the Item of ListView in Drawer Navigation action click
     * It point to subroutine selectItem (int position) where position is the
     * order of item we click
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView adapterView, View view, int position, long l) {
            switch (position) {
                // Do action here
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    //Intent intent=new Intent(getApplicationContext(), SettingsActivity.class);
                    //startActivity(intent);
                    break;
            }
        }

    }


    /**
     * Create account sync adapter
     */

    //public static Account createAccount(Context context){
    //Account account=new Account(Common.ACCOUNT, Common.ACCOUNT_TYPE);
    //AccountManager accountManager=(AccountManager) context.getSystemService()
    //}

    /**
     * For TabListener implement action
     *
     * @param title
     */
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        super.setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // Sync with View Pager
        // Tab Change -> page Change
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}
