package com.github.savkk.propeller.constants;

public final class ErrorMessages {
    public static final String INVALID_FEEDBACK_IS_DISPLAYED = "Отобразилось предупреждение о неправильности заполнения поля";
    public static final String INVALID_FEEDBACK_IS_NOT_DISPLAYED = "Не отобразилось предупреждение о неправильности заполнения поля";
    public static final String PAGE_IS_NOT_LOADED = "Страница не загрузилась полностью";

    private ErrorMessages() {
        throw new IllegalStateException("Constants class");
    }
}
