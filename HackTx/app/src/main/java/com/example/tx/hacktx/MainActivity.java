package com.example.tx.hacktx;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import java.io.File;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView contentListView;
    private EventAdapter eventAdapter;
    private AudioManager am;

    public static ArrayList<Profile> profileList = new ArrayList<>();

    public static final String PREFS_NAME = "MyPrefsFile";

    private static boolean newProfile = false;

    private PendingIntent pendingIntent;
    
    static Alarm alarm = null;

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

        if(alarm==null) {
            alarm = new Alarm();
            alarm.checkTime();
        }

        getProfilesFromStorage();
    }

    @Override
    protected void onStop(){
        super.onStop();
        storeProfilesToStorage();
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

    public void getProfilesFromStorage(){
        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        boolean[] repeats = new boolean[7];
        repeats[0] = settings.getBoolean("repeatM", true);
        repeats[1] = settings.getBoolean("repeatT", true);
        repeats[2] = settings.getBoolean("repeatW", true);
        repeats[3] = settings.getBoolean("repeatH", true);
        repeats[4] = settings.getBoolean("repeatF", true);
        repeats[5] = settings.getBoolean("repeatS", true);
        repeats[6] = settings.getBoolean("repeatU", true);
        Profile p = new Profile(settings.getString("name", null), settings.getString("description", null), settings.getInt("ringerState", 0), settings.getInt("hour", 0),
                settings.getInt("minute", 0), settings.getInt("year", 0), settings.getInt("month", 0), settings.getInt("day", 0), repeats);
        Log.d("refresh", p.getName());
        profileList.add(p);
    }

    public void clearStoredProfiles(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }

    public void storeProfilesToStorage(){
        clearStoredProfiles();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        for(Profile p: profileList) {
            editor.putString("name", p.getName());
            editor.putString("description", p.getDescription());
            editor.putInt("ringerState", p.getRingerState());
            editor.putInt("hour", p.getStartHour());
            editor.putInt("minute", p.getStartMinute());
            editor.putInt("year", p.getYear());
            editor.putInt("month", p.getMonth());
            editor.putInt("day", p.getDay());
            editor.putBoolean("repeatM", p.getRepeatDays()[0]);
            editor.putBoolean("repeatT", p.getRepeatDays()[1]);
            editor.putBoolean("repeatW", p.getRepeatDays()[2]);
            editor.putBoolean("repeatH", p.getRepeatDays()[3]);
            editor.putBoolean("repeatF", p.getRepeatDays()[4]);
            editor.putBoolean("repeatS", p.getRepeatDays()[5]);
            editor.putBoolean("repeatU", p.getRepeatDays()[6]);
        }

        // Commit the edits!
        editor.commit();
    }

    public void buttonClick(View v) {
        Log.d("click", "buttonClick");
        Intent pressFAB = new Intent(this, NewProfile.class);
        startActivity(pressFAB);
        this.finish();
    }

    public static void addProfileToList(Profile profile){
        profileList.add(profile);
    }

    //Sets the ringer to silent
    public void silenceRinger(View v){
        int state = AudioManager.RINGER_MODE_SILENT;
        am.setRingerMode(state);
    }

    //Sets the ringer to vibrate
    public void vibrateRinger(View v){
        int state = AudioManager.RINGER_MODE_VIBRATE;
        am.setRingerMode(state);
    }

    //Sets the ringer to normal
    public void normalRinger(View v) {
        int state = AudioManager.RINGER_MODE_NORMAL;
        am.setRingerMode(state);
    }

    public void changeState(int newState) {
        am.setRingerMode(newState);
    }

    class Alarm {
        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        public void checkTime() {
            scheduler.scheduleAtFixedRate
                    (new Runnable() {
                        public void run() {
                            Log.d("test","test");
                            for(int i = 0; i < profileList.size(); i++)
                            {
                                Profile p = profileList.get(i);
                                Calendar c = Calendar.getInstance();
                                c.set(p.getYear(), p.getMonth(), p.getDay(), p.getStartHour(), p.getStartMinute());
                                long mils = c.getTimeInMillis();
                                if(System.currentTimeMillis() + 1000 > mils) {
                                    changeState(p.getRingerState());
                                    profileList.remove(i);
                                    i--;
                                    eventAdapter.update();
                                    eventAdapter.notifyDataSetChanged();
                                    triggerNotification("Volume profile changed");
                                }
                            }
                            if(newProfile){
                                storeProfilesToStorage();
                                newProfile = false;
                            }
                        }
                    }, 0, 10, TimeUnit.SECONDS);
        }
    }

    private void triggerNotification(String s) {
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_alarm_black)
                        .setContentTitle("Volume Profile Scheduler")
                        .setContentText(s);
        Intent resultIntent = new Intent(this, this.getClass());

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(this.getClass());
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

    public static void setCreatedNewProfile(){
        newProfile = true;
    }
}