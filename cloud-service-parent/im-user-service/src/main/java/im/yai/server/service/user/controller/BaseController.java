package im.kai.server.service.user.controller;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import im.kai.server.service.user.constant.TextCharset;

import io.micrometer.core.instrument.util.IOUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static im.kai.server.utils.EntityValidator.fastValidate;
import static im.kai.server.utils.EntityValidator.validateResult;

@Slf4j
public class BaseController {

    /**
     * 获取Json 请求数据
     * @param request
     * @return JSONObject
     */
    protected JSONObject requestBody(HttpServletRequest request) throws JSONException {


        String json = null;
        try {
            json = IOUtils.toString(request.getInputStream() , TextCharset._UTF8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return JSONObject.parseObject(json) ;
    }

    protected String requestJsonStringBody(HttpServletRequest request) throws JSONException {
        String json = null;
        try {
            json = IOUtils.toString(request.getInputStream() , TextCharset._UTF8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json ;
    }

    /**
     *
     * @param bean
     * @return
     */
    protected void checkRequestArguments(Object bean) throws Exception {

        validateResult(fastValidate(bean)) ;

    }


}

