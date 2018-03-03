package ru.spbau.alferov.javacw.injector;

/**
 * An exception to be thrown in case there is a dependency cycle.
 *
 */
public class InjectionCycleException extends InjectionException {
    /**
     * Base constructor
     *
     * @param ofWhich Some class from the cycle.
     */
    public InjectionCycleException(Class ofWhich) {
        super("Injection cycle detected with class " + ofWhich.getName());
    }
}
