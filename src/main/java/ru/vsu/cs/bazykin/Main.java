package ru.vsu.cs.bazykin;

import ru.vsu.cs.bazykin.repository.ArchiveRepository;
import ru.vsu.cs.bazykin.service.ArchiveService;
import ru.vsu.cs.bazykin.viewer.ArchiveViewer;

public class Main {
    public static void main(String[] args) {
        ArchiveRepository repository = new ArchiveRepository();
        ArchiveService service = new ArchiveService(repository);
        ArchiveViewer viewer = new ArchiveViewer(service);
        viewer.start();
    }
}