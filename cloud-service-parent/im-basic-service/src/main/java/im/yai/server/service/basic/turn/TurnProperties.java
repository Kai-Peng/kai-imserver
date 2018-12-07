package im.kai.server.service.basic.turn;

import lombok.Data;

import java.util.List;

/**
 * Webrtc turn配置信息
 */
@Data
public class TurnProperties {
    /**
     * 密钥
     */
    private String secret;
    /**
     * 服务列表
     */
    private List<String> hosts;
}
