package com.timursoft.suber;

import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class ParserSRTTest {

    private ParserSRT formatSRT;
    private String text;

    @Before
    public void init() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("srt.srt");
        text = IOHelper.stringFromIS(is).replace("\uFEFF", "");
        formatSRT = new ParserSRT();
    }

    @Test
    public void parse() throws Exception {
        SubFileObject subFileObject = formatSRT.parse(text);
        Iterator<Sub> iterator = subFileObject.getSubs().iterator();
        Sub sub = iterator.next();

        assertEquals(2, subFileObject.getSubs().size());
        assertEquals(27160, sub.startTime);
        assertEquals(30000, sub.endTime);
        assertEquals("<i>The Tesseract has awakened.</i>", sub.content);

        sub = iterator.next();

        assertEquals(31760, sub.startTime);
        assertEquals(36515, sub.endTime);
        assertEquals("It is on a little world, a human world.", sub.content);
    }

    @Test
    public void serialize() throws Exception {
        SubFileObject subFileObject = new SubFileObject();

        Sub sub = new Sub();
        sub.startTime = 27160;
        sub.endTime = 30000;
        sub.content = "<i>The Tesseract has awakened.</i>";
        subFileObject.addSub(sub);

        Sub sub2 = new Sub();
        sub2.startTime = 31760;
        sub2.endTime = 36515;
        sub2.content = "It is on a little world, a human world.";
        subFileObject.addSub(sub2);

        String result = formatSRT.serialize(subFileObject);
        assertEquals(text, result);
    }

    @Test
    public void twoWay() throws Exception {
        SubFileObject subFileObject = formatSRT.parse(text);
        String result = formatSRT.serialize(subFileObject);

        assertEquals(text, result);
    }

}