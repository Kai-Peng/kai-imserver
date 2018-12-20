package im.kai.server.service.user.domain.resp;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 用户登录成功响应主体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResp extends BasicResp {

    /**
     * 是否为新用户
     *  -true 是
     *  -false 不是
     */
    @JSONField(name="is_new")
    public boolean isNew ;

    /**
     *  用户登录返回的token
     */
    public String token ;

    /**
     * 用户登录所在的设备ID
     */
    @JSONField(name="device_id")
    public String deviceId ;

    /***
     * 加密秘钥，预留
     */
    public String secret ;

}
