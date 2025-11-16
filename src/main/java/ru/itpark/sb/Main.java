package ru.itpark.sb;

import ru.itpark.sb.repository.DocumentRepository;
import ru.itpark.sb.service.DocumentService;
import ru.itpark.sb.service.FileStorageService;
import ru.itpark.sb.ui.ConsoleUI;


public class Main {
    private static final String STORAGE_DIRECTORY = "documents_storage";

    public static void main(String[] args) {
        DocumentRepository documentRepository = new DocumentRepository();
        FileStorageService fileStorageService = new FileStorageService(STORAGE_DIRECTORY);
        DocumentService documentService = new DocumentService(documentRepository, fileStorageService);
        ConsoleUI consoleUI = new ConsoleUI(documentService);

        consoleUI.start();
    }
}