package ru.otus.sqlskripts;

import java.util.Set;

public final class SqlQueries {
    // TODO выбрал private для запрета на создание экземпляра класса, на сколько данный подход оправдан?
    private SqlQueries() {
    }

    public static final String AUTH_QUERY =
            "SELECT user_name , role  FROM users_chat uc WHERE login=? AND password = ?";

    public static final String REGISTER_QUERY =
            "INSERT INTO users_chat (login, password, user_name, role) VALUES(?, ?, ?, 'USER')";

    public static final String UPDATE_STATUS =
            "UPDATE users SET status=? WHERE user_id=?";

    public static final String VALUE_EXISTS_TEMPLATE  =
    "SELECT 1 FROM users_chat WHERE %s = ?";

    public static final String SELECT_USER_ID_BY_USERNAME =
            "SELECT id FROM users_chat WHERE user_name = ?";

    /**
     * Разрешенные колонки
     */
    public static final Set<String> ALLOWED_COLUMNS =
            Set.of("login", "password", "userName"); // TODO на данный момент проверяем только "login", но в перспектиме можно переиср=пользовать и на другие

    public static final String CHECK_BAN_QUERY =
            "SELECT banned_until, reason FROM bans WHERE user_id = ? AND (banned_until > CURRENT_TIMESTAMP OR is_permanent = TRUE)";
    public static final String ADD_BAN_QUERY =
            "INSERT INTO bans(user_id, banned_until, reason, banned_by) VALUES (?, ?, ?, ?)";
    public static final String REMOVE_BAN_QUERY =
            "DELETE FROM bans WHERE user_id = ?";
    public static final String GET_USER_ID_QUERY =
            "SELECT id FROM users_chat WHERE login = ?";
}
