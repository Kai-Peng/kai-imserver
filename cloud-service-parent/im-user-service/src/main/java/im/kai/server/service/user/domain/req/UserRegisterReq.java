package im.kai.server.service.user.domain.req;

import im.kai.server.service.user.constraint.annotation.XNotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static im.kai.server.constant.ApiFailCode.UserManage.INVALID_NUMBER_LIST;

/**
 * 批量验证手机号是否注册接口  主体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterReq extends BasicReq {

    @XNotNull(code = INVALID_NUMBER_LIST)
    public String friends ;

}
