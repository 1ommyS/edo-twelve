package ru.itpark.sb.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileStorageService {
    private final Path storageDirectory;
    private final Path contentDirectory;

    public FileStorageService(String baseDirectory) {
        storageDirectory = Paths.get(baseDirectory);
        contentDirectory = storageDirectory.resolve("content");

        initDirectories();
    }

    private void initDirectories() {
        try {
            Files.createDirectories(storageDirectory);
            Files.createDirectories(contentDirectory);
        } catch (IOException e) {
            System.err.println("Ошибка при создании директории: " + e.getMessage());
        }
    }

    public void saveDocumentContent(String documentId, byte[] content) {
        try {
            Path contentFile = contentDirectory.resolve(documentId + ".enc");
            Files.write(contentFile, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    public void saveMetadata(String json) {
        try {
            Path metadataFile = storageDirectory.resolve("metadata.json");
            Files.write(metadataFile, json.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readMetadata() {
        try {
            Path metadataFile = storageDirectory.resolve("metadata.json");
            return Files.readString(metadataFile);
        } catch (IOException e) {
            return null;
        }
    }

    public byte[] readDocumentContent(String documentId) {
        try {
            Path contentFile = contentDirectory.resolve(documentId + ".enc");
            var bytes = Files.readAllBytes(contentFile);
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}