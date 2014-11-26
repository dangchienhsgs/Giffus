package com.dangchienhsgs.giffus.postcard;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.client.PreferencesHandler;
import com.dangchienhsgs.giffus.dialogs.datepickers.DatePickerBuilder;
import com.dangchienhsgs.giffus.dialogs.datepickers.DatePickerDialogFragment;
import com.dangchienhsgs.giffus.dialogs.timepicker.TimePickerBuilder;
import com.dangchienhsgs.giffus.dialogs.timepicker.TimePickerDialogFragment;
import com.dangchienhsgs.giffus.map.GiftLocation;
import com.dangchienhsgs.giffus.utils.Common;
import com.google.gson.Gson;

import java.util.Calendar;

public class TimePickerActivity extends ActionBarActivity
        implements TimePickerDialogFragment.TimePickerDialogHandler,
        DatePickerDialogFragment.DatePickerDialogHandler,
        EditText.OnClickListener {


    private CheckBox secretCheckbox;
    private EditText editTime;
    private EditText editDate;

    private int year;
    private int month;
    private int day;

    private int minute;
    private int hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);

        initComponents();
    }

    public void initComponents() {
        secretCheckbox = (CheckBox) findViewById(R.id.check_box_secret_sender);
        editTime = (EditText) findViewById(R.id.edit_time);
        editDate = (EditText) findViewById(R.id.edit_date);

        editDate.setOnClickListener(this);
        editTime.setOnClickListener(this);

        // Get year, month, day
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(calendar.YEAR);
        day = calendar.get(calendar.DAY_OF_MONTH);
        month = calendar.get(calendar.MONTH);

        // get hour
        hour = calendar.get(calendar.HOUR_OF_DAY);
        minute = calendar.get(calendar.MINUTE);


        // edit date time
        editDate.setText(day + "/" + month + "/" + year);
        editTime.setText(hour + ":" + minute);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.time_picker, menu);
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
                actionNext();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void actionNext() {

        String jsonPostcard = PreferencesHandler.getValueFromPreferences(Common.JSON_POSTCARD_STRING, this);
        Postcard postcard = new Gson().fromJson(jsonPostcard, Postcard.class);

        postcard.setDay(day);
        postcard.setMonth(month);
        postcard.setYear(year);
        postcard.setHour(hour);
        postcard.setMinute(minute);
        postcard.setSecretSender(secretCheckbox.isChecked());

        PreferencesHandler.saveValueToPreferences(Common.JSON_POSTCARD_STRING, new Gson().toJson(postcard, Postcard.class), this);

        Intent intent = new Intent(this, ChooseFriendActivity.class);
        startActivity(intent);


    }

    @Override
    public void onDialogTimeSet(int reference, int hourOfDay, int minute) {
        this.minute = minute;
        this.hour = hourOfDay;

        editTime.setText(hour + ":" + minute);
    }

    @Override
    public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {
        this.day = dayOfMonth;
        this.month = monthOfYear;
        this.year = year;

        editDate.setText(day + "/" + month + "/" + year);

    }

    public void obtainDatetime() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.edit_date:
                DatePickerBuilder builder = new DatePickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light);
                builder.show();
                break;
            case R.id.edit_time:
                TimePickerBuilder timeBuilder = new TimePickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light);
                timeBuilder.show();
        }
    }
}
