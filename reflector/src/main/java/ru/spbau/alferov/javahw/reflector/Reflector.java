package ru.spbau.alferov.javahw.reflector;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;

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

    /**
     * The visitor for DiffClasses function. Writes the methods in just the same
     * manner as described in diffClasses description.
     */
    private static class DiffClassesVisitor extends ClassVisitor.Visitor {
        @Override
        protected void appendClassModifiers(Class<?> forClass) { }

        @Override
        protected void appendClassKeyword() { }

        @Override
        protected void appendClassName(Class<?> forClass) { }

        @Override
        protected void appendConstructor(Constructor<?> forConstructor) {
            nextLine.append("Constructor");
        }

        @Override
        protected void appendParameterName(int index, StringBuilder sb) { }

        @Override
        protected void finishClassDeclaration() {
            super.finishClassDeclaration();
            if (nextLine.toString().length() != 0)
                flushLine();
        }

        @Override
        protected void finishConstructorDeclaration() {
            super.finishConstructorDeclaration();
            flushLine();
        }

        @Override
        protected void finishFieldDeclaration() {
            super.finishFieldDeclaration();
            flushLine();
        }

        @Override
        protected void finishMethodDeclaration() {
            super.finishMethodDeclaration();
            flushLine();
        }
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
        List<String> aStruct = new DiffClassesVisitor().visitClass(a, false);
        List<String> bStruct = new DiffClassesVisitor().visitClass(b, false);

        System.out.println("diffClasses:");
        System.out.println("first:");
        for (String s : aStruct) {
            System.out.println(s);
        }
        System.out.println("second:");
        for (String s : bStruct) {
            System.out.println(s);
        }

        Collections.sort(aStruct);
        Collections.sort(bStruct);

        boolean areDifferent = false;
        int i = 0, j = 0;
        while (i < aStruct.size() || j < bStruct.size()) {
            if (j == bStruct.size() || (i != aStruct.size() && aStruct.get(i).compareTo(bStruct.get(j)) < 0)) {
                // aStruct.get(i) is less
                if (!areDifferent) {
                    out.println("@@");
                }
                areDifferent = true;
                out.println("+ " + aStruct.get(i));
                i++;
            } else if (i == aStruct.size() || (j != bStruct.size() && aStruct.get(i).compareTo(bStruct.get(j)) > 0)) {
                // bStruct.get(j) is less
                if (!areDifferent) {
                    out.println("@@");
                }
                areDifferent = true;
                out.println("- " + bStruct.get(j));
                j++;
            } else {
                // aStruct.get(i) == bStruct.get(j)
                i++; j++;
            }
        }
        return !areDifferent;
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
