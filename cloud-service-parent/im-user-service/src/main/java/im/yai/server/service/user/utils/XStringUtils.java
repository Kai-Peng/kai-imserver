package im.kai.server.service.user.utils;

import org.apache.commons.lang.StringUtils;

/**
 * 字符串操作工具
 */
public class XStringUtils {

    /**
     * 指定的字符串是否全部为null
     * @param args
     * @return 如果全部为null，则返回true， 否则为false
     */
    public static boolean allOfNull(String ...args) {

        for(int i = 0 ; i < args.length ; i ++) {

            if(StringUtils.isBlank(args[i]) ) {
                continue;
            } else {
                return false ;
            }
        }

        return true ;
    }

    public static String toString(Object object) {

        return String.valueOf(object) ;

    }

    /**
     * 获取最终值，如果第一个字符串为null的话
     * @param original
     * @param defaultVal
     * @return 返回字符串
     */
    public static String getString(String original , String defaultVal) {

        if(original == null) {
            return defaultVal ;
        }
        return original ;

    }
}
