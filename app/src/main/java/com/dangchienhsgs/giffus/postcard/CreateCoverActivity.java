package com.dangchienhsgs.giffus.postcard;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.dialogs.numberpickers.NumberPickerBuilder;
import com.dangchienhsgs.giffus.dialogs.numberpickers.NumberPickerDialogFragment;
import com.dangchienhsgs.giffus.dialogs.colorpickers.ColorPickerDialog;
import com.dangchienhsgs.giffus.dialogs.picturepickers.PicturesPickerDialogs;
import com.dangchienhsgs.giffus.map.AddLocationActivity;
import com.dangchienhsgs.giffus.utils.Common;
import com.dangchienhsgs.giffus.dialogs.fontpickers.FontPickerDialog;
import com.google.gson.Gson;


public class CreateCoverActivity extends ActionBarActivity implements FontPickerDialog.FontPickerDialogListener,
        NumberPickerDialogFragment.NumberPickerDialogHandler, PicturesPickerDialogs.OnSelectedPicturesListener {

    private Cover cover = new Cover();
    private String TAG = "Create cover activity";


    private RelativeLayout mLayout;

    private EditText editLargeTxt;
    private EditText editSmallTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_create_cover);

        editLargeTxt = (EditText) findViewById(R.id.text_large);
        editSmallTxt = (EditText) findViewById(R.id.edit_small_text);

        editLargeTxt.setTextColor(editLargeTxt.getCurrentTextColor());

        // Init Data cover
        cover.setBackgroundID(1);
        cover.setFontTextLarge(Common.ROBOTO_LIGHT_FONT_PATH);
        cover.setTextLarge("");

        cover.setTextSmall("");
        cover.setFontTextSmall(Common.ROBOTO_LIGHT_FONT_PATH);

        editSmallTxt.setTypeface(Typeface.createFromFile(Common.ROBOTO_LIGHT_FONT_PATH));
        editSmallTxt.setTypeface(Typeface.createFromFile(Common.ROBOTO_LIGHT_FONT_PATH));


        mLayout = (RelativeLayout) findViewById(R.id.layout_postcard);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_cover, menu);
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
                PicturesPickerDialogs picturesPickerDialogs = new PicturesPickerDialogs();
                picturesPickerDialogs.setListImages(Common.COVER_BACKGROUND);
                picturesPickerDialogs.show(getSupportFragmentManager(), "pictures_dialogs");
                break;
            case R.id.action_next:
                //UserHandler.saveValueToPreferences(Cover.IMAGE_ID, );
                Intent intent = new Intent(getApplicationContext(), CreateInnerActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_left, R.anim.pull_out_right);

                break;
            case R.id.action_edit_text_color:
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
                break;

            case R.id.action_change_font:
                FontPickerDialog fontPickerDialog = new FontPickerDialog();
                fontPickerDialog.show(getFragmentManager(), "font_picker");
                break;

            case R.id.action_change_size:
                NumberPickerBuilder numberPickerBuilder = new NumberPickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light);
                numberPickerBuilder.show();
                break;

            case R.id.action_preview:
                intent = new Intent(getApplicationContext(), PreviewCoverActivity.class);

                // obtain cover to send to Preview ACtivity
                cover = obtainCover();

                // convert cover
                String jsonCover = new Gson().toJson(cover, Cover.class);
                intent.putExtra(Cover.JSON_NAME, jsonCover);

                // Start activity
                startActivity(intent);
                break;

            case R.id.action_add_map:
                intent = new Intent(getApplicationContext(), AddLocationActivity.class);
                startActivity(intent);
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_right, R.anim.pull_out_left);
    }

    public Cover obtainCover() {
        cover.setTextLarge(editLargeTxt.getText().toString());
        cover.setSizeLargeText(editLargeTxt.getTextSize());
        cover.setSizeSmallText(editSmallTxt.getTextSize());

        cover.setTextSmall(editSmallTxt.getText().toString());
        cover.setColorLargeText(editLargeTxt.getCurrentTextColor());
        cover.setColorSmallText(editSmallTxt.getCurrentTextColor());

        return cover;
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
                editLargeTxt.setTextSize(number);
            } else if (editSmallTxt.hasFocus()) {
                editSmallTxt.setTextSize(number);
            }
        }
    }

    @Override
    public void onSelectedPictures(PicturesPickerDialogs dialogs, int position) {
        mLayout.setBackgroundResource(Common.COVER_BACKGROUND[position]);
        cover.setBackgroundID(position);
    }
}
