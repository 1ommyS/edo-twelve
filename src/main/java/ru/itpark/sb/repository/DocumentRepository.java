package ru.itpark.sb.repository;

import lombok.RequiredArgsConstructor;
import ru.itpark.sb.domain.Document;
import ru.itpark.sb.repository.exception.DocumentNotFoundException;
import ru.itpark.sb.service.MetadataService;

import java.util.*;

public class DocumentRepository {

    /* всегда пишем final, а если понимаем, что надо изменить переменную - убираем final

    1) final class - класс, от корого нельзя наследоваться
    2) final метод - метод, который нельзя переопределить
    3) final поле класса - поле, которое может быть задано либо сразу, либо в конструкторе
    Задавать его в методе и изменять его - нельзя
    4) final переменная - переменная, которая должна быть задана сразу
     */

    private final Map<UUID, Document> documents;

    private final MetadataService metadataService;

    public DocumentRepository(MetadataService metadataService) {
        this.metadataService = metadataService;
        documents = metadataService.loadDocuments();
    }

    public void save(Document document) {
        documents.put(
                document.getId(),
                document
        );
        metadataService.saveMetadata(documents);
    }

    public List<Document> findAll() {
        return new ArrayList<>(documents.values());
    }

    public boolean existsById(UUID id) {
        return documents.containsKey(id);
    }

    public Document findById(UUID id) {
        final Document document = documents.get(id);

        if (document == null) {
            throw new DocumentNotFoundException(id);
        }

        return document;
    }

    public int size() {
        return documents.size();
    }

    public List<Document> searchByName(String name) {
        ArrayList<Document> foundDocs = new ArrayList<>();

        for (var doc : documents.values()) {
            if (doc.getTitle().toLowerCase().contains(name.toLowerCase())) {
                foundDocs.add(doc);
            }
        }

        return foundDocs;
    }

}
