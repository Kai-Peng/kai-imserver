package im.kai.server.service.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOnlineDevice {
    private Long id;

    private Long userId;

    private Byte deviceType;

    private Long deviceId;

    private String authToken;

    private Long lastSeen;


}