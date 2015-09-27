package com.example.tx.hacktx;

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
