package ru.itpark.sb.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.itpark.sb.domain.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MetadataServiceTest {
    private final FileStorageService fss = mock(FileStorageService.class);

    private final String JSON = """
            {
                  "5d59ee2f-a513-407e-b983-91e664c62ac8": {
                    "id": "5d59ee2f-a513-407e-b983-91e664c62ac8",
                    "title": "Title1",
                    "encryptedContent": "",
                    "passwordHash": "passHash",
                    "createdAt": "2025-12-14T16:29:47.000000047",
                    "modifiedAt": "2025-12-14T16:29:47.000000047"
                  },
                  "050fd9a8-ea79-4b33-ab78-45c095e645cb": {
                    "id": "050fd9a8-ea79-4b33-ab78-45c095e645cb",
                    "title": "Title2",
                    "encryptedContent": "",
                    "passwordHash": "passHash",
                    "createdAt": "2025-12-14T16:29:47.000000047",
                    "modifiedAt": "2025-12-14T16:29:47.000000047"
                  }
                }
            """;

    private final MetadataService metadataService = new MetadataService(
            fss
    );

    private final LocalDateTime START = LocalDateTime.of(2025, 12, 14, 16, 29, 47, 47);
    private final LocalDateTime UPDATED = LocalDateTime.of(2025, 12, 14, 16, 29, 47, 47);

    private final Map<UUID, Document> METADATA = new HashMap<>() {{
        var firstId = UUID.fromString("5d59ee2f-a513-407e-b983-91e664c62ac8");
        put(firstId, new Document(firstId, "Title1", null, "passHash", START, UPDATED));
        var secondId = UUID.fromString("050fd9a8-ea79-4b33-ab78-45c095e645cb");
        put(secondId, new Document(secondId, "Title2", null, "passHash", START, UPDATED));
    }};


    @Test
    public void localDocuments() {
        when(fss.readMetadata()).thenReturn(JSON);

        var result = metadataService.loadDocuments();

        Assertions.assertEquals(METADATA, result);
    }
}
