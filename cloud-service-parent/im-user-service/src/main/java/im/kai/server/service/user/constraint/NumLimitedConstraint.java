package im.kai.server.service.user.constraint;


import im.kai.server.service.user.constraint.annotation.NumLimited;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 数字范围验证
 */
@Slf4j
public class NumLimitedConstraint implements ConstraintValidator<NumLimited, Integer> {

    private int min  ;
    private int max  ;

    private boolean nullable ;
    @Override
    public void initialize(NumLimited constraintAnnotation) {
        min = constraintAnnotation.min() ;
        max = constraintAnnotation.max() ;
        nullable = constraintAnnotation.nullable() ;
    }

    @Override
    public boolean isValid(Integer val, ConstraintValidatorContext constraintValidatorContext) {


        if(!nullable && val == null) {
            return false ;
        }
        //可为null,必须为null的时候直接返回true，如果不为null，继续验证
        if(nullable && val == null) {
            return true ;
        }
        if(min > max) {
            return false ;
        }
        if(max != Integer.MAX_VALUE) {
           if(val > max) {
               return false ;
           }
        }

        if(min != Integer.MIN_VALUE) { //如果有限制的情况下，那么min最小值必须要
            if(val < min) {
                return false ;
            }
        }


        return true ;
    }


}
