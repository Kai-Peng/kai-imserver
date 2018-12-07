package im.kai.server.service.user.constraint.annotation;

import im.kai.server.service.user.constraint.PhoneNumConstraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 匹配电话号码
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumConstraint.class)
public @interface PhoneNum {

    /**验证结果返回的状态码，该状态码由自己定义*/
    public int code()  ;

    //自定义注解，这个方法必须加入
    public String message() default "" ;

    //能否为空
    public boolean nullable() default true ;

    /**自定义注解，这个方法必须加入*/
    Class<?>[] groups() default { };

    /**自定义注解，这个方法必须加入*/
    Class<? extends Payload>[] payload() default { };

}
