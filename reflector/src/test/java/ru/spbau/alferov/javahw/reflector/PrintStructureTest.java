package ru.spbau.alferov.javahw.reflector;

import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Contains tests for Reflector.printStructure function.
 */
public class PrintStructureTest {
    /**
     * An utility class for testClass function.
     */
    private static class Tester {
        /**
         * This function compiles given class using shell command javac and checks that the process finished normally.
         */
        public static void compileClass(String filename) throws Exception {
            //System.out.println("javac " + filename);
            Process p = Runtime.getRuntime().exec("javac " + filename);
            p.waitFor();
            assertFalse(p.isAlive());
            assertEquals(0, p.exitValue());
        }

        /**
         * This function loads class expecting it is already compiled in the tests directory.
         */
        public static Class<?> loadClass(String classname) throws Exception {
            ClassLoader cl = new URLClassLoader(new URL[]{ new File(".").toURI().toURL() });
            return cl.loadClass(classname);
        }

        /**
         * This function finds a declared inner/nested class which matches the given inner/nested class from the
         * desired class.
         *
         * @param where A class where to find inner/nested classes.
         * @param which A class to be matched.
         * @return The class or Nothing.
         */
        public static Optional<Class<?>> findInnerClass(Class<?> where, Class<?> which) {
            return Arrays.stream(where.getDeclaredClasses())
                    .filter(c -> c.getSimpleName().equals(which.getSimpleName()))
                    .findFirst();
        }

        /**
         * Tests diffClasses on class and all its inner and nested subclasses.
         *
         * @param a A class where to find subclasses.
         * @param b A class where to fail if a subclass is not found.
         */
        public static void recursivelyTestClassMatchings(Class<?> a, Class<?> b) throws Exception {
            assertEquals(a.getSimpleName(), b.getSimpleName());
            assertTrue(Reflector.diffClasses(a, b));
            for (Class<?> innerOrNested : a.getDeclaredClasses()) {
                Optional<Class<?>> matching = findInnerClass(b, innerOrNested);
                assertTrue(matching.isPresent());
                recursivelyTestClassMatchings(innerOrNested, matching.orElseThrow(ClassNotFoundException::new));
            }
        }
    }

    /**
     * This function tests printStructure function on a given class.
     *
     * In details, it calls printStructure, then compiles the result file and then calls diffClasses on the result.
     * As long as diffClasses does not check the inner classes, this function also observes inner classes of given class
     * to have them checked on equality.
     *
     * @param toTest A class to be tested.
     */
    private void testClass(Class<?> toTest) throws Exception {
        Reflector.printStructure(toTest);
        Tester.compileClass(toTest.getSimpleName() + ".java");
        Class<?> compiledClass = Tester.loadClass(toTest.getSimpleName());
        Tester.recursivelyTestClassMatchings(toTest, compiledClass);
        Tester.recursivelyTestClassMatchings(compiledClass, toTest);
    }

    /// Here are the classes for the tests

    private static class EmptyClass { }

    private static class HasPrivateIntField {
        private int a;
    }

    private static class HasPackagePrivateIntField {
        int a;
    }

    private static class HasPublicIntField {
        public int a;
    }

    private static class HasPublicStaticIntField {
        public static int a;
    }

    private static class HasPrivateRunner {
        private void foo() { }
    }

    private static class HasPackagePrivateRunner {
        void foo() { }
    }

    private static class HasPublicRunner {
        public void foo() { }
    }

    private static class HasPublicStaticRunner {
        public static void foo() { }
    }

    private static class HasPrivateIntConsumer {
        private void foo(int x) { }
    }

    private static class HasPrivateIntSupplier {
        private int foo() { return 42; }
    }

    private static class HasPrivateIntIntBiConsumer {
        private void foo(int x, int y) { }
    }

    private static class HasPrivateStaticIntIntToCharFunction {
        private static char foo(int x, int y) { return 'a'; }
    }

    private static class EmptyGenericTClass<T> { }

    private static class EmptyGenericTKClass<T, K> { }

    private static class HasPublicTConsumer<T> {
        public void foo(T x) { }
    }

    private static class HasPublicTSupplier<T> {
        public T foo() { return null; }
    }

    private static class HasPublicTField<T> {
        public T x;
    }

    private static class HasPublicSetTConsumer<T> {
        public void foo(Set<T> value) { }
    }

    private static class HasPublicSetTSupplier<T> {
        public Set<T> foo() { return null; }
    }

    private static class HasPublicGenericTConsumer {
        public <T> void foo(T x) { }
    }

    private static class HasPublicGenericTSupplier {
        public <T> T foo() { return null; }
    }

    private static class HasPublicGenericSetTSupplier {
        public <T> Set<T> foo() { return null; }
    }

    private static class HasPublicGenericMapTKSupplier {
        public <T,K> Map<T,K> foo() { return null; }
    }

    private static class EmptyGenericTKextTClass<T, K extends T> { }

    private static class EmptyGenericTComparableTClass<T extends Comparable<T>> { }

    private static class FiveNestedGenericClasses {
        private static class First<A> {
            A a;
            private static class Second<A> {
                A a;
                private static class Third<A> {
                    A a;
                    private static class Fourth<A> {
                        A a;
                        private static class Fifth<A> {
                            A a;
                        }
                    }
                }
            }
        }
    }

