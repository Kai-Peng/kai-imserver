package im.kai.server.service.user.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;
import im.kai.server.service.user.json.StringValueSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @JSONField(name="user_id" , serializeUsing = StringValueSerializer.class)
    private Long id;

    @JSONField(name="im_number")
    private String imNumber;

    @JSONField(name="mobile")
    private String mobile;


}