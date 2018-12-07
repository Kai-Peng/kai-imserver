package im.kai.server.utils;


import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * 工具类
 */
public class Utils {
    /**
     * 是否是null或者空值
     * @param text 需要判断的字符文本
     * @return
     */
    public static boolean isEmpty(String text){
        return text == null || text.length() == 0;
    }
    /**
     * 是否是有效的手机号码
     * @param number 需要判断的手机号，带+国际号
     * @return
     */
    public static boolean isValidMobileNumber(String number) {
        if(isEmpty(number)) return false;

        try{
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(number, "ZZ");
            return phoneNumberUtil.isValidNumber(phoneNumber);
        }catch (Exception ex){
            return false;
        }
    }

    public static void main(String args[]) {

        System.out.println("isValidMobileNumber :" + isValidMobileNumber("+8618802001702"));
    }

   /**
     * 将某对象的值拷贝另外一个对象
     * @param sourceObj 需要拷贝的源对象
     * @param targetCls 目标对象类
     * @param <T> 目标类型
     * @return 拷贝失败则返回null，否则会生成新的对象，并且拷贝源对象与目标对象共有的属性字段值
     */
    public static <T> T copyTo(final Object sourceObj, Class<T> targetCls){
        try {
            final T targetObj = targetCls.newInstance();
            Class sourceCls = sourceObj.getClass();
            ReflectionUtils.doWithFields(targetCls, field -> {
                String fieldName ;
                /*if(field.isAnnotationPresent(JSONField.class) ) {
                    fieldName = field.getAnnotation(JSONField.class).name() ;
                } else {
                    fieldName = field.getName() ;
                }*/
                Field srcField = ReflectionUtils.findField(sourceCls, field.getName());
                if(srcField != null){
                    ReflectionUtils.makeAccessible(field);
                    ReflectionUtils.makeAccessible(srcField);
                    Object srcValue = srcField.get(sourceObj);
                    field.set(targetObj, srcValue);
                }
            }, ReflectionUtils.COPYABLE_FIELDS);
            return targetObj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
