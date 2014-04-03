package ru.ifmo.ctddev.skripnikov.task3;

import info.kgeorgiy.java.advanced.implementor.ImplerException;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("First argument - class name!");
            return;
        }
        try {
            Implementor i = new Implementor();
            i.implement(Class.forName(args[0]), new File("./"));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ImplerException e) {
            e.printStackTrace();
        }
    }
}
