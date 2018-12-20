package im.kai.server.service.room.domain.dto;

import im.kai.server.service.room.domain.dto.base.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * @AUTHER Pengp
 * @DATE 2018/11/1 19:04
 * 聊天群
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class Room extends BaseBean {


        private String    name;               //群名称
        private String    icon;               //群图标
        private boolean   locked;             //是否被锁定
        private int       totalUser;          //群人数 限制最少两人
        private long      ownerId;            //群主id
        private long      createdBy;          //创建者
        private long      created;            //创建时间

        @Override
        public String toString() {
                return "Room{" +
                        "name='" + name + '\'' +
                        ", icon='" + icon + '\'' +
                        ", locked=" + locked +
                        ", totalUser=" + totalUser +
                        ", ownerId=" + ownerId +
                        ", createdBy=" + createdBy +
                        ", created=" + created +
                        '}';
        }
}
