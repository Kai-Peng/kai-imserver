package im.kai.server.service.user.constraint.annotation;

import im.kai.server.service.user.constraint.RegexConstraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 正则式匹配应用
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RegexConstraint.class)
public @interface RegexMatch {

    /**正则式匹配*/
    public String[] regex()  ;
    /**验证结果返回的状态码，该状态码由自己定义*/
    public int code() default 0 ;

    //自定义注解，这个方法必须加入
    public String message() default "" ;

    //能否为空
    public boolean nullable() default true ;

    /**自定义注解，这个方法必须加入*/
    Class<?>[] groups() default { };

    /**自定义注解，这个方法必须加入*/
    Class<? extends Payload>[] payload() default { };

}
