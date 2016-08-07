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
        return stringFromScanner(new Scanner(is));
    }

    public static String stringFromFile(File file) throws FileNotFoundException {
        return stringFromScanner(new Scanner(file));
    }

    private static String stringFromScanner(Scanner scanner) {
        try {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    public static void writeStringToFile(String text, File file) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(file);
        printWriter.println(text);
        printWriter.close();
    }

}
