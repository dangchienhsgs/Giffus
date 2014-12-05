package com.dangchienhsgs.giffus.postcard;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.dialogs.musicplayer.MusicPlayerDialogs;
import com.dangchienhsgs.giffus.utils.Common;
import com.google.gson.Gson;

import java.util.Arrays;


public class PreviewInnerActivity extends ActionBarActivity implements MusicPlayerDialogs.OnReturnedListener {
    private final String TAG = "Preview Inner Activity";
    private Inner inner;

    private TextView txtLarge;
    private TextView txtSmall;
    private ImageView mAvatar;

    private Postcard postcard;
    private String flag;

    private boolean isDownloaded[];
    private String filePath[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        setContentView(R.layout.activity_preview_inner);

        Intent intent = getIntent();

        String jsonPostcard = intent.getStringExtra(Common.JSON_POSTCARD_STRING);
        postcard = new Gson().fromJson(jsonPostcard, Postcard.class);
        flag = intent.getStringExtra(Common.FLAG);


        // convert to Inner
        inner = postcard.getInner();

        isDownloaded = new boolean[inner.getListSongs().size()];
        Arrays.fill(isDownloaded, false);

        filePath = new String[inner.getListSongs().size()];
        Arrays.fill(filePath, null);

        txtLarge = (TextView) findViewById(R.id.text_large_preview);
        txtSmall = (TextView) findViewById(R.id.text_small_preview);

        //set Text Large
        txtLarge.setText(inner.getTextLarge());
        txtLarge.setTextColor(inner.getColorTextLarge());
        txtLarge.setTextSize(TypedValue.COMPLEX_UNIT_PX, inner.getSizeTextLarge());

        if (inner.getBackgroundTextLarge() >= 0) {
            txtLarge.setBackgroundResource(Common.BUBBLE_TEXT_BACKGROUND[inner.getBackgroundTextLarge()]);
        }

        txtLarge.setTypeface(Typeface.createFromFile(inner.getFontTextLarge()));


        txtSmall.setText(inner.getTextSmall());
        txtSmall.setTypeface(Typeface.createFromFile(inner.getFontTextSmall()));
        txtSmall.setTextColor(inner.getColorTextSmall());
        txtSmall.setTextSize(TypedValue.COMPLEX_UNIT_PX, inner.getSizeTextSmall());

        if (inner.getBackgroundTextSmall() >= 0) {
            txtSmall.setBackgroundResource(Common.BUBBLE_TEXT_BACKGROUND[inner.getBackgroundTextSmall()]);
        }

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

                if (inner.getListSongs().size() == 0) {
                    Toast.makeText(this, "Sender do not attach any media ", Toast.LENGTH_SHORT).show();
                } else {
                    MusicPlayerDialogs musicPlayerDialogs = new MusicPlayerDialogs();
                    musicPlayerDialogs.setListSongs(inner.getListSongs());
                    musicPlayerDialogs.setIsDownloaded(isDownloaded);
                    musicPlayerDialogs.setListSongPath(filePath);
                    musicPlayerDialogs.show(getSupportFragmentManager(), "music_player");
                }
                break;
            //case R.id.action_next:
            //    Intent intent=new Intent(getApplicationContext(), TimePicker.class);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReturned(String[] listSingPath, boolean[] isDownloaded, String[] filePath) {
        this.isDownloaded = isDownloaded;
        this.filePath = filePath;
    }
}
