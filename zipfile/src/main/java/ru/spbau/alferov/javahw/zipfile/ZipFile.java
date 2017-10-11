package ru.spbau.alferov.javahw.zipfile;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The main class. See main method documentation for the details.
 */
public class ZipFile {

    /**
     * The main method. Takes the working directory and the regexp.
     * Finds all zip files in the working directory and unpacks all
     * files with names matching the regexp from them.
     *
     * Creates the whole hierarchy from zip files.
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Wrong number of arguments");
            System.exit(2);
        }

        ZipFile.extract(args[0], args[1]);
    }

    public static void extract(String path, String re) {
        File f = new File(path);
        if (!f.exists())
            return;
        ZipFile.findZipFiles(f, re);
    }

    private static void findZipFiles(File path, String re) {
        if (path.isDirectory()) {
            @NotNull File[] files = path.listFiles();
            for (File f : files) {
                ZipFile.findZipFiles(f, re);
            }
        } else {
            if (path.getName().endsWith(".zip")) {
                ZipFile.extractFromZipFile(path, re);
            }
        }
    }

    private static final int BUF_SIZE = 1024 * 1024;

    private static void extractFromZipFile(File path, String re) {
        try(ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(path))) {
            for (@Nullable ZipEntry ze = zipInputStream.getNextEntry(); ze != null; ze = zipInputStream.getNextEntry()) {
                File file = new File(ze.getName());
                if (!ze.isDirectory() && file.getName().matches(re)) {
                    System.out.println("inflating: " + ze.getName() + " from " + path);
                    File filePath = new File(path.getParent() + File.separator + ze.getName());
                    File dirPath = filePath.getParentFile();
                    if (!dirPath.exists() && !dirPath.mkdirs()) {
                        throw new IOException("Could not mkdir " + dirPath + ".");
                    }
                    try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
                        byte[] buf = new byte[BUF_SIZE];
                        for (int len = zipInputStream.read(buf); len > 0; len = zipInputStream.read(buf)) {
                            fileOutputStream.write(buf, 0, len);
                        }
                    } catch (IOException ioe) {
                        System.err.println("Could not open file " + ze.getName() + ": " + ioe.getMessage());
                    }
                }
            }
        } catch (ZipException ze) {
            System.err.println("Zip error occurred while processing " + path.getPath() + ": " + ze.getMessage());
        } catch (IOException ioe) {
            System.err.println("IO error occurred while processing " + path.getPath() + ": " + ioe.getMessage());
        }
    }
}
