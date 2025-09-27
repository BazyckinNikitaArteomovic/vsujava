package ru.vsu.cs.bazykin.repository;

import ru.vsu.cs.bazykin.model.Archive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ArchiveRepository {
    private final Map<UUID, Archive> archiveMap = new HashMap<>();

    public void save(Archive archive) {
        archiveMap.put(archive.getId(), archive);
    }

    public Archive findById(UUID id) {
        return archiveMap.get(id);
    }

    public void deleteById(UUID id) {
        archiveMap.remove(id);
    }

    public List<Archive> getAll() {
        return archiveMap.values().stream().toList();
    }
}
