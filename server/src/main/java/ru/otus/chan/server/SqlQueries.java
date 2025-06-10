package ru.otus.chan.server;

public final class SqlQueries {
    public static final String AUTH_QUERY =
            "SELECT user_name , role  FROM users_chat uc WHERE login=? AND password = ?";
    public static final String REGISTER_QUERY =
            "INSERT INTO users_chat (login, password, user_name, role) VALUES(?, ?, ?, 'USER')";
    public static final String UPDATE_STATUS =
            "UPDATE users SET status=? WHERE user_id=?";

    // TODO выбрал private для запрета на создание экземпляра класса, на сколько данный подход оправдан?
    private SqlQueries() {
    }
}
