package im.kai.server.service.user.service;

import im.kai.server.service.user.domain.dto.UserOnlineDevice;
import im.kai.server.service.user.mapper.UserOnlineDeviceMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 在线设备操作
 */
@Service
public class UserOnlineDeviceService {

    private @Autowired UserOnlineDeviceMapper userOnlineDeviceMapper ;

    public int deleteByPrimaryKey(Long id) {
        return userOnlineDeviceMapper.deleteByPrimaryKey(id) ;
    }

    public int insert(UserOnlineDevice record) {
        return userOnlineDeviceMapper.insert(record) ;
    }

    public int insertSelective(UserOnlineDevice record) {
        return userOnlineDeviceMapper.insertSelective(record) ;
    }

    public UserOnlineDevice selectByPrimaryKey(Long id) {
        return userOnlineDeviceMapper.selectByPrimaryKey(id) ;
    }

    public int updateByPrimaryKeySelective(UserOnlineDevice record) {
        return userOnlineDeviceMapper.updateByPrimaryKeySelective(record) ;
    }

    public int updateByPrimaryKey(UserOnlineDevice record) {
        return userOnlineDeviceMapper.updateByPrimaryKey(record) ;
    }

    public int updateOnlineState(UserOnlineDevice onlineDevice) {
        return userOnlineDeviceMapper.updateOnlineState(onlineDevice) ;
    }

    /**
     * 获取指定的设备ID 的记录数量
     * @param deviceId
     * @return 返回记录数量
     */
    public int existDevice(Long deviceId) {
        return userOnlineDeviceMapper.existDevice(deviceId) ;
    }

    /**
     * 根据设备ID 和用户ID 获取在线设备记录
     * @param deviceId 设备ID
     * @param userId 用户ID
     * @return UserOnlineDevice 实体
     */
    public UserOnlineDevice getDevice(Long deviceId , long userId) {
        return userOnlineDeviceMapper.getDevice(deviceId , userId) ;
    }

    /**
     * 设置设备离线状态
     * @param userId 用户ID
     * @param deviceId 设备ID
     * @return 影响数量
     */
    public int setOffline(Long userId , Long deviceId) {
        return userOnlineDeviceMapper.setOffline(userId , deviceId) ;
    }

    /**
     * 根据用户ID和涉笔类型获取在线设备
     * @param userId
     * @param deviceType
     * @return UserOnlineDevice 实体
     */
    public UserOnlineDevice getOnlineDevice(@Param("user_id") Long userId , @Param("device_type") String deviceType) {
        return userOnlineDeviceMapper.getOnlineDevice(userId , deviceType) ;
    }

}