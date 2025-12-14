package ru.itpark.sb;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.itpark.sb.config.JacksonConfiguration;
import ru.itpark.sb.domain.Document;
import ru.itpark.sb.repository.DocumentRepository;
import ru.itpark.sb.service.DocumentService;
import ru.itpark.sb.service.EncryptionService;
import ru.itpark.sb.service.FileStorageService;
import ru.itpark.sb.service.MetadataService;
import ru.itpark.sb.ui.ConsoleUI;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;


public class Main {
    private static final String STORAGE_DIRECTORY = "documents_storage";

    static void test() throws JsonProcessingException {
        var mapper = JacksonConfiguration.initJackson();

        var doc = new Document(UUID.randomUUID(), "title", new byte[10], "qwer", LocalDateTime.now(), LocalDateTime.now());

        String json = mapper.writeValueAsString(doc);
        System.out.println(json);

        var doc1 = mapper.readValue(json, Document.class);
        System.out.println(doc1);

        ArrayList<Integer> ids = new ArrayList<>() {{
            add(1);
            add(2);
            add(3);
            add(4);
            add(null);
        }};

        ids.stream()
                .filter(id -> id.hashCode() > 0)
                .toList();
    }

    public static void main(String[] args) throws JsonProcessingException {
        test();

        EncryptionService encryptionService = new EncryptionService();

        var result = encryptionService.encrypt("dsasaddas".getBytes(StandardCharsets.UTF_8), encryptionService.hashPassword("123"));
        System.out.println(new String(result));
        System.out.println(new String(
                encryptionService.decrypt(result, encryptionService.hashPassword("123"))
        ));

//        Document.class.getDeclaredAnnotation();

        FileStorageService fileStorageService = new FileStorageService(STORAGE_DIRECTORY);
        MetadataService metadataService = new MetadataService(fileStorageService);

        DocumentRepository documentRepository = new DocumentRepository(metadataService);

        DocumentService documentService = new DocumentService(documentRepository, fileStorageService, encryptionService);
        ConsoleUI consoleUI = new ConsoleUI(documentService);

        consoleUI.start();


    }
}