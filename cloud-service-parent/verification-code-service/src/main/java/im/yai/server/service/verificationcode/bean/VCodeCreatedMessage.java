package im.kai.server.service.verificationcode.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 验证码创建的消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VCodeCreatedMessage implements Serializable {
    public enum Mode{
        SMS,
        VOICE
    }
    /**
     * 手机
     */
    private String mobile;
    /**
     * 验证码
     */
    private String vCode;
    /**
     * 类型
     * sms : 短信验证码
     * voice : 语音验证码
     */
    private Mode mode;
}
