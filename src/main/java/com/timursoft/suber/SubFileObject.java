package com.timursoft.suber;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * These objects can (should) only be created through the implementations of parse() in the {@link Parser} interface
 * They are an object representation of a subtitle file and contain all the subtitles and associated styles.
 */
public class SubFileObject {

    public String scriptInfoText;
    public String stylesText;
    public String fontsText;
    public String graphicsText;

    //list of subtitles (begin time, reference)
    //represented by a tree map to maintain order
    public Map<Integer, Sub> subtitles = new TreeMap<>();

    public void addSub(Sub sub) {
        int key = sub.startTime;
        //in case the key is already there, we increase it by a millisecond, since no duplicates are allowed
        while (subtitles.containsKey(key)) key++;
        subtitles.put(key, sub);
    }

    public Collection<Sub> getSubs() {
        return subtitles.values();
    }

}
