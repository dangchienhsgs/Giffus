package com.dangchienhsgs.giffus.postcard;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.dialogs.datepickers.DatePickerBuilder;
import com.dangchienhsgs.giffus.dialogs.datepickers.DatePickerDialogFragment;
import com.dangchienhsgs.giffus.dialogs.timepicker.TimePickerBuilder;
import com.dangchienhsgs.giffus.dialogs.timepicker.TimePickerDialogFragment;

public class TimePickerActivity extends ActionBarActivity implements TimePickerDialogFragment.TimePickerDialogHandler,
        DatePickerDialogFragment.DatePickerDialogHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);

        DatePickerBuilder builder = new DatePickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment_Light);
        builder.show();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogTimeSet(int reference, int hourOfDay, int minute) {

    }

    @Override
    public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {

    }
}
