package im.kai.server.service.user.constraint;


import im.kai.server.service.user.constraint.annotation.PhoneNum;
import im.kai.server.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 字符串正则式匹配
 */
@Slf4j
public class PhoneNumConstraint implements ConstraintValidator<PhoneNum , CharSequence>  {

    private static final Log LOG = LoggerFactory.make();

    @Override
    public void initialize(PhoneNum constraintAnnotation) {


    }

    @Override
    public boolean isValid(CharSequence o, ConstraintValidatorContext constraintValidatorContext) {

        if(o == null)
            return false ;

        String val =  o.toString() ;

        return Utils.isValidMobileNumber(val) ;

    }


}
