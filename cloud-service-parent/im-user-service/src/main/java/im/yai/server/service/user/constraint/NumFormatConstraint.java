package im.kai.server.service.user.constraint;


import im.kai.server.service.user.constraint.annotation.NumValueFormat;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 数字格式验证
 */
@Slf4j
public class NumFormatConstraint implements ConstraintValidator<NumValueFormat, CharSequence> {


    @Override
    public boolean isValid(CharSequence val, ConstraintValidatorContext constraintValidatorContext) {


        if(val == null) {
            return false ;
        }

        return String.valueOf(val).matches("[0-9]+");

    }


}
