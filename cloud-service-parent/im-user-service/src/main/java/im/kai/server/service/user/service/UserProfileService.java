package im.kai.server.service.user.service;

import im.kai.server.service.user.mapper.UserProfileMapper;
import im.kai.server.service.user.domain.dto.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 操作用户详细信息
 */
@Service
public class UserProfileService {

    private @Autowired UserProfileMapper userProfileMapper;

    public int deleteByPrimaryKey(Long userId) {
        return userProfileMapper.deleteByPrimaryKey(userId);
    }

    public int insert(UserProfile record) {
        return userProfileMapper.insert(record);
    }

    public int insertSelective(UserProfile record){
        return userProfileMapper.insertSelective(record);
    }

    public UserProfile selectByPrimaryKey(Long userId){
        return userProfileMapper.selectByPrimaryKey(userId);
    }

    public int updateByPrimaryKeySelective(UserProfile record){
        return userProfileMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(UserProfile record) {
        return userProfileMapper.updateByPrimaryKey(record);
    }

    /**
     * 用于登录操作
     * @param userId 用户id
     * @param password 密码
     * @return 记录数量
     */
    public int getCount(long userId , String password) {
        return userProfileMapper.getCount(userId , password);
    }

    /**
     * 更新用户信息
     * @param userId 用户ID
     * @param userProfileReq UserProfile 实体记录
     * @return 影响数量
     */
    public int updateUserInfo(long userId,UserProfile userProfileReq) {
        userProfileReq.setUserId(userId);
        return userProfileMapper.updateByPrimaryKeySelective(userProfileReq);
    }
}