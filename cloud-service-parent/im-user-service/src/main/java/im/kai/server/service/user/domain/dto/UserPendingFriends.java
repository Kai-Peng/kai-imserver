package im.kai.server.service.user.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;
import im.kai.server.service.user.json.StringValueSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPendingFriends extends BaseEntity {


    @JSONField(serialize = true , deserialize = false)
    private Long id;

    @JSONField(name="user_id" , serializeUsing = StringValueSerializer.class)
    private Long userId;

    @JSONField(name="friend_id" , serializeUsing = StringValueSerializer.class)

    private Long friendId;
    @NotNull
    private String message;

    @JSONField(name="created" , format="yyyy-MM-dd HH:mm")
    private Long created;

}