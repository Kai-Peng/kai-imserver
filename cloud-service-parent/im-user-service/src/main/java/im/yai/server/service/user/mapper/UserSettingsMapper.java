package im.kai.server.service.user.mapper;

import im.kai.server.service.user.domain.dto.UserSettings;

public interface UserSettingsMapper {
    /**
     * 根据主键id 来删除记录
     * @param id
     * @return 影响数量
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入一条记录
     * @param record
     * @return 影响数量
     */
    int insert(UserSettings record);

    /**
     * 插入一条记录 , 只操作非null 字段
     * @param record
     * @return 影响数量
     */
    int insertSelective(UserSettings record);


    UserSettings selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserSettings record);

    int updateByPrimaryKey(UserSettings record);
}