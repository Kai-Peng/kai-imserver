package im.kai.server.service.room.controller;


import im.kai.server.domain.ApiResult;
import im.kai.server.exception.CustomerErrorException;
import im.kai.server.service.room.Constant;
import im.kai.server.service.room.domain.dto.Room;
import im.kai.server.service.room.domain.rep.RoomRep;
import im.kai.server.service.room.domain.resp.RoomResp;
import im.kai.server.service.room.manager.RoomManager;
import im.kai.server.service.room.uitls.PropertyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @AUTHER Pengp
 * @DATE 2018/11/1 19:02
 * 群组
 */
@RestController()
@RequestMapping(value = "v1/rooms")
@Slf4j
public class RoomController {

    @Autowired
    private RoomManager mRoomManager;

    /**
     * 根据群组id获取群信息
     * @param roomId 群组id
     * @return
     */
    @GetMapping(value = "/{roomId}")
    public ApiResult findById(@PathVariable String roomId) {
        Room room = mRoomManager.findById(Long.valueOf(roomId));
        RoomResp roomDto = new RoomResp();
        PropertyUtils.copyProperties(room,roomDto,"created");
        roomDto.setCreated(room.getCreated());
        return ApiResult.success(roomDto);
    }
    /**
     * 创建群组
     * @param roomRep 待创建群组
     * @return
     */
    @PostMapping("/create")
    public ApiResult createRoom(@RequestBody @Validated RoomRep roomRep) {

        long roomId  = mRoomManager.createRoom(roomRep,Constant.CURRENT_USER);
        return ApiResult.success(String.valueOf(roomId));

    }

    /**
     * 锁定群
     * @param roomId   群组id
     * @return
     */
    @PutMapping(value = "/lock/{roomId}")
    public ApiResult lockRoom(@PathVariable String roomId) {

         mRoomManager.updateLockRoom(Long.valueOf(roomId), Constant.LOCK_ROOM);
         return ApiResult.success(roomId);
    }

    /**
     * 取消锁定群
     * @param roomId   群组id
     * @return
     */
    @PutMapping(value = "/unLock/{roomId}")
    public ApiResult unLockRoom(@PathVariable String roomId) {

        mRoomManager.updateLockRoom(Long.valueOf(roomId),Constant.UN_LOCK_ROOM);
        return ApiResult.success(roomId);
    }

    /** 更改群组图标 名称
     * @param roomRep 待更新群组信息
     * @return
     */
    @PutMapping(value = "/info")
    public ApiResult updateRoomInfo(@RequestBody RoomRep roomRep) {
           mRoomManager.updateRoomInfo(roomRep,Constant.CURRENT_USER);
            return ApiResult.success();
    }

    /**
     * 群主转让
     * @param roomId 群组id
     * @param memberId 成员id
     * @return
     */
    @PutMapping("transfer/{roomId}/{memberId}")
    public ApiResult transferOwner(@PathVariable long roomId,@PathVariable long memberId){
            mRoomManager.transferOwner(roomId,memberId,Constant.CURRENT_USER);
        return ApiResult.success();
    }
}
