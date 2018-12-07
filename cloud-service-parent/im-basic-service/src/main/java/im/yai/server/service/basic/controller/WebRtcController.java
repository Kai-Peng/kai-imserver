package im.kai.server.service.basic.controller;

import im.kai.server.domain.ApiResult;
import im.kai.server.service.basic.turn.TurnTokenBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * webrtc的基础服务
 */
@RestController
@RequestMapping("/v1/webrtc")
public class WebRtcController {
    @Resource
    TurnTokenBuilder turnTokenBuilder;

    /**
     * 获取穿透服务会话
     * @return
     */
    @GetMapping("/turn")
    public ApiResult getTurnToken(){
        return ApiResult.success(turnTokenBuilder.build());
    }
}
