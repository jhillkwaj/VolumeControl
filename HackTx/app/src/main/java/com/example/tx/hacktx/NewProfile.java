package com.example.tx.hacktx;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;

public class NewProfile extends AppCompatActivity implements OnItemSelectedListener {

    private AudioManager am;

    //Profile data
    private static String name = "TEST";
    private static String description = "SAMPLE TEXT";
    private static int state;
    private static int startHour;
    private static int startMinute;
    private static int endHour;
    private static int endMinute;
    private static int year;
    private static int month;
    private static int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);

        am = (AudioManager) this.getApplicationContext().getSystemService(this.getApplicationContext().AUDIO_SERVICE);

        createStateSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_profile, menu);
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

    //Manages the start TimePicker dialog
    public static class StartTimePickerFragment extends DialogFragment
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
            setProfileStartTime(hourOfDay, minute);
        }
    }

    //Manages the end TimePicker dialog
    public static class EndTimePickerFragment extends DialogFragment
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
            setProfileEndTime(hourOfDay, minute);
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


    public void showStartTimePickerDialog(View v) {
        DialogFragment newFragment = new StartTimePickerFragment();
        newFragment.show(getFragmentManager(), "startTimePicker");
    }

    public void showEndTimePickerDialog(View v) {
        DialogFragment newFragment = new EndTimePickerFragment();
        newFragment.show(getFragmentManager(), "endTimePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private static void setProfileStartTime(int h, int m){
        startHour = h;
        endMinute = m;
    }

    private static void setProfileEndTime(int h, int m){
        endHour = h;
        endMinute = m;
    }

    private static void setProfileDate(int y, int m, int d){
        year = y;
        month = m;
        day = d;
    }

//    //Sets the ringer to silent
//    public void silenceRinger(View v){
//        state = AudioManager.RINGER_MODE_SILENT;
//        am.setRingerMode(state);
//    }
//
//    //Sets the ringer to vibrate
//    public void vibrateRinger(View v){
//        state = AudioManager.RINGER_MODE_VIBRATE;
//        am.setRingerMode(state);
//    }
//
//    //Sets the ringer to normal
//    public void normalRinger(View v) {
//        state = AudioManager.RINGER_MODE_NORMAL;
//        am.setRingerMode(state);
//    }

    public void createStateSpinner(){
        Spinner spinner = (Spinner) findViewById(R.id.ringer_state_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ringer_states_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using parent.getItemAtPosition(pos)
        switch(pos){
            case 0: state = AudioManager.RINGER_MODE_SILENT; break;
            case 1: state = AudioManager.RINGER_MODE_VIBRATE; break;
            case 2: state = AudioManager.RINGER_MODE_NORMAL; break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void setProfile(View v) {
        createDescription();

        Profile profile = new Profile(name, description, state, startHour, startMinute, endHour, endMinute, year, month, day);
        MainActivity.addProfileToList(profile);

        Intent profileSet = new Intent(this, MainActivity.class);
        startActivity(profileSet);
        this.finish();
    }

    private void createDescription(){
        switch(state){
            case AudioManager.RINGER_MODE_SILENT: description = "Silence set from " + startHour + ":" + startMinute + " to " + endHour + ":" + endMinute +
                                                                    " on " + month + "/" + day + "/" + year + "."; break;
            case AudioManager.RINGER_MODE_VIBRATE: description = "Vibrate set from " + startHour + ":" + startMinute + " to " + endHour + ":" + endMinute +
                                                                    " on " + month + "/" + day + "/" + year + "."; break;
            case AudioManager.RINGER_MODE_NORMAL: description = "Ringer set from " + startHour + ":" + startMinute + " to " + endHour + ":" + endMinute +
                                                                    " on " + month + "/" + day + "/" + year + "."; break;
        }
    }
}