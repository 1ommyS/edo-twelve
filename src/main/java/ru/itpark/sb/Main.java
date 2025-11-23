package ru.itpark.sb;

import lombok.Data;
import ru.itpark.sb.domain.Document;
import ru.itpark.sb.repository.DocumentRepository;
import ru.itpark.sb.service.DocumentService;
import ru.itpark.sb.service.EncryptionService;
import ru.itpark.sb.service.FileStorageService;
import ru.itpark.sb.ui.ConsoleUI;

import java.nio.charset.StandardCharsets;


public class Main {
    private static final String STORAGE_DIRECTORY = "documents_storage";

    public static void main(String[] args) {
        EncryptionService encryptionService = new EncryptionService();

        var result = encryptionService.encrypt("dsasaddas".getBytes(StandardCharsets.UTF_8), encryptionService.hashPassword("123"));
        System.out.println(new String(result));
        System.out.println(new String(
                encryptionService.decrypt(result, encryptionService.hashPassword("123"))
        ));

//        Document.class.getDeclaredAnnotation();

        DocumentRepository documentRepository = new DocumentRepository();
        FileStorageService fileStorageService = new FileStorageService(STORAGE_DIRECTORY);
        DocumentService documentService = new DocumentService(documentRepository, fileStorageService, encryptionService);
        ConsoleUI consoleUI = new ConsoleUI(documentService);

        consoleUI.start();
    }
}