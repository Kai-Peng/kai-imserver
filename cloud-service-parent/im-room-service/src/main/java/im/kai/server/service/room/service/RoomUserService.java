package im.kai.server.service.room.service;

import im.kai.server.service.room.domain.dto.RoomUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import im.kai.server.service.room.service.mapper.*;
import java.util.Date;
import java.util.List;

/**
 * @AUTHER Pengp
 * @DATE 2018/11/2 16:53
 * 群组-成员
 */
@Slf4j
@Service
public class RoomUserService  {

    @Autowired(required = false)
    private RoomUserMapper mRoomUserMapper;
    /**
     * 获取用户所有群组
     * @param userId 用户id
     * @return
     */
    public List<RoomUser> findByUserId(long userId) {
        List<RoomUser> roomUsers = mRoomUserMapper.findByUserId(userId);
        return roomUsers;
    }

    /**
     * 获取用户所有群组id
     * @param userId
     * @return
     */
//    @Cacheable(value = "rooms",key = "'room_user_'+#userId")
    public List<Long> findRoomIdByUserId(long userId) {
        List<Long> roomIds = mRoomUserMapper.findRoomIdsByUserId(userId);
        return roomIds;
    }

    /**
     * 获取群组所有成员
     * @param roomId 群组id
     * @return
     */
    public List<RoomUser> findByRoomId(long roomId) {
        List<RoomUser> roomUsers = mRoomUserMapper.findByRoomId(roomId);
        return roomUsers;
    }

    /**
     * 获取群组所有成员id
     * @param roomId 群组id
     * @return
     */
//    @Cacheable(value = "users",key = "'room_user_'+#roomId")
    public List<Long> findUserIdsByRoomId(long roomId) {
        List<Long> roomUsers = mRoomUserMapper.findUserIdsByRoomId(roomId);
        return roomUsers;
    }

    /**
     * 群组添加成员
     * @param roomId 群组id
     * @param members 待添加成员id
     * @param inviterId 邀请人id
     */
//    @CacheEvict(value = {"rooms","users"},key = "'room_user_'+#roomId")
    public int addMembers(long roomId, List<Long> members,long inviterId) {
        int  dbResult = mRoomUserMapper.addMembers(roomId,members,inviterId,new Date());
        return dbResult;
    }

    /**
     * 群组移除成员
     * @param roomId 群组id
     * @param members 待移除成员id
     */
//    @CacheEvict(value = {"rooms","users"},key = "'room_user_'+#roomId")
    public int removeMembers(long roomId, List<Long> members) {
        int dbResult = mRoomUserMapper.removeMembers(roomId,members);
        return dbResult;
    }

    /**
     * 更新用户群屏蔽状态
     * @param userId 用户id
     * @param roomId 群组id
     * @param isShield 是否屏蔽群
     */
    public void updateBlocked(long userId, long roomId, boolean isShield) {
        int dbResult = mRoomUserMapper.updateBlocked(userId,roomId,isShield);
    }

    /**
     * 退出群组
     * @param userId 用户id
     * @param roomId 群组id
     * @return
     */
//    @CacheEvict(value = {"rooms","users"},key = "'room_user_'+#roomId")
    public int quitGroup(long userId, long roomId) {
        int dbResult = mRoomUserMapper.quitGroup(userId,roomId);
        return dbResult;
    }

    /**
     *  获取群组-成员关系
     * @param userId
     * @param roomId
     * @return
     */
    public RoomUser findByUserIdAndRoomId(long userId, long roomId) {
        RoomUser roomUser = mRoomUserMapper.findByUserIdAndRoomId(userId,roomId);
        return roomUser;
    }

    /**
     * 设置勿扰模式
     * @param roomId
     * @param flag
     */
    public void  setRoomDND(long roomId,long userId ,boolean flag){
      int dbResult = mRoomUserMapper.updateDnd(roomId,userId,flag);
    }
}
