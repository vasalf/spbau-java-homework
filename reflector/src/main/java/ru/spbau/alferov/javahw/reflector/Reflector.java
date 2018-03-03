package ru.spbau.alferov.javahw.reflector;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Reflector {
    /**
     * This is the visitor for printStructure function. It prints a class into a compilable java file.
     *
     * Inner classes are not supported as long as there is no way to get the difference between the link to the parent
     * and needed stuff.
     */
    private static class StructurePrinter extends ClassVisitor.Visitor {
        private enum recursiveStructs {
            CLASS, FIELD, CONSTRUCTOR, METHOD
        }

        /**
         * Here are the current structures saved.
         */
        Stack<recursiveStructs> currentStack = new Stack<>();

        /**
         * Here is the current indentation level stored.
         */
        int indentation = 0;

        /**
         * Flushes line and fills a new one with indentation.
         */
        @Override
        protected void flushLine() {
            super.flushLine();
            for (int i = 0; i < indentation; i++) {
                nextLine.append("    ");
            }
        }

        /**
         * Pop out the last structure and do something, if needed.
         */
        @Override
        public void goOut() {
            super.goOut();
            if (currentStack.pop() == recursiveStructs.CLASS) {
                indentation--;
                nextLine.delete(nextLine.length() - 4, nextLine.length());
                nextLine.append("}");
                flushLine();
            }
        }

        /**
         * Do not append the upper-level class modifier.
         */
        @Override
        protected void appendClassModifiers(Class<?> forClass) {
            if (indentation != 0)
                super.appendClassModifiers(forClass);
        }

        /**
         * Append the default value for fields to make them compilable.
         */
        @Override
        protected void appendFieldValue(Field toField) {
            super.appendFieldValue(toField);
            nextLine.append(" = ");
            if (toField.getType().isPrimitive()) {
                nextLine.append("0");
            } else if (toField.getType().isArray()) {
                nextLine.append("{}");
            } else {
                nextLine.append("null");
            }
            nextLine.append(";");
        }

        /**
         * Append some realization to the constructor to make it compilable.
         */
        @Override
        protected void appendConstructorRealization(Constructor<?> toConstructor) {
            super.appendConstructorRealization(toConstructor);
            nextLine.append(" {}");
        }

        /**
         * Append some realization to the metod to make it compilable.
         */
        @Override
        protected void appendMethodRealization(Method toMethod) {
            super.appendMethodRealization(toMethod);
            nextLine.append(" {");
            indentation++;
            flushLine();
            if (!toMethod.getReturnType().equals(void.class)) {
                if (toMethod.getReturnType().isPrimitive()) {
                    nextLine.append("return 0;");
                } else if (toMethod.getReturnType().isArray()) {
                    nextLine.append("return {};");
                } else {
                    nextLine.append("return null;");
                }
            }
            indentation--;
            flushLine();
            nextLine.append("}");
        }

        /**
         * Start the new indentation level.
         */
        @Override
        protected void finishClassDeclaration() {
            super.finishClassDeclaration();
            currentStack.push(recursiveStructs.CLASS);
            indentation++;

            nextLine.append(" {");
            flushLine();
        }

        /**
         * Affect the stack and flush the line.
         */
        @Override
        protected void finishFieldDeclaration() {
            super.finishFieldDeclaration();
            currentStack.push(recursiveStructs.FIELD);
            flushLine();
        }

        /**
         * Affect the stack and flush the line.
         */
        @Override
        protected void finishConstructorDeclaration() {
            super.finishConstructorDeclaration();
            currentStack.push(recursiveStructs.CONSTRUCTOR);
            flushLine();
        }

        /**
         * Affect the stack and flush the line.
         */
        @Override
        protected void finishMethodDeclaration() {
            super.finishMethodDeclaration();
            currentStack.push(recursiveStructs.METHOD);
            flushLine();
        }
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
    public static void printStructure(Class<?> someClass) throws IOException {
        List<String> structure = new StructurePrinter().visitClass(someClass, true);

        FileOutputStream outputStream = new FileOutputStream(new File(someClass.getSimpleName() + ".java"));
        PrintWriter pw = new PrintWriter(outputStream);

        for (String s : structure) {
            pw.println(s);
        }

        pw.close();
        outputStream.close();
    }

    /**
     * The visitor for DiffClasses function. Writes the methods in just the same
     * manner as described in diffClasses description.
     */
    private static class DiffClassesVisitor extends ClassVisitor.Visitor {
        /**
         * We do <b>not</b> compare class modifiers.
         */
        @Override
        protected void appendClassModifiers(Class<?> forClass) { }

        /**
         * What for, really?
         */
        @Override
        protected void appendClassKeyword() { }

        /**
         * Of course, it would be strange to compare class names.
         */
        @Override
        protected void appendClassName(Class<?> forClass) { }

        /**
         * Constructor names also have to be hidden.
         */
        @Override
        protected void appendConstructor(Constructor<?> forConstructor) {
            nextLine.append("Constructor");
        }

        /**
         * What for, really?
         */
        @Override
        protected void appendParameterName(int index, StringBuilder sb) { }

        @Override
        protected void finishClassDeclaration() {
            super.finishClassDeclaration();
            // We write something on new line only in case of a generic class.
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
