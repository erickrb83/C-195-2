package Util;

import javafx.scene.control.ComboBox;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Eric Bostard
 *
 * Date and Time Conversions focused in one spot
 */

public class DateTime {
    public static ComboBox<String> populateTimes(ComboBox<String> localTimeComboBox, LocalTime startTime, LocalTime endTime) {
        while(startTime.isBefore(endTime.plusSeconds(1))) {
            localTimeComboBox.getItems().add(String.valueOf(startTime));
            startTime = startTime.plusMinutes(15);
        }
        return localTimeComboBox;
    }

    public static boolean businessHours(LocalDateTime timeToCompare){
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(22, 0);
        ZoneId easternZoneId = ZoneId.of("America/New_York");
        ZonedDateTime easternStart = ZonedDateTime.of(timeToCompare.toLocalDate(), start, easternZoneId);
        ZonedDateTime easternEnd = ZonedDateTime.of(timeToCompare.toLocalDate(), end, easternZoneId);
        LocalDateTime easternTimeToCompare = convertToEST(timeToCompare);

        return easternTimeToCompare.isAfter(ChronoLocalDateTime.from(easternStart)) && easternTimeToCompare.isBefore(ChronoLocalDateTime.from(easternEnd));
    }

    public static LocalDateTime convertToEST(LocalDateTime timeToConvert){
        ZoneId easternTime = ZoneId.of("America/New_York");
        ZoneId localZone = ZoneId.systemDefault();
        ZonedDateTime currentLocalTime = timeToConvert.atZone(localZone);
        ZonedDateTime currentEasternTime = currentLocalTime.withZoneSameInstant(easternTime);

        return currentEasternTime.toLocalDateTime();
    }

    public static String formatToAMPM(LocalDateTime militaryTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");

        return formatter.format(militaryTime);
    }

    /**
     * Overloaded to just LocalTime
     * @param militaryTime
     * @return
     */
    public static String formatToAMPM(LocalTime militaryTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");

        return formatter.format(militaryTime);
    }

    public static String formatDate(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM dd, yyyy");
        return formatter.format(localDateTime);
    }

    /**
     * Convert Date time to UTC for saving to database.
     * @param dateTime
     * @return
     */
    public static String convertToUTC(String dateTime) {
        Timestamp currentTimeStamp = Timestamp.valueOf(String.valueOf(dateTime));
        LocalDateTime LocalDT = currentTimeStamp.toLocalDateTime();
        ZonedDateTime zoneDT = LocalDT.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime utcDT = zoneDT.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime localOUT = utcDT.toLocalDateTime();
        String convertUTC = localOUT.format(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss"));
        return convertUTC;
    }
}
