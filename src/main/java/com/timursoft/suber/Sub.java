package com.timursoft.suber;

import java.io.Serializable;

/**
 * Entity representing one line in the subtitles
 */
public class Sub implements Serializable {

    public String content;
    public int startTime;
    public int endTime;
    public String style;

    public Sub() {
    }

    public Sub(String content, int startTime, int endTime, String style) {
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
        this.style = style;
    }

}
