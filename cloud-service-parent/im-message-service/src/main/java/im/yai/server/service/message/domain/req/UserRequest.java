package im.kai.server.service.message.domain.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 微服务调用时，使用这个实体作为数据载体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    public Long id ;

    //设备ID
    public Long deviceId ;

    public String token ;
}
