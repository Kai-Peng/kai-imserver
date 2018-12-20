package im.kai.server.service.room.domain.dto;

import im.kai.server.service.room.domain.dto.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * @AUTHER Pengp
 * @DATE 2018/11/2 16:55
 * 群组-成员
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class RoomUser extends BaseBean {

    private long    inviter;  //邀请人
    private boolean blocked;  //是否屏蔽
    private long    created;
    private long    userId;
    private long    roomId;
    private boolean dnd;    //勿扰模式
}

