package im.kai.server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

/**
 * 返回服务器出错的异常，HTTP响应码：500
 *
 */
@Slf4j
public class ServerErrorException extends IMAplicationException {
    /**
     * 实例化异常
     * @param throwable
     */
    public ServerErrorException(Throwable throwable) {
        super(throwable);
    }
    /**
     * 实例化异常
     * @param message
     * @param throwable
     */
    public ServerErrorException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * 输出异常信息
     * @return
     */
    @Override
    public ResponseEntity output() {
        log.error(String.format("发生系统错误: %s", this.getMessage()), this.getCause());
        return super.output();
    }
}
