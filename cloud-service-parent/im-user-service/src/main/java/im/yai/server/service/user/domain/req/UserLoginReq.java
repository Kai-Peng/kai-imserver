package im.kai.server.service.user.domain.req;

import com.alibaba.fastjson.annotation.JSONField;
import im.kai.server.constant.ApiFailCode;
import im.kai.server.service.user.constraint.annotation.NotBlank;
import im.kai.server.service.user.constraint.annotation.PhoneNum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static im.kai.server.constant.ApiFailCode.Device.INVALID_DEVICE;
import static im.kai.server.constant.ApiFailCode.VCode.INVALID_CODE;


/**
 * 用户登录
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginReq implements Serializable {


    @JSONField(name = "mobile")
    @PhoneNum(code = ApiFailCode.INVALID_MOBILE_NUMBER)
    private String mobile ;

    @JSONField(name = "verify_code")
    @NotBlank(code = INVALID_CODE)
    private String verifyCode ;

    @JSONField(name = "device")
    @NotBlank(code = INVALID_DEVICE)
    private String device ;

}
