package im.kai.server.service.user.domain.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 批量导入手机通讯录接收 类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserImportItem extends BasicReq {

    public String number ;
    public String name ;

}
