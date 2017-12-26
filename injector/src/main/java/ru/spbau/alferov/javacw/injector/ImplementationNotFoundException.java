package ru.spbau.alferov.javacw.injector;

/**
 * An exception to be thrown in case there are no implementations of some
 * interface.
 */
public class ImplementationNotFoundException extends InjectionException {
    /**
     * Base constructor.
     *
     * @param type The unimplemented interface.
     */
    ImplementationNotFoundException(Class type) {
        super("No implementations found for interface " + type.getName());
    }
}
