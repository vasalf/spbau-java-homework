package ru.spbau.alferov.javahw.myjunit.launcher;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;
import ru.spbau.alferov.javahw.myjunit.launcher.event.FailedTestEvent;
import ru.spbau.alferov.javahw.myjunit.launcher.event.IgnoredTestEvent;
import ru.spbau.alferov.javahw.myjunit.launcher.event.InvocationResult;
import ru.spbau.alferov.javahw.myjunit.launcher.event.PassedTestEvent;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests MyJUnit core
 */
public class LauncherTest {
    // Here come some helper structures

    /**
     * Represents the result of some test
     */
    private enum TestResult {
        IGNORED,
        FAILED,
        PASSED
    }

    /**
     * Transforms an InvocationResult to map
     */
    private Map<String, TestResult> invocationResultToMap(InvocationResult result) {
        Map<String, TestResult> ret = new HashMap<>();
        for (IgnoredTestEvent event : result.getIgnoredTestEvents()) {
            ret.put(event.getTestName(), TestResult.IGNORED);
        }
        for (FailedTestEvent event : result.getFailedTestEvents()) {
            ret.put(event.getTestName(), TestResult.FAILED);
        }
        for (PassedTestEvent event : result.getPassedTestEvents()) {
            ret.put(event.getTestName(), TestResult.PASSED);
        }
        return ret;
    }

    /**
     * Asserts that the test result is equal to some expected map
     */
    private void assertTestResultEquals(Map<String, TestResult> expected, InvocationResult actual) {
        assertEquals(expected, invocationResultToMap(actual));
    }

    // Here come the tests

    public static class NoTestsClass {}

    /**
     * Tests the Launcher on empty class
     */
    @Test
    public void testWithNoTests() throws Exception {
        InvocationResult result = Launcher.launchTests(NoTestsClass.class);
        assertTestResultEquals(ImmutableMap.<String, TestResult>builder().build(), result);
    }

    public static class ClassWithPassingAndIgnoredFailingTest {
        @ru.spbau.alferov.javahw.myjunit.test.Test
        public void passingTest() {
            // procrastinate
        }

        @ru.spbau.alferov.javahw.myjunit.test.Test(ignored = "Because it fails")
        public void failingTest() throws Exception {
            throw new Exception();
        }
    }

    /**
     * Tests passing and ignored tests
     */
    @Test
    public void testPassingAndIgnoredFailingTest() throws Exception {
        InvocationResult result = Launcher.launchTests(ClassWithPassingAndIgnoredFailingTest.class);
        assertTestResultEquals(ImmutableMap.<String, TestResult>builder()
                .put("passingTest", TestResult.PASSED)
                .put("failingTest", TestResult.IGNORED)
                .build(), result);
    }

    public static class ClassWithTwoFailingTests {
        @ru.spbau.alferov.javahw.myjunit.test.Test
        public void firstFailingTest() throws Exception {
            throw new Exception("Fail in first fashion");
        }

        @ru.spbau.alferov.javahw.myjunit.test.Test
        public void secondFailingTest() throws Exception {
            throw new Exception("Fail in second fashion");
        }
    }

    /**
     * Tests Launcher on two failing tests
     */
    @Test
    public void testTwoFailingTest() throws Exception {
        InvocationResult result = Launcher.launchTests(ClassWithTwoFailingTests.class);
        assertTestResultEquals(ImmutableMap.<String, TestResult>builder()
                .put("firstFailingTest", TestResult.FAILED)
                .put("secondFailingTest", TestResult.FAILED)
                .build(), result);
    }

    public static class ClassWithExpectedFailingTests {
        private static class FirstInternalException extends Exception {}
        private static class SecondInternalException extends Exception {}

        @ru.spbau.alferov.javahw.myjunit.test.Test(expected = Exception.class)
        public void throwExceptionExpectException() throws Exception {
            throw new Exception();
        }

        @ru.spbau.alferov.javahw.myjunit.test.Test(expected = FirstInternalException.class)
        public void throwExceptionExpectInternalException() throws Exception {
            throw new Exception();
        }

        @ru.spbau.alferov.javahw.myjunit.test.Test(expected = Exception.class)
        public void throwInternalExceptionExpectException() throws Exception {
            throw new FirstInternalException();
        }

