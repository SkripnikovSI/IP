package ru.ifmo.ctddev.skripnikov.task3;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public class Implementor implements Impler {

    private static String getLink(Class<?> c) {
        if (c.isInterface())
            return " implements ";
        else
            return " extends ";
    }

    private static void printConstructor(Class<?> c, PrintWriter out) throws ImplerException {
        if (!c.isInterface()) {
            Constructor<?> constructor = null;
            for (Constructor<?> con : c.getDeclaredConstructors()) {
                if (!Modifier.isPrivate(con.getModifiers()) && con.getParameterTypes().length == 0 && con.getExceptionTypes().length == 0)
                    return;
                if (Modifier.isPublic(con.getModifiers()) || constructor == null && !Modifier.isPrivate(con.getModifiers()))
                    constructor = con;
            }
            if (constructor != null) {
                out.print("\tpublic " + getName(c) + "(" + paramsToString(constructor.getParameterTypes(), true) + ") ");
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

    private static String getName(Class<?> c) {
        return c.getSimpleName() + "Impl";
    }

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

    private static String accessMod(Method m) {
        if (Modifier.isProtected(m.getModifiers()))
            return "protected ";
        else if (Modifier.isPublic(m.getModifiers()))
            return "public ";
        return "";
    }

    private static void addAll(HashMap<String, Method> data, Method[] methods) {
        for (Method m : methods)
            if (Modifier.isAbstract(m.getModifiers()))
                data.put(getKey(m), m);
            else
                data.remove(getKey(m));
    }

    private static String getKey(Method m) {
        return m.getName() + paramsToString(m.getParameterTypes(), true);
    }

    private static String getPath(Class<?> c, File file) {
        StringBuilder sb = new StringBuilder();
        String[] s = c.getPackage().getName().split("\\.");
        sb.append(file.getName());
        for (String value : s) {
            sb.append(File.separator);
            sb.append(value);
        }
        return sb.toString();
    }

    @Override
    public void implement(Class<?> c, File file) throws ImplerException {
        if (Modifier.isFinal(c.getModifiers()))
            throw new ImplerException();
        String path = getPath(c, file);
        new File(path).mkdirs();
        PrintWriter out = null;
        try {
            out = new PrintWriter(path + File.separator + getName(c) + ".java");
            out.println("package " + c.getPackage().getName() + ";");
            out.println("public class " + getName(c) + getLink(c) + c.getCanonicalName() + " {");
            printConstructor(c, out);
            printMethods(addMethods(c, new HashMap<String, Method>()), out);
            out.print("}");
        } catch (FileNotFoundException e) {
            throw new ImplerException();
        } finally {
            if (out != null)
                out.close();
        }
    }
}
