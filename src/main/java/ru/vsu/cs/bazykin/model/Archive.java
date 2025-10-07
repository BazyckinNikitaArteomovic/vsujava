package ru.vsu.cs.bazykin.model;

import java.io.Serializable;
import java.util.List;

public class Archive implements Serializable {
    private String id;
    private List<String> content;
    private String archivePath;

    public Archive(String id, List<String> content, String archivePath) {
        this.id = id;
        this.content = content;
        this.archivePath = archivePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public String getArchivePath() {
        return archivePath;
    }

    public void setArchivePath(String archivePath) {
        this.archivePath = archivePath;
    }
}
