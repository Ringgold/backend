package template;

import org.skife.jdbi.v2.DBI;

import java.util.UUID;

public class Constant {
    public static String SUCCESS = "SUCCESS";
    public static String FAIL = "FAIL";
    public static String NULL = "NULL";
    public static String EMPTY = "EMPTY";
    public static String NOTVALID = "NOT VALID";
    public static String MESSAGE_SENT = "Message sent";
    public static Strubg FAIL_MESSAGE = "Message failed";
    public static DBI dbi;

    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
