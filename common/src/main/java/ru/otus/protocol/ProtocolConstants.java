package ru.otus.protocol;

/**
 * - константы команд ("/auth", "/reg", "/exit", "/authok", и т.д.)
 */
public final class ProtocolConstants {
    public static final String COMMAND_PREFIX = "/";

    ;
    public static final String AUTH = "/auth ";
    public static final String REG = "/reg";
    public static final String EXIT = "/exit";
    public static final String KICK = "/kick";
    public static final String AUTH_OK = "/authok ";
    public static final String REG_OK = "/regok";
    public static final String EXIT_OK = "/exitok";
    public static final String KICK_OK = "/kickok";
    public static final String PRIVATE_MSG = "/w";
    public static final String SHUTDOWN = "/shutdown";
    /**
     * Константы для банов
     */
    public static final String BAN = "/ban ";
    public static final String UNBAN = "/unban ";
    public static final String BAN_OK = "/banok";
    public static final String UNBAN_OK = "/unbanok";
    public static final String BAN_FAILED = "/banfailed";

    private ProtocolConstants() {
    }
}
