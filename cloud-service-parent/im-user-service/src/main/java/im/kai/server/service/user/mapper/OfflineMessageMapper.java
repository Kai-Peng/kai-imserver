package im.kai.server.service.user.mapper;

import im.kai.server.service.user.domain.dto.OfflineMessage;

public interface OfflineMessageMapper {
    /**
     * 根据主键id 来删除记录
     * @param id
     * @return 影响数量
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入记录
     * @param record
     * @return 影响数量
     */
    int insert(OfflineMessage record);

    /**
     * 插入记录 ，只插入非null的字段
     * @param record
     * @return 影响数量
     */
    int insertSelective(OfflineMessage record);

    /**
     * 通过主键ID 获取 OfflineMessage
     * @param id
     * @return
     */
    OfflineMessage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OfflineMessage record);

    int updateByPrimaryKey(OfflineMessage record);
}