package com.example.tx.hacktx;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import java.util.Calendar;

public class NewProfile extends AppCompatActivity implements OnItemSelectedListener {

    private AudioManager am;

    //Profile data
    private static String name;
    private static String description;
    private static int state;
    private static int startHour;
    private static int startMinute;
    private static int year;
    private static int month;
    private static int day;

    //Boolean values indicating which days to repeat the profile
    private static boolean[] repeatDays = new boolean[9];

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

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private static void setProfileStartTime(int h, int m){
        startHour = h;
        startMinute = m;
    }

    private static void setProfileDate(int y, int m, int d){
        year = y;
        month = m;
        day = d;
    }

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

        EditText mEdit   = (EditText)findViewById(R.id.title_text_box);
        setName(mEdit.getText().toString());

        if(name.equals("") || startHour == 0 || day == 0 || month == 0 || year == 0){
            Toast.makeText(NewProfile.this, "You must enter a name, time, and date to create a profile.", Toast.LENGTH_LONG).show();
        }

        else {
            Profile profile = new Profile(name, description, state, startHour, startMinute, year, month, day, repeatDays);
            MainActivity.addProfileToList(profile);

            Intent profileSet = new Intent(this, MainActivity.class);
            startActivity(profileSet);
            this.finish();
        }

        MainActivity.setCreatedNewProfile();
    }

    private void createDescription(){
        if(startMinute == 0) {
            switch (state) {
                case AudioManager.RINGER_MODE_SILENT:
                    description = "Silence set at " + startHour + ":00 on " + month + "/" + day + "/" + year + ".";
                    break;
                case AudioManager.RINGER_MODE_VIBRATE:
                    description = "Vibrate set at " + startHour + ":00 on " + month + "/" + day + "/" + year + ".";
                    break;
                case AudioManager.RINGER_MODE_NORMAL:
                    description = "Ringer set at " + startHour + ":00 on " + month + "/" + day + "/" + year + ".";
                    break;
            }
        }
        else{
            switch (state) {
                case AudioManager.RINGER_MODE_SILENT:
                    description = "Silence set at " + startHour + ":" + startMinute + " on " + month + "/" + day + "/" + year + ".";
                    break;
                case AudioManager.RINGER_MODE_VIBRATE:
                    description = "Vibrate set at " + startHour + ":" + startMinute + " on " + month + "/" + day + "/" + year + ".";
                    break;
                case AudioManager.RINGER_MODE_NORMAL:
                    description = "Ringer set at " + startHour + ":" + startMinute + " on " + month + "/" + day + "/" + year + ".";
                    break;
            }
        }
    }

    private void setName(String n){
        name = n;
    }

    //Called if one of the profile repeat check boxes is checked
    public void itemClicked(View v) {
        if(v instanceof CheckBox) {
            //code to check if this checkbox is checked!
            CheckBox checkBox = (CheckBox) v;
            switch (checkBox.getId()) {
                case (R.id.mondayCheckBox):
                    repeatDays[0] = checkBox.isChecked();
                    break;
                case (R.id.tuesdayCheckBox):
                    repeatDays[1] = checkBox.isChecked();
                    break;
                case (R.id.wednesdayCheckBox):
                    repeatDays[2] = checkBox.isChecked();
                    break;
                case (R.id.thursdayCheckBox):
                    repeatDays[3] = checkBox.isChecked();
                    break;
                case (R.id.fridayCheckBox):
                    repeatDays[4] = checkBox.isChecked();
                    break;
                case (R.id.saturdayCheckBox):
                    repeatDays[5] = checkBox.isChecked();
                    break;
                case (R.id.sundayCheckBox):
                    repeatDays[6] = checkBox.isChecked();
                    break;
            }
        }
    }
}