package im.kai.server.service.user.domain.resp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;


/**
 * 好友申请列表具体信息   单项
 */
@Data
public class PendingFriendsResp extends BasicResp {

    /**申请日期*/
    public Long created ;
    /**验证信息*/
    public String message ;
    /**用户ID*/
    @JSONField(name="user_id")
    public Long userId ;

    /**用户昵称*/
    @JSONField(name="nick_name")
    public String nickName ;

    /**性别*/
    public Byte gender ;

    /**头像*/
    public String avatar ;

}
