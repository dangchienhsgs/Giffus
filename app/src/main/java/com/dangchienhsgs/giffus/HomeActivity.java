package com.dangchienhsgs.giffus;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dangchienhsgs.giffus.friend.FriendCursorAdapter;
import com.dangchienhsgs.giffus.friend.FriendFragment;
import com.dangchienhsgs.giffus.friend.SearchFriendsActivity;
import com.dangchienhsgs.giffus.notification.NotifyActivity;
import com.dangchienhsgs.giffus.utils.PreferencesHandler;
import com.dangchienhsgs.giffus.postcard.CreateCoverActivity;
import com.dangchienhsgs.giffus.provider.FriendContract;
import com.dangchienhsgs.giffus.provider.PostcardContract;
import com.dangchienhsgs.giffus.provider.NotificationContract;
import com.dangchienhsgs.giffus.utils.Common;


public class HomeActivity extends ActionBarActivity implements ActionBar.TabListener {
    public static final String TAG = "Home Activity";
    private ViewPager viewPager;
    private TabAdapter tabAdapter;
    private ActionBar actionBar;

    // Attributes for DrawerLayout
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mDrawerListTitle;
    private CharSequence mDrawerTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;


    // Title of Tabs
    private String tabsName[] = {"Home", "Friend", "Library"};

    @Override
    protected void onResume() {
        super.onResume();

        FriendFragment listFragment = (FriendFragment) tabAdapter.getItem(1);
        FriendCursorAdapter cursorAdapter = (FriendCursorAdapter) listFragment.getListAdapter();


        // Update the Friend Fragment

        if (cursorAdapter != null) {

            Cursor cursor = getContentResolver().query(
                    FriendContract.URI,
                    null,
                    null,
                    null,
                    null
            );

            cursorAdapter.swapCursor(cursor);
            cursorAdapter.notifyDataSetChanged();

            // Update Listener
            listFragment.getListView().setOnItemClickListener(listFragment);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        createTabs();
        createNavigationDrawer();

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
        mDrawerTitle = mTitle = getTitle();
        mDrawerList = (ListView) findViewById(R.id.list_drawer);

        mDrawerListTitle = getResources().getStringArray(R.array.nav_drawer_items);
        DrawerArrayAdapter drawerAdapter = new DrawerArrayAdapter(this, R.layout.drawer_list_item, mDrawerListTitle);
        mDrawerList.setAdapter(drawerAdapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

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


    /**
     * Create account sync adapter
     */

    //public static Account createAccount(Context context){
    //Account account=new Account(Common.ACCOUNT, Common.ACCOUNT_TYPE);
    //AccountManager accountManager=(AccountManager) context.getSystemService()
    //}
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
        switch (id) {
            case R.id.action_search_friend:
                Intent intent = new Intent(getApplicationContext(), SearchFriendsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_add_gift:
                Intent intent2 = new Intent(getApplicationContext(), CreateCoverActivity.class);
                intent2.putExtra(Common.FLAG, Common.FLAG_NEW_COVER);
                startActivity(intent2);
                break;
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

    /**
     * This class handle the Item of ListView in Drawer Navigation action click
     * It point to subroutine selectItem (int position) where position is the
     * order of item we click
     */


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView adapterView, View view, int position, long l) {
            Intent intent;
            switch (position) {
                // Do action here
                case Common.DRAWER_NOTIFICATIONS_ID:
                    //Start Notify Activity
                    intent = new Intent(getApplicationContext(), NotifyActivity.class);
                    startActivity(intent);
                    break;
                case Common.DRAWER_ACCOUNT_ID:
                    intent = new Intent(getApplicationContext(), AccountActivity.class);
                    startActivity(intent);
                    break;

                case Common.DRAWER_SIGN_OUT_ID:
                    ContentResolver contentResolver = getContentResolver();
                    contentResolver.delete(FriendContract.URI, null, null);
                    contentResolver.delete(PostcardContract.URI, null, null);
                    contentResolver.delete(NotificationContract.URI, null, null);
                    PreferencesHandler.deleteFromPreferences(Common.USERNAME, getApplicationContext());
                    PreferencesHandler.deleteFromPreferences(Common.PASSWORD, getApplicationContext());
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }

    }
}
