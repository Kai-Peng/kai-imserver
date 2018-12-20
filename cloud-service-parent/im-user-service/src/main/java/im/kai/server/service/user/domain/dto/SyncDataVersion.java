package im.kai.server.service.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncDataVersion {
    private Long userId;

    private Long friendsVersion;

    private Long settingsVersion;

    private Long roomsVersion ;

}