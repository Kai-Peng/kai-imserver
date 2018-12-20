package im.kai.server.service.user.mapper;

import im.kai.server.service.user.domain.dto.UserOnlineDevice;
import org.apache.ibatis.annotations.Param;

/**
 * 操作在线设备
 */
public interface UserOnlineDeviceMapper {

    /**
     * 根据表主键id 来删除记录
     * @param id
     * @return 影响数量
     */
    int deleteByPrimaryKey(Long id);
    /**
     * 插入一条UserOnlineDevice 表记录
     * @param record User 实例
     * @return 影响数量
     */
    int insert(UserOnlineDevice record);
    /**
     * 插入一条UserOnlineDevice 表记录 ，只操作非null的值
     * @param record User 实例
     * @return 影响数量
     */
    int insertSelective(UserOnlineDevice record);

    /**
     * 根据主键ID 查询一条UserOnlineDevice实体
     * @param id
     * @return UserOnlineDevice
     */
    UserOnlineDevice selectByPrimaryKey(Long id);

    /**
     * 根据主键ID 更新一条UserOnlineDevice实体 ，操作的是非null的字段
     * @return 影响数量
     */
    int updateByPrimaryKeySelective(UserOnlineDevice record);
    /**
     * 根据主键ID 更新一条UserOnlineDevice实体 ，包括null的字段
     * @return 影响数量
     */
    int updateByPrimaryKey(UserOnlineDevice record);

    /**
     * 更新设备在线状态
     * @param onlineDevice
     * @return 影响数量
     */
    int updateOnlineState(UserOnlineDevice onlineDevice) ;

    /**
     * 根据设备ID 判断数据表中是否存在
     * @param device_id 设备ID
     * @return 记录数量
     */
    int existDevice(@Param("device_id") Long device_id);

    /**
     * 根据设备id 和用户id 判断数据表是否存在指定设备
     * @param deviceId 设备ID
     * @param userId 用户ID
     * @return UserOnlineDevice
     */
    UserOnlineDevice getDevice(@Param("device_id") Long deviceId, @Param("user_id") Long userId) ;

    /**
     * 设置指定设备为离线状态
     * @param userId 用户ID
     * @param deviceId 设备ID
     * @return 影响数量
     */
    int setOffline(@Param("user_id") Long userId , @Param("device_id") Long deviceId);

    /**
     * 根据用户id 和设备类型获取在线设备实体
     * @param userId
     * @param deviceType
     * @return UserOnlineDevice
     */
    UserOnlineDevice getOnlineDevice(@Param("user_id") Long userId , @Param("device_type") String deviceType) ;

}