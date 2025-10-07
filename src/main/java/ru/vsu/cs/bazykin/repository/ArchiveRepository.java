package ru.vsu.cs.bazykin.repository;

import ru.vsu.cs.bazykin.model.Archive;
import ru.vsu.cs.bazykin.service.FileEditor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArchiveRepository {
    private final String filename = "ArchiveData.ser";
    private Map<String, Archive> archiveMap = new HashMap<>();

    public void updateArchiveMap(){
        archiveMap = FileEditor.loadArchiveMap(filename);
    }

    private void saveToFile() {
        FileEditor.saveArchiveMap(archiveMap, filename);
    }

    public void save(Archive archive) {
        archiveMap.put(archive.getId(), archive);
        FileEditor.addOrUpdateArchive(archive.getId(), archive, filename);
    }

    public Archive findById(String id) {
        return archiveMap.get(id);
    }

    public void deleteById(String id) {
        archiveMap.remove(id);
        FileEditor.removeArchive(id, filename);
    }

    public List<Archive> getAll() {
        return archiveMap.values().stream().toList();
    }
}
