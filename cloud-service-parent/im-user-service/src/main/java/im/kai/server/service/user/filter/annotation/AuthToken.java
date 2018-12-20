package im.kai.server.service.user.filter.annotation;

import im.kai.server.service.user.filter.validator.DefaultHeaderValidator;
import im.kai.server.service.user.filter.validator.HeaderValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *
 * 在所有的方法头上，都必须加入这个注解，否则，验证直接返回false
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthToken {

    /**
     * 是否要验证，默认是true，如果是登录等其他不需要验证的操作，可以设置为false
     */
    public boolean mustDoAuth() default true;
    /**
     * 需要验证的请求头
     * */
    public String[] basicHeaders() default {"X-TOKEN" , "X-DEVICE-DATA"};

    /**其他的请求头验证*/
    public String[] extraHeaders() default {} ;
    /**验证的处理逻辑*/
    public Class<? extends HeaderValidator> validator() default DefaultHeaderValidator.class;

}
