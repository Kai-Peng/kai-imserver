package im.kai.server.service.message.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketMessage;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MqMsg implements Serializable {
    private String userId;
    private String deviceTypeId;
    private byte[] mqMsg;
}
