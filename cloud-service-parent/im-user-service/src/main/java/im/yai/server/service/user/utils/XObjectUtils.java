package im.kai.server.service.user.utils;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 依托的是fastjson 进行对象操作的
 */
@Slf4j
public class XObjectUtils {

    /**
     * 判断一个对象是否为null
     * @param object
     * @return 如果为null ，则为true ，否则为false
     */
    public static boolean isNull(Object object) {

        return object == null ;

    }

    /**
     * 判断一个对象是否非null
     * @param object
     * @return 如果为null ，则为false ，否则为true
     */
    public static boolean isNotNull(Object object) {

        return !isNull(object);

    }

    @Deprecated
    public static <T> T copy(Object from , Class<T> clazz) throws Exception {

        return copy(from , clazz.newInstance());
    }
    /**
     *
     * 两个对象的拷贝 ，优先使用JSONField 指定的字段进行赋值
     *
     * 注意:
     *
     * 1. 这种类型的拷贝，是针对基础类型，较简单数据类型的拷贝，对于复杂类型，只是引用指向
     *
     * 2 . 如果类型不匹配，也不会赋值
     * @param from
     * @param des
     * @throws IllegalAccessException
     */
    @Deprecated
    public static <T> T copy(Object from , T des) throws IllegalAccessException {

         if(isNull(from) || isNull(des)) {
             return null ;
         }

         Field from_fields[] = from.getClass().getDeclaredFields() ;

         Map<String , Field> fieldMap = new HashMap<String , Field>() ;

         for(int i = 0 ; i < from_fields.length ; i ++) {

             Field field = from_fields[i];
             field.setAccessible(true) ;

             String fieldName ;
             if(field.isAnnotationPresent(JSONField.class) ) {
                fieldName = field.getAnnotation(JSONField.class).name() ;
             } else {
                 fieldName = field.getName() ;
             }

             fieldMap.put(fieldName , field) ;

         }

        Field dest_fields[] = des.getClass().getDeclaredFields() ;
        for(int i = 0 ; i < dest_fields.length ; i ++) {

            Field field = dest_fields[i];
            field.setAccessible(true) ;


            String fieldName ;
            if(field.isAnnotationPresent(JSONField.class) ) {
                fieldName = field.getAnnotation(JSONField.class).name() ;
            } else {
                fieldName = field.getName() ;
            }

            Field field1 = fieldMap.get(fieldName) ;
            if(field1 == null) {
                continue ;
            }
           /* if(field.getType() == null) {
                continue;
            }*/
            if(field.getType() == field1.getType()) {
                if(field1 != null) {
                    field.set(des , field1.get(from));
                }
            }

        }

        fieldMap.clear();

        return des ;
    }



}
