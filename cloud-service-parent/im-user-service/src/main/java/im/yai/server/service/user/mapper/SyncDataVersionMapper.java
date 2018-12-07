package im.kai.server.service.user.mapper;

import im.kai.server.service.user.domain.dto.SyncDataVersion;


/**
 * 版本信息同步
 */
public interface SyncDataVersionMapper {

    /**
     * 通过主键ID 删除记录
     * @param userId
     * @return 影响数量
     */
    int deleteByPrimaryKey(Long userId);

    /**
     * 插入一条记录
     * @param record
     * @return
     */
    int insert(SyncDataVersion record);

    int insertSelective(SyncDataVersion record);

    SyncDataVersion selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(SyncDataVersion record);

    int updateByPrimaryKey(SyncDataVersion record);
}