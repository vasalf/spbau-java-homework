package ru.spbau.alferov.javacw.md5;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The single-thread version of the {@link ChecksumCalculator}
 */
public class SingleThreadChecksumCalculator implements ChecksumCalculator {
    /**
     * {@link ChecksumCalculator#calculate(File)}
     */
    @Override
    public @NotNull byte[] calculate(File ofFile) throws IOException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // That never happens...
            throw new RuntimeException("...", e);
        }
        if (ofFile.isDirectory()) {
            digest.update(ofFile.getName().getBytes());
            for (File toFile : ofFile.listFiles()) {
                digest.update(calculate(toFile));
            }
            return digest.digest();
        } else {
            DigestInputStream dig = new DigestInputStream(new FileInputStream(ofFile), digest);
            dig.read();
            return dig.getMessageDigest().digest();
        }
    }
}
