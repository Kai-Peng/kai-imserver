package im.kai.server.service.room.controller;

import im.kai.server.domain.ApiResult;
import im.kai.server.service.room.Constant;
import im.kai.server.service.room.domain.dto.Room;
import im.kai.server.service.room.domain.resp.RoomVersionResp;
import im.kai.server.service.room.domain.resp.SyncRoomResp;
import im.kai.server.service.room.manager.RoomManager;
import im.kai.server.service.room.uitls.PropertyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @AUTHER Pengp
 * @DATE 2018/11/2 17:12
 * 群组-成员
 */
@RestController()
@RequestMapping(value = "v1/rooms")
@Slf4j
public class RoomUserController {

    @Autowired
    private RoomManager     mRoomManager;
    /***
     * 获取群成员
     * @param roomId  群组id
     * @return
     */
    @GetMapping(value = "/members/{roomId}")
    public ApiResult getMembers(@PathVariable String roomId) {
        List<Long> userIds = mRoomManager.findUserIdsByRoomId(Long.valueOf(roomId),Constant.CURRENT_USER);
        //TODO 访问UserService        mUserRemoteClient.findUsersById(roomUsers);
        return ApiResult.success(userIds);
    }

    /**
     * 添加群组成员
     * @param members 待添加成员id
     * @param roomId    群组id
     * @return
     */
    @PutMapping(value = "/members/{roomId}")
    public ApiResult addMembers( @RequestBody    List<String> members,
                                 @PathVariable   String roomId) {
            List<Long> memberIds = new ArrayList<>();
            for (String memberId:members) {
                memberIds.add(Long.valueOf(memberId));
            }
            int  dbResult = mRoomManager.addMembers(Long.valueOf(roomId),memberIds,Constant.CURRENT_USER,false);
            return ApiResult.success(dbResult);
    }

    /**
     * 移除群组成员
     * @param members 待移除成员id
     * @param roomId  群组id
     * @return
     */
    @DeleteMapping(value = "/members/{roomId}")
    public ApiResult removeMembers(  @RequestBody    List<String> members,
                                     @PathVariable   String roomId) {
        List<Long> memberIds = new ArrayList<>();
        for (String memberId:members) {
            memberIds.add(Long.valueOf(memberId));
        }
        int dbResult  = mRoomManager.removeMembers(Long.valueOf(roomId),memberIds,Constant.CURRENT_USER);
        return ApiResult.success(dbResult);
    }

    /**
     * 退出群组
     * @param roomId 群组id
     * @return
     */
    @DeleteMapping(value = "/quit/{roomId}")
    public ApiResult quitGroup(@PathVariable String roomId) {

        mRoomManager.quitGroup(Constant.CURRENT_USER,Long.valueOf(roomId));
        return ApiResult.success();
    }

    /**
     * 获取用户拥有的群组
     * @param version 群组同步版本号
     * @return
     */
    @GetMapping(value = "/sync/{version}")
    public ApiResult getRooms(@PathVariable long version){

        long roomsVersion = mRoomManager.getRoomsVersion(version);
        List<Room> rooms = null;
        if (roomsVersion != version) //群组版本不同
            rooms = mRoomManager.findRoomByUserId(Constant.CURRENT_USER);
        if (rooms == null || rooms.isEmpty())   //用户没有加入群组
            return ApiResult.success();
        RoomVersionResp roomVersionResp = new RoomVersionResp();
        roomVersionResp.setVersion(roomsVersion);
        roomVersionResp.setRoomResp(PropertyUtils.copyProperties(rooms,new SyncRoomResp()));
        return ApiResult.success(roomVersionResp);
    }

    /**
     * 屏蔽群组
     * @param roomId    群组id
     * @return
     */
    @PutMapping(value = "blocklist/{roomId}")
    public ApiResult shieldingRoom(   @PathVariable String roomId) {

        mRoomManager.updateBlocked(Constant.CURRENT_USER,Long.valueOf(roomId), Constant.SHIELD_ROOM);
        return ApiResult.success();
    }

    /**
     * 取消屏蔽群组
     * @param roomId    群组id
     * @return
     */
    @DeleteMapping(value = "blocklist/{roomId}")
    public ApiResult unShieldingRoom(  @PathVariable String roomId) {

        mRoomManager.updateBlocked(Constant.CURRENT_USER,Long.valueOf(roomId),Constant.UN_SHIELD_ROOM);
        return ApiResult.success();
    }

    /**
     * 设置群组勿扰
     */
    @PutMapping(value = "/dnd/{roomId}")
    public ApiResult setDnd(@PathVariable String roomId){

        mRoomManager.setRoomDND(Long.valueOf(roomId),true,Constant.CURRENT_USER);
        return ApiResult.success();
    }

    /**
     * 设置群组勿扰
     */
    @DeleteMapping(value = "/dnd/{roomId}")
    public ApiResult setUnDnd(@PathVariable String roomId){

        mRoomManager.setRoomDND(Long.valueOf(roomId),true,Constant.CURRENT_USER);
        return ApiResult.success();
    }

}
