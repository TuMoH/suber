package com.timursoft.suber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * These objects can (should) only be created through the implementations of parse() in the {@link Parser} interface
 * They are an object representation of a subtitle file and contain all the subtitles and associated styles.
 */
public class SubFileObject {

    public String scriptInfoText;
    public String stylesText;
    public String fontsText;
    public String graphicsText;

    public List<Sub> subs = new ArrayList<>();

    public void sortSubs() {
        Collections.sort(subs, new Comparator<Sub>() {
            @Override
            public int compare(Sub sub1, Sub sub2) {
                return Integer.compare(sub1.startTime, sub2.startTime);
            }
        });
    }

}
