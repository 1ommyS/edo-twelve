package ru.itpark.sb.domain;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class Document {
    private UUID id;

    private String title;

    private byte[] encryptedContent;

    private String passwordHash;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public Document(UUID id, String title, byte[] encryptedContent, String passwordHash) {
        this.id = id;
        this.title = title;
        this.encryptedContent = encryptedContent;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    public void setEncryptedContent(byte[] encryptedContent) {
        this.encryptedContent = encryptedContent;
        this.modifiedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public byte[] getEncryptedContent() {
        return encryptedContent;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Document document)) return false;

        return Objects.equals(id, document.id) && Objects.equals(title, document.title) && Arrays.equals(encryptedContent, document.encryptedContent) && Objects.equals(passwordHash, document.passwordHash) && Objects.equals(createdAt, document.createdAt) && Objects.equals(modifiedAt, document.modifiedAt);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(title);
        result = 31 * result + Arrays.hashCode(encryptedContent);
        result = 31 * result + Objects.hashCode(passwordHash);
        result = 31 * result + Objects.hashCode(createdAt);
        result = 31 * result + Objects.hashCode(modifiedAt);
        return result;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", encryptedContent=" + new String(encryptedContent) +
                ", passwordHash='" + passwordHash + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }
}
