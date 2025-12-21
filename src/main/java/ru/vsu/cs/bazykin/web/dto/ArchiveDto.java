package ru.vsu.cs.bazykin.web.dto;

import java.util.List;

public class ArchiveDto {
    public String id;
    public String archivePath;
    public List<String> content;

    public ArchiveDto() {}
    public ArchiveDto(String id, String archivePath, List<String> content) {
        this.id = id;
        this.archivePath = archivePath;
        this.content = content;
    }
}