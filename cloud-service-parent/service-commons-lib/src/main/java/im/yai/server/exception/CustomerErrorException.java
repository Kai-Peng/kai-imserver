package im.kai.server.exception;

import im.kai.server.domain.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 返回自定义错误异常。
 * HTTP响应码：200，返回json {"code": xxx}
 */
@Slf4j
public class CustomerErrorException extends IMAplicationException {
    /**
     * 状态码
     * @return
     */
    public int getCode() {
        return code;
    }

    private int code;

    /**
     * 根据返回码及描述信息实例化
     * @param code 返回码
     * @param message 描述信息
     */
    public CustomerErrorException(int code, String message) {
        super(HttpStatus.OK, message);
        this.code = code;
    }

    /**
     * 根据返回码、描述信息、异常实例化
     * @param code 返回码
     * @param message 描述信息
     * @param cause 异常
     */
    public CustomerErrorException(int code, String message, Throwable cause) {
        super(HttpStatus.OK, message, cause);
        this.code = code ;
    }

    /**
     * 输出异常信息
     * @return
     */
    @Override
    public ResponseEntity output() {
        if(this.getCause() != null){
            log.error(String.format("发生用户错误(%s): %s", this.getCode(), this.getMessage()), this.getCause());
        }else if(log.isDebugEnabled()){
            log.debug(String.format("发生用户错误(%s): %s", this.getCode(), this.getMessage()));
        }
        return ResponseEntity.ok(ApiResult.fail(this.getCode()));
    }
}
