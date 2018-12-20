package im.kai.server.exception;

import org.springframework.http.HttpStatus;

/**
 * 返回请求操作过于频繁的异常，HTTP响应码：429
 *
 */
public class TooManyRequestsException extends IMAplicationException {
    /**
     * 实例化异常
     */
    public TooManyRequestsException() {
        super(HttpStatus.TOO_MANY_REQUESTS);
    }

}
