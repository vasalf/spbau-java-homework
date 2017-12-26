package ru.spbau.alferov.javahw.zipfile;

import org.junit.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.Assert.*;

public class ZipFileTest {
    private static String readString(File f) throws FileNotFoundException {
        InputStream is = new FileInputStream(f);
        Scanner s = new Scanner(is);
        return s.next();
    }

    @Test
    public void zipTest() throws Exception {
        ZipFile.extract("./build/classes/test/", "a.*");

        File a = new File("./build/classes/test/sample_test/a");
        assertTrue(a.exists());

        File ab = new File("./build/classes/test/sample_test/ab");
        assertTrue(ab.exists());

        File b = new File("./build/classes/test/sample_test/b");
        assertFalse(b.exists());

        assertEquals("b", readString(a));
        assertEquals("ba", readString(ab));
    }
}