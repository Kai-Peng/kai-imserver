package im.kai.server.utils;


import im.kai.server.domain.ApiResult;
import im.kai.server.exception.CustomerErrorException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;

import javax.validation.*;
import java.util.Map;
import java.util.Set;

/**
 * 实体验证器
 */
@Slf4j
public class EntityValidator {

    private static Validator validator = null ;
    static {
        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
                .configure()
               // .failFast( false )
                .addProperty( "hibernate.validator.fail_fast", "true" )
                .buildValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    /***
     * 实体参数合法性快速验证
     * @param entity
     * @return T
     */
    public static <T> Set<ConstraintViolation<T>> fastValidate(T entity) {

        return validator.validate(entity);

    }

    public static <T> void validateResult(Set<ConstraintViolation<T>> set) throws Exception {

        if(set.size() > 0) {
            throw checkAndCreateException(set.iterator().next()) ;
        }
    }


    private static Exception checkAndCreateException(ConstraintViolation cst) {

        Map<String, Object> attributes = cst.getConstraintDescriptor().getAttributes() ;

        int code = 0 ;
        String message = "" ;
        if(attributes.containsKey("code")) {
            code = Integer.parseInt(attributes.get("code").toString()) ;
        }

        if(attributes.containsKey("message")) {
            message = attributes.get("message").toString() ;
        }

        return new CustomerErrorException(code , message) ;

    }
    private static ApiResult check(ConstraintViolation cst)  {

        Map<String, Object> attributes = cst.getConstraintDescriptor().getAttributes() ;

        if(attributes.containsKey("code")) {
            return ApiResult.fail(Integer.parseInt(attributes.get("code").toString())) ;
        }

        throw new ValidationException("bad request : " + cst.getPropertyPath());

    }
    public static ApiResult validateConstraint(Set<ConstraintViolation<?>> set) {
        if(set.size() > 0) {
            return check(set.iterator().next());
        }
        return null ;
    }

}
