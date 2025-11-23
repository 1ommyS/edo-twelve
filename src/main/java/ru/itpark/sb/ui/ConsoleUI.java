package ru.itpark.sb.ui;

import ru.itpark.sb.domain.Document;
import ru.itpark.sb.repository.exception.DocumentNotFoundException;
import ru.itpark.sb.service.DocumentService;
import ru.itpark.sb.ui.validation.DocumentValidator;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);

    private final DocumentService documentService;

    public ConsoleUI(DocumentService documentService) {
        this.documentService = documentService;
    }

    public void start() {
        System.out.println("=== Система электронного документооборота ===");
        System.out.println("Добро пожаловать!");

        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> createDocument();
                    case "2" -> viewDocument();
                    case "3" -> findAllDocuments();
                    case "4" -> searchDocuments();
                    case "5" -> updateDocument();
                    case "6" -> deleteDocument();
                    case "7" -> changePassword();
                    case "8" -> showStatics();
                    case "0" -> {
                        System.out.println("До свидания!");
                        return;
                    }
                    default -> System.out.println("Неверный выбор. Попробуйте снова.");
                }
            } catch (RuntimeException e) {
                System.err.println("ОШИБКА: " + e.getMessage());
            }
            System.out.println("\n Нажмите Enter для продолжения...");
            scanner.nextLine();
        }
    }


    private void printMenu() {
        System.out.println("\n === Меню ===");
        System.out.println("1. Создать документ");
        System.out.println("2. Просмотреть документ");
        System.out.println("3. Просмотреть список всех документов");
        System.out.println("4. Поиск документов");
        System.out.println("5. Обновить документ");
        System.out.println("6. Удалить документ");
        System.out.println("7. Изменить пароль документа");
        System.out.println("8. Статистика");
        System.out.println("0. Выход");
        System.out.println("Выберите действия: ");
    }

    private void showStatics() {
        System.out.println("\n === Всего документов: ===");
        System.out.println(documentService.statistics());
    }

    private void deleteDocument() {

    }

    private void changePassword() {

    }

    private void updateDocument() {

    }

    private void searchDocuments() {
        System.out.println("\n === Введите название документа, который хотите найти ===");
        String name = scanner.nextLine().trim();

        List<Document> docs = documentService.searchByName(name);

        printDocuments(docs);
    }

    private void findAllDocuments() {
        var allDocuments = documentService.findAll();

        printDocuments(allDocuments);
    }

    private void printDocuments(List<Document> allDocuments) {
        System.out.println("Всего документов: " + allDocuments.size());

        for (Document document : allDocuments) {
            System.out.println(document);
        }
    }

    private void viewDocument() {
        System.out.println("\n Просмотр документа");
        System.out.println("Введите ID документа");
        String id = scanner.nextLine().trim();

        DocumentValidator.baseStringValidate(id);

        UUID uuid = null;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new DocumentNotFoundException();
        }
        final var document = documentService.findById(uuid);

        System.out.println("ID: " + document.getId());
        System.out.println("Название: " + document.getTitle());
        System.out.println("Создан: " + document.getCreatedAt());
        System.out.println("Обновлен: " + document.getModifiedAt());

        System.out.print("Введите пароль для просмотра содержимого: ");
        String password = scanner.nextLine().trim();

        DocumentValidator.baseStringValidate(password);

        if (documentService.canReadFile(password, document.getPasswordHash())) {
            System.out.println("Содержимое: " + documentService.readDocument(document.getEncryptedContent(), document.getPasswordHash()));
        } else {
            System.out.println("Ты че! Пшел нафиг отсюда, у тебя нет прав на чтение этого файла");
        }
    }

    private void createDocument() {
        System.out.println("\n=== Создание документа===");
        System.out.println("Введите названия документа: ");
        String name = scanner.nextLine().trim();

        DocumentValidator.baseStringValidate(name);

        System.out.println("Введите содержимое документа: ");
        String description = scanner.nextLine().trim();

        DocumentValidator.validateDescription(description);

        System.out.println("Введите пароль: ");
        String password = scanner.nextLine().trim();

        DocumentValidator.baseStringValidate(password);

        Document document = documentService.saveDocument(name, description, password);

        System.out.println("ID документа: " + document.getId());
        System.out.println("Название документа: " + document.getTitle());
        System.out.println("Описание документа: " + new String(document.getEncryptedContent()));
    }

}
