package im.kai.server.service.user.domain.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 批量判断好友是否注册返回的列表  单项
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterItemResp extends BasicResp {

    public String number ;
    public boolean register ;
}
