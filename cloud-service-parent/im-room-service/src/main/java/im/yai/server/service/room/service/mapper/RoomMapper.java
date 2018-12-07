package im.kai.server.service.room.service.mapper;

import im.kai.server.service.room.domain.dto.Room;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;


/**
 * @AUTHER Pengp
 * @DATE 2018/11/2 12:38
 *  群组
 *
 */
@Mapper
public interface RoomMapper {

        /**
         * 获取群组信息
         * @param roomId
         * @return
         */
//        @Cacheable(value = "room",key = "'room_'+#roomId")
        Room findById(@Param("roomId") long roomId);

        /**
         * 根据id获取群组
         * @param roomIds
         * @return
         */
        List<Room> findByIds(@Param("roomIds") List<Long> roomIds);

        /**
         * 创建群组
         * @param room 带创建群组
         */
        //TODO 更新群组-成员缓存
        int            save(                        Room room);

        /**
         * 更新群名称
         * @param roomId 群组id
         * @param name  待更新名称
         */
//        @CacheEvict(value = "room",key = "'room_'+#roomId")
        int            updateRoomName(@Param("roomId") long roomId,@Param("name") String name);

        /**
         * 更新群图标
         * @param roomId 群组id
         * @param icon  待更新图标
         */
//        @CacheEvict(value = "room",key = "'room_'+#roomId")
        int updateRoomIcon(@Param("roomId") long roomId, @Param("icon") String icon);

        /**
         * 更新群组锁定
         * @param roomId 群组id
         * @param isLock 是否锁定
         */
//        @CacheEvict(value = "room",key = "'room_'+#roomId")
        int updateLockRoom(@Param("roomId") long roomId, @Param("isLock") boolean isLock);

        /**
         * 更新群人数
         * @param roomId 群组id
         * @param addNumber 人数
         */
//        @CacheEvict(value = "room",key = "'room_'+#roomId")
        int addTotalUser(@Param("roomId")long roomId, @Param("totalUser") int addNumber);

        /**
         * 更新群人数
         * @param roomId 群组id
         * @param removeNumber 人数
         */
    //        @CacheEvict(value = "room",key = "'room_'+#roomId")
        int removeTotalUser(@Param("roomId")long roomId, @Param("totalUser") int removeNumber);
        /**
         * 更新群主
         * @param roomId 群组id
         * @param successorId 待更新群主
         */
//        @CacheEvict(value = "room",key = "'room_'+#roomId")
        int updateOwner(@Param("roomId") long roomId, @Param("successorId") long successorId);

        /**
         * 解散群组
         * @param roomId 群组id
         */
//        @CacheEvict(value = "room",key = "'room_'+#roomId")
        int delete(@Param("roomId") long roomId);

        /**
         * 更新群组头像 名称
         * @param room 待更新信息
         * @return
         */
        int updateRoomInfo(Room room);
}
