package im.kai.server.filter;

import com.alibaba.fastjson.JSONException;
import im.kai.server.constant.ApiFailCode;
import im.kai.server.domain.ApiResult;
import im.kai.server.exception.AuthHeaderException;
import im.kai.server.exception.CustomerErrorException;
import im.kai.server.exception.IMAplicationException;
import im.kai.server.utils.EntityValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static im.kai.server.constant.ApiFailCode.HEADER_AUTH_FAILED;
import static im.kai.server.constant.ApiFailCode.INVALID_JSON;

/**
 * 异常的统一处理入口
 */
@Slf4j
@RestControllerAdvice
public class WebExceptionAspect {
    /**
     * 捕捉IM服务异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) throws IOException {

        e.printStackTrace();
        if(e instanceof JSONException || e instanceof HttpMessageNotReadableException) {

            JSONException exp = (JSONException) e;

            //无效json
            return ResponseEntity.ok().body(ApiResult.fail(INVALID_JSON));
        } else if(e instanceof AuthHeaderException) {
            //请求头验证错误
            return ResponseEntity.ok().body(ApiResult.fail(HEADER_AUTH_FAILED));

        } else if(e instanceof NoHandlerFoundException) {

            //页面不存在
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build() ;

        } else if(e instanceof HttpRequestMethodNotSupportedException){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build() ;

        } else if(e instanceof ConstraintViolationException) {

            ConstraintViolationException exp2 = (ConstraintViolationException) e;

            ApiResult apiResult = EntityValidator.validateConstraint(exp2.getConstraintViolations()) ;

            return ResponseEntity.ok().body(apiResult) ;

        } else if(e instanceof IMAplicationException) {

            log.info("this code : " + ((CustomerErrorException)e).getCode());
            return ((IMAplicationException)e).output();

        }else if(e instanceof MethodArgumentNotValidException){

            return notValidHandler((MethodArgumentNotValidException)e);

        }else {
            log.error("发生系统未知错误", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     *
     * @param exception
     * @return
     */
    private ResponseEntity notValidHandler(MethodArgumentNotValidException exception) {
        List<String> invalidArguments = new ArrayList<String>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            invalidArguments.add(error.getField());
        }
        return ResponseEntity.ok(ApiResult.fail(ApiFailCode.INVALID_REQUEST_PARAMETER, invalidArguments));
    }
}
