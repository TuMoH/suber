package com.timursoft.suber;

import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

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
        Sub sub = subFileObject.subs.get(0);

        assertEquals(3, subFileObject.subs.size());
        assertEquals(27160, sub.startTime);
        assertEquals(30000, sub.endTime);
        assertEquals("<i>The Tesseract has awakened.</i>", sub.content);

        sub = subFileObject.subs.get(1);

        assertEquals(31760, sub.startTime);
        assertEquals(36515, sub.endTime);
        assertEquals("It is on a little world, a human world.", sub.content);

        sub = subFileObject.subs.get(2);

        assertEquals(33333, sub.startTime);
        assertEquals(44444, sub.endTime);
        assertEquals("When you first came back,\nyou told me you loved me.", sub.content);
    }

    @Test
    public void serialize() throws Exception {
        SubFileObject subFileObject = new SubFileObject();

        Sub sub = new Sub();
        sub.startTime = 27160;
        sub.endTime = 30000;
        sub.content = "<i>The Tesseract has awakened.</i>";
        subFileObject.subs.add(sub);

        Sub sub2 = new Sub();
        sub2.startTime = 31760;
        sub2.endTime = 36515;
        sub2.content = "It is on a little world, a human world.";
        subFileObject.subs.add(sub2);

        Sub sub3 = new Sub();
        sub3.startTime = 33333;
        sub3.endTime = 44444;
        sub3.content = "When you first came back,\nyou told me you loved me.";
        subFileObject.subs.add(sub3);

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