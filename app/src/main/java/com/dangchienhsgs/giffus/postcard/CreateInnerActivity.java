package com.dangchienhsgs.giffus.postcard;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dangchienhsgs.giffus.dialogs.colorpickers.ColorPickerDialog;
import com.dangchienhsgs.giffus.dialogs.fontpickers.FontPickerDialog;
import com.dangchienhsgs.giffus.dialogs.numberpickers.NumberPickerBuilder;
import com.dangchienhsgs.giffus.dialogs.numberpickers.NumberPickerDialogFragment;
import com.dangchienhsgs.giffus.dialogs.picturepickers.PicturesPickerDialogs;
import com.dangchienhsgs.giffus.dialogs.songpickers.LyricsPickerDialogs;
import com.dangchienhsgs.giffus.dialogs.songpickers.SongsPickerDialogs;
import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.utils.PreferencesHandler;
import com.dangchienhsgs.giffus.map.Song;
import com.dangchienhsgs.giffus.utils.Common;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
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

    private Postcard postcard;

    private ProgressBar progressBar;

    private List<Song> listSongs = new ArrayList<Song>();

    private int backgroundLargeText = -1;
    private int backgroundSmallText = 0;

    private int avatarID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);

        setContentView(R.layout.activity_create_inner);

        String jsonPostcard = PreferencesHandler.getValueFromPreferences(Common.JSON_POSTCARD_STRING, this);
        postcard = new Gson().fromJson(jsonPostcard, Postcard.class);

        Log.d(TAG, jsonPostcard);

        if (postcard.getInner() == null) {
            initComponents();
        } else {
            inner = postcard.getInner();
            initComponentsByInner(postcard.getInner());
        }

    }

    public void initComponents() {
        editLargeText = (EditText) findViewById(R.id.edit_text_large);
        editSmallText = (EditText) findViewById(R.id.edit_small_text);

        editSmallText.setTypeface(Typeface.createFromFile(Common.ROBOTO_LIGHT_FONT_PATH));
        editLargeText.setTypeface(Typeface.createFromFile(Common.ROBOTO_LIGHT_FONT_PATH));


        inner.setFontTextLarge(Common.ROBOTO_LIGHT_FONT_PATH);
        inner.setFontTextSmall(Common.ROBOTO_LIGHT_FONT_PATH);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        avatar = (ImageView) findViewById(R.id.avatar);
        avatar.setImageResource(
                Common.HUMAN_ICON[Integer.parseInt(PreferencesHandler.getValueFromPreferences(Common.AVATAR_ID, getApplicationContext()))]
        );

        inner.setBackgroundID(0);

        if (inner.getBackgroundTextLarge() >= 0) {
            editLargeText.setBackgroundResource(Common.BUBBLE_TEXT_BACKGROUND[inner.getBackgroundTextLarge()]);
        }

        if (inner.getBackgroundTextSmall() >= 0) {
            editSmallText.setBackgroundResource(Common.BUBBLE_TEXT_BACKGROUND[inner.getBackgroundTextSmall()]);
        }

        avatarID = Integer.parseInt(PreferencesHandler.getValueFromPreferences(Common.AVATAR_ID, getApplicationContext()));

        backgroundSmallText = inner.getBackgroundTextSmall();
        backgroundLargeText = inner.getBackgroundTextLarge();
    }

    public void initComponentsByInner(Inner inner) {
        editLargeText = (EditText) findViewById(R.id.edit_text_large);
        editSmallText = (EditText) findViewById(R.id.edit_small_text);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        avatar = (ImageView) findViewById(R.id.avatar);

        editLargeText.setText(inner.getTextLarge());
        editLargeText.setTextColor(inner.getColorTextLarge());
        editLargeText.setTextSize(TypedValue.COMPLEX_UNIT_PX, inner.getSizeTextLarge());
        editLargeText.setTypeface(Typeface.createFromFile(inner.getFontTextLarge()));

        if (inner.getBackgroundTextLarge() >= 0) {
            editLargeText.setBackgroundResource(Common.BUBBLE_TEXT_BACKGROUND[inner.getBackgroundTextLarge()]);
        }

        if (inner.getBackgroundTextSmall() >= 0) {
            editSmallText.setBackgroundResource(Common.BUBBLE_TEXT_BACKGROUND[inner.getBackgroundTextSmall()]);
        }

        editSmallText.setText(inner.getTextSmall());
        editSmallText.setTextColor(inner.getColorTextSmall());
        editSmallText.setTextSize(TypedValue.COMPLEX_UNIT_PX, inner.getSizeTextSmall());
        editSmallText.setTypeface(Typeface.createFromFile(inner.getFontTextSmall()));

        this.inner = inner;

        avatar.setImageResource(Common.HUMAN_ICON[inner.getAvatarID()]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_inner, menu);
        return true;
    }


    @Override
    public void onBackPressed() {

        // Pack data
        Gson gson = new Gson();
        String jsonPostcard = PreferencesHandler.getValueFromPreferences(Common.JSON_POSTCARD_STRING, this);
        Postcard postcard = gson.fromJson(jsonPostcard, Postcard.class);
        postcard.setInner(obtainInner());

        Intent intent = new Intent(getApplicationContext(), CreateCoverActivity.class);
        intent.putExtra(Common.FLAG, Common.FLAG_BACK_TO_COVER);
        PreferencesHandler.saveValueToPreferences(Common.JSON_POSTCARD_STRING, gson.toJson(postcard, Postcard.class), this);

        startActivity(intent);

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_next:
                actionNext();
                break;
            case R.id.action_edit_text_color:
                actionChangeTextColor();
                break;
            case R.id.action_change_size:
                actionChangeSize();
                break;
            case R.id.action_change_font:
                actionChangeFont();
                break;
            case R.id.action_preview:
                actionPreview();
                break;

            case R.id.action_change_text_background:
                actionChangeTextBackground();
                break;
            case R.id.action_save_draft:
                actionSaveDraft();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void actionChangeTextBackground() {
        PicturesPickerDialogs dialogs = new PicturesPickerDialogs();
        dialogs.setListImages(Common.BUBBLE_TEXT_BACKGROUND);
        dialogs.setListener(new PicturesPickerDialogs.OnSelectedPicturesListener() {
            @Override
            public void onSelectedPictures(PicturesPickerDialogs dialogs, int position) {
                if (editLargeText.hasFocus()) {
                    editLargeText.setBackgroundResource(Common.BUBBLE_TEXT_BACKGROUND[position]);
                    backgroundLargeText = position;
                } else if (editSmallText.hasFocus()) {
                    editSmallText.setBackgroundResource(Common.BUBBLE_TEXT_BACKGROUND[position]);
                    backgroundSmallText = position;
                }
            }
        });
        dialogs.show(getSupportFragmentManager(), "Background Picker");
    }

    public void actionNext() {
        Gson gson = new Gson();

        // Save inner to postcard
        String jsonPostcard = PreferencesHandler.getValueFromPreferences(Common.JSON_POSTCARD_STRING, this);
        Postcard postcard = gson.fromJson(jsonPostcard, Postcard.class);
        inner=obtainInner();
        inner.setListSongs(listSongs);
        postcard.setInner(inner);


        // Save postcard to preferences
        PreferencesHandler.saveValueToPreferences(
                Common.JSON_POSTCARD_STRING,
                gson.toJson(postcard, Postcard.class),
                this
        );

        Log.d(TAG, gson.toJson(postcard, Postcard.class));


        Intent intent = new Intent(getApplicationContext(), TimePickerActivity.class);
        startActivity(intent);
    }

    public void actionChangeSize() {
        NumberPickerBuilder numberPickerBuilder = new NumberPickerBuilder();
        numberPickerBuilder.setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment_Light);
        numberPickerBuilder.show();
    }

    public void actionPreview() {
        Intent intent = new Intent(getApplicationContext(), PreviewInnerActivity.class);

        // get Inner
        inner = obtainInner();
        postcard.setInner(inner);

        String jsonPostcard = new Gson().toJson(postcard, Postcard.class);
        // add jsonInner to intent
        intent.putExtra(Common.JSON_POSTCARD_STRING, jsonPostcard);
        intent.putExtra(Common.FLAG, Common.FLAG_PREVIEW_POSTCARD);

        // start Preview
        startActivity(intent);

    }

    public void actionChangeTextColor() {
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
    }

    public void actionChangeFont() {
        FontPickerDialog fontPickerDialog = new FontPickerDialog();
        fontPickerDialog.show(getFragmentManager(), "font_picker");
    }


    public void actionSaveDraft() {
        inner = obtainInner();
        postcard.setInner(inner);

        String arrayJSONDraft = PreferencesHandler.getValueFromPreferences(Common.ARRAY_POSTCARD_DRAFT, getApplicationContext());
        String jsonPostcard = new Gson().toJson(postcard, Postcard.class);

        Log.d("Chien", arrayJSONDraft);

        if (arrayJSONDraft == null) {
            JSONArray jsonArray = new JSONArray();

            jsonArray.put(jsonPostcard);
            arrayJSONDraft = jsonArray.toString();

            PreferencesHandler.saveValueToPreferences(Common.ARRAY_POSTCARD_DRAFT, arrayJSONDraft, getApplicationContext());
        } else {

            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(arrayJSONDraft);
            } catch (JSONException e) {
                Log.d(TAG, arrayJSONDraft);
                jsonArray = new JSONArray();
            }

            jsonArray.put(jsonPostcard);
            arrayJSONDraft = jsonArray.toString();

            PreferencesHandler.saveValueToPreferences(Common.ARRAY_POSTCARD_DRAFT, arrayJSONDraft, getApplicationContext());
        }

        Toast.makeText(this, "Save to draft successful !", Toast.LENGTH_SHORT).show();
    }


    public void onAvatarClick(View view) {
        final PicturesPickerDialogs picturesPickerDialogs = new PicturesPickerDialogs();
        picturesPickerDialogs.setListImages(Common.HUMAN_ICON);
        picturesPickerDialogs.setListener(this);
        picturesPickerDialogs.show(getSupportFragmentManager(), "pictures_dialogs");
    }

    @Override
    public void onFontSelected(FontPickerDialog dialog) {
        Typeface font = Typeface.createFromFile(dialog.getSelectedFont());
        if (editLargeText.hasFocus()) {
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
                editLargeText.setTextSize(TypedValue.COMPLEX_UNIT_PX, number);
            } else if (editSmallText.hasFocus()) {
                editSmallText.setTextSize(TypedValue.COMPLEX_UNIT_PX, number);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Number input <0 fail", Toast.LENGTH_SHORT).show();
        }

    }


    public void onReturn(SongsPickerDialogs songsPickerDialogs) {
        listSongs = songsPickerDialogs.getListSongs();
    }

    public Inner obtainInner() {
        inner.setAvatarID(avatarID);
        inner.setBackgroundID(0);
        inner.setListSongs(listSongs);

        inner.setColorTextLarge(editLargeText.getCurrentTextColor());
        inner.setSizeTextLarge(editLargeText.getTextSize());

        try {
            inner.setTextLarge(editLargeText.getText().toString());
        } catch (NullPointerException e) {
            inner.setTextLarge("");
        }

        try {
            inner.setTextSmall(editSmallText.getText().toString());
        } catch (NullPointerException e) {
            inner.setTextSmall("");
        }

        inner.setColorTextSmall(editSmallText.getCurrentTextColor());
        inner.setSizeTextSmall(editSmallText.getTextSize());

        inner.setBackgroundTextLarge(backgroundLargeText);
        inner.setBackgroundTextSmall(backgroundSmallText);

        return inner;
    }

    public void onClickSetMusic(View view) {
        SongsPickerDialogs dialogs = new SongsPickerDialogs();
        dialogs.show(getSupportFragmentManager(), "add_music");
        dialogs.setListSongs(listSongs);
        dialogs.setListSpinnerTitle(Arrays.asList(Common.SONGS_TITLE));
        dialogs.setListSpinnerUrl(Arrays.asList(Common.SONGS_URL));
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
