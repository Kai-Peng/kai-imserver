package im.kai.server.service.user.domain.req;

import com.alibaba.fastjson.annotation.JSONField;
import im.kai.server.service.user.constraint.annotation.IntValueEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static im.kai.server.constant.ApiFailCode.UserManage.FRIEND_MODE_INVALID;

/**
 * @AUTHER Pengp
 * @DATE 2018/11/12 17:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileReq {

    private Byte     gender ;
    @JSONField(name="nick_name")
    private String  nickName;
    private String  signature;
    private String  avatar;
    private String  area;

    /**
     * 添加好友模式
     *
     * 1 = 任何人可以添加为好友
     *
     * 2 = 添加好友需要验证
     *
     * */
    @JSONField(name="friend_mode")
    @IntValueEnum(set={1 , 2} , code = FRIEND_MODE_INVALID)
    private Byte friendMode ;
}
