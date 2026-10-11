
package bai3;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeService {

    private static final DateTimeFormatter DATE_FORMAT =
        DateTimeFormatter.ofPattern("dd MM yyyy");

    private static final DateTimeFormatter TIME_FORMAT =
        DateTimeFormatter.ofPattern("HH mm ss");

    private static final DateTimeFormatter DATETIME_FORMAT =
        DateTimeFormatter.ofPattern("dd MM yyyy HH mm ss");

    public static String process(String request) {

        String command = request.trim().toUpperCase(Locale.ROOT);

        switch (command) {

            case "DATE":
                return "OK DATE "
                    + LocalDate.now().format(DATE_FORMAT);

            case "TIME":
                return "OK TIME "
                    + LocalTime.now().format(TIME_FORMAT);

            case "DATETIME":
                return "OK DATETIME "
                    + LocalDateTime.now().format(DATETIME_FORMAT);

            default:
                return "ERR UNKNOWN_COMMAND";
        }
    }
}
