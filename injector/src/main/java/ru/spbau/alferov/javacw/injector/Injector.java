package ru.spbau.alferov.javacw.injector;

import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Class for creating objects by class names.
 */
public class Injector {
    /**
     * Implementation of the injector.
     * This class is created every time Injector.initialize() is called.
     */
    private static class InjectorImpl {
        /**
         * Created objects. For each class only one object is created.
         */
        Map<Class, Object> createdObjects;

        /**
         * Is the class in current dependencies?
         */
        Set<Class> currentDependencies;

        /**
         * Finds subclasses.
         */
        Reflections reflections;

        /**
         * Creates an object of class (not interface).
         */
        private Object createObject(Class type) throws InjectionException {
            Constructor constructors[] = type.getDeclaredConstructors();
            assert constructors.length == 1;
            Constructor constructor = constructors[0];

            Object[] parameters = new Object[constructor.getParameterCount()];
            Class [] deps = constructor.getParameterTypes();
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = getRecursivelyCreatedObject(deps[i]);
            }

            try {
                return constructor.newInstance(parameters);
            } catch (InstantiationException e) {
                InjectionException f = new InjectionException("InstantiationException occurred while creating object of class " + type.getName());
                f.addSuppressed(e);
                throw f;
            } catch (IllegalAccessException e) {
                InjectionException f = new InjectionException("IllegalAccessException occurred while creating object of class " + type.getName());
                f.addSuppressed(e);
                throw f;
            } catch (InvocationTargetException e) {
                InjectionException f = new InjectionException("InvocationTargetException occurred while creating object of class " + type.getName());
                f.addSuppressed(e);
                throw f;
            }
        }

        /**
         * Creates object of an interface.
         */
        private Object createInterface(Class type) throws InjectionException {
            Set<Class> classes = reflections.getSubTypesOf(type);
            if (classes.size() > 1)
                throw new AmbigousImplementationException(type);
            if (classes.isEmpty())
                throw new ImplementationNotFoundException(type);
            Class found = classes.stream().findAny().orElse(null);
            return getRecursivelyCreatedObject(found);
        }

        /**
         * Depth-first search for creating objects.
         */
        private void createRecursively(Class type) throws InjectionException {
            if (currentDependencies.contains(type))
                throw new InjectionCycleException(type);
            currentDependencies.add(type);

            if (!createdObjects.containsKey(type)) {
                if (type.isInterface()) {
                    createdObjects.put(type, createInterface(type));
                } else {
                    createdObjects.put(type, createObject(type));
                }
            }

            currentDependencies.remove(type);
        }

        /**
         * Call DFS and take object from Map.
         */
        private Object getRecursivelyCreatedObject(Class type) throws InjectionException {
            createRecursively(type);
            return createdObjects.get(type);
        }

        /**
         * Base constructor.
         */
        public InjectorImpl() {
            createdObjects = new HashMap<>();
            currentDependencies = new HashSet<>();
            reflections = new Reflections("");
        }

        /**
         * ~initialize()
         */
        public Object create(String rootClassName) throws ClassNotFoundException, InjectionException {
            return getRecursivelyCreatedObject(Class.forName(rootClassName));
        }
    }

    /**
     * See the task for the description :)
     */
    public static Object initialize(String rootClassName) throws ClassNotFoundException, InjectionException {
        InjectorImpl impl = new InjectorImpl();
        return impl.create(rootClassName);
    }
}