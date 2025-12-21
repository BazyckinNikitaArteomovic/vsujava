package ru.vsu.cs.bazykin.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.vsu.cs.bazykin.api.CreateArchiveRequest;
import ru.vsu.cs.bazykin.api.UpdateArchiveRequest;
import ru.vsu.cs.bazykin.service.ArchiveService;
import ru.vsu.cs.bazykin.web.dto.AddFileRequestDto;
import ru.vsu.cs.bazykin.web.dto.ArchiveDto;
import ru.vsu.cs.bazykin.web.dto.CreateArchiveRequestDto;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/archives/*")
public class ArchiveServlet extends HttpServlet {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            // GET /api/archives — список всех архивов
            listArchives(req, resp);
        } else {
            // GET /api/archives/{id} — содержимое архива
            getArchiveById(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && !pathInfo.startsWith("/")) {
            WebUtils.sendError(resp, 400, "Invalid path");
            return;
        }
        if (pathInfo != null){
            String[] parts = pathInfo.substring(1).split("/", 2);
            if (parts.length >= 2 && parts[1].equals("files")) {
                // POST /api/archives/{id}/files — добавить файл
                addFileToArchive(req, resp, parts[0]);
            }else {
                WebUtils.sendError(resp, 400, "Unknown endpoint");
            }
        }
        else {
            // POST /api/archives — создать архив
            createArchive(req, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || !pathInfo.startsWith("/")) {
            WebUtils.sendError(resp, 400, "Invalid path");
            return;
        }

        String[] parts = pathInfo.substring(1).split("/", 3);
        if (parts.length < 3 || !parts[1].equals("files")) {
            WebUtils.sendError(resp, 400, "Expected format: /{id}/files/{filename}");
            return;
        }

        String id = parts[0];
        String encodedFilename = parts[2];
        String filename = URLDecoder.decode(encodedFilename, StandardCharsets.UTF_8);

        deleteFileFromArchive(req, resp, id, filename);
    }

    private void listArchives(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ArchiveService service = (ArchiveService) getServletContext().getAttribute("archiveService");
        List<ru.vsu.cs.bazykin.model.Archive> archives = service.getAll();
        List<ArchiveDto> dtos = archives.stream()
                .map(a -> new ArchiveDto(a.getId(), a.getArchivePath(), a.getContent()))
                .collect(Collectors.toList());
        WebUtils.sendJson(resp, dtos);
    }

    private void getArchiveById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        String id = pathInfo.substring(1); // убираем первый /

        ArchiveService service = (ArchiveService) getServletContext().getAttribute("archiveService");
        ru.vsu.cs.bazykin.model.Archive archive = service.getById(id);

        if (archive == null) {
            WebUtils.sendError(resp, 404, "Archive not found");
            return;
        }

        WebUtils.sendJson(resp, new ArchiveDto(archive.getId(), archive.getArchivePath(), archive.getContent()));
    }

    private void createArchive(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CreateArchiveRequestDto dto = mapper.readValue(req.getInputStream(), CreateArchiveRequestDto.class);

        if (dto.archivePath == null || dto.archivePath.isEmpty()) {
            WebUtils.sendError(resp, 400, "archivePath is required");
            return;
        }

        ArchiveService service = (ArchiveService) getServletContext().getAttribute("archiveService");
        ru.vsu.cs.bazykin.model.Archive archive = service.create(new CreateArchiveRequest(dto.archivePath));

        if (archive == null) {
            WebUtils.sendError(resp, 500, "Failed to create archive");
            return;
        }

        WebUtils.sendJson(resp, new ArchiveDto(archive.getId(), archive.getArchivePath(), archive.getContent()));
    }

    private void addFileToArchive(HttpServletRequest req, HttpServletResponse resp, String id) throws IOException {
        AddFileRequestDto dto = mapper.readValue(req.getInputStream(), AddFileRequestDto.class);
        if (dto.filePath == null || dto.filePath.isEmpty()) {
            WebUtils.sendError(resp, 400, "filePath is required");
            return;
        }

        ArchiveService service = (ArchiveService) getServletContext().getAttribute("archiveService");
        ru.vsu.cs.bazykin.model.Archive updated = service.update(id, new UpdateArchiveRequest(dto.filePath, "add"));

        if (updated == null) {
            WebUtils.sendError(resp, 400, "Failed to add file");
            return;
        }

        WebUtils.sendJson(resp, new ArchiveDto(updated.getId(), updated.getArchivePath(), updated.getContent()));
    }

    private void deleteFileFromArchive(HttpServletRequest req, HttpServletResponse resp, String id, String filename) throws IOException {
        ArchiveService service = (ArchiveService) getServletContext().getAttribute("archiveService");
        ru.vsu.cs.bazykin.model.Archive updated = service.update(id, new UpdateArchiveRequest(filename, "delete"));

        if (updated == null) {
            WebUtils.sendError(resp, 400, "Failed to delete file");
            return;
        }

        WebUtils.sendJson(resp, new ArchiveDto(updated.getId(), updated.getArchivePath(), updated.getContent()));
    }
}