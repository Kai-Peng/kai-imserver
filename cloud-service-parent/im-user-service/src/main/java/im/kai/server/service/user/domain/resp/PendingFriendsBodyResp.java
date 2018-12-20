package im.kai.server.service.user.domain.resp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 好友申请列表  响应主体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendingFriendsBodyResp extends BasicResp {

    @JSONField(name="next_start")
    public Long nextStart ;
    @JSONField(name="has_more")
    public boolean hasMore ;

    public List<PendingFriendsResp> friends ;


}
