package ru.vsu.cs.bazykin.api;

public class UpdateArchiveRequest {
    private String filename;

    private String type;

    public UpdateArchiveRequest(String filename, String type) {
        this.filename = filename;
        this.type = type;
    }

    public String getFilename() {
        return filename;
    }

    public String getType() {return type;}
}
