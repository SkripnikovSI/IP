package ru.ifmo.ctddev.skripnikov.task4;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * @author Skripnikov Sergey
 */
public class Implementor implements Impler, JarImpler {

    /**
     * Main method.
     * <p/>
     * Create implementing class in directory result or jar file in root.
     *
     * @param args "class-name" or "-jar class-name name.jar".
     */
    public static void main(String[] args) {
        try {
            if (args.length == 1 && !"-jar".equals(args[0])) {
                new ImplClassMaker(Class.forName(args[0])).makeImplClass(new File("./result"));
            } else if (args.length == 3 && "-jar".equals(args[0])) {
                makeJar(Class.forName(args[1]), new File(args[2]));
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

    /**
     * Wrapper.
     * <p/>
     * Not-static wrapper of method {@link ru.ifmo.ctddev.skripnikov.task4.ImplClassMaker#makeImplClass(java.io.File)}.
     *
     * @param token parent class.
     * @param root  root directory.
     * @throws info.kgeorgiy.java.advanced.implementor.ImplerException when
     * {@link ru.ifmo.ctddev.skripnikov.task4.ImplClassMaker#ImplClassMaker(Class)} or
     * {@link ru.ifmo.ctddev.skripnikov.task4.ImplClassMaker#makeImplClass(java.io.File)} throw this exception.
     */
    @Override
    public void implement(Class<?> token, File root) throws ImplerException {
        new ImplClassMaker(token).makeImplClass(root);
    }

    /**
     * Wrapper.
     * <p/>
     * Not-static wrapper of method {@link #makeJar(Class, java.io.File)}.
     *
     * @param c       parent class.
     * @param jarFile file name with path.
     * @throws info.kgeorgiy.java.advanced.implementor.ImplerException when
     * {@link ru.ifmo.ctddev.skripnikov.task4.Implementor#makeJar(Class, java.io.File)} throw this exception.
     */
    @Override
    public void implementJar(Class<?> c, File jarFile) throws ImplerException {
        makeJar(c, jarFile);
    }

    /**
     * Create jar.
     * <p/>
     * Create a inherited class and packs it in jar.
     *
     * @param c       parent class.
     * @param jarFile file name with path.
     * @throws info.kgeorgiy.java.advanced.implementor.ImplerException when parent class is bad for implementation or
     * throw {@link java.io.IOException}.
     */
    private static void makeJar(Class<?> c, File jarFile) throws ImplerException {
        File root = new File("tmp");
        ImplClassMaker icm = new ImplClassMaker(c);
        icm.makeImplClass(root);
        File f = new File(root, icm.jPath);
        compileFile(root, f.getAbsolutePath());
        clean(f);
        try {
            Manifest manifest = new Manifest();
            manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
            JarOutputStream target = new JarOutputStream(new FileOutputStream(jarFile), manifest);
            addToJAR(new File(root, icm.cPath), target);
            target.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ImplerException();
        }
        clean(root);
    }

    /**
     * Create jar.
     * <p/>
     * Direct creation jar file.
     *
     * @param source directory with the compiled code.
     * @param target stream of jar file.
     * @throws java.io.IOException when
     * {@link ru.ifmo.ctddev.skripnikov.task4.Implementor#writeEntry(java.io.File, String, java.util.jar.JarOutputStream)}
     * throw this exception.
     */
    private static void addToJAR(File source, JarOutputStream target) throws IOException {
        String name = source.getPath().replace("\\", "/").substring(4);
        if (source.isDirectory()) {
            if (!name.isEmpty()) {
                if (!name.endsWith("/"))
                    name += "/";
                writeEntry(source, name, target);
            }
            for (File nestedFile : source.listFiles())
                addToJAR(nestedFile, target);
        } else {
            writeEntry(source, name, target);
        }
    }

    /**
     * Write entry.
     * <p/>
     * Creating a record in jar file.
     *
     * @param source directory with the compiled code.
     * @param name   entry name.
     * @param target stream of jar file.
     * @throws java.io.IOException
     */
    private static void writeEntry(File source, String name, JarOutputStream target) throws IOException {
        JarEntry entry = new JarEntry(name);
        entry.setTime(source.lastModified());
        target.putNextEntry(entry);

        BufferedInputStream in = null;
        if (source.isFile())
            try {
                in = new BufferedInputStream(new FileInputStream(source));
                byte[] buffer = new byte[1024];
                int count;
                while ((count = in.read(buffer)) != -1)
                    target.write(buffer, 0, count);
            } finally {
                if (in != null)
                    in.close();
            }
        target.closeEntry();
    }

    /**
     * Compile.
     * <p/>
     * Compilation of the specified file.
     *
     * @param root root directory.
     * @param file file for compilation.
     */
    private static void compileFile(File root, String file) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        String[] args = {"-cp", root.getAbsolutePath(), file};
        compiler.run(null, null, null, args);
    }

    /**
     * Clean.
     * <p/>
     * Delete directory tree.
     *
     * @param file root of tree.
     */
    private static void clean(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null)
                for (File child : files)
                    clean(child);
        }
        file.delete();
    }
}
