package ru.spbau.alferov.javahw.reflector;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class DiffClassesTest {
    /**
     * Returns the content written to ByteArrayOutputStream as String.
     */
    private static String getContent(ByteArrayOutputStream baos) {
        return new String(baos.toByteArray());
    }

    /**
     * Holds the result of class comparison.
     * Has toString() and equals() overrides so that assertEquals method would like it.
     */
    private static class DiffClassesOutput {
        /**
         * The string class is created from.
         */
        private String fromString;

        /**
         * The lines contained in fromString.
         */
        private String[] lines;

        /**
         * Constructs the object from ByteArrayOutputStream.
         */
        DiffClassesOutput(ByteArrayOutputStream baos) {
            fromString = getContent(baos);
            lines = fromString.split("\n");
        }

        /**
         * Constructs the object from String.
         */
        DiffClassesOutput(String s) {
            fromString = s;
            lines = s.split("\n");
        }

        /**
         * Get the string the DiffClassesOutput is created from.
         */
        @Override
        public String toString() {
            return fromString;
        }

        /**
         * Compares two outputs on equality.
         *
         * A smart one, compares that sets of lines are equal (no matter for order of lines).
         */
        @Override
        public boolean equals(Object other) {
            if (!(other instanceof DiffClassesOutput))
                return false;

            DiffClassesOutput dOther = (DiffClassesOutput)other;
            if (dOther.lines.length != lines.length)
                return false;

            String[] aLines = lines.clone();
            String[] bLines = lines.clone();
            Arrays.sort(aLines);
            Arrays.sort(bLines);

            return Arrays.equals(aLines, bLines);
        }
    }

    private static void testEquality(Class<?> a, Class<?> b) {
        ByteArrayOutputStream res = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(res);
        assertTrue(Reflector.diffClasses(a, b, ps));
        assertTrue(getContent(res).isEmpty());
    }

    private static void testInequality(Class<?> a, Class<?> b, String desiredDiff) {
        ByteArrayOutputStream res = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(res);
        assertFalse(Reflector.diffClasses(a, b, ps));
        assertEquals(new DiffClassesOutput(desiredDiff), new DiffClassesOutput(res));
    }

    /// Here are the classes for the tests

    private static class EmptyClass { }
    private static class AnotherEmptyClass { }

    private static class HasPrivateIntFieldA {
        private int a;
    }

    private static class HasPrivateIntFieldB {
        private int b;
    }

    private static class HasPrivateStaticIntFieldA {
        private static int a;
    }

    private static class HasPrivateCharFieldA {
        private char a;
    }

    private static class HasPackagePrivateIntFieldA {
        int a;
    }

    private static class HasPackagePrivateIntFieldB {
        int b;
    }

    private static class HasPublicIntFieldA {
        public int a;
    }

    private static class HasPublicIntFieldB {
        public int b;
    }

    private static class EmptyClassWithInnerClass {
        public class InnerClass { }
    }

    private static class HasPrivateRunnerFoo {
        private void foo() { }
    }

    private static class HasPrivateRunnerBar {
        private void bar() { }
    }

    private static class HasPackagePrivateRunnerFoo {
        private void foo() { }
    }

    private static class HasPackagePrivateRunnerBar {
        private void bar() { }
    }

    private static class HasPublicRunnerFoo {
        private void foo() { }
    }

    private static class  HasPublicRunnerBar {
        private void bar() { }
    }

    private static class HasPublicIntSupplierFoo {
        public int foo() { return 42; }
    }

    private static class HasPublicCharSupplierFoo {
        public char foo() { return 42; }
    }

    private static class HasPublicIntIntBiConsumerFoo {
        public void foo(int x, int y) { }
    }

    private static class HasPublicIntCharBiConsumerFoo {
        public void foo(int x, char y) { }
    }

    private static class HasPublicIntConsumerFoo {
        public void foo(int x) { }
    }

    private static class HasPublicStaticIntConsumerFoo {
        public static void foo(int x) { }
    }

    private static class OverloadingOrderA {
        public void foo(int x) { }
        public void foo(char x) { }
    }

    private static class OverloadingOrderB {
        public void foo(char x) { }
        public void foo(int x) { }
    }

    private static class OverloadingAndExtendingA {
        public void foo(int x, int y) { }
        public void foo(int x, char y) { }
    }

    private static class OverloadingAndExtendingB extends HasPublicIntIntBiConsumerFoo {
        public void foo(int x, char y) { }
    }

    private static class EmptyGenericTClass<T> { }

    private static class EmptyGenericTKClass<T,K> { }

    private static class HasPublicTFieldA<T> {
        public T a;
    }

    private static class HasPublicKFieldA<K> {
        public K a;
    }

    private static class HasPublicGenericTConsumerFoo {
        public <T> void foo(T x) { }
    }

    private static class HasPublicGenericIntConsumerFoo {
        public <T> void foo(int x) { }
    }

    private static class HasPublicGenericKConsumerFoo {
        public <K> void foo(K x) { }
    }

    private static class EmptyGenericComparableTClass<T extends Comparable<T>> { }

    private static class EmptyGenericObjectTClass<T extends Object> { }

    private static class EmptyGenericTKexTClass<T, K extends T> { }

    private static class EmptyGenericKTexKClass<K, T extends K> { }

    private static class GenericTKHasPublicSetTConsumer<T, K> {
        public void foo(Set<T> x) { }
    }

    private static class GenericTKHasPublicSetKConsumer<T, K> {
        public void foo(Set<K> x) { }
    }

    private static class HasMethodWithWildcardGenericParameter {
        public void foo(TreeSet<? extends Comparable> ts) { }
    }

    private static class HasMethodWithNamedGenericParameter {
        public <T extends Comparable> void foo(TreeSet<T> ts) { }
    }

    private static class HasMethodWithSuperWildcardGenericParameter {
        public void foo(Set<? super Set> s) { }
    }

    /// Lower are the tests

    /**
     * Tests that equal empty classes are compared correctly.
     */
    @Test
    public void equalEmptyClassesTest() {
        testEquality(EmptyClass.class, EmptyClass.class);
    }

    /**
     * Tests that empty class is equal to itself.
     */
    @Test
    public void emptyClassSelfTest() {
        testEquality(EmptyClass.class, AnotherEmptyClass.class);
    }

    /**
     * Tests that class with one private field is equal to itself.
     */
    @Test
    public void onePrivateFieldSelfTest() {
        testEquality(HasPrivateIntFieldA.class, HasPrivateIntFieldA.class);
    }

    /**
     * Tests that class with one package-private field is equal to itself.
     */
    @Test
    public void onePackagePrivateFieldSelfTest() {
        testEquality(HasPackagePrivateIntFieldA.class, HasPackagePrivateIntFieldA.class);
    }

    /**
     * Tests that class with one public field is equal to itself.
     */
    @Test
    public void onePublicFieldSelfTest() {
        testEquality(HasPublicIntFieldA.class, HasPublicIntFieldA.class);
    }

    /**
     * Tests that private fields with different names are not equal to each other.
     */
    @Test
    public void privateFieldWithDifferentNames() {
        final String desiredDiff = "@@\n" +
                "- private int a;\n" +
                "+ private int b;\n";
        testInequality(HasPrivateIntFieldA.class, HasPrivateIntFieldB.class, desiredDiff);
    }

    /**
     * Tests that public fields with different names are not equal to each other.
     */
    @Test
    public void packagePrivateFieldsWithDifferentNames() {
        final String desiredDiff = "@@\n" +
                "- int a;\n" +
                "+ int b;\n";
        testInequality(HasPackagePrivateIntFieldA.class, HasPackagePrivateIntFieldB.class, desiredDiff);
    }

    /**
     * Tests that public fields with different names are not equal to each other.
     */
    @Test
    public void publicFieldsWithDifferentNames() {
        final String desiredDiff = "@@\n" +
                "- public int a;\n" +
                "+ public int b;\n";
        testInequality(HasPublicIntFieldA.class, HasPublicIntFieldB.class, desiredDiff);
    }

    /**
     * Tests that private static fields are not equal to private non-static fields.
     */
    @Test
    public void privateStaticAndNonStaticFields() {
        final String desiredDiff = "@@\n" +
                "- private int a;\n" +
                "+ private static int a;\n";
        testInequality(HasPublicIntFieldA.class, HasPrivateStaticIntFieldA.class, desiredDiff);
    }

    /**
     * Tests that private fields with different types are not equal to each other.
     */
    @Test
    public void differentTypesPrivateFields() {
        final String desiredDiff = "@@\n" +
                "- private int a;\n" +
                "+ private char a;\n";
        testInequality(HasPrivateIntFieldA.class, HasPrivateCharFieldA.class, desiredDiff);
    }

    /**
     * Tests that public fields are not equal to package-private fields.
     */
    @Test
    public void publicAndPackagePrivateFields() {
        final String desiredDiff = "@@\n" +
                "- private int a;\n" +
                "+ int a;\n";
        testInequality(HasPrivateIntFieldA.class, HasPackagePrivateIntFieldA.class, desiredDiff);
    }

    /**
     * Tests that public fields are not equal to private fields.
     */
    @Test
    public void publicAndPrivateFields() {
        final String desiredDiff = "@@\n" +
                "- private int a;\n" +
                "+ public int a;\n";
        testInequality(HasPrivateIntFieldA.class, HasPublicIntFieldA.class, desiredDiff);
    }

    /**
     * Tests that package-private fields are not equal to private fields.
     */
    @Test
    public void packagePrivateAndPrivateFields() {
        final String desiredDiff = "@@\n" +
                "- int a;\n" +
                "+ private int a;\n";
        testInequality(HasPackagePrivateIntFieldA.class, HasPrivateIntFieldA.class, desiredDiff);
    }

    /**
     * Tests that inner class does not affect the diffClasses verdict.
     */
    @Test
    public void innerClassTest() {
        testEquality(EmptyClass.class, EmptyClassWithInnerClass.class);
    }

    /**
     * Tests that class with one private method is equal to itself.
     */
    @Test
    public void onePrivateMethodSelfTest() {
        testEquality(HasPrivateRunnerFoo.class, HasPrivateRunnerFoo.class);
    }

    /**
     * Tests that class with one package-private method is equal to itself.
     */
    @Test
    public void onePackagePrivateMethodSelfTest() {
        testEquality(HasPackagePrivateRunnerFoo.class, HasPackagePrivateRunnerFoo.class);
    }

    /**
     * Tests that class with one public method is equal to itself.
     */
    @Test
    public void onePublicMethodSelfTest() {
        testEquality(HasPublicRunnerFoo.class, HasPublicRunnerFoo.class);
    }

    /**
     * Tests that private methods with different names are not equal to each other.
     */
    @Test
    public void privateMethodsWithDifferentNames() {
        final String desiredDiff = "@@\n" +
                "- private void foo();\n" +
                "+ private void bar();\n";
        testInequality(HasPrivateRunnerFoo.class, HasPrivateRunnerBar.class, desiredDiff);
    }

    /**
     * Tests that package-private methods with different names are not equal to each other.
     */
    @Test
    public void packagePrivateMethodsWithDifferentNames() {
        final String desiredDiff = "@@\n" +
                "- void foo();\n" +
                "+ void bar();\n";
        testInequality(HasPackagePrivateRunnerFoo.class, HasPackagePrivateRunnerBar.class, desiredDiff);
    }

    /**
     * Tests that public methods with different names are not equal to each other.
     */
    @Test
    public void publicMethodsWithDifferentNames() {
        final String desiredDiff = "@@\n" +
                "- public void foo();\n" +
                "+ public void bar();\n";
        testInequality(HasPublicRunnerFoo.class, HasPublicRunnerBar.class, desiredDiff);
    }

    /**
     * Tests that public methods are not equal to package-private methods.
     */
    @Test
    public void publicAndPackagePrivateMethods() {
        final String desiredDiff = "@@\n" +
                "- public void foo();\n" +
                "+ void foo();\n";
        testInequality(HasPublicRunnerFoo.class, HasPackagePrivateRunnerFoo.class, desiredDiff);
    }

    /**
     * Tests that public methods are not equal to private methods.
     */
    @Test
    public void publicAndPrivateMethods() {
        final String desiredDiff = "@@\n" +
                "- public void foo();\n" +
                "+ private void foo();\n";
        testInequality(HasPublicRunnerFoo.class, HasPrivateRunnerFoo.class, desiredDiff);
    }

    /**
     * Tests that package-private methods are not equal to private methods.
     */
    @Test
    public void packagePrivateAndPrivateMethods() {
        final String desiredDiff = "@@\n" +
                "- void foo();\n" +
                "+ private void foo();\n";
        testInequality(HasPackagePrivateRunnerFoo.class, HasPrivateRunnerFoo.class, desiredDiff);
    }

    /**
     * Tests that public methods with different return types are not equal to each other.
     */
    @Test
    public void publicMethodsWithDifferentReturnTypes() {
        final String desiredDiff = "@@\n" +
                "- public int foo();\n" +
                "+ public char foo();\n";
        testInequality(HasPublicIntSupplierFoo.class, HasPublicCharSupplierFoo.class, desiredDiff);
    }

    /**
     * Tests that public methods with different numbers of arguments are not equal to each other.
     */
    @Test
    public void publicMethodsWithDifferentNumbersOfArguments() {
        final String desiredDiff = "@@\n" +
                "- public void foo(int, int);\n" +
                "+ public void foo(int);\n";
        testInequality(HasPublicIntIntBiConsumerFoo.class, HasPublicIntConsumerFoo.class, desiredDiff);
    }

    /**
     * Tests that public methods with different types of arguments are not equal to each other.
     */
    @Test
    public void publicMethodsWithDifferentTypesOfArguments() {
        final String desiredDiff = "@@\n" +
                "- public void foo(int, int);\n" +
                "+ public void foo(int, char);\n";
        testInequality(HasPublicIntIntBiConsumerFoo.class, HasPublicIntCharBiConsumerFoo.class, desiredDiff);
    }

    /**
     * Tests that public method is not equal to public static method.
     */
    @Test
    public void publicAndPublicStaticMethod() {
        final String desiredDiff = "@@\n" +
                "- public void foo(int);\n" +
                "+ public static void foo(int);\n";
        testInequality(HasPublicIntConsumerFoo.class, HasPublicStaticIntConsumerFoo.class, desiredDiff);
    }

    /**
     * Tests that order of different overloads does not affect the diffClasses verdict.
     */
    @Test
    public void overloadingOrder() {
        testEquality(OverloadingOrderA.class, OverloadingOrderB.class);
    }

    /**
     * Tests that extended and overloaded methods are equal to each other.
     */
    @Test
    public void overloadingAndExtending() {
        testEquality(OverloadingAndExtendingA.class, OverloadingAndExtendingB.class);
    }

    /**
     * Tests that empty class is not equal to empty generic class
     */
    @Test
    public void emptyAndEmptyGenericClasses() {
        final String desiredDiff = "@@\n" +
                "+ <T>\n";
        testInequality(EmptyClass.class, EmptyGenericTClass.class, desiredDiff);
    }

    /**
     * Tests that empty generic classes with different numbers of arguments are not equal to each other.
     */
    @Test
    public void emptyGenericClassesWithDifferentNumbersOfArguments() {
        final String desiredDiff = "@@\n" +
                "- <T>\n" +
                "+ <T,K>\n";
        testInequality(EmptyGenericTClass.class, EmptyGenericTKClass.class, desiredDiff);
    }

    /**
     * Tests that generic field type is not equal to non-generic field type.
     */
    @Test
    public void genericFieldTypeAndInt() {
        final String desiredDiff = "@@\n" +
                "- <T>\n" +
                "- public int a;" +
                "+ public T a;";
        testInequality(HasPublicTFieldA.class, HasPublicIntFieldA.class, desiredDiff);
    }

    /**
     * Tests that similar classes with different generic parameters are not equal to each other.
     */
    @Test
    public void differentGenericTypeNames() {
        final String desiredDiff = "@@\n" +
                "- <T>\n" +
                "- public T a;\n" +
                "+ <K>\n" +
                "+ public K a;\n";
        testInequality(HasPublicTFieldA.class, HasPublicKFieldA.class, desiredDiff);
    }

    /**
     * Tests that generic methods with different argument types (generic and non-generic) are not equal to each other.
     */
    @Test
    public void genericAndNonGenericArgumentTypes() {
        final String desiredDiff = "@@\n" +
                "- public <T> void foo(T);\n" +
                "+ public <T> void foo(int);\n";
        testInequality(HasPublicGenericTConsumerFoo.class, HasPublicGenericIntConsumerFoo.class, desiredDiff);
    }

    /**
     * Tests that similar generic methods with different generic parameters are not equal to each other.
     */
    @Test
    public void genericMethodsWithDifferentParameters() {
        final String desiredDiff = "@@\n" +
                "- public <T> void foo(T);\n" +
                "+ public <K> void foo(K);\n";
        testInequality(HasPublicGenericTConsumerFoo.class, HasPublicGenericKConsumerFoo.class, desiredDiff);
    }

    /**
     * Tests that generic extends do affect diffClasses verdict.
     */
    @Test
    public void genericExtends() {
        final String desiredDiff = "@@\n" +
                "- <T>\n" +
                "+ <T extends Comparable<T>>\n";
        testInequality(EmptyGenericTClass.class, EmptyGenericComparableTClass.class, desiredDiff);
    }

    /**
     * Tests that Object extendings does not affect diffClasses verdict.
     */
    @Test
    public void genericObjectExtendings() {
        testEquality(EmptyGenericTClass.class, EmptyGenericObjectTClass.class);
    }

    /**
     * Tests that similar generic extends with different names are not equal to each other.
     */
    @Test
    public void genericExtendsWithDifferentNames() {
        final String desiredDiff = "@@\n" +
                "- <T,K extends T>\n" +
                "+ <K,T extends K>\n";
        testInequality(EmptyGenericTKexTClass.class, EmptyGenericKTexKClass.class, desiredDiff);
    }

    /**
     * Tests that completely similar generic extends are equal to each other.
     */
    @Test
    public void similarGenericExtends() {
        testEquality(EmptyGenericKTexKClass.class, EmptyGenericKTexKClass.class);
    }

    /**
     * Tests that selection of generic parameters affect the diffClasses verdict.
     */
    @Test
    public void genericSelection() {
        final String desiredDiff = "@@\n" +
                "- public void foo(Set<T>);\n" +
                "+ public void foo(Set<K>);\n";
        testInequality(GenericTKHasPublicSetTConsumer.class, GenericTKHasPublicSetKConsumer.class, desiredDiff);
    }

    /**
     * Tests that class with method with wildcard generic parameter is equal to itself.
     */
    @Test
    public void oneMethodWithWildcardGenericParameterSelfTest() {
        testEquality(HasMethodWithWildcardGenericParameter.class, HasMethodWithWildcardGenericParameter.class);
    }

    /**
     * Tests that class with method with wildcard generic parameter is not equal to just generic.
     */
    @Test
    public void wildcardGenericMethodAndGenericMethod() {
        final String desiredDiff = "@@\n" +
                "- public void foo(TreeSet<? extends Comparable>)\n" +
                "+ public <T extends Comparable> void foo(TreeSet<T>)\n";
        testInequality(HasMethodWithWildcardGenericParameter.class, HasMethodWithNamedGenericParameter.class, desiredDiff);
    }

    /**
     * Tests that super wildcard parameter is OK.
     */
    @Test
    public void superWildcardParameterSelfTest() {
        testEquality(HasMethodWithSuperWildcardGenericParameter.class, HasMethodWithSuperWildcardGenericParameter.class);
    }

    /**
     * Tests that java.util.Map class is equal to itself.
     *
     * Just a big test :)
     */
    @Test
    public void javaUtilMapSelfTest() {
        testEquality(Map.class, Map.class);
    }
}