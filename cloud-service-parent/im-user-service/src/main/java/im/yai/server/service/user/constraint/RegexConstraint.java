package im.kai.server.service.user.constraint;


import im.kai.server.service.user.constraint.annotation.RegexMatch;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 字符串正则式匹配, 只要有一个正则式满足条件，那么就通过验证
 */
@Slf4j
public class RegexConstraint implements ConstraintValidator<RegexMatch, CharSequence>  {

    //正则式
    private String regex[] ;

    private int code ;
    private boolean nullable = true ;
    @Override
    public void initialize(RegexMatch constraintAnnotation) {

        nullable = constraintAnnotation .nullable() ;
        regex = constraintAnnotation.regex();
        code = constraintAnnotation.code() ;

    }

    @Override
    public boolean isValid(CharSequence o, ConstraintValidatorContext constraintValidatorContext) {


        if(nullable && o == null) {
            return true ;
        }

        String val = o.toString() ;
        for(int i = 0 ; i < regex.length ; i ++) {

            if(val.matches(regex[i])) {
                return true ;
            }
        }
        return false ;

    }


}
