package ru.vsu.cs.bazykin.api;

public class CreateArchiveRequest {

    private String archivePath;

    public CreateArchiveRequest(String archivePath) {
        this.archivePath = archivePath;
    }

    public String getArchivePath() {
        return archivePath;
    }
}
