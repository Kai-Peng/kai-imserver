package im.kai.server.service.user.constraint.annotation;

import im.kai.server.service.user.constraint.IntValEnumConstraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;


/**
 * 数字数据类型枚举值范围
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IntValEnumConstraint.class)
public @interface IntValueEnum {

    int[] set() ;

    public boolean nullable() default true ;

    /**验证结果返回的状态码，该状态码由自己定义*/
    public int code() default 0 ;

    /**自定义注解，这个方法必须加入*/
    public String message() default "" ;

    /**自定义注解，这个方法必须加入*/
    Class<?>[] groups() default { };

    /**自定义注解，这个方法必须加入*/
    Class<? extends Payload>[] payload() default { };

}
