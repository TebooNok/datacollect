package com.pginfo.datacollect.util;

/**
 * 常量
 */
public interface Constants {

    /**
     * 统一时间格式
     */
    public static final String TIME_FORMAT="yyyy-MM-dd HH:mm:ss";

    /**
     * 统一时间格式
     */
    public static final String TIME_IGNORE_SECONDS="yyyy-MM-dd HH:mm";

    /**
     * 过期时间
     */
    public static final int DATE_EXPIRE = 30;

    /**
     * 传感器数量
     */
    public static final int DEVICE_NUM = 42;

    /**
     * cacheMap_queue缓存大小
     */
    public static final int CACHE_QUEUE_SIZE = 3600;

    public static final String NOT_USE_STRING = "0";

    public static final String TEMPLATE_HOURS = "Hours";

    public static final String TEMPLATE_DAY = "Day";

    public static final String TEMPLATE_HALFDAY = "HalfDay";

    public static final String TEMPLATE_WEEK = "Week";

    public static final String TEMPLATE_MONTH = "Month";

    public static final String TEMPLATE_YEAR = "Year";

    public static final long DEFAULT_ALARMTHRE_LV1 = 25;

    public static final long DEFAULT_ALARMTHRE_LV2 = 15;

    public static final long DEFAULT_ALARMTHRE_LV3 = 5;
}
