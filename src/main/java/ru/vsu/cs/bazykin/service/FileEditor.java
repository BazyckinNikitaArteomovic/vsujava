package ru.vsu.cs.bazykin.service;

import ru.vsu.cs.bazykin.model.Archive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class FileEditor {
    public static String getFileContent(String filepath){
        try {
            return Files.readString(Path.of(filepath));
        } catch (Exception e) {
            return null;
        }
    }
    public static boolean isValidPath(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return Files.exists(path);
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean createArchive(String archivePath) {
        try {
            Path path = Paths.get(archivePath);

            if (Files.exists(path)) {
                System.out.println("Ошибка: файл уже существует: " + archivePath);
                return false;
            }
            Path parentDir = path.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
            Files.createFile(path);
            System.out.println("Архив успешно создан: " + archivePath);
            return true;
        } catch (Exception e) {
            System.out.println("Ошибка при создании архива");
            return false;
        }
    }

    public static boolean writeListToFile(String filePath, List<String> list) {
        try {
            Path path = Paths.get(filePath);
            Files.write(path, list);
            return true;

        } catch (Exception e) {
            System.out.println("Ошибка при записи в файл");
            return false;
        }
    }

    public static List<String> readListFromFile(String filePath) {
        try {
            Path path = Paths.get(filePath);

            if (!Files.exists(path)) {
                System.out.println("Файл не существует: " + filePath);
                return null;
            }

            return Files.readAllLines(path);

        } catch (Exception e) {
            System.out.println("Ошибка при чтении файла");
            return null;
        }
    }

    public static boolean saveArchiveMap(Map<String, Archive> archiveMap, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(System.getProperty("java.io.tmpdir") + filename))) {

            oos.writeObject(archiveMap);
            return true;

        } catch (Exception e) {
            System.out.println("Ошибка при сохранении HashMap");
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Archive> loadArchiveMap(String filename) {
        File file = new File(System.getProperty("java.io.tmpdir") + filename);
        if (!file.exists()) {
            Map<String, Archive> emptyMap = new HashMap<>();
            saveArchiveMap(emptyMap, filename);
            return emptyMap;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(System.getProperty("java.io.tmpdir") + filename))) {

            Map<String, Archive> archiveMap = (Map<String, Archive>) ois.readObject();
            return archiveMap;

        } catch (Exception e) {
            System.out.println("Ошибка при загрузке HashMap");
            return null;
        }
    }

    public static boolean addOrUpdateArchive(String key, Archive archive, String filename) {
        Map<String, Archive> archiveMap = loadArchiveMap(filename);
        archiveMap.put(key, archive);
        return saveArchiveMap(archiveMap, filename);
    }

    public static boolean removeArchive(String key, String filename) {
        Map<String, Archive> archiveMap = loadArchiveMap(filename);
        if (archiveMap.containsKey(key)) {
            archiveMap.remove(key);
            return saveArchiveMap(archiveMap, filename);
        }
        return false;
    }
}
