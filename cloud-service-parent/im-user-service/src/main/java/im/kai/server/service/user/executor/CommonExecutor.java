package im.kai.server.service.user.executor;


/**
 * 用于ConditionUtils ， BranchUtils类的流程控制执行类
 * @param <Result>
 * @param <Param>
 */
public interface CommonExecutor<Result , Param> {

    public Result doExecutor(Param p) throws Exception;

    public CommonExecutor nextExecutor() ;

}
