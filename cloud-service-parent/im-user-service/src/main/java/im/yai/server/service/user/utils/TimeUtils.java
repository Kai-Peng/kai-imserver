package im.kai.server.service.user.utils;

import org.apache.commons.lang.time.DurationFormatUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 时间工具类
 */
public class TimeUtils {

    /**
     * 默认是yyyy-MM-dd HH:mm
     * @param timestamp 时间戳
     * @return String yyyy-MM-dd HH:mm 格式的字符串
     */
    public static String defaultFormat(long timestamp) {
        return format(timestamp,"yyyy-MM-dd HH:mm");
    }

    /**
     * 以format日期格式化 对时间戳进行转换
     * @param timestamp
     * @param format
     * @return 格式化后的字符串
     */

    public static String format(long timestamp , String format) {

        return DurationFormatUtils.formatDuration(timestamp , format) ;
    }

    /**
     * 获取当前日期
     * @return Date
     */
    public static Date now() {
        return new Date() ;
    }

    /**
     * 获取当前时间戳
     * @return
     */
    public static Long nowTimeStamp() {
        return now().getTime() ;
    }

    public static String getStrTime(Long timestamp) {

        DateFormat format  = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(timestamp);
    }
}
