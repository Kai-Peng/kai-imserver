package im.kai.server.service.user.service;

import im.kai.server.service.user.domain.dto.SyncDataVersion;
import im.kai.server.service.user.mapper.SyncDataVersionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SyncDataVersionService {

    private @Autowired SyncDataVersionMapper syncDataVersionMapper ;
    public int deleteByPrimaryKey(Long userId) {
        return syncDataVersionMapper.deleteByPrimaryKey(userId);
    }

    public int insert(SyncDataVersion record) {
        return syncDataVersionMapper.insert(record);
    }

    public int insertSelective(SyncDataVersion record) {
        return syncDataVersionMapper.insertSelective(record);
    }

    public SyncDataVersion selectByPrimaryKey(Long userId) {
        return syncDataVersionMapper.selectByPrimaryKey(userId);
    }

    public int updateByPrimaryKeySelective(SyncDataVersion record) {
        return syncDataVersionMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(SyncDataVersion record) {
        return syncDataVersionMapper.updateByPrimaryKey(record);
    }

}
