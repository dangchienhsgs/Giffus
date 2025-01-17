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
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.utils.PreferencesHandler;
import com.dangchienhsgs.giffus.dialogs.numberpickers.NumberPickerBuilder;
import com.dangchienhsgs.giffus.dialogs.numberpickers.NumberPickerDialogFragment;
import com.dangchienhsgs.giffus.dialogs.colorpickers.ColorPickerDialog;
import com.dangchienhsgs.giffus.dialogs.picturepickers.PicturesPickerDialogs;
import com.dangchienhsgs.giffus.map.AddLocationActivity;
import com.dangchienhsgs.giffus.map.GiftLocation;
import com.dangchienhsgs.giffus.utils.Common;
import com.dangchienhsgs.giffus.dialogs.fontpickers.FontPickerDialog;
import com.google.gson.Gson;


public class CreateCoverActivity extends ActionBarActivity implements FontPickerDialog.FontPickerDialogListener,
        NumberPickerDialogFragment.NumberPickerDialogHandler, PicturesPickerDialogs.OnSelectedPicturesListener {

    private static final int ADD_LOCATION_CODE = 1;

    private Cover cover = new Cover();
    private GiftLocation location;

    private Menu menu;

    private Postcard postcard = new Postcard();

    private String TAG = "Create cover activity";
    private RelativeLayout mLayout;
    private EditText editLargeTxt;
    private EditText editSmallTxt;

    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
        setContentView(R.layout.activity_create_cover);

        Intent intent = getIntent();

        String flag = intent.getStringExtra(Common.FLAG);

        if (flag.equals(Common.FLAG_NEW_COVER)) {
            postcard = new Postcard();
            cover = new Cover();
            initComponents();
        } else if (flag.equals(Common.FLAG_BACK_TO_COVER)) {
            String jsonString = PreferencesHandler.getValueFromPreferences(Common.JSON_POSTCARD_STRING, this);
            postcard = new Gson().fromJson(jsonString, Postcard.class);
            cover = postcard.getCover();
            try {
                location = postcard.getLocation();
            } catch (NullPointerException e) {
                location = null;
            }

            initComponentsByPostcard(cover);
        } else if (flag.equals(Common.FLAG_OPEN_DRAFT)) {
            String jsonString = PreferencesHandler.getValueFromPreferences(Common.JSON_POSTCARD_STRING, this);
            postcard = new Gson().fromJson(jsonString, Postcard.class);
            cover = postcard.getCover();
            try {
                location = postcard.getLocation();
            } catch (NullPointerException e) {
                location = null;
            }

            initComponentsByPostcard(cover);
        }

    }

    public void initComponentsByPostcard(Cover cover) {
        // init components
        editLargeTxt = (EditText) findViewById(R.id.edit_large);
        editSmallTxt = (EditText) findViewById(R.id.edit_small);

        mLayout = (RelativeLayout) findViewById(R.id.layout_postcard);

        editLargeTxt.setTextColor(editLargeTxt.getCurrentTextColor());

        this.cover = cover;

        try {
            editLargeTxt.setText(cover.getTextLarge());
        } catch (NullPointerException e) {
            editLargeTxt.setText("");
        }


        editLargeTxt.setTextColor(cover.getColorLargeText());
        editLargeTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, cover.getSizeLargeText());
        editLargeTxt.setTypeface(Typeface.createFromFile(cover.getFontTextLarge()));

        try {
            editSmallTxt.setText(cover.getTextSmall());
        } catch (NullPointerException e) {
            editSmallTxt.setText("");
        }

        editSmallTxt.setTextColor(cover.getColorSmallText());
        editSmallTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, cover.getSizeSmallText());
        editSmallTxt.setTypeface(Typeface.createFromFile(cover.getFontTextSmall()));

        Log.d(TAG, editLargeTxt.getTextSize() + "");
        Log.d(TAG, editSmallTxt.getTextSize() + "");

        mLayout.setBackgroundResource(Common.COVER_BACKGROUND[cover.getBackgroundID()]);

    }

    public void initActionBar() {
        // remove title bar from action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
    }

    public void initComponents() {
        // init components
        editLargeTxt = (EditText) findViewById(R.id.edit_large);
        editSmallTxt = (EditText) findViewById(R.id.edit_small);

        mLayout = (RelativeLayout) findViewById(R.id.layout_postcard);

        editLargeTxt.setTextColor(editLargeTxt.getCurrentTextColor());

        // Init data cover
        cover.setBackgroundID(0);

        cover.setFontTextLarge(Common.ROBOTO_LIGHT_FONT_PATH);
        cover.setTextLarge("");

        cover.setTextSmall("");
        cover.setFontTextSmall(Common.ROBOTO_LIGHT_FONT_PATH);

        editSmallTxt.setTypeface(Typeface.createFromFile(Common.ROBOTO_LIGHT_FONT_PATH));
        editSmallTxt.setTypeface(Typeface.createFromFile(Common.ROBOTO_LIGHT_FONT_PATH));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_cover, menu);

        this.menu = menu;
        MenuItem menuItem = menu.findItem(R.id.action_remove_location);
        menuItem.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.change_background:
                actionChangeBackground();
                break;
            case R.id.action_next:
                actionNext();

                break;
            case R.id.action_edit_text_color:
                actionChangeColor();
                break;

            case R.id.action_change_font:
                actionChangeFont();
                break;

            case R.id.action_change_size:
                actionChangeSize();
                break;

            case R.id.action_preview:
                actionPreview();
                break;

            case R.id.action_add_map:
                actionAddMap();
                break;

            case R.id.action_remove_location:
                actionRemoveLocation();
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public Cover obtainCover() {
        try {
            cover.setTextLarge(editLargeTxt.getText().toString());
        } catch (NullPointerException e) {
            cover.setTextLarge("");
        }

        cover.setSizeLargeText(editLargeTxt.getTextSize());
        cover.setSizeSmallText(editSmallTxt.getTextSize());

        Log.d(TAG, editLargeTxt.getTextSize() + "");
        Log.d(TAG, editSmallTxt.getTextSize() + "");


        try {
            cover.setTextSmall(editSmallTxt.getText().toString().trim());
        } catch (NullPointerException e) {
            cover.setTextSmall("");
        }

        cover.setColorLargeText(editLargeTxt.getCurrentTextColor());
        cover.setColorSmallText(editSmallTxt.getCurrentTextColor());

        return cover;
    }

    public void actionChangeBackground() {
        PicturesPickerDialogs picturesPickerDialogs = new PicturesPickerDialogs();
        picturesPickerDialogs.setListener(this);
        picturesPickerDialogs.setListImages(Common.COVER_BACKGROUND);
        picturesPickerDialogs.show(getSupportFragmentManager(), "pictures_dialogs");
    }

    public void actionRemoveLocation() {
        MenuItem menuItem = menu.findItem(R.id.action_remove_location);
        menuItem.setVisible(false);

        postcard.setLocation(null);
        location = null;
    }

    public void actionChangeColor() {
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(
                this,
                Color.WHITE,
                new ColorPickerDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                        if (editLargeTxt.hasFocus()) {
                            editLargeTxt.setTextColor(color);
                        } else if (editSmallTxt.hasFocus()) {
                            editSmallTxt.setTextColor(color);

                        }
                    }
                }
        );
        colorPickerDialog.show();
    }

    public void actionNext() {

        // Go to the next step
        cover = obtainCover();

        postcard.setCover(cover);
        postcard.setLocation(location);

        PreferencesHandler.saveValueToPreferences(
                Common.JSON_POSTCARD_STRING,
                new Gson().toJson(postcard, Postcard.class),
                this
        );

        Intent intent = new Intent(getApplicationContext(), CreateInnerActivity.class);
        startActivity(intent);

        finish();
    }


    public void actionPreview() {
        Intent intent = new Intent(getApplicationContext(), PreviewCoverActivity.class);

        // obtain cover and location to send to Preview Activity
        Cover cover = obtainCover();
        postcard.setCover(cover);
        postcard.setLocation(location);


        // convert cover
        String jsonPostcard = new Gson().toJson(postcard, Postcard.class);
        intent.putExtra(Common.JSON_POSTCARD_STRING, jsonPostcard);
        intent.putExtra(Common.FLAG, Common.FLAG_PREVIEW_POSTCARD);

        Log.d(TAG, jsonPostcard);
        // Start activity
        startActivity(intent);

    }

    public void actionAddMap() {
        Intent intent = new Intent(getApplicationContext(), AddLocationActivity.class);
        startActivityForResult(intent, ADD_LOCATION_CODE);
    }

    public void actionChangeSize() {
        NumberPickerBuilder numberPickerBuilder = new NumberPickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment_Light);
        numberPickerBuilder.show();
    }

    public void actionChangeFont() {
        FontPickerDialog fontPickerDialog = new FontPickerDialog();
        fontPickerDialog.show(getFragmentManager(), "font_picker");
    }

    @Override
    public void onFontSelected(FontPickerDialog dialog) {
        Typeface typeface = Typeface.createFromFile(dialog.getSelectedFont());
        if (editLargeTxt.hasFocus()) {
            editLargeTxt.setTypeface(typeface);
            cover.setFontTextLarge(dialog.getSelectedFont());
        } else if (editSmallTxt.hasFocus()) {
            editSmallTxt.setTypeface(typeface);
            cover.setFontTextSmall(dialog.getSelectedFont());
        }
    }

    @Override
    public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber) {
        if (!isNegative && number > 0) {
            if (editLargeTxt.hasFocus()) {
                editLargeTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, number);
            } else if (editSmallTxt.hasFocus()) {
                editSmallTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, number);
            }
        }
    }

    @Override
    public void onSelectedPictures(PicturesPickerDialogs dialogs, int position) {
        mLayout.setBackgroundResource(Common.COVER_BACKGROUND[position]);
        cover.setBackgroundID(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_LOCATION_CODE) {
            // This is the data from the adding map location process
            // Check result code
            if (resultCode == RESULT_OK) {

                String jsonLocation = data.getStringExtra(Common.JSON_GIFFUS_LOCATION);
                Gson gson = new Gson();
                location = gson.fromJson(jsonLocation, GiftLocation.class);
                postcard.setLocation(location);

                MenuItem removeLocation = menu.findItem(R.id.action_remove_location);
                removeLocation.setVisible(true);

            }
        }
    }
}
