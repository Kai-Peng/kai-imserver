package im.kai.server.service.user.filter.validator;

import io.micrometer.core.instrument.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 权限请求头验证
 *
 * TODO 还需要验证头部数据的合法性
 *
 */
public class AuthHeaderValidator implements HeaderValidator {

    @Override
    public boolean verify(String[] headers , String extraHeaders[] , HttpServletRequest request,
                                                                    HttpServletResponse response) {

        for(int i = 0 ; i < headers.length ; i ++) {
            if(StringUtils.isBlank(request.getHeader(headers[i]))) {
                return false ;
            }
        }

        return true ;
    }
}
