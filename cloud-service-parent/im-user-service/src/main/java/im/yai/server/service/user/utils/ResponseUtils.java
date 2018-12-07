package im.kai.server.service.user.utils;


/**
 * 条件代码块封装
 */
public class ResponseUtils {

    /**
     * 对于满足条件isTrue , 那么返回ifResult , 否则返回elseResult
     * @param ifTrue 是否满足条件
     * @param ifResult 满足条件返回该对象
     * @param elseResult 否则返回该对象
     * @return
     */
    public static <T> T returnFor(boolean ifTrue , T ifResult , T elseResult) {

        return ifTrue ? ifResult : elseResult ;

    }

    /**
     * 如果影响数量大于1 ，那么返回ifResult , 否则返回 elseResult
     * @param count
     * @param ifResult
     * @param elseResult
     * @return ApiResult
     */
    public static <T> T affectedOk(int count , T ifResult , T elseResult) {

        return returnFor(count > 0 , ifResult , elseResult) ;

    }


}
