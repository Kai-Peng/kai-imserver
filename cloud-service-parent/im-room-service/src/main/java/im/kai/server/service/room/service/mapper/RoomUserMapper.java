package im.kai.server.service.room.service.mapper;

import im.kai.server.service.room.domain.dto.RoomUser;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.Date;
import java.util.List;

/**
 * @AUTHER Pengp
 * @DATE 2018/11/2 16:53
 *  群组-成员
 */
@Mapper
public interface RoomUserMapper {

    /**
     * 获取用户所有群
     * @param userId 用户id
     * @return
     */
//    @Cacheable(value = "room_user",key = "'room_user_'+#userId")
     List<RoomUser> findByUserId(@Param("userId") long userId);
    /**
     * 获取用户群组id
     * @param userId
     * @return
     */
//    @Cacheable(value = "room_ids",key = "'room_ids_'+#userId")
     List<Long> findRoomIdsByUserId(@Param("userId") long userId);
    /**
     * 获取群组所有成员
     * @param roomId 群组id
     * @return
     */
//    @Cacheable(value = "room_user",key = "'user_room_'+#roomId")
     List<RoomUser> findByRoomId(@Param("roomId") long roomId);

    /**
     * 获取群组所有成员id
     * @param roomId 群组id
     * @return
     */
//    @Cacheable(value = "user_ids",key = "'user_ids_'+#roomId")
     List<Long> findUserIdsByRoomId(@Param("roomId") long roomId);
    /**
     * 群组添加成员
     * @param roomId 群组id
     * @param members 待添加成员
     */
//    @CacheEvict(value = "user_ids",key = "'user_ids'+#roomId")
     int addMembers(@Param("roomId")     long roomId, @Param("members") List<Long> members,
                    @Param("inviterId")  long inviterId, @Param("created") Date created);

    /**
     * 群组添加成员
     * @param roomId 群组id
     * @param member 待添加成员
     */
//    @CacheEvict(value = "user_ids",key = "'user_ids'+#roomId")
    int addMember(@Param("roomId")     long roomId, @Param("members") Long member,
                   @Param("inviterId")  long inviterId, @Param("created") Date created);

    /**
     * 群组移除成员
     * @param roomId 群组id
     * @param members 待移除成员id
     */
//    @CacheEvict(value = "user_ids",key = "'user_ids'+#roomId")
     int removeMembers(@Param("roomId") long roomId, @Param("members") List<Long> members);

    /**
     * 群组移除成员
     * @param roomId 群组id
     * @param member 待移除成员id
     * @return
     */
//    @CacheEvict(value = "user_ids",key = "'user_ids'+#roomId")
    int removeMember(@Param("roomId") long roomId, @Param("members") Long member);

    /**
     * 退出群组
     * @param userId 成员id
     * @param roomId 群组id
     */
    int quitGroup(@Param("userId") long userId, @Param("roomId") long roomId);

    /**
     * 更新群屏蔽
     * @param userId 成员id
     * @param roomId 群组id
     * @param isBlocked 更新屏蔽状态
     */
     int updateBlocked(@Param("userId") long userId, @Param("roomId") long roomId, @Param("isBlocked") boolean isBlocked);
    /**
     * 判断用户是否在群组中
     * @param roomId 群组id
     * @param userId 用户id
     * @return
     */
     RoomUser findByUserIdAndRoomId(@Param("userId") long userId,@Param("roomId") long roomId);

    /**
     * 更新群组勿扰模式
     * @param roomId
     * @param flag
     * @return
     */
    int updateDnd(@Param("roomId") long roomId,@Param("userId") long userId,@Param("isDnd") boolean flag);
}
