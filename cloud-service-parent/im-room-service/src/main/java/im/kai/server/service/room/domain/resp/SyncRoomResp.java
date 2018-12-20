package im.kai.server.service.room.domain.resp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @AUTHER Pengp
 * @DATE 2018/11/9 17:13
 */
@Data
@NoArgsConstructor
public class SyncRoomResp {
    private String id;
    @NotNull
    private String    name;               //群名称
    @NotNull
    private String    icon;               //群图标
    @JSONField(name = "total_user")
    private int       totalUser;          //群人数 限制最少两人
}
