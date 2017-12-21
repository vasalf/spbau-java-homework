package ru.spbau.alferov.javahw.reflector;

import java.io.PrintStream;

/**
 * Class for some reflection-based operations.
 * Contains some static methods which provides some functionality.
 * See the methods documentation for description.
 */
public class Reflector {
    private static class StructurePrinter {

    }

    /**
     * <p>Creates new java-file with given class and empty implementation.</p>
     *
     * <p>Denote {@literal someClass.name = A}. Then the file A.java is created in the
     * working directory. The file contains class and all its inner and nested
     * with all private and public methods, inner and nested classes, etc.
     * All generics are preserved. The realization is empty.</p>
     *
     * <p>Interfaces and enums are not supported.</p>
     */
    public static void printStructure(Class<?> someClass) {
        throw new UnsupportedOperationException("Unimplemented");
    }

    private static class ClassDifferentiator {

    }

    /**
     * <p>Writes difference of two classes to the stream out.</p>
     *
     * <p>If two classes differ, it writes some information on stream out.</p>
     *
     * <p>It writes all of the fields and methods that differs in classes a and b.
     * It does not compare inner and nested classes. Methods are compared by
     * name, return type, parameters and generics. Fields are compared by type
     * and name.</p>
     *
     *
     * <p>First, it writes two at signs ({@literal "@@"}).</p>
     *
     * <p>Then it writes some lines starting with {@literal "- "} with fields and methods that are
     * present in class a and absent in class b. Then it writes some lines starting
     * with {@literal "+ "} with fields and methods that are present in class b and absent
     * in class a.</p>
     *
     * @param out The stream for the information
     * @return true if classes are similar.
     */
    public static boolean diffClasses(Class<?> a, Class<?> b, PrintStream out) {
        throw new UnsupportedOperationException("Unimplemented");
    }

    /**
     * <p>Same as {@link #diffClasses(Class, Class, PrintStream) diffClasses(Class, Class, PrintStream)}
     * but writes all of the information to stdout.</p>
     *
     * <p>In fact, this overload is the main and the previous is created for testing
     * purposes.</p>
     */
    public static boolean diffClasses(Class<?> a, Class<?> b) {
        return diffClasses(a, b, System.out);
    }
}
