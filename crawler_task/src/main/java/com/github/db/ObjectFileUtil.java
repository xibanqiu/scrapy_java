package com.github.db;

import java.io.*;

public class ObjectFileUtil {

    public static long readLong(File file) throws IOException {
        if (file.exists() && file.canRead()) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file))) {
                return objectInputStream.readLong();
            }
        }
        return 0;
    }

    public static void writeLong(File file, long id) throws IOException {
        if (!file.exists()) file.createNewFile();

        if (file.exists() && file.canWrite()) {
            try (ObjectOutputStream objectInputStream = new ObjectOutputStream(new FileOutputStream(file))) {
                objectInputStream.writeLong(id);
            }
        }
    }
}
