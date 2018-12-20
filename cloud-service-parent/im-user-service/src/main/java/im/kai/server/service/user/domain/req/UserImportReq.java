package im.kai.server.service.user.domain.req;

import im.kai.server.service.user.constraint.annotation.XNotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 批量导入好友接收主体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserImportReq extends BasicReq {

    @XNotNull
    public String friends ;

}
