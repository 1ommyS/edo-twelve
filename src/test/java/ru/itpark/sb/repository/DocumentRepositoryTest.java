package ru.itpark.sb.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.itpark.sb.domain.Document;
import ru.itpark.sb.service.MetadataService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DocumentRepositoryTest {

    private final DocumentRepository documentRepository;

    //    @Mock
    public MetadataService metadataService = mock(MetadataService.class);

    public DocumentRepositoryTest() {
        when(metadataService.loadDocuments()).thenReturn(new HashMap<UUID, Document>() {{
            var firstId = UUID.randomUUID();
            put(firstId, new Document(firstId, "Title1", new byte[1], "passHash", LocalDateTime.now(), LocalDateTime.now()));
            var secondId = UUID.randomUUID();
            put(secondId, new Document(secondId, "Title2", new byte[1], "passHash", LocalDateTime.now(), LocalDateTime.now()));
        }});

        this.documentRepository = new DocumentRepository(
                metadataService
        );
    }

    @Test
    public void isDocumentsSavedCorrectly() {
        assertEquals(2, documentRepository.findAll().size(), "Documents size should equals 2");
    }

    private void insertDoc() {
        documentRepository.save(new Document(
                UUID.randomUUID(),
                "test title",
                new byte[1],
                "passssHash",
                LocalDateTime.now(),
                LocalDateTime.now()
        ));
    }

    @Test
    @DisplayName("Проверяем, что документ корректно сохраняется в БД и записывается в метадата")
    public void saveDocumentTest() {
        insertDoc();

        assertEquals(3, documentRepository.findAll().size());

        verify(metadataService, times(1)).saveMetadata(any());
    }

    @Test
    /*
    AAA
    Arrange - присвоить
    Act - сделать
    Assert - сравнить
     */
    public void isSearchByNameWorkCorrectly() {
        insertDoc();

        List<Document> docs = documentRepository.searchByName("tit");

        assertEquals(3, docs.size(), "Documents size should be 3");
    }
}
