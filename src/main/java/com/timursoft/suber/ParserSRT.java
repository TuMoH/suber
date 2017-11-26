package com.timursoft.suber;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents the .SRT subtitle format
 */
public class ParserSRT implements Parser {

    private final static Pattern PARSE_PATTERN = Pattern.compile(
            "(\\d+)(?:\\r\\n|\\n)(\\d+:\\d+:\\d+,\\d+)(?: --> )(\\d+:\\d+:\\d+,\\d+)(?:\\r\\n|\\n)((?:.*(?:\\r\\n|\\n)*[^\\n\\d]+)*)");

    private final static Pattern TIME_PATTERN = Pattern.compile(
            "(\\d+):(\\d+):(\\d+),(\\d+)");

    @Override
    public SubFileObject parse(String text) {
        SubFileObject subFileObject = new SubFileObject();

        Matcher matcher = PARSE_PATTERN.matcher(text);
        while (matcher.find()) {
            String startTime = matcher.group(2);
            String endTime = matcher.group(3);
            String content = matcher.group(4);

            Sub sub = new Sub(content, parseTime(startTime), parseTime(endTime), null);
            subFileObject.subs.add(sub);
        }
        subFileObject.sortSubs();
        return subFileObject;
    }

    public String serialize(SubFileObject subFileObject) {
        StringBuilder result = new StringBuilder();
        int captionNumber = 1;

        for (Sub sub : subFileObject.subs) {
            //number is written
            result.append(captionNumber++).append(LINE_SEPARATOR);
            //time is written
            result.append(serializeTime(sub.startTime))
                    .append(" --> ")
                    .append(serializeTime(sub.endTime))
                    .append(LINE_SEPARATOR);
            //text is added
            result.append(sub.content).append(LINE_SEPARATOR);

            //we add the next blank line
            result.append(LINE_SEPARATOR);
        }
        return result.toString();
    }

    private int parseTime(String text) {
        // this type of format:  01:02:22,501
        int h = 0, m = 0, s = 0, ms = 0;

        Matcher matcher = TIME_PATTERN.matcher(text);
        if (matcher.find()) {
            h = Integer.parseInt(matcher.group(1));
            m = Integer.parseInt(matcher.group(2));
            s = Integer.parseInt(matcher.group(3));
            ms = Integer.parseInt(matcher.group(4));
        }

        return (ms + s * 1000 + m * 60000 + h * 3600000);
    }

    private String serializeTime(int milliseconds) {
        // this type of format:  01:02:22,501
        StringBuilder time = new StringBuilder();
        String aux;
        int h, m, s, ms;

        h = milliseconds / 3600000;
        aux = String.valueOf(h);
        if (aux.length() == 1) time.append('0');
        time.append(aux);
        time.append(':');
        m = (milliseconds / 60000) % 60;
        aux = String.valueOf(m);
        if (aux.length() == 1) time.append('0');
        time.append(aux);
        time.append(':');
        s = (milliseconds / 1000) % 60;
        aux = String.valueOf(s);
        if (aux.length() == 1) time.append('0');
        time.append(aux);
        time.append(',');
        ms = milliseconds % 1000;
        aux = String.valueOf(ms);
        if (aux.length() == 1) time.append("00");
        else if (aux.length() == 2) time.append('0');
        time.append(aux);

        return time.toString();
    }

}
