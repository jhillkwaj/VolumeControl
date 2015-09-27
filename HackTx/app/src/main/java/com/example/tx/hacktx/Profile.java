package com.example.tx.hacktx;

import android.media.AudioManager;

import java.util.Calendar;

public class Profile {

    private String name;
    private String description;
    private int ringerState;
    private int startHour;
    private int startMinute;
    private int year;
    private int month;
    private int day;
    private boolean[] repeatDays;
    private boolean active = true;

    public Profile(String n, String desc, int state, int starth, int startmin, int y, int m, int d, boolean[] repeats){
        name = n;
        description = desc;
        ringerState = state;
        startHour = starth;
        startMinute = startmin;
        year = y;
        month = m;
        day = d;
        repeatDays = repeats;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public int getRingerState(){
        return ringerState;
    }

    public int getStartHour(){
        return startHour;
    }

    public int getStartMinute(){
        return startMinute;
    }

    public int getYear(){
        return year;
    }

    public int getMonth(){
        return month;
    }

    public int getDay(){
        return day;
    }

    public boolean[]  getRepeatDays(){
        return repeatDays;
    }

    public boolean getActive()
    {
        return active;
    }

    public void changeActive()
    {
        active = !active;
    }

    public void addDay(int times){
        if(times>0) {
            Calendar c = Calendar.getInstance();
            c.set(getYear(), getMonth(), getDay(), getStartHour(), getStartMinute());
            c.add(Calendar.DAY_OF_MONTH, 1);
            /*if(day==1) {
                c.roll(Calendar.MONTH, 1);
                if(month==1)
                    c.add(Calendar.YEAR, 1);
            }*/
            day = c.get(Calendar.DAY_OF_MONTH);
            month = c.get(Calendar.MONTH);
            year = c.get(Calendar.YEAR);
            addDay(times - 1);
        }else
            createDescription();

    }

    private void createDescription() {
        if (startMinute == 0) {
            switch (ringerState) {
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
        } else {
            switch (ringerState) {
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

    public void setName(String n){
        name = n;
    }

    public void setDescription(String desc){
        description = desc;
    }

    public void setRingerState(int state){
        ringerState = state;
    }

    public void setStartHour(int h){
        startHour = h;
    }

    public void setStartMinute(int m){
        startMinute = m;
    }

    public void setYear(int y){
        year = y;
    }

    public void setMonth(int m){
        month = m;
    }

    public void setDay(int d){
        day = d;
    }

    public void setRepeatDays(boolean[] repeat){
        repeatDays = repeat;
    }
}
