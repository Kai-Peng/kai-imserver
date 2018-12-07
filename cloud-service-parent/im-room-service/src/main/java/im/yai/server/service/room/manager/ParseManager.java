package im.kai.server.service.room.manager;

import im.kai.server.domain.ApiResult;
import org.springframework.stereotype.Component;

/**
 * @AUTHER Pengp
 * @DATE 2018/11/22 18:27
 */
@Component
public class ParseManager {

    /**
     * 解析
     * @param result
     * @return
     */
    public Object parse(ApiResult result){
        int code = result.getCode();
        if (code != 0) return null;
        Object obj = result.getData();
        return obj;
    }
}
