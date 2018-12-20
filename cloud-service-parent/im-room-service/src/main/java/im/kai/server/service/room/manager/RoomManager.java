package im.kai.server.service.room.manager;

import im.kai.server.api.user.UserService;
import im.kai.server.constant.ApiFailCode;
import im.kai.server.domain.ApiResult;
import im.kai.server.exception.CustomerErrorException;
import im.kai.server.service.room.Constant;
import im.kai.server.service.room.domain.dto.Room;
import im.kai.server.service.room.domain.dto.RoomUser;
import im.kai.server.service.room.domain.dto.base.BaseBean;
import im.kai.server.service.room.domain.rep.RoomRep;
import im.kai.server.service.room.service.RoomService;
import im.kai.server.service.room.service.RoomUserService;
import im.kai.server.service.room.uitls.PropertyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @AUTHER Pengp
 * @DATE 2018/11/2 10:10
 * 群组管理
 */
@Slf4j
@Component
@Transactional
public class RoomManager <T extends BaseBean> {

    @Autowired
    private RoomService mRoomService;
    @Autowired
    private RoomUserService mRoomUserService;
    @Autowired
    private ParseManager mParseManager;
    @Autowired(required = false)
    private UserService mUserService;

    /**
     *@描述 从userIds中 只返回存在的userId
     *@参数
     *@返回值
     *@创建人 Pengp
     *@创建时间 2018/11/22
     */
    private List<Long> accessCheckUsers(List<Long> userIds){
        ApiResult apiResult = mUserService.checkUsers(userIds);
        return (List<Long>) mParseManager.parse(apiResult);
    }

    /**
     *@描述 从给定的用户好友中返回正确的好友列表
     *@参数 userId 用户id  friends 用户好友ids
     *@返回值
     *@创建人 Pengp
     *@创建时间 2018/11/23
     */
    private List<Long> accessCheckUserFriends(long userId, List<Long> friends){
        ApiResult apiResult = mUserService.checkUserFriends(userId,friends);
        return (List<Long>) mParseManager.parse(apiResult);
    }

    /**
     * 群组添加成员
     * @param roomId 群组id
     * @param userIds 待添加成员id
     * @param inviterId 邀请人id
     */
    public int addMembers(long roomId, List<Long> userIds,long inviterId,boolean isNew){
        userIds.add(inviterId);
        List<Long> checkUserIds = accessCheckUsers(userIds);
        if (checkUserIds == null || checkUserIds.isEmpty()){
            throw new CustomerErrorException(ApiFailCode.UserManage.NOT_FIND_FRIEND,
                    String.format("邀请人 Id = %s 或待添加成员 Ids = %s 不存在",inviterId,Arrays.toString(userIds.toArray())));
        }
        if (!checkUserIds.contains(inviterId)){
            throw new CustomerErrorException(ApiFailCode.UserManage.NOT_FIND_FRIEND,
                    String.format("邀请人 Id = %s 不存在",inviterId));
        }
        List<Long> checkUserFriends = accessCheckUserFriends(inviterId,checkUserIds);
        if (checkUserFriends == null || checkUserFriends.isEmpty()){
            throw new CustomerErrorException(ApiFailCode.UserManage.ERROR_FRIENDS,
                    String.format("邀请人 Id = %s 与一下用户 Ids = %s 不是好友",inviterId,Arrays.toString(userIds.toArray())));
        }
        userIds = checkUserFriends;
        if (!isNew && !isInRoom(roomId,inviterId)) //邀请人不是群内成员 (当前登录用户)
            throw new CustomerErrorException(ApiFailCode.RoomManage.NOT_GROUP_MEMBERS,
                String.format("邀请人 Id = %s 不是群组 Id = %s 的成员",inviterId,roomId));
        if (isLocked(roomId))   //群组锁定
            throw new CustomerErrorException(ApiFailCode.RoomManage.ROOM_HAD_LOCKED,
                String.format("群组 Id = %s 已鎖定",roomId));
        int dbResult = mRoomUserService.addMembers(roomId,userIds,inviterId);
        log.info(String.format("添加成员，更新room_user roomId:%s userIds:%s dbResult: %s",roomId, Arrays.toString(userIds.toArray()),dbResult));
        updateTotalUser(Constant.UPDATE_TOTAL_USER_ADD,roomId,dbResult);//更新群组人数
        return dbResult;
    }

