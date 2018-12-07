package im.kai.server.service.push.service.imp;

import im.kai.server.service.push.domain.NoticeMessage;
import im.kai.server.service.push.service.PushService;
import im.kai.server.service.push.util.SenderUtil;
import javapns.devices.Devices;
import javapns.notification.PushedNotifications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @AUTHER Pengp
 * @DATE 2018/11/21 18:47
 */
@Slf4j
@Service
public class PushServiceImp implements PushService {


    @Override
    public void pushToApns(NoticeMessage noticeMessage) {
        PushedNotifications pushedNotifications = null;
        try {
            pushedNotifications = SenderUtil.pushPayLoadByThread(
                    Devices.asDevices(noticeMessage.getToken()),
                    noticeMessage.getNoticeMessage(),
                    noticeMessage.getBadge(),
                    noticeMessage.getSound(),
                    noticeMessage.getCustomDictionary(),
                    false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (pushedNotifications != null &&
                pushedNotifications.size() > 0){

        }
        log.info(Thread.currentThread().getName() + "push apns:" + noticeMessage);
    }

}
