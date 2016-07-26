package com.timursoft.suber;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static com.timursoft.suber.Suber.suber;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SuberTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = mock(Parser.class);
        suber().registerParser("test", parser);
    }

    @Test
    public void parse() throws Exception {
        File file = File.createTempFile("suber", ".test");
        file.deleteOnExit();

        suber().parse(file);

        verify(parser).parse(anyString());
    }

    @Test
    public void serialize() throws Exception {
        File file = File.createTempFile("suber", ".test");
        file.deleteOnExit();
        SubFileObject subFileObject = new SubFileObject();

        suber().serialize(subFileObject, file);

        verify(parser).serialize(subFileObject);
    }

}