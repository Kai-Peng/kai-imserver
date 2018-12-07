package im.kai.server.service.basic.turn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * webrtc 穿透服务turn会话配置
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TurnToken implements Serializable {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * turn服务列表
     */
    private List<String> hosts;
}
