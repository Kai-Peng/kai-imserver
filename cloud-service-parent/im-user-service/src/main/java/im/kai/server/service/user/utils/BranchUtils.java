package im.kai.server.service.user.utils;

import im.kai.server.domain.ApiResult;
import im.kai.server.service.user.executor.CommonExecutor;
import im.kai.server.service.user.executor.ConditionExecutor;

public class BranchUtils {

    /**
     * 流程控制，执行代码块
     * @param ifTrue
     * @param commonExecutor 可选的公共代码块，会被传递到两个流程当中
     * @param ifTrueExecutor ifTrue 条件为真，那么就执行该ConditionExecutor
     * @param elseExecutor ifTrue 条件为为加，那么就执行ConditionExecutor
     * @return ApiResult
     * @throws Exception
     */
    public static ApiResult execute(boolean ifTrue , CommonExecutor commonExecutor , ConditionExecutor ifTrueExecutor ,
                                    ConditionExecutor elseExecutor) throws Exception {

        if(ifTrue) {
            return (ApiResult) ifTrueExecutor.execute(commonExecutor);
        } else {
            return (ApiResult) elseExecutor.execute(commonExecutor);
        }


    }
}
