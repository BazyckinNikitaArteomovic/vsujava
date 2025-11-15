package ru.vsu.cs.bazykin.repository;

import ru.vsu.cs.bazykin.db.H2ConnectionManager;
import ru.vsu.cs.bazykin.model.Archive;
import ru.vsu.cs.bazykin.service.FileEditor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArchiveRepository {
    public void updateArchiveMap() {
        // Not needed anymore - data is in DB
    }

    private void saveToFile() {
        // Removed - now using DB
    }

    public void save(Archive archive) {
        String sql = "MERGE INTO archives (id, archive_path) VALUES (?, ?)";
        try (Connection conn = H2ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, archive.getId());
            stmt.setString(2, archive.getArchivePath());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save archive", e);
        }
    }

    public Archive findById(String id) {
        String sql = "SELECT * FROM archives WHERE id = ?";
        try (Connection conn = H2ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                List<String> content = FileEditor.readListFromFile(rs.getString("archive_path"));
                return new Archive(
                        rs.getString("id"),
                        content != null ? content : new ArrayList<>(),
                        rs.getString("archive_path")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find archive by id", e);
        }
        return null;
    }

    public void deleteById(String id) {
        String sql = "DELETE FROM archives WHERE id = ?";
        try (Connection conn = H2ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete archive", e);
        }
    }

    public List<Archive> getAll() {
        List<Archive> archives = new ArrayList<>();
        String sql = "SELECT * FROM archives";
        try (Connection conn = H2ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                List<String> content = FileEditor.readListFromFile(rs.getString("archive_path"));
                archives.add(new Archive(
                        rs.getString("id"),
                        content != null ? content : new ArrayList<>(),
                        rs.getString("archive_path")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all archives", e);
        }
        return archives;
    }
}