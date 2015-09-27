package com.example.tx.hacktx;

public class Profile {

    public String name;
    public String description;
    public int ringerState;
    public int startHour;
    public int startMinute;
    public int endHour;
    public int endMinute;
    public int year;
    public int month;
    public int day;

    public Profile(String n, String desc, int state, int sh, int smin, int eh, int em, int y, int m, int d){
        name = n;
        description = desc;
        ringerState = state;
        startHour = sh;
        startMinute = smin;
        endHour = eh;
        endMinute = em;
        year = y;
        month = m;
        day = d;
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

    public int getEndHour(){
        return endHour;
    }

    public int getEndMinute(){
        return endMinute;
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
}
