package im.kai.server.service.user.domain.resp;

import com.alibaba.fastjson.annotation.JSONField;
import im.kai.server.service.user.json.StringValueSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 *  用户搜索返回的响应类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchResp extends BasicResp {

    public Byte gender ;
    public String signature ;

    @JSONField(name = "nick_name")
    public String nickName ;

    @JSONField(name = "user_id" , serializeUsing = StringValueSerializer.class)
    public Long userId ;
    public String avatar ;
    public String area ;

    @JSONField(name = "friend_mode")
    public Byte friendMode ;
}
