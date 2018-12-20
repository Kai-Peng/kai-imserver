package im.kai.server.service.user.utils;

import im.kai.server.exception.CustomerErrorException;
import im.kai.server.service.user.executor.ExceptionExecutor;


/**
 * 拦断执行工具类
 */
public class AssertUtils {

    /**
     * 如果第一个参数为真，抛出指定的异常
     * @param ifTrueToThrow
     * @param exp
     * @throws Exception
     */
    public static void assertTo(boolean ifTrueToThrow , Exception exp) throws Exception {
        if(ifTrueToThrow) {
            throw exp ;
        }
    }
    /**
     * 如果第一个参数是为NULL，抛出指定的异常
     * @param checkNullToThrow
     * @param exp
     * @throws Exception
     */
    public static void assertIfNull(Object checkNullToThrow , Exception exp) throws Exception {

        if(checkNullToThrow == null) {
            throw exp ;
        }
    }
    /**
     * 如果第一个参数是为NULL，抛出CustomerErrorException异常
     * @param ifTrue
     * @param code
     * @param message 抛出的异常信息提示
     * @throws Exception
     */
    public static void assertIfTrue(boolean ifTrue , int code , String message) {

        if(ifTrue) {
            throw new CustomerErrorException(code , message) ;
        }
    }

    /**
     * 如果执行ExceptionExecutor 的execute 出现问题将会抛出exceptionResult 指定的异常
     * @param exceptionExecutor
     * @param param
     * @param <T>
     * @param <P>
     * @return
     */
    public static <T , P> T assertExecutor(ExceptionExecutor<T , P> exceptionExecutor , P param) throws Exception {

           T t = null ;
           try {
               t = exceptionExecutor.execute(param) ;
           } catch (Exception e) {
               throw exceptionExecutor.exceptionResult(e) ;
           }

           //也有可能是非异常的情况导致的null值
           if(t == null) {
               throw exceptionExecutor.exceptionResult(new NullPointerException()) ;
           }
           return t ;
    }
}
