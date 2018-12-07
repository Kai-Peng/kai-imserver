package im.kai.server.domain;

/**
 * API的响应结果数据
 */
public class ApiResult<T> {


    public ApiResult() {}

    /**
     * 响应码
     * @return
     */
    public int getCode() {
        return code;
    }

    /**
     * 结果数据
     * @return
     */
    public T getData() {
        return data;
    }

    private int code;
    private T data;

    /**
     * 实例化
     * @param code
     * @param data
     */
    private ApiResult(int code, T data) {
        this.code = code;
        this.data = data;
    }

    /**
     * 成功码
     */
    private static final int SUCCESS_CODE = 0;

    //成功的结果
    private static final ApiResult successResult = new ApiResult<String>(SUCCESS_CODE, null);

    /**
     * 成功处理的返回结果
     * @return
     */
    public static ApiResult success(){
        return successResult;
    }
    /**
     * 成功处理的返回结果
     * @param data 结果数据
     * @return
     */
    public static <T> ApiResult success(T data){
        return new ApiResult<T>(SUCCESS_CODE, data);
    }

    /**
     * 返回失败的结果
     * @param code 错误码
     * @return
     */
    public static ApiResult fail(int code){
        return new ApiResult<String>(code, null);
    }
    /**
     * 返回失败的结果
     * @param code 错误码
     * @param data 结果数据
     * @return
     */
    public static <T> ApiResult fail(int code, T data){
        return new ApiResult<T>(code, data);
    }
}