    /**
     * 群组移除成员
     * @param roomId 群组id
     * @param userIds 待移除成员id
     */
    public int removeMembers(long roomId,List<Long> userIds,Long executorId) {
        List<Long> checkUserIds = accessCheckUsers(userIds);
        if (checkUserIds == null || checkUserIds.isEmpty()){
            throw new CustomerErrorException(ApiFailCode.UserManage.NOT_FIND_FRIEND,
                    String.format("待移除成员 Ids = %s 不存在",Arrays.toString(userIds.toArray())));
        }
        userIds = checkUserIds;
        if (!isOwner(roomId,executorId)) //移除操作执行者不是群主/不是群成员 (当前登录用户)
        throw new CustomerErrorException(ApiFailCode.RoomManage.NOT_GROUP_MANAGER,
                String.format("用户 Id = %s 不是群组 Id = %s 群主",executorId,roomId));
        int dbResult = mRoomUserService.removeMembers(roomId,userIds);
        log.info(String.format("移除成员，更新room_user roomId:%s userIds:%s dbResult: %s",roomId, Arrays.toString(userIds.toArray()),dbResult));
        updateTotalUser(Constant.UPDATE_TOTAL_USER_REMOVE,roomId,dbResult);//更新群组人数
        return dbResult;
    }
    /**
     * 是否锁定群组
     * @param type 操作类型 1：添加 2：移除
     * @return
     */
    public boolean isSetLock(int type){
        if (type == 1) return true;
        if (type == 2) return false;
        return false;
    }

    /**
     * 是否屏蔽群组
     * @param type 操作类型 1：屏蔽 2：取消屏蔽
     * @return
     */
    public boolean isBlocked(int type){
        if (type == 1) return true;
        if (type == 2) return false;
        return false;
    }

    /**
     * 判断某个用户是否群主
     * @param roomId 群组id
     * @param userId 用户id
     * @return
     */
    private boolean isOwner(long roomId,long userId){
        Room room = mRoomService.findById(roomId);
        if (room == null) return false;
        long ownerId = room.getOwnerId();
        if (ownerId != userId) return false;
        return true;
    }

    /**
     * 获取群人数
     * @param roomId
     * @return
     */
    private int getMemberNumber(long roomId) {
        Room room = mRoomService.findById(roomId);
        int totalUser = room.getTotalUser();
        return totalUser;
    }

    /**
     * 更新群主
     * @param roomId 群组id
     * @param successorId 待更新群主id
     * @return
     */
    public void transferOwner(long roomId, long successorId,long currentUser) {
        if (!isInRoom(roomId,currentUser) ||
                !isInRoom(roomId,successorId)) //不是群组成员
            throw new CustomerErrorException(ApiFailCode.RoomManage.NOT_GROUP_MEMBERS,
                    String.format("待更新群主 Id = %s 不是群组 Id = %s 的成员",successorId,roomId));
        if (!isOwner(roomId,currentUser))      //不是群主（当前登录用户）
            throw new CustomerErrorException(ApiFailCode.RoomManage.NOT_GROUP_MANAGER,
                    String.format("当前登录用户 currentUser Id = %s,不是群组 Id = %s 的群主",currentUser,roomId));
        mRoomService.updateOwner(roomId,successorId);
    }

    /**
     * 更新群组人数
     * @param isAddMembers 是否是添加成员
     * @param roomId
     * @param size
     */
    private void updateTotalUser(boolean isAddMembers, long roomId, int size) {
        mRoomService.updateTotalUser(isAddMembers,roomId,size);
        log.info(String.format("更新群人数 ：type : %s  roomId : %s totalUser: %s",isAddMembers?"添加":"移除",roomId,size));
    }

    /**
     * 解散群组
     * @param roomId
     */
    public void dissolutionRoom(long roomId) {
        mRoomService.dissolutionRoom(roomId);
    }

    /**
     * 判断群组是否是锁定状态
     */
    public boolean isLocked(long roomId) {
        Room room = mRoomService.findById(roomId);
        return room.isLocked();
    }

    /**
     * 根据id获取群组
     * @param userId 用戶id
     * @return
     */
    public List<Room> findRoomByUserId(Long userId){
        List<Long> roomIds = mRoomUserService.findRoomIdByUserId(userId);
        if (roomIds == null||
                roomIds.isEmpty())
            return null;
        List<Room> rooms = mRoomService.findByIds(roomIds);
        return rooms;
    }

    /**
     * 判断用户是否在群组中
     * @param roomId 群组id
     * @param userId 用户id
     * @return
     */
    private boolean isInRoom(long roomId,long userId){
        RoomUser roomUser = mRoomUserService.findByUserIdAndRoomId(userId,roomId);
        if (roomUser != null)
            return true;
        return false;
    }

    /**
     * 用户群组版本同步
     */
    public long getRoomsVersion(long version) {
        //TODO 判断客户端版本号 == 服务端版本号
        return 1;
    }

    /**
     * 获取群组所有成员id
     * @param roomId 群组id
     * @param memberId 成员id
     * @return
     */
    public List<Long> findUserIdsByRoomId(Long roomId, long memberId) {
        if (!isInRoom(roomId,memberId))    //不是群成员 （当前登录用户）
            throw new CustomerErrorException(ApiFailCode.RoomManage.NOT_GROUP_MEMBERS,
                    String.format("成员 Id = %s不在群组 Id = %s中",memberId,roomId));
        List<Long> userIds = mRoomUserService.findUserIdsByRoomId(roomId);
        return userIds;
    }

