package im.kai.server.service.user.constraint;


import im.kai.server.service.user.constraint.annotation.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 字符字节长度验证
 */
@Slf4j
public class NotBlankConstraint implements ConstraintValidator<NotBlank, CharSequence> {

    private int code ;
    @Override
    public void initialize(NotBlank constraintAnnotation) {

        code = constraintAnnotation.code() ;

    }

    @Override
    public boolean isValid(CharSequence o, ConstraintValidatorContext constraintValidatorContext) {

        if(o == null) {
            return false ;
        }

        return !StringUtils.isBlank(String.valueOf(o));

    }


}
