package im.kai.server.service.user.domain.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 获取好友列表主体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFriendListResp extends BasicResp {

    public Long version ;
    public List<UserFriendResp> friends ;

}
