package im.kai.server.service.message.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/websocket")
public class WebSocketTestPageController {
    //跳转websocket页面
    @GetMapping("/index")
    public String websocket(){
        return "websocket";
    }
}
