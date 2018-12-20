package im.kai.server.service.user.executor;


/***
 * 适配器，用户BranchUtils 流程控制 ， 代码块封装执行
 * @param <Result>
 * @param <Param>
 */
public abstract class DefaultCommonExecutor<Result , Param>  implements CommonExecutor<Result , Param> {

    public CommonExecutor nextExecutor() {
        return null ;
    }
}
