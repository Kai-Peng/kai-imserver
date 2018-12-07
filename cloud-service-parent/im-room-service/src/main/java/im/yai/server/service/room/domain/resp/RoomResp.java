package im.kai.server.service.room.domain.resp;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import im.kai.server.service.room.domain.resp.base.BaseResp;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * @AUTHER Pengp
 * @DATE 2018/11/6 17:08
 */
@Data
@NoArgsConstructor
public class RoomResp {

    @NotNull
    private String    name;               //群名称
    @NotNull
    private String    icon;               //群图标
    private boolean   locked = false;     //是否被锁定
    @JSONField(name = "total_user")
    private int       totalUser;          //群人数 限制最少两人
    @JSONField(name = "owner_id",serializeUsing= ToStringSerializer.class)
    private long      ownerId;            //群主id
    @JSONField(name = "created_by",serializeUsing= ToStringSerializer.class)
    private long      createdBy;          //创建者
    private long      created;            //创建时间
    private List<String> members;

}
