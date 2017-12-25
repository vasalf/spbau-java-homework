package ru.spbau.alferov.javahw.reflector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *  A set of abstract classes and realizations for walking through declarations in some class. The purpose is to walk
 *  through all declarations in some class and write all of them into some List of Strings. The visitors are used both
 *  in diffClasses and printStructure methods.
 *
 *  @see <a href="https://sourcemaking.com/design_patterns/visitor">Detalis of visitor design pattern</a>
 */
public class ClassVisitor {
    /**
     * A visitor-designed storage for some reflection classes, such as Method, Class, etc.
     * All of the architecture is designed in way that it's nearly not necessary for programmer to call visit() methods,
     * so accept() methods try to walk as far as possible.
     */
    public abstract class ReflectionElement {
        /**
         * The visitors may or may not want to visit subclasses. So the subclasses will or will not be visited according
         * to the shouldVisitSubclasses value.
         */
        protected boolean visitSubclasses;

        /**
         * Constructs the abstract class with the shouldVisitSubclasses variable. It is inherited by all child
         * elements.
         */
        ReflectionElement(boolean shouldVisitSubclasses) {
            visitSubclasses = shouldVisitSubclasses;
        }

        /**
         * Accept the visitor.
         */
        public abstract void accept(Visitor v);
    }

    /**
     * An acceptor for Class&lt;?&gt; elements.
     */
    public class ClassElement extends ReflectionElement {
        private Class<?> stored;

        /**
         * Get the stored class.
         */
        public Class<?> getStored() {
            return stored;
        }

        /**
         * Construct a new acceptor.
         */
        ClassElement(Class<?> toStore, boolean shouldVisitSubclasses) {
            super(shouldVisitSubclasses);
            stored = toStore;
        }

        /**
         * Accept the visitor. Walks through all declared subclasses, fields, constructors and methods.
         */
        @Override
        public void accept(Visitor v) {
            v.visitClass(this);

            if (visitSubclasses) {
                for (Class<?> subclass : stored.getDeclaredClasses()) {
                    v.visit(new ClassElement(subclass, visitSubclasses));
                }
            }

            for (Field field : stored.getDeclaredFields()) {
                v.visit(new FieldElement(field, visitSubclasses));
            }

            for (Constructor<?> constructor : stored.getDeclaredConstructors()) {
                v.visit(new ConstructorElement(constructor, visitSubclasses));
            }

            for (Method method : stored.getDeclaredMethods()) {
                v.visit(new MethodElement(method, visitSubclasses));
            }

            v.goOut();
        }
    }

    /**
     * An acceptor for fields.
     */
    public class FieldElement extends ReflectionElement {
        private Field stored;

        /**
         * Get the stored field.
         */
        public Field getStored() {
            return stored;
        }

        /**
         * Construct a new acceptor.
         */
        FieldElement(Field toStore, boolean shouldVisitSubclasses) {
            super(shouldVisitSubclasses);
            stored = toStore;
        }

        /**
         * Accept the visitor.
         */
        @Override
        public void accept(Visitor v) {
            v.visitField(this);
            v.goOut();
        }
    }

    /**
     * An acceptor for Constructor&lt;?&gt; elements.
     */
    public class ConstructorElement extends ReflectionElement {
        private Constructor<?> stored;

        /**
         * Get the stored constructor.
         */
        public Constructor<?> getStored() {
            return stored;
        }

        /**
         * Construct a new acceptor.
         */
        public ConstructorElement(Constructor<?> toStore, boolean shouldVisitSubclasses) {
            super(shouldVisitSubclasses);
            stored = toStore;
        }

        /**
         * Accept the visitor.
         */
        @Override
        public void accept(Visitor v) {
            v.visitConstructor(this);
            v.goOut();
        }
    }

    /**
     * An acceptor for methods.
     */
    public class MethodElement extends ReflectionElement {
        private Method stored;

        /**
         * Get the stored method.
         */
        public Method getStored() {
            return stored;
        }

        /**
         * Construct a new acceptor.
         */
        public MethodElement(Method toStore, boolean shouldVisitSubclass) {
            super(shouldVisitSubclass);
            stored = toStore;
        }

        /**
         * Accept the visitor.
         */
        @Override
        public void accept(Visitor v) {
            v.visitMethod(this);
            v.goOut();
        }
    }

    /**
     * The base class for visitors. Contains a StringBuilder for next line and an ArrayList for storing existent lines.
     * Designed in such way that realization should be minimal.
     */
    public abstract class Visitor {
        /**
         * Visit some reflection element. Just calling the accept method in element.
         */
        public void visit(ReflectionElement element) {
            element.accept(this);
        }

        /**
         * This function might be overridden in case the visitor has some special implementation details for getting out
         * of visited functions (e.g. the visitor is indentation-sensitive). Does nothing by default.
         */
        public void goOut() { }

        /**
         * Visit the class.
         */
        public abstract void visitClass(ClassElement classElement);

        /**
         * Visit the field.
         */
        public abstract void visitField(FieldElement fieldElement);

        /**
         * Visit the constructor.
         */
        public abstract void visitConstructor(ConstructorElement constructorElement);

        /**
         * Visit the method.
         */
        public abstract void visitMethod(MethodElement methodElement);
    }
}
