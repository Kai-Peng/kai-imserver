package im.kai.server.service.user.filter.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;


/**
 * 用来注解当前用户的基础信息，主要用于Controller 方法内部
 *
 * @RequestMapping("/demo")
 * method_name(@NowSession UserRequest userRequest) {
 *
 * }
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NowSession {


}
