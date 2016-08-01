package com.timursoft.suber;

import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class ParserASSTest {

    private ParserASS formatASS;
    private String text;

    @Before
    public void init() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("ass.ass");
        text = IOHelper.stringFromIS(is).replace("\uFEFF", "");
        formatASS = new ParserASS();
    }

    @Test
    public void parse() throws Exception {
        SubFileObject subFileObject = formatASS.parse(text);
        Sub sub = subFileObject.subs.get(0);

        assertEquals("info", subFileObject.scriptInfoText);
        assertEquals("styles", subFileObject.stylesText);
        assertEquals(2, subFileObject.subs.size());

        assertEquals(0, sub.startTime);
        assertEquals(5100, sub.endTime);
        assertEquals("{\\pos(320,13)}Test-group {\\b1\\c&H0BA1C6&}ImTr{\\c&H645937&\\b0} present", sub.content);
        assertEquals("credits (top)", sub.style);

        sub = subFileObject.subs.get(1);

        assertEquals(53560, sub.startTime);
        assertEquals(57970, sub.endTime);
        assertEquals("- Your Majesty! \\N- eunuch Pang nothing to blame", sub.content);
        assertEquals("Default", sub.style);
    }

    @Test
    public void serialize() throws Exception {
        SubFileObject subFileObject = new SubFileObject();
        subFileObject.scriptInfoText = "info";
        subFileObject.stylesText = "styles";

        Sub sub = new Sub();
        sub.startTime = 0;
        sub.endTime = 5100;
        sub.content = "{\\pos(320,13)}Test-group {\\b1\\c&H0BA1C6&}ImTr{\\c&H645937&\\b0} present";
        sub.style = "credits (top)";
        subFileObject.subs.add(sub);

        Sub sub2 = new Sub();
        sub2.startTime = 53560;
        sub2.endTime = 57970;
        sub2.content = "- Your Majesty! \\N- eunuch Pang nothing to blame";
        subFileObject.subs.add(sub2);

        String text = formatASS.serialize(subFileObject);
        assertEquals(text, text);
    }

    @Test
    public void twoWay() throws Exception {
        SubFileObject subFileObject = formatASS.parse(text);
        String serialized = formatASS.serialize(subFileObject);

        assertEquals(text, serialized);
    }

}