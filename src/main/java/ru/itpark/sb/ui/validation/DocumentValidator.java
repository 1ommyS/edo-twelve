package ru.itpark.sb.ui.validation;

public class DocumentValidator {
    public static void baseStringValidate(String name) {
        if (name.isEmpty() || name.length() < 4) {
            throw new DocumentValidationException("Некорректное значение строки");
        }
    }

    public static void validateDescription(String description) {
        if (description.isEmpty() || description.length() < 4 || description.length() > 255) {
            throw new DocumentValidationException("Некорректный размер описания: Оно должно быть от 5 до 255 символов");
        }
    }


}
