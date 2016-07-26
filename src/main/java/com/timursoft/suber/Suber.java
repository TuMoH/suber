package com.timursoft.suber;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The entry point of Suber is this class.
 * The typical way to get an instance of this class is to use a static import:
 * import static com.timursoft.suber.Suber.suber;
 */
public final class Suber {

    private static volatile Suber INSTANCE;

    private Map<String, Parser> parsers = new HashMap<>();

    public static Suber suber() {
        if (INSTANCE == null) {
            synchronized (Suber.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Suber();
                }
            }
        }
        return INSTANCE;
    }

    private Suber() {
        registerParser("srt", new ParserSRT());
        registerParser("ass", new ParserASS());
        registerParser("ssa", new ParserASS());
    }

    public void registerParser(String extension, Parser parser) {
        parsers.put(extension, parser);
    }

    public SubFileObject parse(File file) throws SuberParseException, FileNotFoundException {
        Parser parser = getParser(file);
        String text = IOHelper.stringFromFile(file);
        return parser.parse(text);
    }

    public void serialize(SubFileObject subFileObject, File file) throws SuberParseException, FileNotFoundException {
        Parser parser = getParser(file);
        String text = parser.serialize(subFileObject);
        IOHelper.writeStringToFile(text, file);
    }

    private Parser getParser(File file) throws SuberParseException {
        String name = file.getName();
        int dotLastIndex = name.lastIndexOf('.');
        if (dotLastIndex < 0) {
            throw new SuberParseException("Extension not found");
        }
        String extension = name.substring(dotLastIndex + 1);
        if (!parsers.containsKey(extension)) {
            throw new SuberParseException("Parser for extension: [" + extension + "] not found");
        }
        return parsers.get(extension);
    }

}
