package im.kai.server.service.user.service;

import im.kai.server.service.user.domain.dto.UserDevice;
import im.kai.server.service.user.mapper.UserDeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 用户设备操作Service
 *
 * user_device
 */
@Service
public class UserDeviceService {

    private @Autowired UserDeviceMapper userDeviceMapper ;

    /**
     * 通过主键ID 删除一个UserDevice
     * @param id
     * @return 影响记录数量
     */
    public int deleteByPrimaryKey(Long id) {
        return userDeviceMapper.deleteByPrimaryKey(id) ;
    }

    /**
     * 插入一个 UserDevice 实例
     * @param record
     * @return 影响记录数量
     */
    public int insert(UserDevice record){
        return userDeviceMapper.insert(record) ;
    }

    /**
     * 插入一个UserDevice实例，只处理非null的字段
     * @param record
     * @return 影响记录数量
     */
    public int insertSelective(UserDevice record){
        return userDeviceMapper.insertSelective(record) ;
    }

    /**
     * 查询一个UserDevice 实体对象
     * @param id 设备ID
     * @return UserDevice 实例
     */
    public UserDevice selectByPrimaryKey(Long id){
        return userDeviceMapper.selectByPrimaryKey(id) ;
    }

    /**
     * 更新一个UserDevice 实体，只更新非null的字段
     * @param record UserDevice 实体
     * @return  影响记录数量
     */
    public int updateByPrimaryKeySelective(UserDevice record){
        return userDeviceMapper.updateByPrimaryKeySelective(record) ;
    }

    /**
     * 更新一个UserDevice 实体 ，包括null的字段
     * @param record UserDevice 实体
     * @return 影响记录数量
     */
    public int updateByPrimaryKey(UserDevice record){
        return userDeviceMapper.updateByPrimaryKey(record) ;
    }

    /**
     * 根据UUID 和 用户ID 获取UserDevice 实体
     * @param code
     * @param user_id
     * @return UserDevice 实体
     */
    public UserDevice getDeviceWithCode(String code , Long user_id) {

        return userDeviceMapper.getDeviceWithCode(code , user_id) ;

    }

}