        @ru.spbau.alferov.javahw.myjunit.test.Test(expected = FirstInternalException.class)
        public void throwInternalExceptionExpectAnotherInternalException() throws Exception {
            throw new SecondInternalException();
        }
    }

    /**
     * Tests all cases of expected-thrown exception (non-)equivalence
     */
    @Test
    public void testExpectedFailingTests() throws Exception {
        InvocationResult result = Launcher.launchTests(ClassWithExpectedFailingTests.class);
        assertTestResultEquals(ImmutableMap.<String, TestResult>builder()
                .put("throwExceptionExpectException", TestResult.PASSED)
                .put("throwExceptionExpectInternalException", TestResult.FAILED)
                .put("throwInternalExceptionExpectException", TestResult.PASSED)
                .put("throwInternalExceptionExpectAnotherInternalException", TestResult.FAILED)
                .build(), result);
    }

    public static class BeforeTester {
        private int anIntToSet = 0;

        @ru.spbau.alferov.javahw.myjunit.test.Before
        public void runBeforeEachTest() {
            anIntToSet = 179;
        }

        @ru.spbau.alferov.javahw.myjunit.test.Test
        public void firstTest() throws Exception {
            if (anIntToSet != 179) {
                throw new Exception();
            }
            anIntToSet = 0;
        }

        @ru.spbau.alferov.javahw.myjunit.test.Test
        public void secondTest() throws Exception {
            if (anIntToSet != 179) {
                throw new Exception();
            }
            anIntToSet = 0;
        }
    }

    /**
     * Tests whether @Before methods are run once before each test
     */
    @Test
    public void testBefore() throws Exception {
        InvocationResult result = Launcher.launchTests(BeforeTester.class);
        assertTestResultEquals(ImmutableMap.<String, TestResult>builder()
                .put("firstTest", TestResult.PASSED)
                .put("secondTest", TestResult.PASSED)
                .build(), result);
    }

    public static class AfterTester {
        private int anIntToSet = 0;

        @ru.spbau.alferov.javahw.myjunit.test.Before
        public void runBeforeEachTest() {
            anIntToSet = 179;
        }

        @ru.spbau.alferov.javahw.myjunit.test.After
        public void runAfterEachTest() {
            anIntToSet = 0;
        }

        @ru.spbau.alferov.javahw.myjunit.test.Test
        public void firstTest() throws Exception {
            if (anIntToSet != 179) {
                throw new Exception();
            }
        }

        @ru.spbau.alferov.javahw.myjunit.test.Test
        public void secondTest() throws Exception {
            if (anIntToSet != 179) {
                throw new Exception();
            }
        }
    }

    /**
     * Tests whether @After methods are run once after each test
     */
    @Test
    public void testAfter() throws Exception {
        InvocationResult result = Launcher.launchTests(AfterTester.class);
        assertTestResultEquals(ImmutableMap.<String, TestResult>builder()
                .put("firstTest", TestResult.PASSED)
                .put("secondTest", TestResult.PASSED)
                .build(), result);
    }

    public static class BeforeClassTester {
        private int anIntToSet = 0;
        private int beforeClassCallsCounter = 0;

        @ru.spbau.alferov.javahw.myjunit.test.Test
        public void firstTest() throws Exception {
            if (anIntToSet != 179) {
                throw new Exception();
            }
        }

        @ru.spbau.alferov.javahw.myjunit.test.Test
        public void secondTest() throws Exception {
            if (anIntToSet != 179) {
                throw new Exception();
            }
        }

        @ru.spbau.alferov.javahw.myjunit.test.BeforeClass
        public void runBeforeAllTests() throws Exception {
            anIntToSet = 179;
            beforeClassCallsCounter++;
            if (beforeClassCallsCounter > 1) {
                throw new Exception("I should not be called twice");
            }
        }
    }

    /**
     * Tests that @BeforeAll methods are run once before all of the tests
     */
    @Test
    public void testBeforeAll() throws Exception {
        InvocationResult result = Launcher.launchTests(BeforeClassTester.class);
        assertTestResultEquals(ImmutableMap.<String, TestResult>builder()
                .put("firstTest", TestResult.PASSED)
                .put("secondTest", TestResult.PASSED)
                .build(), result);
    }

    public static class AfterClassTester {
        private int anIntToSet = 179;
        private int afterClassCallsCounter = 0;

        @ru.spbau.alferov.javahw.myjunit.test.Test
        public void firstTest() throws Exception {
            if (anIntToSet != 179) {
                throw new Exception();
            }
        }

