package im.kai.server.service.push.controller;

import im.kai.server.api.room.RoomService;
import im.kai.server.domain.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @AUTHER Pengp
 * @DATE 2018/11/17 16:34
 */
@RestController
@RequestMapping(value = "/v1/pushs")
public class PushNoticeController {


    @Autowired(required = false)
    private RoomService mRoomService;

    @GetMapping(value = "/access/room/{roomId}")
    public ApiResult findById(@PathVariable(value = "roomId") String roomId){
        return mRoomService.findById(roomId);
    }

}
