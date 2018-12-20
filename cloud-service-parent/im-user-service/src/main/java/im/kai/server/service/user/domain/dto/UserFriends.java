package im.kai.server.service.user.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFriends {
    
    private Long id;

    private Long userId;

    private Long friendId;

    private Integer type;

    private Boolean blocked;

    private String realName;

    private String mobile;

    private String description;

    private Long created;

    /**
     * 勿扰模式
     *
     * 0=不开启
     *
     * 1=开启
     */
    private byte dnd_mode ;

}