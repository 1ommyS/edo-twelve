package ru.itpark.sb.repository.exception;

import java.util.UUID;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(UUID id) {
        super(
                String.format("Document with id %s not found exception", id.toString())
        );
    }

    public DocumentNotFoundException() {
        super(
                "Document not found exception"
        );
    }
}
