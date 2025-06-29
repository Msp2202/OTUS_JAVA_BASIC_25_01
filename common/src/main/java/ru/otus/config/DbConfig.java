package ru.otus.config;

public final class DbConfig {
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/chat_db";

    ;
    public static final String DB_USER = "postgres";
    public static final String DB_PASSWORD = "postgres";

    private DbConfig() {
    }
}
