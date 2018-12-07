package im.kai.server.service.user.constraint;


import im.kai.server.service.user.constraint.annotation.IntValueEnum;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 取值集合约束
 */
@Slf4j
public class IntValEnumConstraint implements ConstraintValidator<IntValueEnum, Number> {


    private int numbers [] ;
    private boolean nullable ;
    @Override
    public void initialize(IntValueEnum constraintAnnotation) {

        numbers = constraintAnnotation.set() ;
        nullable = constraintAnnotation.nullable() ;
    }

    @Override
    public boolean isValid(Number n, ConstraintValidatorContext constraintValidatorContext) {

        if(nullable && n == null) {
            return true ;
        }
        if(n == null) {     //非null
            return false ;
        }
        int intVal = n.intValue() ;
        for(int i = 0 ; i < numbers.length ; i ++) {
            if(numbers[i] == intVal) {
                return true ;
            }
        }
        return false ;

    }


}
