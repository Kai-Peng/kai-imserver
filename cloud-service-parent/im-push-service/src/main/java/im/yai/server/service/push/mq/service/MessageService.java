package im.kai.server.service.push.mq.service;
/**
 * @AUTHER Pengp
 * @DATE 2018/11/21 16:40
 * 消息推送
 */
public interface MessageService {
    void sendMsg(Object messageContent, String exchange, String routerKey, final long delayTimes);

    void sendTransMsg(Object messageContent, String exchange, String routerKey);
}