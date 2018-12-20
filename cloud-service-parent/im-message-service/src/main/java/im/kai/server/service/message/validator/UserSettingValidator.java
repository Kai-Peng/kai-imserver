package im.kai.server.service.message.validator;

import im.kai.server.service.message.validator.AbstractValidator;

public class UserSettingValidator extends AbstractValidator {
    @Override
    protected boolean validate() {
        //todo:接收方状态判断1.接收方是否已经屏蔽发送方；2.接收方是否开启好友验证...
        return true;
    }
}
