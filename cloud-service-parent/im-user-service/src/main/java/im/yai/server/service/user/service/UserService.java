package im.kai.server.service.user.service;

import im.kai.server.service.user.domain.dto.SyncDataVersion;
import im.kai.server.service.user.domain.dto.User;
import im.kai.server.service.user.mapper.SyncDataVersionMapper;
import im.kai.server.service.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户实体操作Service
 */
@Service
public class UserService {

    @Autowired  private UserMapper userMapper;
    @Autowired  private SyncDataVersionMapper syncDataVersionMapper;
    /**
     * 根据用户id 来查询用户实体
     * @param id 用户id
     * @return User 实体
     */
    public User get(long id) {
        return userMapper.selectByPrimaryKey(id) ;
    }

    /**
     * 根据手机号码获取用户实体
     * @param mobile 手机号码
     * @return User 实体
     */
    public User getWithMobile(String mobile) {
        return userMapper.selectByMobile(mobile) ;
    }

    /**
     * 根据IM 号获取用户User实体
     * @param im_number im号
     * @return User实体
     */
    public User getWithIMNum(String im_number) {
        return userMapper.selectByIMNum(im_number) ;
    }

    /**
     *  插入用户记录和用户记录信息
     * @param user
     * @return 新增的记录数量
     */
    public int insertUserAndSynData(User user) {

        int affected = userMapper.insert(user) ;

        //插入版本号信息
        SyncDataVersion syncDataVersion = new SyncDataVersion() ;
        syncDataVersion.setUserId(user.getId());
        syncDataVersion.setFriendsVersion(System.currentTimeMillis());
        syncDataVersion.setSettingsVersion(0L);
        syncDataVersion.setRoomsVersion(0L);
        syncDataVersionMapper.insert(syncDataVersion) ;

        return affected ;
    }

    /**
     * 获取指定手机号码的记录数量
     * @param mobile
     * @return 记录数
     */
    public int getCount(String mobile) {
        return userMapper.getCount(mobile) ;
    }

    /**
     *@描述 获取待操作用户中，用户存在的ids
     *@参数   userIds 待操作用户ids
     *@返回值
     *@创建人 Pengp
     *@创建时间 2018/11/22
     */
    public List<Long> checkUsers(List<Long> userIds) {
        userIds = userMapper.checkUsers(userIds);
        return userIds;
    }
}
