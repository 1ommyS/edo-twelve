package ru.itpark.sb.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

//@ToString
//@EqualsAndHashCode
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@RequiredArgsConstructor

@Data
@AllArgsConstructor
public class Document {
    private UUID id;

    private String title;

    private byte[] encryptedContent;

    private String passwordHash;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
