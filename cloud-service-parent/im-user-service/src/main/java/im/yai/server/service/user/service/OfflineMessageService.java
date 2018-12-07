package im.kai.server.service.user.service;

import im.kai.server.service.user.mapper.OfflineMessageMapper;
import im.kai.server.service.user.domain.dto.OfflineMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfflineMessageService {

    private @Autowired
    OfflineMessageMapper imOfflineMessageDao ;

    public int deleteByPrimaryKey(Long id) {
        return imOfflineMessageDao.deleteByPrimaryKey(id) ;
    }

    public int insert(OfflineMessage record) {
        return imOfflineMessageDao.insert(record) ;
    }

    public int insertSelective(OfflineMessage record){
        return imOfflineMessageDao.insertSelective(record) ;
    }

    OfflineMessage selectByPrimaryKey(Long id){
        return imOfflineMessageDao.selectByPrimaryKey(id) ;
    }

    public int updateByPrimaryKeySelective(OfflineMessage record){
        return imOfflineMessageDao.updateByPrimaryKeySelective(record) ;
    }

    public int updateByPrimaryKey(OfflineMessage record) {
        return imOfflineMessageDao.updateByPrimaryKey(record) ;
    }
}