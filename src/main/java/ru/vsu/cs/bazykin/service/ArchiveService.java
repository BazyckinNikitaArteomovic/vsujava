package ru.vsu.cs.bazykin.service;

import ru.vsu.cs.bazykin.api.CreateArchiveRequest;
import ru.vsu.cs.bazykin.api.UpdateArchiveRequest;
import ru.vsu.cs.bazykin.model.Archive;
import ru.vsu.cs.bazykin.repository.ArchiveRepository;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArchiveService {
    private final ArchiveRepository repository;

    public ArchiveService(ArchiveRepository repository) {
        this.repository = repository;
    }

    public Archive create(CreateArchiveRequest archiveRequest) {
        if (archiveRequest.getArchivePath() == null || archiveRequest.getArchivePath().isEmpty()) {
            return null;
        }
        String id = UUID.randomUUID().toString();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatted = now.format(formatter);

        List<String> content = new ArrayList<>(List.of(id, formatted));

        if (FileEditor.createArchive(archiveRequest.getArchivePath())){
            FileEditor.writeListToFile(archiveRequest.getArchivePath(), content);
        }


        var archive = new Archive(
                id,
                content,
                archiveRequest.getArchivePath()
        );
        repository.save(archive);
        return archive;
    }

    public Archive update(String id, UpdateArchiveRequest archiveRequest) {
        if (archiveRequest.getFilename() == null || archiveRequest.getFilename().isEmpty() ||
                archiveRequest.getType() == null || archiveRequest.getType().isEmpty()) {
            return null;
        }
        Archive archive = repository.findById(id);
        if (archive == null) {
            return null;
        }
        List<String> modifiedContent = archive.getContent();
        switch (archiveRequest.getType()){
            case "delete":
                if (!modifiedContent.contains(archiveRequest.getFilename())){
                    return null;
                }
                int index = modifiedContent.indexOf(archiveRequest.getFilename());
                modifiedContent.remove(index + 1);
                modifiedContent.remove(index);
                break;
            case "add":
                String fileContent = FileEditor.getFileContent(archiveRequest.getFilename());
                String filename = Path.of(archiveRequest.getFilename()).getFileName().toString();
                modifiedContent.add(filename);
                modifiedContent.add(fileContent);
                break;

        }
        if (!FileEditor.writeListToFile(archive.getArchivePath(), modifiedContent)){
            return null;
        }
        archive.setContent(modifiedContent);

        repository.save(archive);
        return archive;
    }

    public Archive getById(String id){
        return repository.findById(id);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public List<Archive> getAll() {
        return repository.getAll();
    }
}
