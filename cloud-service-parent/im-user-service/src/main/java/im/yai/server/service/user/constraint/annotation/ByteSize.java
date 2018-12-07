package im.kai.server.service.user.constraint.annotation;


import im.kai.server.service.user.constraint.ByteSizeConstraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 字节长度验证
 */
@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ByteSizeConstraint.class)  // 关联解析类
public @interface ByteSize {

    /***最小字节数，默认是0*/
    public int min() default 0 ;

    /**最大字节数，默认是Integer.MAX_VALUE*/
    public int max() default Integer.MAX_VALUE ;

    /**是否可为空*/
    public boolean nullable() default true ;

    /**取字节编码方式*/
    public String charset() default "utf-8";

    /**验证结果返回的状态码，该状态码由自己定义*/
    public int code() default 0 ;

    //自定义注解，这个方法必须加入
    public String message() default "" ;

    /**自定义注解，这个方法必须加入*/
    Class<?>[] groups() default { };

    /**自定义注解，这个方法必须加入*/
    Class<? extends Payload>[] payload() default { };

}
