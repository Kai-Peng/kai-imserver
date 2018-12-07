package im.kai.server.service.user.constraint;



import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.UnsupportedEncodingException;

import im.kai.server.service.user.constraint.annotation.ByteSize;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
/**
 * 字符字节长度验证
 */
@Slf4j
public class ByteSizeConstraint implements ConstraintValidator<ByteSize, CharSequence> {

    private static final Log LOG = LoggerFactory.make();

    private int min = 0 ;
    private int max = 0 ;

    private boolean nullable = false ;
    private String charset ;

    private int code ;
    @Override
    public void initialize(ByteSize constraintAnnotation) {
        min = constraintAnnotation.min() ;
        max = constraintAnnotation.max() ;
        nullable = constraintAnnotation.nullable() ;
        charset = constraintAnnotation.charset() ;
        code = constraintAnnotation.code() ;

        validateParameters() ;
    }

    @Override
    public boolean isValid(CharSequence o, ConstraintValidatorContext constraintValidatorContext) {

        if(nullable && o == null) {
            return true ;
        }
        try {

            int len = o.toString().getBytes(charset).length ;

            return min <= len && max >= len ;

        } catch (UnsupportedEncodingException e) {
           return false ;
        }

    }

    private void validateParameters() {
        if (this.min < 0) {
            throw LOG.getMinCannotBeNegativeException();
        } else if (this.max < 0) {
            throw LOG.getMaxCannotBeNegativeException();
        } else if (this.max < this.min) {
            throw LOG.getLengthCannotBeNegativeException();
        }
    }
}
