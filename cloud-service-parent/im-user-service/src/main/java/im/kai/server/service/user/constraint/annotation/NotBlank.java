package im.kai.server.service.user.constraint.annotation;

import im.kai.server.service.user.constraint.NotBlankConstraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBlankConstraint.class)
public @interface NotBlank {

    /**验证结果返回的状态码，该状态码由自己定义*/
    public int code() default 0 ;

    /**自定义注解，这个方法必须加入*/
    public String message() default "" ;

    /**自定义注解，这个方法必须加入*/
    Class<?>[] groups() default { };

    /**自定义注解，这个方法必须加入*/
    Class<? extends Payload>[] payload() default { };

}
