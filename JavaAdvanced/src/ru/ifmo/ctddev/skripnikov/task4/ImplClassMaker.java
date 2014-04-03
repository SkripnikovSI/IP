package ru.ifmo.ctddev.skripnikov.task4;

import info.kgeorgiy.java.advanced.implementor.ImplerException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * @author Skripnikov Sergey
 */
public class ImplClassMaker {

    /**
     * Implementing class.
     */
    public final Class<?> clazz;
    /**
     * Old class name + Impl
     */
    public final String name;
    /**
     * Class package where was replaced "." to "/"
     */
    public final String packageAsPath;
    /**
     * packageAsPath + / + name + .java
     */
    public final String jPath;
    /**
     * packageAsPath + / + name + .class
     */
    public final String cPath;

    /**
     * Initialization constants.
     * <p/>
     * Initialization constants depending on the transmitted class.
     *
     * @param clazz type token to create implementation for.
     * @throws info.kgeorgiy.java.advanced.implementor.ImplerException when param clazz is null.
     */
    ImplClassMaker(Class<?> clazz) throws ImplerException {
        if (clazz == null)
            throw new ImplerException();
        this.clazz = clazz;
        name = clazz.getSimpleName() + "Impl";
        if (clazz.getPackage() != null)
            packageAsPath = clazz.getPackage().getName().replace(".", "/");
        else
            packageAsPath = "";
        String cnr = clazz.getCanonicalName().replace(".", "/");
        jPath = cnr + "Impl.java";
        cPath = cnr + "Impl.class";
    }

    /**
     * Create a inherited class.
     * <p/>
     * Create a inherited class in root directory.
     *
     * @param root root directory.
     * @throws info.kgeorgiy.java.advanced.implementor.ImplerException when param clazz is null.
     */
    public void makeImplClass(File root) throws ImplerException {
        if (root == null || Modifier.isFinal(clazz.getModifiers()))
            throw new ImplerException();
        new File(root, packageAsPath).mkdirs();
        PrintWriter out = null;
        try {
            out = new PrintWriter(root + File.separator + jPath);
            printClassHeader(out);
            printConstructor(out);
            printMethods(addMethods(clazz, new HashMap<String, Method>()), out);
            out.print("}");
        } catch (FileNotFoundException e) {
            throw new ImplerException();
        } finally {
            if (out != null)
                out.close();
        }
    }

    /**
     * Print class header.
     * <p/>
     * Print class package(if available), name, parent and {.
     *
     * @param out where header be written.
     */
    private void printClassHeader(PrintWriter out) {
        if (clazz.getPackage() != null) {
            out.println("package " + clazz.getPackage().getName() + ";");
            out.println();
        }
        out.print("public class " + name);
        if (clazz.isInterface())
            out.print(" implements ");
        else
            out.print(" extends ");
        out.println(clazz.getCanonicalName() + " {");
    }

    /**
     * Print class constructor.
     * <p/>
     * Print class constructor if default constructor is unavailable.
     *
     * @param out where constructor be written.
     * @throws info.kgeorgiy.java.advanced.implementor.ImplerException when all constructor is private.
     */
    private void printConstructor(PrintWriter out) throws ImplerException {
        if (!clazz.isInterface()) {
            Constructor<?> constructor = null;
            for (Constructor<?> con : clazz.getDeclaredConstructors()) {
                if (!Modifier.isPrivate(con.getModifiers()) && con.getParameterTypes().length == 0 && con.getExceptionTypes().length == 0)
                    return;
                if (Modifier.isPublic(con.getModifiers()) || constructor == null && !Modifier.isPrivate(con.getModifiers()))
                    constructor = con;
            }
            if (constructor != null) {
                out.print("\tpublic " + name + "(" + paramsToString(constructor.getParameterTypes(), true) + ") ");
                Class<?>[] exceptions = constructor.getExceptionTypes();
                if (exceptions.length != 0) {
                    out.print("throws ");
                    for (int i = 0; i < exceptions.length; i++) {
                        if (i != 0)
                            out.print(", ");
                        out.print(exceptions[i].getCanonicalName());
                    }
                }
                out.println("{");
                out.println("\t\tsuper(" + paramsToString(constructor.getParameterTypes(), false) + ");");
                out.println("\t}");
            } else {
                throw new ImplerException();
            }
        }
    }

    /**
     * Print methods.
     * <p/>
     * Print methods in out.
     *
     * @param methods implemented methods.
     * @param out      where methods be written.
     */
    private static void printMethods(HashMap<String, Method> methods, PrintWriter out) {
        if (methods != null)
            for (Method m : methods.values()) {
                out.println();
                out.print("\t" + accessMod(m) + m.getReturnType().getCanonicalName() + " " + m.getName() +
                        "(" + paramsToString(m.getParameterTypes(), true) + ") {");
                if (m.getReturnType() != void.class) {
                    out.println();
                    out.print("\t\treturn ");
                    if (!m.getReturnType().isPrimitive())
                        out.print("null");
                    else if (m.getReturnType() != boolean.class)
                        out.print("0");
                    else
                        out.print("false");
                    out.println(";");
                    out.print("\t");
                }
                out.println("}");
            }
    }

    /**
     * Search methods for implementing.
     * <p/>
     * Search methods for implementing in interfaces and superclasses with the exception of the already implemented.
     *
     * @param c there is a search method.
     * @param m intermediate data.
     * @return prepared data.
     */
    private static HashMap<String, Method> addMethods(Class<?> c, HashMap<String, Method> m) {
        if (c != null)
            if (c.isInterface()) {
                addAll(m, c.getMethods());
            } else {
                for (Class<?> i : c.getInterfaces())
                    addMethods(i, m);
                addMethods(c.getSuperclass(), m);
                addAll(m, c.getDeclaredMethods());
            }
        return m;
    }

    /**
     * Add methods in {@link java.util.HashMap}.
     * <p/>
     * Add method in {@link java.util.HashMap} if is abstract else delete.
     *
     * @param data    data.
     * @param methods methods for adding.
     */
    private static void addAll(HashMap<String, Method> data, Method[] methods) {
        for (Method m : methods)
            if (Modifier.isAbstract(m.getModifiers()))
                data.put(getKey(m), m);
            else
                data.remove(getKey(m));
    }

    /**
     * Generates a key.
     * <p/>
     * Generates a key for use in {@link java.util.HashMap}.
     *
     * @param m method for generation key.
     * @return generated key.
     */
    private static String getKey(Method m) {
        return m.getName() + paramsToString(m.getParameterTypes(), true);
    }

    /**
     * Access mod.
     * <p/>
     * Return access mod for method.
     *
     * @param m @link java.lang.reflect.Method} to capture access.
     * @return access mod for method.
     */
    private static String accessMod(Method m) {
        if (Modifier.isProtected(m.getModifiers()))
            return "protected ";
        else if (Modifier.isPublic(m.getModifiers()))
            return "public ";
        return "";
    }

    /**
     * Generates a string parameter list.
     * <p/>
     * Generates a string parameter list depending on flag. If true specify the name and type, else specify only name.
     *
     * @param c  params array.
     * @param fl flag responsible for the inclusion of types.
     * @return generated string.
     */
    private static String paramsToString(Class<?>[] c, boolean fl) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < c.length; i++) {
            if (i != 0)
                sb.append(", ");
            if (fl) {
                sb.append(c[i].getCanonicalName());
                sb.append(" ");
            }
            sb.append("a");
            sb.append(i);
        }
        return sb.toString();
    }
}
