package ru.itpark.sb.service;

import ru.itpark.sb.domain.Document;
import ru.itpark.sb.repository.DocumentRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public class DocumentService {
    private final DocumentRepository documentRepository;
    private final FileStorageService fileStorageService;

    public DocumentService(DocumentRepository documentRepository, FileStorageService fileStorageService) {
        this.documentRepository = documentRepository;
        this.fileStorageService = fileStorageService;
    }

    public Document saveDocument(String name, String content, String password) {
        UUID id = UUID.randomUUID();

        Document doc = new Document(id, name, content.getBytes(), password);
        documentRepository.save(doc);
        fileStorageService.saveDocumentContent(id.toString(), content.getBytes(StandardCharsets.UTF_8));

        return doc;
    }

    public Document findById(UUID id) {
        return documentRepository.findById(id);
    }

    public boolean canReadFile(String password, String passwordHash) {
        return password.equals(passwordHash);
    }

    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    public List<Document> searchByName(String name) {
        return documentRepository.searchByName(name);
    }

    public int statistics() {
        return documentRepository.size();
    }
}
