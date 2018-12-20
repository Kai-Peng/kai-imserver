package im.kai.server.exception;

import org.springframework.http.HttpStatus;

/**
 * 返回服务器出错的异常，HTTP响应码：404
 *
 */
public class NotFoundException extends IMAplicationException {
    /**
     * 实例化异常
     */
    public NotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }

}
