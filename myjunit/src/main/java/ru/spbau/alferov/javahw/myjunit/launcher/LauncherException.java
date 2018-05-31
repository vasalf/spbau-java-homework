package ru.spbau.alferov.javahw.myjunit.launcher;

public class LauncherException extends Exception {
    public LauncherException(String message) {
        super(message);
    }
    public LauncherException(String message, Throwable cause) {
        super(message, cause);
    }
}
