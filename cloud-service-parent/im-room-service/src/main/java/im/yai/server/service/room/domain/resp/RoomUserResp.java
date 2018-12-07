package im.kai.server.service.room.domain.resp;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import im.kai.server.service.room.domain.resp.base.BaseResp;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @AUTHER Pengp
 * @DATE 2018/11/9 15:35
 */
@Data
@NoArgsConstructor
public class RoomUserResp extends BaseResp {

    private long    inviter;  //邀请人
    private boolean blocked;  //是否屏蔽
    private long    created;
    @JSONField(name = "user_id",serializeUsing= ToStringSerializer.class)
    private long    userId;
    @JSONField(name = "room_id",serializeUsing= ToStringSerializer.class)
    private long    roomId;
}
