package im.kai.server.service.push.service;

import im.kai.server.service.push.domain.NoticeMessage;

/**
 * @AUTHER Pengp
 * @DATE 2018/11/21 18:40
 */
public interface PushService {


    void pushToApns(NoticeMessage noticeMessage);
}
