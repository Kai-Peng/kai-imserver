package im.kai.server.service.user.constraint;


import im.kai.server.service.user.constraint.annotation.XNotNull;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 字符字节长度验证
 */
@Slf4j
public class NotNullConstraint implements ConstraintValidator<XNotNull, Object> {

    @Override
    public void initialize(XNotNull constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {

        log.info("o : " + o);
       return o != null ;
    }


}
