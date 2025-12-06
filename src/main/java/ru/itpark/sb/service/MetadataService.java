package ru.itpark.sb.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itpark.sb.config.JacksonConfiguration;
import ru.itpark.sb.domain.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MetadataService {
    private final ObjectMapper mapper;
    private final FileStorageService fileStorageService;

    public MetadataService(FileStorageService fileStorageService) {
        mapper = JacksonConfiguration.initJackson();
        this.fileStorageService = fileStorageService;
    }

    public void saveMetadata(Map<UUID, Document> metadata) {
        String json;
        try {
            json = mapper.writeValueAsString(prepareData(metadata));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        fileStorageService.saveMetadata(json);
    }

    public Map<UUID, Document> loadDocuments() {
        String metadataJson = fileStorageService.readMetadata();
        if (metadataJson == null || metadataJson.isEmpty()) {
            return new HashMap<>();
        }
        try {
            Map<String, Document> map = mapper.readValue(metadataJson, new TypeReference<Map<String, Document>>() {});

            for (Map.Entry<String, Document> entry : map.entrySet()) {
                byte[] encryptedContent = fileStorageService
                        .readDocumentContent(entry.getKey().toString());

                entry.getValue().setEncryptedContent(encryptedContent);
            }

            Map<UUID, Document> result = new HashMap<>();
            for (Map.Entry<String, Document> entry : map.entrySet()) {
                result.put(UUID.fromString(entry.getKey()), entry.getValue());
            }

            return result;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private Map<UUID, Document> prepareData(Map<UUID, Document> metadata) {
        var copy = new HashMap<UUID, Document>();

        for (Map.Entry<UUID, Document> entry : metadata.entrySet()) {
            Document copyDocument = entry.getValue().clone();

            copyDocument.setEncryptedContent(new byte[0]);

            copy.put(entry.getKey(), copyDocument);
        }

        return copy;
    }
}