    /// Lower are the tests

    /**
     * Tests the printStructure method on an empty class.
     */
    @Test
    public void emptyClass() throws Exception {
        testClass(EmptyClass.class);
    }

    /**
     * Tests a class with one private int field
     */
    @Test
    public void onePrivateIntField() throws Exception {
        testClass(HasPrivateIntField.class);
    }

    /**
     * Tests a class with one package-private int field
     */
    @Test
    public void onePackagePrivateIntField() throws Exception {
        testClass(HasPackagePrivateIntField.class);
    }

    /**
     * Tests a class with one public int field
     */
    @Test
    public void onePublicIntField() throws Exception {
        testClass(HasPublicIntField.class);
    }

    /**
     * Tests a class with one public static int field
     */
    @Test
    public void onePublicStaticIntField() throws Exception {
        testClass(HasPublicStaticIntField.class);
    }

    /**
     * Tests a class with one private runner
     */
    @Test
    public void onePrivateRunner() throws Exception {
        testClass(HasPrivateRunner.class);
    }

    /**
     * Tests a class with one package-private runner
     */
    @Test
    public void onePackagePrivateRunner() throws Exception {
        testClass(HasPackagePrivateRunner.class);
    }

    /**
     * Tests a class with one public runner
     */
    @Test
    public void onePublicRunner() throws Exception {
        testClass(HasPublicRunner.class);
    }

    /**
     * Tests a class with one public static runner
     */
    @Test
    public void onePublicStaticRunner() throws Exception {
        testClass(HasPublicStaticRunner.class);
    }

    /**
     * Tests a class with one private int consumer
     */
    @Test
    public void onePrivateIntConsumer() throws Exception {
        testClass(HasPrivateIntConsumer.class);
    }

    /**
     * Tests a class with one private int supplier
     */
    @Test
    public void onePrivateIntSupplier() throws Exception {
        testClass(HasPrivateIntSupplier.class);
    }

    /**
     * Tests a class with one `private void foo(int, int)` function
     */
    @Test
    public void onePrivateIntIntBiConsumer() throws Exception {
        testClass(HasPrivateIntIntBiConsumer.class);
    }

    /**
     * Tests a class with one `private static char foo(int, int)` function
     */
    @Test
    public void onePrivateStaticIntIntToCharFunction() throws Exception {
        testClass(HasPrivateStaticIntIntToCharFunction.class);
    }

    /**
     * Tests an empty generic class
     */
    @Test
    public void emptyGenericTClass() throws Exception {
        testClass(EmptyGenericTClass.class);
    }

    /**
     * Tests an empty generic class with two arguments
     */
    @Test
    public void emptyGenericTKClass() throws Exception {
        testClass(EmptyGenericTKClass.class);
    }

    /**
     * Tests a generic class with one `public void foo(T);` function.
     */
    @Test
    public void onePublicTConsumer() throws Exception {
        testClass(HasPublicTConsumer.class);
    }

    /**
     * Tests a generic class with one `public T foo();` function.
     */
    @Test
    public void onePublicTSupplier() throws Exception {
        testClass(HasPublicTSupplier.class);
    }

    /**
     * Tests a generic class with one public T field.
     */
    @Test
    public void onePublicTField() throws Exception {
        testClass(HasPublicTField.class);
    }

    /**
     * Tests a generic class with one `Set&lt;T&gt;` consumer.
     */
    @Test
    public void onePublicSetTConsumer() throws Exception {
        testClass(HasPublicSetTConsumer.class);
    }

    /**
     * Tests a generic class with one `Set&lt;T&gt;` supplier.
     */
    @Test
    public void onePublicSetTSupplier() throws Exception {
        testClass(HasPublicSetTSupplier.class);
    }

    /**
     * Tests a class with one public generic T consumer.
     */
    @Test
    public void onePublicGenericTConsumer() throws Exception {
        testClass(HasPublicGenericTConsumer.class);
    }

    /**
     * Tests a class with one public generic T supplier.
     */
    @Test
    public void onePublicGenericTSupplier() throws Exception {
        testClass(HasPublicGenericTSupplier.class);
    }

    /**
     * Tests a class with one public generic Set&lt;T&gt; supplier.
     */
    @Test
    public void onePublicGenericSetTSupplier() throws Exception {
        testClass(HasPublicGenericSetTSupplier.class);
    }

    /**
     * Tests a class with one public generic Map&lt;T,K&gt; supplier.
     */
    @Test
    public void onePublicGenericMapTKConsumer() throws Exception {
        testClass(HasPublicGenericMapTKSupplier.class);
    }

    /**
     * Tests a class with generic parameter extends.
     */
    @Test
    public void emptyGenericTKextT() throws Exception {
        testClass(EmptyGenericTKextTClass.class);
    }

    /**
     * Tests a class with generic parameter extending Comparable.
     */
    @Test
    public void emptyGenericTComparable() throws Exception {
        testClass(EmptyGenericTComparableTClass.class);
    }

    /**
     * Tests a chain of nested generic classes.
     */
    @Test
    public void fiveNestedGenericClasses() throws Exception {
        testClass(FiveNestedGenericClasses.class);
    }
}