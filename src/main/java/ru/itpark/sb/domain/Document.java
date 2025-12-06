package ru.itpark.sb.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
public class Document implements Cloneable {
    private UUID id;

    private String title;

    private byte[] encryptedContent;

    private String passwordHash;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @Override
    public Document clone() {
        try {
            return (Document) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("Не смогли клонировать");
            throw new RuntimeException(e);
        }
    }

}
/*
1) копируем нашу мапу объектов
2) убираем в копии поле encryptedContent (делаем его пустым) null
3) записываем на диск

при чтении
1) подгружаем все объекты
2) для каждого объекта идем в папку content и там ищем файл с названием '{document.id}.enc'
3) пробуем прочесть файл. Если его нет, бросаем ошибку - у нас неконсистентноссть данных: в списке объектов есть документ, а на диске - нет
4) читаем файл, результат чтения = encryptedContent. Для этого документа проставляем encryptedContent.
5) записываем все документы в нашу БД
 */
