package im.kai.server.service.user.constraint.annotation;


import im.kai.server.service.user.constraint.NumLimitedConstraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 字节长度验证
 */
@Inherited
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NumLimitedConstraint.class)  // 关联解析类
public @interface NumLimited {

    /***最小字节数，默认是Integer.MIN_VALUE*/
    public int min() default Integer.MIN_VALUE  ;

    /**最大字节数，默认是Integer.MAX_VALUE*/
    public int max() default Integer.MAX_VALUE ;

    /**是否可为空*/
    public boolean nullable()  ;

    /**验证结果返回的状态码，该状态码由自己定义*/
    public int code() default 0 ;

    //自定义注解，这个方法必须加入
    public String message() default "" ;

    /**自定义注解，这个方法必须加入*/
    Class<?>[] groups() default { };

    /**自定义注解，这个方法必须加入*/
    Class<? extends Payload>[] payload() default { };

}
