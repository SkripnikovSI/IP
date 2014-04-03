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

    public static void main(String[] args) {
        try {
            if (args.length == 1) {
                staticImplement(Class.forName(args[0]), new File("./result"));
            } else {
                System.out.println("Illegal arguments.");
                System.out.println("Try: \"<class-name>\" or \"-jar <class-name> <name>.jar\".");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ImplerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void implement(Class<?> token, File root) throws ImplerException {
        staticImplement(token, root);
    }

    private static void staticImplement(Class<?> c, File root) throws ImplerException {
        if (Modifier.isFinal(c.getModifiers()))
            throw new ImplerException();
        String filePath = c.getCanonicalName().replace(".", "/") + "Impl.java";
        new File(root + File.separator + getPath(filePath)).mkdirs();
        PrintWriter out = null;
        try {
            out = new PrintWriter(root + File.separator + filePath);
            if (c.getPackage() != null) {
                out.println("package " + c.getPackage().getName() + ";");
                out.println();
            }
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

    private static String getPath(String filePath) {
        String[] parts = filePath.split("/");
        return filePath.substring(0, filePath.length() -  parts[parts.length - 1].length());
    }

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
}