    /**
     * 获取群组所有成员
     * @param roomId 群组id
     * @return
     */
    public List<RoomUser> findByRoomId(long roomId, long memberId) {
        if (!isInRoom(roomId,memberId))    //不是群成员 （当前登录用户）
            throw new CustomerErrorException(ApiFailCode.RoomManage.NOT_GROUP_MEMBERS,
                    String.format("成员 Id = %s不在群组 Id = %s中",memberId,roomId));
        List<RoomUser> roomUsers = mRoomUserService.findByRoomId(roomId);
        return roomUsers;
    }

    /**
     * 更新用户群屏蔽状态
     * @param userId 用户id
     * @param roomId 群组id
     * @param isShield 是否屏蔽群
     */
    public void updateBlocked(long userId, long roomId, boolean isShield) {
        if (!isInRoom(roomId,userId)) //用户不是群成员(当前登录用户)
            throw new CustomerErrorException(ApiFailCode.RoomManage.NOT_GROUP_MEMBERS,
                    String.format("用户 Id = %s不在群组 Id = %s中",userId,roomId));
        mRoomUserService.updateBlocked(userId,roomId,isShield);
    }

    /**
     * 退出群组
     * @param userId 用户id
     * @param roomId 群组id
     * @return
     */
    public void quitGroup(long userId, long roomId) {
        boolean isOwner = isOwner(roomId,userId);
        int totalUser = getMemberNumber(roomId);
        if (!isInRoom(roomId,userId))  //用户不是群成员(当前登录用户)
            throw new CustomerErrorException(ApiFailCode.RoomManage.NOT_GROUP_MEMBERS,
                    String.format("用户 Id = %s不在群组 Id = %s中",userId,roomId));
        if (totalUser == 2) {
            int dbResult = mRoomUserService.quitGroup(userId,roomId);
            dissolutionRoom(roomId); //最后一人退出解散
            log.info(String.format("群人数不满足，群组解散  roomId : %s ",roomId));
            return;
        }
        if (isOwner) { //群主不能退出
            throw new CustomerErrorException(ApiFailCode.RoomManage.NOT_GROUP_MEMBERS,
                    String.format("用户 Id = %s是群组 Id = %s群主，不能退出",userId,roomId));
        }
        mRoomUserService.quitGroup(userId,roomId);
    }

    /**
     * 根据群组id获取
     * @param roomId
     * @return
     */
    public Room findById(Long roomId) {
        Room room = mRoomService.findById(roomId);
        if (room == null)
        throw new CustomerErrorException(ApiFailCode.RoomManage.INVALID_ROOM_ID,
                String.format("群组 Id = %s 不存在",roomId));
        return room;
    }

    /**
     * 创建群组
     * @param roomRep 待创建群组 {room:创建群组信息 members:群成员}
     * @param currentUser 当前登录用户
     */
    public long createRoom(RoomRep roomRep, long currentUser) {
        Room room = new Room();
        PropertyUtils.copyProperties(roomRep,room);
        List<String> members = roomRep.getMembers();
        List<Long> memberIds = new ArrayList<>();
        for (String memberId:members) {
            memberIds.add(Long.valueOf(memberId));
        }
        memberIds.add(currentUser);
        int totalUser = roomRep.getMembers().size()+1;
        room.setCreatedBy(currentUser);
        room.setOwnerId(currentUser);
        room.setCreated(new Date().getTime());
        room.setLocked(false);
        room.setTotalUser(totalUser);
        long roomId = mRoomService.save(room);
        log.info(String.format("创建群组：roomInfo: %s ",room.toString()));
        addMembers(roomId,memberIds,room.getOwnerId(),true);
        return room.getId();
    }


    /**
     * 更新群组名称/图标
     * @param roomRep 待更新群组信息
     * @param memberId  更新信息成员
     * @return
     */
    public void updateRoomInfo(RoomRep roomRep,long memberId) {
        Room room = new Room();
        PropertyUtils.copyProperties(roomRep,room,"members","id","locked");
        Long roomId = Long.valueOf(roomRep.getId());
        room.setId(roomId);
        if (!isInRoom(roomId,memberId))
            throw new CustomerErrorException(ApiFailCode.RoomManage.NOT_GROUP_MANAGER,
                    String.format("成员 Id = %s ,不在 群组 Id = %s 中",memberId,roomId));
        if (StringUtils.isEmpty(roomRep.getName())&&
                StringUtils.isEmpty(roomRep.getIcon()))
           return;
        mRoomService.updateRoomInfo(room);
    }

    /**
     * 更新群组锁定
     * @param roomId 群组id
     * @param isLock 是否锁定
     */
    public void updateLockRoom(long roomId, boolean isLock) {
        mRoomService.updateLockRoom(roomId,isLock);
    }

    /**
     * 设置群组勿扰模式
     * @param roomId
     * @param flag
     */
    public void  setRoomDND(long roomId, boolean flag,long currentUser){
        if (!isInRoom(roomId,currentUser))
        throw new CustomerErrorException(ApiFailCode.RoomManage.NOT_GROUP_MANAGER,
                String.format("用户 Id = %s 不是群组 Id = %s 群主",currentUser,roomId));
         mRoomUserService.setRoomDND(roomId,currentUser,flag);
    }
}
