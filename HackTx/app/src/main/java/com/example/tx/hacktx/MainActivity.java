package com.example.tx.hacktx;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static ListView contentListView;
    private static EventAdapter eventAdapter;
    private AudioManager am;

    public static ArrayList<Profile> profileList = new ArrayList<>();

    private PendingIntent pendingIntent;
    
    static Alarm alarm = null;

    public static GetContent g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        g = new GetContent();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentListView = (ListView) findViewById(R.id.event_list);
        contentListView.setClickable(true);

        eventAdapter = new EventAdapter(this, getLayoutInflater());
        contentListView.setAdapter(eventAdapter);

        contentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                profileList.get(position).changeActive();
                g.update();
                eventAdapter.notifyDataSetChanged();
            }
        });

        contentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long arg3) {
                profileList.remove(pos);
                g.update();
                eventAdapter.notifyDataSetChanged();
                return false;
            }
        });

        am = (AudioManager) this.getApplicationContext().getSystemService(this.getApplicationContext().AUDIO_SERVICE);

        if(alarm==null) {
            alarm = new Alarm();
            alarm.checkTime();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

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
                            for (int i = 0; i < profileList.size(); i++) {
                                Profile p = profileList.get(i);
                                Calendar c = Calendar.getInstance();
                                c.set(p.getYear(), p.getMonth(), p.getDay(), p.getStartHour(), p.getStartMinute());
                                long mils = c.getTimeInMillis();
                                if (System.currentTimeMillis() + 1000 > mils) {
                                    if(p.getActive())
                                        changeState(p.getRingerState());

                                    int day = c.get(Calendar.DAY_OF_WEEK) - 2;

                                    if (day <= -1)
                                        day = day + 7;


                                    int newDay = day + 1;
                                    boolean exit = false;
                                    int addDays = 1;
                                    while (addDays <= 7 && !exit) {
                                        if (newDay >= 7)
                                            newDay -= 7;
                                        if (p.getRepeatDays()[newDay]) {
                                            exit = true;
                                            p.addDay(addDays);
                                        }
                                        newDay++;
                                        addDays++;
                                    }
                                    if (!exit) {
                                        //if it is not a repeating event
                                        profileList.remove(i);
                                        g.update();
                                        eventAdapter.notifyDataSetChanged();
                                        i--;
                                    }

                                    triggerNotification("Volume profile changed");

                                }
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


}