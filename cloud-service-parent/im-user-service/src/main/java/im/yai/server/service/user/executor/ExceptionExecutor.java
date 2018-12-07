package im.kai.server.service.user.executor;


/**
 * 异常捕捉器，主要代码再execute 中进行，如果有异常，由exceptionResult 返回的异常，直接抛出。
 * @param <T>
 * @param <Param>
 */
public interface ExceptionExecutor<T , Param> {

    /***
     * 正常情况返回的数据
     * @param p
     * @return
     */
    public T execute (Param p) throws Exception;

    /**
     * 异常返回的数据
     * @return ApiResult instance
     */
    public Exception exceptionResult(Exception originExp) ;

}
