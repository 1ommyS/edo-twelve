package ru.itpark.sb.service;

import org.junit.jupiter.api.Test;
import ru.itpark.sb.domain.Document;
import ru.itpark.sb.repository.DocumentRepository;
import ru.itpark.sb.repository.exception.DocumentNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DocumentServiceTest {
    private final DocumentRepository documentRepository = mock(DocumentRepository.class);
    private final FileStorageService fileStorageService = mock(FileStorageService.class);
    private final EncryptionService encryptionService = mock(EncryptionService.class);

    private final DocumentService documentService = new DocumentService(documentRepository, fileStorageService, encryptionService);

    @Test
    public void shouldReturnDocumentByIdIfDocExist() {
        var SAVED_DOC = new Document(UUID.randomUUID(),
                "Title1",
                new byte[1],
                "passHash",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(documentRepository.findById(any()))
                .thenReturn(Optional.of(SAVED_DOC));

        var doc = documentService.findById(UUID.randomUUID());

        assertEquals(SAVED_DOC, doc);
    }

    @Test
    public void shouldThrowExceptionIfDocNull() {
        when(documentRepository.findById(any()))
                .thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(DocumentNotFoundException.class, () -> {
            documentService.findById(UUID.randomUUID());
        });

        assertEquals(exception.getMessage(), "Document not found exception");


    }
}
