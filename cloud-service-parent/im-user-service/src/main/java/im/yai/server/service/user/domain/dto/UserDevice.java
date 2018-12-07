package im.kai.server.service.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDevice {

    private Long id;

    private Long userId;

    private Byte type;

    private String name;

    private String version;

    private String lang;

    private String gcmId;

    private String apnId;

    private String os;

    private String imei;

    private String uuid;

    private Long created;

    private Long lastSeen;

    public String code ;

    public String subtype ;


}