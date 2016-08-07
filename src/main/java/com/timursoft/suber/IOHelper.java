package com.timursoft.suber;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public final class IOHelper {

    private IOHelper() {
    }

    public static String stringFromIS(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static String stringFromFile(File file) throws FileNotFoundException {
        Scanner s = new Scanner(file).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static void writeStringToFile(String text, File file) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(file);
        printWriter.println(text);
        printWriter.close();
    }

}
