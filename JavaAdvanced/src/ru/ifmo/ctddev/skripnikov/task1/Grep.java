package ru.ifmo.ctddev.skripnikov.task1;

import java.io.*;
import java.util.ArrayList;

public class Grep {
    private static final String[] encodings = {"UTF-8", "KOI8-R", "CP1251", "CP866"};

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Error");//TODO Change report
            return;
        } else if (args[0].equals("-")) {
            BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Введите строки через пробел:");
            String str = r.readLine();
            args = str.split(" ");
        }
        long time = System.currentTimeMillis();
        ArrayList<PrefixFunction> list = new ArrayList<PrefixFunction>();
        for (String arg : args)
            for (String encoding : encodings)
                list.add(new PrefixFunction(arg.getBytes(encoding), encoding));
        checkAllFiles(new File("./"), list);
        System.out.println(System.currentTimeMillis() - time);
    }

    public static void checkAllFiles(File f, ArrayList<PrefixFunction> list) throws IOException {
        if (f.isFile()) {
            search(f.getPath(), new RandomAccessFile(f, "r"), list);
        } else if (f.isDirectory()) {
            File[] files = f.listFiles();
            if (files != null)
                for (File file : files) checkAllFiles(file, list);
        }

    }

    public static void search(String path, RandomAccessFile raf, ArrayList<PrefixFunction> list) throws IOException {
        int buffSize = 8192;
        long start = 0;
        byte[] buff = new byte[buffSize];
        mainWhile:
        while (true) { //file
            long cur = raf.getFilePointer();
            int size = raf.read(buff);
            if (size == -1)
                break;
            for (int i = 0; i < size; i++) { //buff
                if (buff[i] != '\n' && buff[i] != '\r') {
                    String encoding = null;
                    for (PrefixFunction aList : list)
                        if (aList.checkNextByte(buff[i])) {
                            encoding = aList.encoding;
                            break;
                        }
                    if (encoding != null) {
                        System.out.print(path + ": ");
                        raf.seek(start);
                        boolean newLine = false;
                        while (!newLine) { //line
                            cur = raf.getFilePointer();
                            size = raf.read(buff);
                            if (size == -1)
                                break mainWhile;
                            int len = size;
                            for (i = 0; i < size; i++)
                                if (buff[i] == '\n' || buff[i] == '\r') {
                                    len = i;
                                    newLine = true;
                                    start = cur + i + 1;
                                    break;
                                }
                            System.out.print(new String(buff, 0, len, encoding));
                            if (newLine)
                                System.out.println();
                        }
                        resetAllPF(list);
                    }
                } else {
                    resetAllPF(list);
                    start = cur + i + 1;
                }
            }
        }
        resetAllPF(list);
    }

    private static void resetAllPF(ArrayList<PrefixFunction> list) {
        for (PrefixFunction aList : list)
            aList.reset();
    }

    static class PrefixFunction {
        final String encoding;
        private int lastK = 0;
        private byte[] main;
        private int[] p;

        PrefixFunction(byte[] main, String encoding) {
            this.encoding = encoding;
            this.main = main;
            p = new int[main.length];
            p[0] = 0;
            for (int i = 1; i < main.length; i++) {
                int k = p[i - 1];
                while (k > 0 && main[i] != main[k])
                    k = p[k - 1];
                if (main[i] == main[k])
                    k++;
                p[i] = k;
            }
        }

        public boolean checkNextByte(byte b) {
            int k = lastK;
            while (k > 0 && b != main[k])
                k = p[k - 1];
            if (b == main[k])
                k++;
            lastK = k;
            return k == main.length;
        }

        public void reset() {
            lastK = 0;
        }
    }
}
