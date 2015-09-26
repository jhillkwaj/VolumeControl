package com.example.wavej.hacktx;

import java.util.ArrayList;

/**
 * Created by justi on 9/26/2015.
 */
public class GetContent {

    public static ArrayList<String> names = new ArrayList<>();

    public GetContent()
    {
        names.add("Turn on Airplane mode between 9 and 9 on weekends");
        names.add("Mute my phone when I am out");
    }


    /**
     * Returns an array containing the title, text, imageURL, source and other information about a post
     */
    public static String[] getContent(int spot)
    {
        String[] info = new String[2];
        info[0] = names.get(spot);
        info[1] = null;
        return info;
    }



    /**
     * Returns the number of posts from all of the open feeds
     */
    public static int getLength()
    {
        return names.size();
    }


}
