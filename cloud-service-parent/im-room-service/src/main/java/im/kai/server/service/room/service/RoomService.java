package im.kai.server.service.room.service;

import im.kai.server.service.room.domain.dto.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import im.kai.server.service.room.service.mapper.*;
import java.util.List;
/**
 * @AUTHER Pengp
 * @DATE 2018/11/1 19:34
 * 群组
 */
@Slf4j
@Service
@Transactional
public class RoomService  {

    @Autowired(required = false)
    private RoomMapper mRoomMapper;
    /**
     * 获取群组信息
     * @param roomId 群组id
     * @return
     */
//    @Cacheable(key = "'room_info_'+#roomId")
    public Room findById(long roomId){
        Room room = mRoomMapper.findById(roomId);
        return room;
    }

    /**
     * 根据id获取群组
     * @param roomIds
     * @return
     */
    public List<Room> findByIds(List<Long> roomIds) {
        List<Room> rooms = mRoomMapper.findByIds(roomIds);
        return rooms;
    }
    /**
     * 创建群组
     * @param room 待创建群组 {room:创建群组信息 members:群成员}
     */
    public long save(Room room) {
//        Room room = new Room();
//        PropertyUtils.copyProperties(roomRep,room);
//        List<String> members = roomRep.getMembers();
//        members.add(String.valueOf(currentUser));
//        int totalUser = roomRep.getMembers().size()+1;
//        room.setCreatedBy(currentUser);
//        room.setOwnerId(currentUser);
//        room.setCreated(new Date());
//        room.setLocked(false);
//        room.setTotalUser(totalUser);
        int dbResult = mRoomMapper.save(room);
        return room.getId();
    }

    /**
     * 更新群组名称/图标
     * @param room 待更新群组信息
     * @return
     */
    public void updateRoomInfo(Room room){
        int dbResult = mRoomMapper.updateRoomInfo(room);
    }
    /**
     * 更新群名称
     * @param roomId 群组id
     * @param name  待更新名称
     */
    private void updateRoomName(long roomId, String name) {
        int dbResult = mRoomMapper.updateRoomName(roomId,name);
    }

    /**
     * 更新群图标
     * @param roomId 群组id
     * @param icon  待更新图标
     */
    private void updateRoomIcon(long roomId, String icon) {
        int dbResult = mRoomMapper.updateRoomIcon(roomId,icon);
    }

    /**
     * 更新群组锁定
     * @param roomId 群组id
     * @param isLock 是否锁定
     */
//    @CacheEvict(key = "'room_info_'+#roomId")
    public void updateLockRoom(long roomId, boolean isLock) {
        int dbResult = mRoomMapper.updateLockRoom(roomId,isLock);
    }

    /**
     * 更新群人数
     * @param isAddMembers 是否是添加成员
     * @param roomId 群组id
     * @param totalUser 人数
     *
     */
    //    @CacheEvict(key = "'room_info_'+#roomId")
    public void updateTotalUser(boolean isAddMembers, long roomId, int totalUser) {
        if (isAddMembers) {
            int dbResult = mRoomMapper.addTotalUser(roomId,totalUser);
        } else {
            int dbResult = mRoomMapper.removeTotalUser(roomId,totalUser);
        }
    }

    /**
     * 更新群主
     * @param roomId 群组id
     * @param successorId 待更新群主id
     */
    //    @CacheEvict(key = "'room_info_'+#roomId")
    public int updateOwner(long roomId, long successorId) {
        int dbResult = mRoomMapper.updateOwner(roomId,successorId);
        return dbResult;
    }

    /**
     * 解散群组
     * @param roomId 群组id
     */
    //    @CacheEvict(key = "'room_info_'+#roomId")
    public void dissolutionRoom(long roomId) {
        int dbResult = mRoomMapper.delete(roomId);
    }

}
