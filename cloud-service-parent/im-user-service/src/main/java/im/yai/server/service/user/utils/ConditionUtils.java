package im.kai.server.service.user.utils;

import im.kai.server.exception.CustomerErrorException;


/**
 * 条件执行代码模块封装
 *
 */
public class ConditionUtils {

    private ConditionUtils() {}
    private static volatile ConditionUtils condition ;

    /**
     *
     * @param b 如果条件为真，直接抛出异常
     * @param code 返回码
     * @param message 错误信息
     * @return ConditionUtils 用户级联调用
     */
    public ConditionUtils ifTrue(boolean b , int code , String message) {

       if(b) {
           throw new CustomerErrorException(code , message)  ;
       }
       return this ;
    }

    /**
     *
     * @param b 如果条件为真，直接抛出异常
     * @param code 返回码
     * @param message 错误信息
     * @return ConditionUtils 用户级联调用
     */
    public ConditionUtils elseIf(boolean b , int code , String message) {
        if(b) {
            return ifTrue(b , code , message);
        }
        return this ;
    }

    public static ConditionUtils create() {
        if(condition == null) {
            synchronized (ConditionUtils.class) {
                condition = new ConditionUtils() ;
            }
        }
        return condition ;
    }

}