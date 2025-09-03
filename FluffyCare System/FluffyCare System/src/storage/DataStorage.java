package storage;

import java.io.*;

public class DataStorage {
    private static final String FILE_NAME = "petcare_data.ser";

    public static void save(Object obj) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return ois.readObject();
        } catch (Exception e) {
            return null; // no file yet or incompatible
        }
    }
}