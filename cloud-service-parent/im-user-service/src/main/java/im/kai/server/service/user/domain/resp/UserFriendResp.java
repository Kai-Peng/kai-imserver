package im.kai.server.service.user.domain.resp;

import com.alibaba.fastjson.annotation.JSONField;
import im.kai.server.service.user.json.StringValueSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 好友列表响应数据主体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFriendResp extends BasicResp {


    @JSONField(name = "id" , serializeUsing = StringValueSerializer.class)
    private Long friendId;

    @JSONField(name = "nick_name")
    private String nickName;

    @JSONField(name = "avatar")
    private String avatar;

    private Integer type ;

    @JSONField(name = "blocked")
    private boolean blocked ;

    @JSONField(name = "real_name")
    private String realName;

    private String mobile;

    private String description;

    private Long created;

    /**
     * 勿扰模式
     *
     * 0=不开启
     *
     * 1=开启
     */
    private byte dnd_mode ;

}
