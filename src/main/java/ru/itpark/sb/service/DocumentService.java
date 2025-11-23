package ru.itpark.sb.service;

import lombok.RequiredArgsConstructor;
import ru.itpark.sb.domain.Document;
import ru.itpark.sb.repository.DocumentRepository;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final FileStorageService fileStorageService;
    private final EncryptionService encryptionService;

    public Document saveDocument(String name, String content, String password) {
        UUID id = UUID.randomUUID();

        var passwordHash = encryptionService.hashPassword(password);

        var encryptedContent = encryptionService.encrypt(content.getBytes(StandardCharsets.UTF_8), passwordHash);
        Document doc = new Document(id, name, encryptedContent, encryptionService.hashPassword(password), LocalDateTime.now(), LocalDateTime.now());
        documentRepository.save(doc);

        fileStorageService.saveDocumentContent(id.toString(), encryptedContent);

        return doc;
    }

    public String readDocument(byte[] encryptedContent, String password) {
        return new String(encryptionService.decrypt(encryptedContent, password), StandardCharsets.UTF_8);
    }

    public Document findById(UUID id) {
        return documentRepository.findById(id);
    }

    public boolean canReadFile(String password, String passwordHash) {
        return encryptionService.verifyPassword(password, passwordHash);
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
