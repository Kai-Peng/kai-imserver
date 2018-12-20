package im.kai.server.service.user.domain.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 批量验证手机号是否注册接口 单项
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterItem extends BasicReq {

    public String number ;

}
