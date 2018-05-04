package ru.spbau.alferov.javacw.md5;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.RecursiveTask;

/**
 * Fork-join version of the {@link ChecksumCalculator}
 */
public class ForkJoinChecksumCalculator implements ChecksumCalculator {
    /**
     * {@link ChecksumCalculator#calculate(File)}
     */
    @Override
    public @NotNull byte[] calculate(File ofFile) {
        return new CalculatorTask(ofFile).compute().getWrapped();
    }

    /**
     * The wrapper for the byte arrays.
     * Used for not wrapping each separate byte.
     */
    private static class ByteArrayWrapper {
        /**
         * The wrapped array
         */
        private @NotNull byte[] wrapped;

        /**
         * Base constructor
         */
        public ByteArrayWrapper(@NotNull byte[] wrapped) {
            this.wrapped = wrapped;
        }

        /**
         * Get the wrapped byte array.
         */
        public @NotNull byte[] getWrapped() {
            return wrapped;
        }
    }

    /**
     * The recursive fork-join task
     */
    private static class CalculatorTask extends RecursiveTask<ByteArrayWrapper> {
        /**
         * The file for which the checksum should be calculated
         */
        private File ofFile;

        /**
         * Base constructor
         */
        public CalculatorTask(File ofFile) {
            this.ofFile = ofFile;
        }

        /**
         * The calculating function, see {@link ForkJoinChecksumCalculator#calculate(File)} and {@link RecursiveTask#compute()}
         */
        @Override
        protected @NotNull ByteArrayWrapper compute() {
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
                    CalculatorTask task = new CalculatorTask(toFile);
                    task.fork();
                    digest.update(task.join().getWrapped());
                }
                return new ByteArrayWrapper(digest.digest());
            } else {
                try {
                    DigestInputStream dig = new DigestInputStream(new FileInputStream(ofFile), digest);
                    dig.read();
                    return new ByteArrayWrapper(dig.getMessageDigest().digest());
                } catch (Exception e) {
                    throw new RuntimeException("File or IO exception while calculating the checksum", e);
                }
            }
        }
    }
}
