package io.gitee.dqcer.mcdull.admin.util.dump.util;

/**
 * Process values to literal value to be suitable to use in "INSERT"
 * SQL statement.
 *
 * @author  Elyar Adil
 * @since   1.0
 */
public class SqlValueUtility {
    /**
     * Escape string for use in SQL statements.
     *
     * @param string {@code String} to be escaped
     * @return escaped {@code String}
     */
    public static String escapeString(String string) {
        return string.replace("\\", "\\\\")
                .replace("\b", "\\b")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                .replace("\\x1A", "\\Z")
                .replace("\\x00", "\\0")
                .replace("'", "\\'")
                .replace("\"", "\\\"");
    }

    /**
     * Convert {@code byte[]} to equivalent {@code String} of hexadecimal value.
     *
     * @param byteArray {@code byte[]} to be converted to hexadecimal value
     * @return {@code String} of hexadecimal value
     */
    public static String bytesToHex(byte[] byteArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : byteArray) {
            stringBuilder.append(String.format("%02X", b));
        }
        return stringBuilder.toString();
    }

}
