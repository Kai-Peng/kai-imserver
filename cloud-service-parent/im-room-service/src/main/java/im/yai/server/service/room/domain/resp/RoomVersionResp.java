package im.kai.server.service.room.domain.resp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @AUTHER Pengp
 * @DATE 2018/11/9 14:22
 */
@Data
@NoArgsConstructor
public class RoomVersionResp  {

    @JSONField(name = "version")
    long version = 0;               //群组版本
    @JSONField(name = "rooms")
    private List<SyncRoomResp> roomResp; //返回同步群组信息

}
