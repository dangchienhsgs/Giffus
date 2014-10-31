package com.dangchienhsgs.giffus.postcard;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.dialogs.musicplayer.MusicPlayerDialogs;
import com.dangchienhsgs.giffus.dialogs.timepicker.TimePicker;
import com.dangchienhsgs.giffus.media.Song;
import com.dangchienhsgs.giffus.utils.Common;
import com.google.gson.Gson;

import java.util.Arrays;


public class PreviewInnerActivity extends ActionBarActivity implements MusicPlayerDialogs.OnReturnedListener {
    private final String TAG = "Preview Inner Activity";
    private Inner inner;

    private TextView txtLarge;
    private TextView txtSmall;

    private ImageView mAvatar;

    private boolean isDownloaded[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        setContentView(R.layout.activity_preview_inner);

        // get innerString
        String jsonInner = getIntent().getStringExtra(Inner.JSON_NAME);
        // convert to Inner
        Gson gson = new Gson();
        inner = gson.fromJson(jsonInner, Inner.class);

        isDownloaded = new boolean[inner.getListSongs().size()];
        Arrays.fill(isDownloaded, false);

        txtLarge = (TextView) findViewById(R.id.text_large_preview);
        txtSmall = (TextView) findViewById(R.id.text_small_preview);

        //set Text Large
        txtLarge.setText(inner.getTextLarge());
        txtLarge.setTextColor(inner.getColorTextLarge());
        txtLarge.setTextSize(inner.getSizeTextLarge());

        Log.d(TAG, inner.getFontTextLarge());
        txtLarge.setTypeface(Typeface.createFromFile(inner.getFontTextLarge()));

        // set Text Small
        Log.d(TAG, inner.getTextSmall());
        txtSmall.setText(inner.getTextSmall());
        txtSmall.setTypeface(Typeface.createFromFile(inner.getFontTextSmall()));
        txtSmall.setTextColor(inner.getColorTextSmall());
        txtSmall.setTextSize(inner.getSizeTextSmall());

        // Set Avatar
        mAvatar = (ImageView) findViewById(R.id.avatar_preview);
        mAvatar.setImageResource(Common.HUMAN_ICON[inner.getAvatarID()]);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.preview_inner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_play_music:
                MusicPlayerDialogs musicPlayerDialogs = new MusicPlayerDialogs();
                Song song = new Song(
                        "Chien",
                        "http://dl2.org.mp3.zdn.vn/fsdd1131lwwjA/64c96742e3c3866a061019c660f80e3f/544d9910/2014/07/26/1/2/12bbb88ea519d8a68cd2724597c064eb.mp3?filename=Goi%20Mua%20-%20Trung%20Quan%20Idol.mp3",
                        "Deo biet la gi nua"
                );
                inner.getListSongs().add(song);
                musicPlayerDialogs.setListSongs(inner.getListSongs());
                musicPlayerDialogs.setIsDownloaded(isDownloaded);
                musicPlayerDialogs.show(getSupportFragmentManager(), "music_player");
                break;
            //case R.id.action_next:
            //    Intent intent=new Intent(getApplicationContext(), TimePicker.class);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReturned(String[] listSingPath, boolean[] isDownloaded) {
        this.isDownloaded = isDownloaded;
    }
}
