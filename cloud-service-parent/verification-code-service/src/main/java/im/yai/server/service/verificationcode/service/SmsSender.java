package im.kai.server.service.verificationcode.service;

/**
 * sms短信发送服务
 */
public interface SmsSender {
    /**
     * 发送消息
     * @param body
     * @param phoneNumber
     */
    void send(String body, String phoneNumber);
}
