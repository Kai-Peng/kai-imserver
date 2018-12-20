package im.kai.server.api.room;

import feign.Param;
import feign.RequestLine;
import im.kai.server.domain.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;

/**
 * @AUTHER Pengp
 * @DATE 2018/11/23 10:00
 */
@Service
@FeignClient(name= "im-room-service")//,fallback = RoomServiceHystrix.class)
public interface RoomService {
    /**
     * 根据q群组id获取
     * @param roomId
     * @return
     */
    @RequestLine(value = "GET /v1/rooms/{roomId}")
    public ApiResult findById(@Param("roomId") String roomId);
}
