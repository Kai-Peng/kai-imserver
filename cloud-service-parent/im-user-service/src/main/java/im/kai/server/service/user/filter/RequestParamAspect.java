package im.kai.server.service.user.filter;

import im.kai.server.exception.CustomerErrorException;
import im.kai.server.service.user.domain.req.UserDeviceReq;
import im.kai.server.service.user.domain.req.UserRequest;
import im.kai.server.service.user.filter.annotation.NowSession;
import im.kai.server.service.user.utils.XObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static im.kai.server.constant.ApiFailCode.Device.INVALID_DEVICE;
import static im.kai.server.constant.ApiFailCode.Device.INVALID_TOKEN;
import static im.kai.server.constant.ApiFailCode.UserManage.NO_SIGN_IN;
import static im.kai.server.utils.EntityValidator.fastValidate;
import static im.kai.server.utils.EntityValidator.validateResult;

/**
 *
 * 统一参数验证
 * 验证请求头和请求体
 * AOP 参数验证切入点
 *
 */
@Component
@Aspect
@Slf4j
public class RequestParamAspect {


    private void checkRequestArguments(Object bean) throws Exception {

        validateResult(fastValidate(bean)) ;

    }

    @Pointcut("@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController)")
    public void pointcut()
    {
        // 空方法
    }


    @Around("pointcut()")
    public Object handle(ProceedingJoinPoint joinPoint) throws Throwable {

        RequestAttributes attributes = RequestContextHolder.getRequestAttributes() ;
        HttpServletRequest request = ((ServletRequestAttributes)attributes).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) attributes).getResponse();


        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method targetMethod = methodSignature.getMethod();

        /*AuthToken authToken = targetMethod.getAnnotation(AuthToken.class) ;
        log.info("AuthToken : " + authToken +" , targetMethod : " + targetMethod.getName());

        if(XObjectUtils.isNull(authToken)) {
            throw AuthHeaderException.create() ;
        }

        HeaderValidator header = authToken.validator().newInstance() ;

        //必须验证，但是验证不通过
        if(authToken.mustDoAuth()
                && !header.verify(authToken.basicHeaders() , authToken.extraHeaders() , request , response)) {
             throw AuthHeaderException.create() ;
        }*/

        //请求头验证通过，继续验证RequestBody 参数
        Parameter parameter[] = targetMethod.getParameters() ;

        Object args[] = joinPoint.getArgs() ;
        for(int i = 0 ; i < parameter.length ; i ++) {
            if(parameter[i].isAnnotationPresent(RequestBody.class)) {
                checkRequestArguments(joinPoint.getArgs()[i]);
            } else if(parameter[i].isAnnotationPresent(NowSession.class)) {

                //用户设备信息解析
                String xDevice = request.getHeader("X-DEVICE") ;
                if(XObjectUtils.isNull(xDevice)) {
                    throw new CustomerErrorException(INVALID_DEVICE , "无效设备") ;
                }
                String[] deviceInfo = xDevice.split(",") ;
                if(deviceInfo.length < 4) {
                    throw new CustomerErrorException(INVALID_DEVICE, "无效设备") ;
                }

                if(parameter[i].getType() == UserRequest.class) {
                    //用户ID
                    String userId = request.getHeader("X-USER-ID") ;
                    log.info("userId : " + userId);
                    if(StringUtils.isBlank(userId)) {
                        throw new CustomerErrorException(NO_SIGN_IN , "没有登录");
                    }
                    UserRequest user = new UserRequest();
                    user.setId(Long.parseLong(userId));
                    try { //其中如果类型转换失败，都会抛出异常
                        user.setDeviceId(Long.parseLong(deviceInfo[3]));
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new CustomerErrorException(INVALID_DEVICE, "无效设备") ;
                    }
                    String xToken = request.getHeader("X-TOKEN") ;

                    if(StringUtils.isBlank(xToken)) {
                        throw new CustomerErrorException(INVALID_TOKEN, "无效TOKEN") ;
                    }

                    user.setToken(xToken);

                    args[i] = user ;
                } else if(parameter[i].getType() == UserDeviceReq.class) { //用户设备信息

                    UserDeviceReq userDeviceReq = new UserDeviceReq() ;

                    userDeviceReq.setVersion(deviceInfo[0]);
                    userDeviceReq.setLang(deviceInfo[1]);
                    try { //其中如果类型转换失败，都会抛出异常
                        userDeviceReq.setType(Byte.parseByte(deviceInfo[2]));
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new CustomerErrorException(INVALID_DEVICE, "无效设备") ;
                    }
                    args[i] = userDeviceReq ;
                }

            }
        }

        //继续执行方法体
        return joinPoint.proceed(args) ;
    }

}
