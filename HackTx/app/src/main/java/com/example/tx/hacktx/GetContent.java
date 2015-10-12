package com.example.tx.hacktx;

import java.util.ArrayList;

public class GetContent {

    public static ArrayList<String> names = new ArrayList<>();
    public static ArrayList<Integer> image = new ArrayList<>();

    public GetContent() {
        names.clear();
        image.clear();

        for(Profile p : MainActivity.profileList){
            names.add(p.getName() + ": " + p.getDescription());
            if(p.getActive())
            { image.add(0);}
            else
                image.add(1);
        }
    }

    public void update(){
        names.clear();
        image.clear();

        for(Profile p : MainActivity.profileList){
            names.add(p.getName() + ": " + p.getDescription());
            if(p.getActive())
            { image.add(0);}
            else
                image.add(1);
        }
    }



    /**
     * Returns an array containing the title, text, imageURL, source and other information about a post
     */
    public static String[] getContent(int spot) {
        String[] info = new String[2];
        info[0] = names.get(spot);
        info[1] = ""+image.get(spot);
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
