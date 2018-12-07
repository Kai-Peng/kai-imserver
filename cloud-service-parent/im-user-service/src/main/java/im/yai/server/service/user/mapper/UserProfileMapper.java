package im.kai.server.service.user.mapper;

import im.kai.server.service.user.domain.dto.UserProfile;
import org.apache.ibatis.annotations.Param;

public interface UserProfileMapper {

    /**
     * 根据主键id 来删除记录
     * @return 影响数量
     */
    int deleteByPrimaryKey(Long userId);

    /**
     * 插入一条UserProfile 记录
     * @param record
     * @return  影响数量
     */
    int insert(UserProfile record);

    /**
     * 插入一条UserProfile 记录 ，只操作非null 字段
     * @param record
     * @return  影响数量
     */
    int insertSelective(UserProfile record);

    /**
     * 根据用户ID ，获取UserProfile
     * @param userId 用户ID
     * @return UserProfile 实例
     */
    UserProfile selectByPrimaryKey(Long userId);

    /**
     * 更新UserProfile 实体 , 只更新非null的字段
     * @param record
     * @return 影响数量
     */
    int updateByPrimaryKeySelective(UserProfile record);

    /**
     * 更新UserProfile 实体 ，会操作非null的字段
     * @param record
     * @return 影响数量
     */
    int updateByPrimaryKey(UserProfile record);

    /**
     * 根据用户ID 和密码获取记录的数量，通常是登录判断
     * @param userId
     * @param password
     * @return 记录数量
     */
    int getCount(@Param("userId") long userId ,  @Param("password") String password);
}