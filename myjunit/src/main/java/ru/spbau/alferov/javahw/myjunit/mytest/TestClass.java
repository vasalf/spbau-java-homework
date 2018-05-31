package ru.spbau.alferov.javahw.myjunit.mytest;

import ru.spbau.alferov.javahw.myjunit.test.Before;
import ru.spbau.alferov.javahw.myjunit.test.BeforeClass;
import ru.spbau.alferov.javahw.myjunit.test.Test;

public class TestClass {
    @Test
    public void passingTest() {
        // Do nothing.
    }

    @Test
    public void passingUselessTest() {
        for (int i = 0; i < 1e7; i++) {
            // procrastinate
        }
    }

    @Test
    public void failingTest() throws Exception {
        throw new Exception();
    }

    private int setInBeforeClass = 0;

    @BeforeClass
    public void toDoBefore() {
        setInBeforeClass = 179;
    }

    @Test
    public void testBeforeWorking() {
        if (setInBeforeClass != 179) {
            throw new RuntimeException();
        }
    }

    private int setInBeforeEach = 0;

    @Before
    public void toDoBeforeEach() {
        setInBeforeEach = 179;
    }

    @Test
    public void testBeforeEach() {
        if (setInBeforeEach != 179) {
            throw new RuntimeException();
        }
    }

    @Test(ignored = "Because I can")
    public void ignoredTest() {

    }

    @Test(expected = Exception.class)
    public void passingFailingTest() throws Exception {
        throw new Exception("HELP! I'm stuck in there!");
    }

    private class CorrectException extends Exception { }
    private class WrongException extends Exception { }

    @Test(expected = CorrectException.class)
    public void failingFailingTest() throws Exception {
        throw new WrongException();
    }

    @Test(expected = Exception.class)
    public void failingPassingTest() throws Exception {

    }
}
