package im.kai.server.service.user.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;
import im.kai.server.domain.Base;
import im.kai.server.service.user.json.StringValueSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile extends Base {

    @JSONField(name="user_id" , serializeUsing = StringValueSerializer.class)
    private Long userId;

    @JSONField(name="nick_name")
    private String nickName;

    private Byte gender;

    private String area;

    private String avatar;

    private String signature;

    @JSONField(serialize=false)
    private String password;

    //@JSONField(name="created" , format="yyyy-MM-dd HH:mm")
    private Long created;

   // @JSONField(name="last_updated" , format="yyyy-MM-dd HH:mm")
    private Long lastUpdated;

    /**
     * 添加好友模式
     *
     * 1 = 任何人可以添加为好友
     *
     * 2 = 添加好友需要验证
     *
     * */
    private Byte friendMode ;

}