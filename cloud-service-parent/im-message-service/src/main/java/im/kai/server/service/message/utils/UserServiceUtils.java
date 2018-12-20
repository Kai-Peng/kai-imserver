package im.kai.server.service.message.utils;

import im.kai.server.service.message.constant.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户服务工具类
 */
public class UserServiceUtils {
    private @Autowired RedisUtils redisUtils ;

    /**
     * 验证连接用户Token
     * @param userId
     * @param deviceId
     * @return true:验证通过，false:验证失败
     */
    public boolean checkUserToken(String userId , String deviceId, String token){
        if(redisUtils.get(generateUserTokenKey(userId, deviceId)).equals(token)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 根据参数生成Token的Redis Key值（参照im-user-service模块的生成规则）
     * @param userId
     * @param deviceId
     * @return
     */
    public String generateUserTokenKey(String userId , String deviceId) {
        return RedisCache.KEY_USER_TOKEN.concat(String.valueOf(userId)).concat(deviceId) ;
    }
}
