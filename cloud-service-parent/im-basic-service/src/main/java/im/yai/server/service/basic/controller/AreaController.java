package im.kai.server.service.basic.controller;


import im.kai.server.utils.JarUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 公共服务
 */
@Slf4j
@RestController
@RequestMapping("/v1/area")
public class AreaController {

    @Autowired
    private RedisTemplate redisTemplate;

    private final static String AREA_CACHE_KEY = "v1/area/region/cache" ;

    /**
     *
     * extends BaseController
     * 获取全球地理信息
     *
     * TODO 计划加入redis 缓存。防止频繁读入文件
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping(value = "/region" , method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void responseAreaInfo(HttpServletRequest request , HttpServletResponse response) throws Exception {

        String area =  null ;//(String) redisTemplate.opsForValue().get(AREA_CACHE_KEY) ;

        area = JarUtils.getResourceTextFile(AreaController.class , "statics/loclist.txt");
        //缓存为空
        //if(area == null) {
         //   area = JarUtils.getResourceTextFile(AreaController.class , "statics/loclist.txt");
            ///*if(!TextUtils.isEmpty(area)) {
                //缓存30天
               // redisTemplate.opsForValue().set(AREA_CACHE_KEY , area , 30 , TimeUnit.DAYS);
           // }*/
        //}

        response.addHeader(HttpHeaders.CONTENT_TYPE , MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(area);

    }
}
