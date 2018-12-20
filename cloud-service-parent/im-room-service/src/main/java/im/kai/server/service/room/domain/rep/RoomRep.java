package im.kai.server.service.room.domain.rep;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


/**
 * @AUTHER Pengp
 * @DATE 2018/11/6 17:08
 */
@Data
@NoArgsConstructor
public class RoomRep {

    private String id;
    @NotNull
    private String    name;               //群名称
    @NotNull
    private String    icon;               //群图标
    @NotNull @Size(min = 2)
    private List<String> members;

}
