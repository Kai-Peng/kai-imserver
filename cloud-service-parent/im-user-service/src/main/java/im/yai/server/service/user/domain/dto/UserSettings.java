package im.kai.server.service.user.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSettings {
    private Long id;

    private Long userId;

    private String targetKey;

    private String data;

    private Long version;

    public Long getId() {
        return id;
    }
}