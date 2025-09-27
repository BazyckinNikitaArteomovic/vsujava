package ru.vsu.cs.bazykin.service;

import ru.vsu.cs.bazykin.api.CreateArchiveRequest;
import ru.vsu.cs.bazykin.api.UpdateArchiveRequest;
import ru.vsu.cs.bazykin.model.Archive;
import ru.vsu.cs.bazykin.repository.ArchiveRepository;

import java.util.List;
import java.util.UUID;

public class ArchiveService {
    private final ArchiveRepository repository;

    public ArchiveService(ArchiveRepository repository) {
        this.repository = repository;
    }

    public Archive create(CreateArchiveRequest archiveRequest) {
        if (archiveRequest.getContent() == null || archiveRequest.getContent().isEmpty() ||
                archiveRequest.getName() == null || archiveRequest.getName().isEmpty()) {
            return null;
        }
        var archive = new Archive(
                UUID.randomUUID(),
                archiveRequest.getName(),
                archiveRequest.getContent()
        );
        repository.save(archive);
        return archive;
    }

    public Archive update(UUID id, UpdateArchiveRequest archiveRequest) {
        if (archiveRequest.getContent() == null || archiveRequest.getContent().isEmpty()) {
            return null;
        }
        Archive archive = repository.findById(id);
        if (archive == null) {
            return null;
        }

        archive.setContent(archiveRequest.getContent());

        repository.save(archive);
        return archive;
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public List<Archive> getAll() {
        return repository.getAll();
    }
}
