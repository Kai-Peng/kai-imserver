package im.kai.server.service.user.mapper;

import im.kai.server.service.user.domain.dto.UserDevice;
import org.apache.ibatis.annotations.Param;

/**
 * 用户设备操作Mapper
 */
public interface UserDeviceMapper {

    /**
     * 通过主键ID 删除一个UserDevice
     * @param id
     * @return 影响记录数量
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入一个 UserDevice 实例
     * @param record
     * @return 影响记录数量
     */
    int insert(UserDevice record);

    /**
     * 插入一个UserDevice实例，只处理非null的字段
     * @param record
     * @return 影响记录数量
     */
    int insertSelective(UserDevice record);

    /**
     * 查询一个UserDevice 实体对象
     * @param id 设备ID
     * @return UserDevice 实例
     */
    UserDevice selectByPrimaryKey(Long id);

    /**
     * 更新一个UserDevice 实体，只更新非null的字段
     * @param record UserDevice 实体
     * @return  影响记录数量
     */
    int updateByPrimaryKeySelective(UserDevice record);

    /**
     * 更新一个UserDevice 实体 ，包括null的字段
     * @param record UserDevice 实体
     * @return 影响记录数量
     */
    int updateByPrimaryKey(UserDevice record);

    /**
     * 根据code 和 用户ID 获取UserDevice 实体
     * @param code 设备码
     * @param user_id 用户ID
     * @return UserDevice 实体
     */
    UserDevice getDeviceWithCode(@Param("code") String code , @Param("user_id") Long user_id) ;
}