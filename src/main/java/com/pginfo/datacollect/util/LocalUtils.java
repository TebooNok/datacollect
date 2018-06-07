package com.pginfo.datacollect.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalUtils {

    public static String convertTimestamp2String(Timestamp timestamp)
    {
        return timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern(Constants.TIME_FORMAT));
    }

    public static String convertLocalDataTime2String(LocalDateTime localDateTime)
    {
        return localDateTime.format(DateTimeFormatter.ofPattern(Constants.TIME_FORMAT));
    }

    public static LocalDateTime convertString2LocalDataTime(String dateTime)
    {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(Constants.TIME_FORMAT));
    }

    /**
     * 输出异常信息
     * @return String
     */
    public static String errorTrackSpace(Exception e) {
        StringBuffer sb = new StringBuffer();
        if (e != null) {
            for (StackTraceElement element : e.getStackTrace()) {
                sb.append("\r\n\t").append(element);
            }
        }
        return sb.length() == 0 ? null : sb.toString();
    }

    public static String formatCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.TIME_FORMAT));
    }

    public static String formatDataTimeIgnoreSec(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(Constants.TIME_IGNORE_SECONDS));
    }

    // 去掉秒，精度保留分钟级
    public static String formatIgnoreSeconds(String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(Constants.TIME_FORMAT))
                    .format(DateTimeFormatter.ofPattern(Constants.TIME_IGNORE_SECONDS));
    }

    // 获取今日 yyyy:MM:dd
    public static String formatCurrentDay() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.TIME_TODAY));
    }
}
