package com.example.tx.hacktx;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;
import java.util.Calendar;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ListView contentListView;
    private EventAdapter eventAdapter;
    private AudioManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GetContent g = new GetContent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentListView = (ListView) findViewById(R.id.event_list);
        contentListView.setClickable(true);
        contentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Log.d("click", "click on " + position);
            }
        });

        am = (AudioManager) this.getApplicationContext().getSystemService(this.getApplicationContext().AUDIO_SERVICE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        eventAdapter = new EventAdapter(this, getLayoutInflater());
        contentListView.setAdapter(eventAdapter);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Manages the TimePicker dialog
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            setProfileTime(hourOfDay, minute);
        }
    }

    //Manages the DatePicker dialog
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            setProfileDate(year, month, day);
        }
    }

    //Shows the search dialog when the search button is clicked
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    //Shows the search dialog when the search button is clicked
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    //Sets a profile at the given time
    private static void setProfileTime(int hour, int minute){
        //TODO
    }

    //Sets a profile at the given date
    private static void setProfileDate(int year, int month, int day){
        //TODO
    }

    //Sets the ringer to silent
    public void silenceRinger(View v){
        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

    //Sets the ringer to vibrate
    public void vibrateRinger(View v){
        am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
    }

    //Sets the ringer to normal
    public void normalRinger(View v){
        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
    }
}
