package im.kai.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * IM 的服务异常
 */
public class IMAplicationException extends RuntimeException {

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    /**
     * HTTP状态码
     */
    private HttpStatus httpStatus;

    /**
     * 实例化异常
     * @param httpStatus 状态
     */
    IMAplicationException(HttpStatus httpStatus){
        super(httpStatus.getReasonPhrase());
        this.httpStatus = httpStatus;
    }
    /**
     * 实例化异常
     * @param httpStatus 状态
     * @param message 状态描述消息
     */
    IMAplicationException(HttpStatus httpStatus, String message){
        super(message);
        this.httpStatus = httpStatus;
    }
    /**
     * 实例化异常
     * @param httpStatus 状态
     * @param message 状态描述消息
     * @param cause 异常
     */
    IMAplicationException(HttpStatus httpStatus, String message, Throwable cause){
        super(message, cause);
        this.httpStatus = httpStatus;
    }
    /**
     * 根据异常实例化
     * @param cause 异常
     */
    IMAplicationException(Throwable cause){
        super(cause);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * 根据异常实例化
     * @param message 异常信息
     * @param cause 异常
     */
    IMAplicationException(String message, Throwable cause){
        super(message, cause);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * 输出异常信息
     * @return
     */
    public ResponseEntity output(){
        return ResponseEntity.status(this.getHttpStatus()).build();
    }
}
