package ru.spbau.alferov.javahw.reflector;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public static abstract class ReflectionElement {
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
    public static class ClassElement extends ReflectionElement {
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
    public static class FieldElement extends ReflectionElement {
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
    public static class ConstructorElement extends ReflectionElement {
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
    public static class MethodElement extends ReflectionElement {
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
    public static abstract class Visitor {
        /**
         * A StringBuilder containing the next line.
         */
        protected StringBuilder nextLine = new StringBuilder();

        /**
         * All of the lines stored.
         */
        private List<String> lines = new ArrayList<>();

        /**
         * Flush the current line. Should be used by visitors to push back the nextLine.
         */
        public void flushLine() {
            lines.add(nextLine.toString());
            nextLine = new StringBuilder();
        }

        /**
         * Visit some reflection element. Just calling the accept method in element.
         */
        public void visit(ReflectionElement element) {
            element.accept(this);
        }

        /**
         * This function might be overridden in case the visitor has some special implementation details for getting out
         * of visited elements (e.g. the visitor is indentation-sensitive). Also it is overridden for flushing the lines
         * when necessary.
         */
        public void goOut() { }

        /**
         * Write the class modifiers. Appends the modifiers to the nextLine. May be overridden, though.
         */
        protected void appendClassModifiers(Class<?> forClass) {
            String modifiers = Modifier.toString(forClass.getModifiers());
            nextLine.append(modifiers);
            if (modifiers.length() > 0)
                nextLine.append(' ');
        }

        /**
         * Write the field modifiers. Appends the modifiers to the nextLine. May be overridden, though.
         */
        protected void appendFieldModifiers(Field forField) {
            String modifiers = Modifier.toString(forField.getModifiers());
            nextLine.append(modifiers);
            if (modifiers.length() > 0)
                nextLine.append(' ');
        }

        /**
         * Write the constructor modifiers. Appends the modifiers to the nextLine. May be overridden, though.
         */
        protected void appendConstructorModifiers(Constructor<?> forConstructor) {
            String modifiers = Modifier.toString(forConstructor.getModifiers());
            nextLine.append(modifiers);
            if (modifiers.length() > 0)
                nextLine.append(' ');
        }

        /**
         * Write the method modifiers. Appends the modifiers to the nextLine. May be overridden, though.
         */
        protected void appendMethodModifiers(Method forMethod) {
            String modifiers = Modifier.toString(forMethod.getModifiers());
            nextLine.append(modifiers);
            if (modifiers.length() > 0)
                nextLine.append(' ');
        }

        /**
         * Write the class keyword. Appends the "class " string to the nextLine. May be overridden, though.
         */
        protected void appendClassKeyword() {
            nextLine.append("class ");
        }

        /**
         * Get the type name (with generics). Used by different functions. There is no need in overriding it, really.
         */
        protected String getTypeName(Type t) {
            return t.getTypeName();
        }

        /**
         * Write an array of type parameters. Appends the type parameters enclosed in &lt; and &gt; with all their
         * dependencies to some StringBuilder. There is no need in overriding it, really.
         */
        protected void appendTypeParameters(TypeVariable[] typeVariables, StringBuilder where) {
            if (typeVariables.length != 0) {
                List<String> params = new ArrayList<>();
                for (TypeVariable<?> tv : typeVariables) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(tv.getName());
                    Type[] bounds = tv.getBounds();
                    List<String> boundStrings = new ArrayList<>();
                    for (Type t : bounds) {
                        if (!t.equals(Object.class)) {
                            boundStrings.add(getTypeName(t));
                        }
                    }
                    if (boundStrings.size() > 0) {
                        sb.append(" extends ");
                        sb.append(
                                boundStrings
                                .stream()
                                .collect(Collectors.joining(" & "))
                        );
                    }
                    params.add(sb.toString());
                }
                where.append(
                        params
                        .stream()
                        .collect(Collectors.joining(", ", "<", ">"))
                );
            }
        }

        /**
         * Write an array of type parameters. Appends the type parameters enclosed in &lt; and &gt; with all their
         * dependencies to the nextLine. There is no need in overriding it, really.
         */
        protected void appendTypeParameters(TypeVariable[] typeVariables) {
            appendTypeParameters(typeVariables, nextLine);
        }

        /**
         * Writes some Class name to some StringBuilder. There is no need in overriding it, really.
         */
        protected void writeClassName(Class<?> clazz, StringBuilder sb) {
            sb.append(clazz.getSimpleName());
            appendTypeParameters(clazz.getTypeParameters(), sb);
        }

        /**
         * Writes some Class name to the nextLine. There is no need in overriding it, really.
         */
        protected void writeClassName(Class<?> clazz) {
            writeClassName(clazz, nextLine);
        }

        /**
         * Writes the field type. Appends it to the nextLine. May be overridden, though.
         */
        protected void appendFieldType(Field forField) {
            nextLine.append(getTypeName(forField.getGenericType()));
            nextLine.append(' ');
        }

        /**
         * Writes the class name. Appends it to the nextLine. May be overridden, though.
         */
        protected void appendClassName(Class<?> forClass) {
            nextLine.append(forClass.getSimpleName());
        }

        /**
         * Writes the method return type. Appends it to the nextLine. May be overridden, though.
         */
        protected void appendMethodReturnType(Method forMethod) {
            writeClassName(forMethod.getReturnType());
            nextLine.append(' ');
        }

        /**
         * Writes the field name. Appends it to the nextLine. May be overridden, though.
         */
        protected void appendFieldName(Field field) {
            nextLine.append(field.getName());
        }

        /**
         * Being called for every constructor, this function appends the class name to the nextLine. May be overridden,
         * though.
         */
        protected void appendConstructor(Constructor<?> forConstructor) {
            nextLine.append(forConstructor.getDeclaringClass().getSimpleName());
        }

        /**
         * Writes the method name. Appends it to the nextLine. May be overridden, though.
         */
        protected void appendMethodName(Method forMethod) {
            nextLine.append(forMethod.getName());
        }

        /**
         * Appends name of some parameter of a method/constructor to some StringBuilder. Generates a new name according
         * to the index by default. May be overridden, though.
         */
        protected void appendParameterName(int index, StringBuilder sb) {
            sb.append(" arg");
            sb.append(index);
        }

        /**
         * Appends the constructor/method parameters to the nextLine. May be overridden, though.
         */
        protected void appendParameters(Type[] parameters) {
            List<String> params = new ArrayList<>();
            for (int i = 0; i < parameters.length; i++) {
                StringBuilder sb = new StringBuilder();
                sb.append(getTypeName(parameters[i]));
                appendParameterName(i, sb);
                params.add(sb.toString());
            }
            nextLine.append(
                    params
                    .stream()
                    .collect(Collectors.joining(", ", "(", ")"))
            );
        }

        /**
         * Indicates the end of some class declaration. Does nothing by default.
         * Overridden in case it is required to write a curly bracket or increment the indentation level.
         */
        protected void finishClassDeclaration() { }

        /**
         * Indicates the end of some field declaration. Does nothing by default.
         * May be overridden, though.
         */
        protected void finishFieldDeclaration() { }

        /**
         * Indicates the end of some constructor declaration. Does nothing by default.
         * May be overridden, though.
         */
        protected void finishConstructorDeclaration() { }

        /**
         * Indicates the end of some method declaration. Does nothing by default.
         * May be overridden, thouh.
         */
        protected void finishMethodDeclaration() { }

        /**
         * Visit the class.
         */
        public void visitClass(ClassElement classElement) {
            appendClassModifiers(classElement.getStored());
            appendClassKeyword();
            appendClassName(classElement.getStored());
            appendTypeParameters(classElement.getStored().getTypeParameters());
            finishClassDeclaration();
        }

        /**
         * Visit the field.
         */
        public void visitField(FieldElement fieldElement) {
            appendFieldModifiers(fieldElement.getStored());
            appendFieldType(fieldElement.getStored());
            appendFieldName(fieldElement.getStored());
            finishFieldDeclaration();
        }

        /**
         * Visit the constructor.
         */
        public void visitConstructor(ConstructorElement constructorElement) {
            appendConstructorModifiers(constructorElement.getStored());
            appendConstructor(constructorElement.getStored());
            appendParameters(constructorElement.getStored().getGenericParameterTypes());
            finishConstructorDeclaration();
        }

        /**
         * Visit the method.
         */
        public void visitMethod(MethodElement methodElement) {
            appendMethodModifiers(methodElement.getStored());
            appendTypeParameters(methodElement.getStored().getTypeParameters());
            if (methodElement.getStored().getTypeParameters().length > 0) {
                nextLine.append(' ');
            }
            appendMethodReturnType(methodElement.getStored());
            appendMethodName(methodElement.getStored());
            appendParameters(methodElement.getStored().getGenericParameterTypes());
            finishMethodDeclaration();
        }

        /**
         * Visit some class and return all of the lines. The main method of the class :)
         */
        public List<String> visitClass(Class<?> whichClass, boolean shouldVisitSubclasses) {
            visit(new ClassElement(whichClass, shouldVisitSubclasses));
            return lines;
        }
    }
}
