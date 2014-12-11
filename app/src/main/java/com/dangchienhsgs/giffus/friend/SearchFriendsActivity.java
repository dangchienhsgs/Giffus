package com.dangchienhsgs.giffus.friend;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dangchienhsgs.giffus.HomeActivity;
import com.dangchienhsgs.giffus.R;


public class SearchFriendsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_friends, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);

        finish();
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

    public void onClickSyncContacts(View view) {
        Intent intent = new Intent(getApplicationContext(), SyncFriendsActivity.class);
        startActivity(intent);

        finish();
    }

    public void onClickSearchContactsByName(View view) {
        Intent intent = new Intent(getApplicationContext(), SearchByNameActivity.class);
        startActivity(intent);

        finish();
    }
}
