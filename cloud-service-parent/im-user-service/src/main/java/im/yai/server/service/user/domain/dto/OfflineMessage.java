package im.kai.server.service.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfflineMessage {
    private Long id;

    private Long userId;

    private Long seq;

    private Long fromUserId;

    private Long fromDeviceId;

    private Long fromRoomId;

    private Long created;

    private Integer type;

    private String content;


}