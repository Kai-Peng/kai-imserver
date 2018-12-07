package im.kai.server.api.user;

import feign.Param;
import feign.RequestLine;
import im.kai.server.domain.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @AUTHER Pengp
 * @DATE 2018/11/23 9:59
 */
@Service
@FeignClient(name= "im-user-service")
public interface UserService {

    /**
     *@描述 获取用户存在的用户ids
     *@参数 userIds  用户Ids
     *@返回值
     *@创建人 Pengp
     *@创建时间 2018/11/22
     */
    @RequestLine(value ="POST /v1/check/users")
    public ApiResult checkUsers(@RequestBody List<Long> userIds);

    /**
     *@描述 检查用户好友，返回正确好友
     *@参数 uiserId 用户id  friends 用户好友ids
     *@返回值
     *@创建人 Pengp
     *@创建时间 2018/11/23
     */
    @RequestLine(value = "POST /v1/check/user/{userId}/friends")
    public ApiResult checkUserFriends(@Param("userId") long userId,@RequestBody List<Long> friends);
}
