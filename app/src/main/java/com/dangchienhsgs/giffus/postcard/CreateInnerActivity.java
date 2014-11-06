package com.dangchienhsgs.giffus.postcard;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dangchienhsgs.giffus.dialogs.colorpickers.ColorPickerDialog;
import com.dangchienhsgs.giffus.dialogs.fontpickers.FontManager;
import com.dangchienhsgs.giffus.dialogs.fontpickers.FontPickerDialog;
import com.dangchienhsgs.giffus.dialogs.numberpickers.NumberPickerBuilder;
import com.dangchienhsgs.giffus.dialogs.numberpickers.NumberPickerDialogFragment;
import com.dangchienhsgs.giffus.dialogs.picturepickers.PicturesPickerDialogs;
import com.dangchienhsgs.giffus.dialogs.songpickers.LyricsPickerDialogs;
import com.dangchienhsgs.giffus.dialogs.songpickers.SongsPickerDialogs;
import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.client.PreferencesHandler;
import com.dangchienhsgs.giffus.dialogs.timepicker.TimePicker;
import com.dangchienhsgs.giffus.media.Song;
import com.dangchienhsgs.giffus.utils.Common;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class CreateInnerActivity extends ActionBarActivity
        implements FontPickerDialog.FontPickerDialogListener, NumberPickerDialogFragment.NumberPickerDialogHandler, SongsPickerDialogs.OnReturnListener,
        PicturesPickerDialogs.OnSelectedPicturesListener, LyricsPickerDialogs.OnFinishedDialogListener {

    private final String TAG = "Create Inner Activity";

    private EditText editSmallText;
    private EditText editLargeText;
    private ImageView avatar;
    private Button playMedia;

    private Inner inner = new Inner();

    private ProgressBar progressBar;

    private List<Song> listSongs = new ArrayList<Song>();


    private int avatarID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);

        setContentView(R.layout.activity_create_inner);

        editLargeText = (EditText) findViewById(R.id.edit_text_large);
        editSmallText = (EditText) findViewById(R.id.edit_small_text);


        inner.setFontTextLarge(Common.ROBOTO_LIGHT_FONT_PATH);
        Log.d(TAG, inner.getFontTextLarge());
        inner.setFontTextSmall(Common.ROBOTO_LIGHT_FONT_PATH);
        Log.d(TAG, inner.getFontTextSmall());

        inner.setBackgroundID(0);


        editSmallText.setTypeface(Typeface.createFromFile(Common.ROBOTO_LIGHT_FONT_PATH));
        editLargeText.setTypeface(Typeface.createFromFile(Common.ROBOTO_LIGHT_FONT_PATH));

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);


        avatar = (ImageView) findViewById(R.id.avatar);
        avatar.setImageResource(
                Common.HUMAN_ICON[Integer.parseInt(PreferencesHandler.getValueFromPreferences(Common.AVATAR_ID, getApplicationContext()))]
        );

        avatarID = 0;


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_inner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_next:
                Intent intent = new Intent(getApplicationContext(), TimePickerActivity.class);
                startActivity(intent);
                break;
            case R.id.action_edit_text_color:
                Log.d(TAG, "Edit Text Color");
                ColorPickerDialog colorPickerDialog = new ColorPickerDialog(
                        this,
                        Color.WHITE,
                        new ColorPickerDialog.OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int color) {
                                if (editSmallText.hasFocus()) {
                                    editSmallText.setTextColor(color);
                                } else if (editLargeText.hasFocus()) {
                                    editLargeText.setTextColor(color);
                                }
                            }
                        }
                );
                colorPickerDialog.show();
                break;
            case R.id.action_change_size:
                NumberPickerBuilder numberPickerBuilder = new NumberPickerBuilder();
                numberPickerBuilder.setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light);
                numberPickerBuilder.show();
                break;
            case R.id.action_change_font:
                FontPickerDialog fontPickerDialog = new FontPickerDialog();
                fontPickerDialog.show(getFragmentManager(), "font_picker");
                break;

            case R.id.action_preview:
                intent = new Intent(getApplicationContext(), PreviewInnerActivity.class);

                // get Inner
                inner = obtainSetting();

                // convert Inner to JSON
                Gson gson = new Gson();
                String jsonInner = gson.toJson(inner);
                Log.d(TAG, "Inner json " + jsonInner);

                // add jsonInner to intent
                intent.putExtra(Inner.JSON_NAME, jsonInner);

                // start Preview
                startActivity(intent);
                break;

            case R.id.action_change_text_background:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onAvatarClick(View view) {
        PicturesPickerDialogs picturesPickerDialogs = new PicturesPickerDialogs();
        picturesPickerDialogs.setListImages(Common.HUMAN_ICON);
        picturesPickerDialogs.show(getSupportFragmentManager(), "pictures_dialogs");
    }

    @Override
    public void onFontSelected(FontPickerDialog dialog) {
        Typeface font = Typeface.createFromFile(dialog.getSelectedFont());
        if (editLargeText.hasFocus()) {
            // Change font and save data
            Log.d(TAG, "LArge");
            editLargeText.setTypeface(font);
            inner.setFontTextLarge(dialog.getSelectedFont());

        } else if (editSmallText.hasFocus()) {
            Log.d(TAG, "Small");
            editSmallText.setTypeface(font);
            inner.setFontTextSmall(dialog.getSelectedFont());
        }
    }

    @Override
    public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber) {
        if (number > 0) {
            if (editLargeText.hasFocus()) {
                editLargeText.setTextSize(number);
            } else if (editSmallText.hasFocus()) {
                editSmallText.setTextSize(number);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Number input <0 fail", Toast.LENGTH_SHORT).show();
        }

    }


    public void onReturn(SongsPickerDialogs songsPickerDialogs) {
        listSongs = songsPickerDialogs.getListSongs();
    }

    public Inner obtainSetting() {
        inner.setAvatarID(avatarID);
        inner.setBackgroundID(0);
        inner.setListSongs(listSongs);

        inner.setColorTextLarge(editLargeText.getCurrentTextColor());
        inner.setSizeTextLarge(editLargeText.getTextSize());
        inner.setTextLarge(editLargeText.getText().toString());

        inner.setTextSmall(editSmallText.getText().toString());
        inner.setColorTextSmall(editSmallText.getCurrentTextColor());
        inner.setSizeTextSmall(editSmallText.getTextSize());

        Log.d(TAG, inner.getTextSmall());
        Log.d(TAG, inner.getFontTextLarge());
        return inner;
    }

    public void onClickSetMusic(View view) {
        SongsPickerDialogs dialogs = new SongsPickerDialogs();
        dialogs.show(getSupportFragmentManager(), "add_music");
        dialogs.setListSongs(listSongs);
    }

    public void onClickSetLyrics(View view) {
        if (listSongs.size() != 0) {
            LyricsPickerDialogs dialogs = new LyricsPickerDialogs();
            dialogs.show(getSupportFragmentManager(), "add_lyrics");
            dialogs.setListSongs(listSongs);
        } else {
            Toast.makeText(getApplicationContext(), "You have not attach any mp3", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSelectedPictures(PicturesPickerDialogs dialogs, int position) {
        avatar.setImageResource(Common.HUMAN_ICON[position]);
        avatarID = position;
        inner.setAvatarID(position);
    }

    @Override
    public void onFinished(LyricsPickerDialogs dialogs, List<Song> listSongs) {
        this.listSongs = listSongs;
    }
}
