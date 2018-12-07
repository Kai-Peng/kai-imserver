package im.kai.server.service.user.domain.req;

import com.alibaba.fastjson.annotation.JSONField;
import im.kai.server.constant.ApiFailCode;
import im.kai.server.service.user.constraint.annotation.ByteSize;
import im.kai.server.service.user.constraint.annotation.XNotNull;
import im.kai.server.service.user.json.StringToLongDeSerializer;
import im.kai.server.service.user.json.StringValueSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 设置好友备注信息接收类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMarkReq implements Serializable {

    @JSONField(name = "friend_id" , deserializeUsing = StringToLongDeSerializer.class ,
                                    serializeUsing = StringValueSerializer.class)

    @XNotNull(code = ApiFailCode.UserManage.UNKNOWN_USER_ID)
    private Long friendId ;

    @JSONField(name = "mobile")
    @ByteSize(max = 30 , code = ApiFailCode.DATA_TOO_LONG)
    //@PhoneNum(code = ApiFailCode.INVALID_MOBILE_NUMBER)
    private String mobile ;

    @JSONField(name = "real_name")
    @ByteSize(max = 50 , code = ApiFailCode.DATA_TOO_LONG )
    private String realName ;

    @JSONField(name = "description")
    @ByteSize(max = 200 , code = ApiFailCode.DATA_TOO_LONG )
    private String description ;

}
