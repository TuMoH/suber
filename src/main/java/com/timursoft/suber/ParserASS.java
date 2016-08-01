package com.timursoft.suber;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that represents the .ASS and .SSA subtitle file format
 */
public class ParserASS implements Parser {

    private final static Pattern BLOCK_PATTERN = Pattern.compile("\\[(.*)\\](\r\n|\n)([^\\[]*)");

    public static final String SCRIPT_INFO = "Script Info";
    public static final String V4_STYLES = "V4+ Styles";
    public static final String EVENTS = "Events";
    public static final String FONTS = "Fonts";
    public static final String GRAPHICS = "Graphics";

    public SubFileObject parse(String text) {
        SubFileObject subFileObject = new SubFileObject();

        Matcher matcher = BLOCK_PATTERN.matcher(text);
        while (matcher.find()) {
            String head = matcher.group(1);
            String body = matcher.group(3).trim();

            if (SCRIPT_INFO.equalsIgnoreCase(head)) {
                subFileObject.scriptInfoText = body;
            } else if (V4_STYLES.equalsIgnoreCase(head)) {
                subFileObject.stylesText = body;
            } else if (EVENTS.equalsIgnoreCase(head)) {
                String[] lines = body.split("\r\n|\n");
                String[] dialogueFormat = lines[0].split(":")[1].trim().split(",");
                for (String line : lines) {
                    if (line.startsWith("Dialogue:")) {
                        //we parse the dialogue
                        Sub sub = parseDialogue(line.split(":", 2)[1].trim()
                                .split(",", dialogueFormat.length), dialogueFormat);
                        subFileObject.subs.add(sub);
                    }
                }
            } else if (FONTS.equalsIgnoreCase(head)) {
                subFileObject.fontsText = body;
            } else if (GRAPHICS.equalsIgnoreCase(head)) {
                subFileObject.graphicsText = body;
            }
        }
        subFileObject.sortSubs();
        return subFileObject;
    }

    public String serialize(SubFileObject subFileObject) {
        StringBuilder result = new StringBuilder();

        result.append("[").append(SCRIPT_INFO).append("]").append(LINE_SEPARATOR);
        result.append(subFileObject.scriptInfoText).append(LINE_SEPARATOR);
        result.append(LINE_SEPARATOR);

        if (subFileObject.stylesText != null) {
            result.append("[").append(V4_STYLES).append("]").append(LINE_SEPARATOR);
            result.append(subFileObject.stylesText).append(LINE_SEPARATOR);
            result.append(LINE_SEPARATOR);
        }

        result.append("[").append(EVENTS).append("]").append(LINE_SEPARATOR);
        //define the format
        result.append("Format: Layer, Start, End, Style, Name, MarginL, MarginR, MarginV, Effect, Text")
                .append(LINE_SEPARATOR);
        //Next we iterate over the subtitles
        for (Sub sub : subFileObject.subs) {
            //for each caption
            result.append("Dialogue: 0,");
            //start time
            result.append(serializeTime(sub.startTime)).append(",");
            //end time
            result.append(serializeTime(sub.endTime)).append(",");
            //style
            if (sub.style != null) {
                result.append(sub.style);
            } else {
                result.append("Default");
            }
            //default margins are used, no name or effect is recognized
            result.append(",,0000,0000,0000,,");

            //we add the caption text with \N as line breaks  and clean of XML
            result.append(sub.content);
            //and we add the caption line
            result.append(LINE_SEPARATOR);
        }

        if (subFileObject.fontsText != null) {
            result.append("[").append(FONTS).append("]").append(LINE_SEPARATOR);
            result.append(subFileObject.fontsText).append(LINE_SEPARATOR);
            result.append(LINE_SEPARATOR);
        }
        if (subFileObject.graphicsText != null) {
            result.append("[").append(GRAPHICS).append("]").append(LINE_SEPARATOR);
            result.append(subFileObject.graphicsText).append(LINE_SEPARATOR);
            result.append(LINE_SEPARATOR);
        }

        return result.toString();
    }

    /**
     * This methods transforms a dialogue line from ASS according to a format definition into an Sub object.
     *
     * @param line           the dialogue line without its declaration
     * @param dialogueFormat the list of attributes in this dialogue line
     * @return a new Sub object
     */
    private Sub parseDialogue(String[] line, String[] dialogueFormat) {
        Sub sub = new Sub();

        for (int i = 0; i < dialogueFormat.length; i++) {
            String trimmedDialogueFormat = dialogueFormat[i].trim();
            //we go through every format parameter and save the interesting values
            if (trimmedDialogueFormat.equalsIgnoreCase("Style")) {
                //we save the style
                sub.style = line[i].trim();
            } else if (trimmedDialogueFormat.equalsIgnoreCase("Start")) {
                //we save the starting time
                sub.startTime = parseTime(line[i].trim());
            } else if (trimmedDialogueFormat.equalsIgnoreCase("End")) {
                //we save the starting time
                sub.endTime = parseTime(line[i].trim());
            } else if (trimmedDialogueFormat.equalsIgnoreCase("Text")) {
                //we save the text
                //text is cleaned before being inserted into the caption
                sub.content = line[i]/*.replaceAll("\\{.*?\\}", "")*/;
            }
        }
        return sub;
    }

    private int parseTime(String text) {
        // this type of format:  1:02:22.51
        int h, m, s, cs;
        String[] hms = text.split(":");
        h = Integer.parseInt(hms[0]);
        m = Integer.parseInt(hms[1]);
        s = Integer.parseInt(hms[2].substring(0, 2));
        cs = Integer.parseInt(hms[2].substring(3, 5));

        return (cs * 10 + s * 1000 + m * 60000 + h * 3600000);
    }

    public static String serializeTime(int milliseconds) {
        // this type of format:  1:02:22.51
        StringBuilder time = new StringBuilder();
        String aux;
        int h, m, s, cs;

        h = milliseconds / 3600000;
        time.append(h);
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
        time.append('.');
        cs = (milliseconds / 10) % 100;
        aux = String.valueOf(cs);
        if (aux.length() == 1) time.append('0');
        time.append(aux);

        return time.toString();
    }

}
