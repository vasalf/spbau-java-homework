package ru.spbau.alferov.javacw.injector;

/**
 * An exception to be thrown in case it is impossible to select between
 * two implementations of some interface.
 */
public class AmbigousImplementationException extends InjectionException {
    /**
     * Base constructor.
     *
     * @param which The ambigously implemented interface.
     */
    AmbigousImplementationException(Class which) {
        super("Multiple implementations of interface " + which.getName() + "found.");
    }
}
