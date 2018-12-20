package im.kai.server.service.user.domain.req;

import com.alibaba.fastjson.annotation.JSONField;
import im.kai.server.constant.ApiFailCode;
import im.kai.server.service.user.constraint.annotation.ByteSize;
import im.kai.server.service.user.constraint.annotation.NotBlank;
import im.kai.server.service.user.constraint.annotation.XNotNull;
import im.kai.server.service.user.json.StringToLongDeSerializer;
import im.kai.server.service.user.json.StringValueSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static im.kai.server.constant.ApiFailCode.UserManage.EMPTY_VERIFY_MESSAGE;

/**
 * 接收好友验证信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVerifyMessageReq {

    @JSONField(name = "user_id" , deserializeUsing = StringToLongDeSerializer.class ,
            serializeUsing = StringValueSerializer.class)
    @XNotNull(code = ApiFailCode.UserManage.UNKNOWN_USER_ID)
    private Long userId ;

    @JSONField(name = "message")
    @NotBlank(code=EMPTY_VERIFY_MESSAGE)
    @ByteSize(max = 250 , code = ApiFailCode.DATA_TOO_LONG)
    public String message ;

}
