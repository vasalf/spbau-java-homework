package ru.spbau.alferov.javacw.injector;

import jdk.nashorn.internal.objects.annotations.Constructor;
import org.junit.Test;

import static org.junit.Assert.*;

public class InjectorTest {
    public static class justInitA {
        public justInitA() {}
    }

    @Test
    public void justInit() throws Exception {
        Injector.initialize(justInitA.class.getName());
    }

    public static class constructorCalledA {
        public static boolean constructorCalled = false;

        public constructorCalledA() {
            constructorCalled = true;
        }
    }

    @Test
    public void constructorCalled() throws Exception {
        Injector.initialize(constructorCalledA.class.getName());
        assertTrue(constructorCalledA.constructorCalled);
    }

    public static class constructorDependenciesA {
        public static boolean constructorCalled = false;

        public constructorDependenciesA() {
            constructorCalled = true;
        }
    }

    public static class constructorDependenciesB {
        public constructorDependenciesA cd;

        public constructorDependenciesB(constructorDependenciesA cda) {
            cd = cda;
        }
    }

    @Test
    public void constructorDependencies() throws Exception {
        Injector.initialize(constructorDependenciesB.class.getName());
        constructorDependenciesB cdb = (constructorDependenciesB)Injector.initialize(constructorDependenciesB.class.getName());
        assertTrue(constructorDependenciesA.constructorCalled);
        assertNotNull(cdb.cd);
    }

    public interface interfaceDependenciesI { }

    public static class interfaceDependenciesIR implements interfaceDependenciesI {
        public static boolean constructorCalled = false;

        interfaceDependenciesIR() {
            constructorCalled = true;
        }
    }

    public static class interfaceDependenciesA {
        interfaceDependenciesA(interfaceDependenciesI ifc) { }
    }

    @Test
    public void interfaceDependencies() throws Exception {
        Injector.initialize(interfaceDependenciesA.class.getName());
        assertTrue(interfaceDependenciesIR.constructorCalled);
    }

    public interface initializeOnceI1 { }
    public interface initializeOnceI2 { }

    public static class initializeOnceIR implements initializeOnceI1, initializeOnceI2 {
        public static int constructorCalls = 0;

        initializeOnceIR() {
            constructorCalls++;
        }
    }

    public static class initializeOnceA {
        initializeOnceA(initializeOnceI1 i1, initializeOnceI2 i2) { }
    }

    @Test
    public void initializeOnce() throws Exception {
        Injector.initialize(initializeOnceA.class.getName());
        assertEquals(1, initializeOnceIR.constructorCalls);
    }

    public interface noImplI { }

    public class noImplA {
        noImplA(noImplI n) { }
    }

    @Test(expected = ImplementationNotFoundException.class)
    public void noImpl() throws Exception {
        Injector.initialize(noImplA.class.getName());
    }

    public static class injectionCycleA {
        injectionCycleA(injectionCycleB b) { }
    }

    public static class injectionCycleB {
        injectionCycleB(injectionCycleA a) { }
    }

    @Test(expected = InjectionCycleException.class)
    public void injectionCycle() throws Exception {
        Injector.initialize(injectionCycleA.class.getName());
    }

    public static class longInjectionCycleA {
        longInjectionCycleA(longInjectionCycleB b) { }
    }

    public static class longInjectionCycleB {
        longInjectionCycleB(longInjectionCycleC c) { }
    }

    public static class longInjectionCycleC {
        longInjectionCycleC(longInjectionCycleD d) { }
    }

    public static class longInjectionCycleD {
        longInjectionCycleD(longInjectionCycleA a) { }
    }

    @Test(expected = InjectionCycleException.class)
    public void longInjectionCycle() throws Exception {
        Injector.initialize(longInjectionCycleA.class.getName());
    }

    public interface ambigousImplementationI { }
    public static class ambigousImplementationA implements ambigousImplementationI { }
    public static class ambigousImplementationB implements ambigousImplementationI { }

    @Test(expected = AmbigousImplementationException.class)
    public void ambigousImplementation() throws Exception {
        Injector.initialize(ambigousImplementationI.class.getName());
    }
}