        @ru.spbau.alferov.javahw.myjunit.test.Test
        public void secondTest() throws Exception {
            if (anIntToSet != 179) {
                throw new Exception();
            }
        }

        @ru.spbau.alferov.javahw.myjunit.test.AfterClass
        public void runAfterAllTests() throws Exception {
            anIntToSet = 0;
            afterClassCallsCounter++;
            if (afterClassCallsCounter > 1) {
                throw new Exception("I should not be called twice");
            }
        }
    }

    /**
     * Tests whether @AfterAll tests are run no more than once after all of the tests
     */
    @Test
    public void testAfterAll() throws Exception {
        InvocationResult result = Launcher.launchTests(AfterClassTester.class);
        assertTestResultEquals(ImmutableMap.<String, TestResult>builder()
                .put("firstTest", TestResult.PASSED)
                .put("secondTest", TestResult.PASSED)
                .build(), result);
    }

    public static class ClassWithFailingAfterClassMethod {
        @ru.spbau.alferov.javahw.myjunit.test.AfterClass
        public void fail() throws Exception {
            throw new Exception();
        }
    }

    /**
     * Tests whether @AfterAll tests are run at least once
     */
    @Test
    public void testAfterAllRuns() throws Exception {
        assertThrows(LauncherException.class, () -> Launcher.launchTests(ClassWithFailingAfterClassMethod.class));
    }
    
    public static class ClassWithNoDefaultConstructor {
        public ClassWithNoDefaultConstructor(int ignore) {}
    }

    /**
     * Tests whether Launcher fails on non-default-constructible classes.
     */
    @Test
    public void testNoDefaultConstructor() throws Exception {
        assertThrows(LauncherException.class, () -> Launcher.launchTests(ClassWithNoDefaultConstructor.class));
    }
    
    public static class ClassWithUnaccessibleDefaultConstructor {
        private ClassWithUnaccessibleDefaultConstructor() {}
    }

    /**
     * Tests whether Launcher fails on non-default-constructible classes.
     */
    @Test
    public void testUnaccessibleDefaultConstructor() {
        assertThrows(LauncherException.class, () -> Launcher.launchTests(ClassWithUnaccessibleDefaultConstructor.class));
    }
    
    public static abstract class AbstractClassWithTest {
        @ru.spbau.alferov.javahw.myjunit.test.Test
        public void passingTest() {
            // procrastinate
        }
    }

    /**
     * Tests whether Launcher fails on abstract classes
     */
    @Test
    public void testAbstractClassWithTest() {
        assertThrows(LauncherException.class, () -> Launcher.launchTests(AbstractClassWithTest.class));
    }

    public static class ClassWithExceptionInConstructor {
        public ClassWithExceptionInConstructor() throws Exception {
            throw new Exception("Because I can");
        }
    }

    /**
     * Tests whether Launcher fails if an exception is thrown in the constructor
     */
    @Test
    public void testExceptionInConstructor() {
        assertThrows(LauncherException.class, () -> Launcher.launchTests(ClassWithExceptionInConstructor.class));
    }

    public static class ClassWithStaticTest {
        @ru.spbau.alferov.javahw.myjunit.test.Test
        public static void staticTest() {
            // procrastinate
        }
    }

    /**
     * Tests whether Launcher fails on static tests
     */
    @Test
    public void testStaticTest() {
        assertThrows(LauncherException.class, () -> Launcher.launchTests(ClassWithStaticTest.class));
    }

    public static class ClassWithNonVoidTest {
        @ru.spbau.alferov.javahw.myjunit.test.Test
        public int nonVoidTest() {
            return 179;
        }
    }

    /**
     * Tests whether Launcher fails on non-returning void tests
     */
    @Test
    public void testNonVoidTest() {
        assertThrows(LauncherException.class, () -> Launcher.launchTests(ClassWithNonVoidTest.class));
    }

    public static class ClassWithTestWithParameters {
        @ru.spbau.alferov.javahw.myjunit.test.Test
        public void testWithParameters(int a, int b) {
            // procrastinate
        }
    }

    /**
     * Tests whether Launcher fails to run tests with more than zero parameters
     */
    @Test
    public void testTestWithParameters() {
        assertThrows(LauncherException.class, () -> Launcher.launchTests(ClassWithTestWithParameters.class));
    }
}