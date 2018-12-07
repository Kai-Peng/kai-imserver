package im.kai.server.service.room.uitls;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @AUTHER Pengp
 * @DATE 2018/11/9 15:05
 * DAO <-----> DTO
 */
@Slf4j
public class PropertyUtils {

    /**
     * 将source对象的属性填充到destination对象对应属性中
     * @param sList 原始对象集
     * @param d 目标对象
     */
    public static<S,D> List<D> copyProperties(List<S> sList,D d) {
        List<D> dList = new ArrayList<>();
        for (S s:sList) {
            copyProperties(s,d);
            dList.add(d);
            try {
                d = (D) d.getClass().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return dList;
    }

    /**
     * 将source对象的属性填充到destination对象对应属性中
     * @param sList 原始对象集
     * @param d 目标对象
     * @param ignoreProperties 不转换的属性
     */
    public static<S,D> List<D> copyProperties(List<S> sList,D d,String... ignoreProperties ) {
        List<D> dList = new ArrayList<>();
        for (S s:sList) {
            copyProperties(s,d,ignoreProperties);
            dList.add(d);
            try {
                d = (D) d.getClass().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return dList;
    }

    /**
     * 将source对象的属性填充到destination对象对应属性中
     * @param source 原始对象
     * @param destination 目标对象
     */
    public static <S,D> void copyProperties(S source, D destination)  {
        Class clsDestination;
        try {
            clsDestination = Class.forName(destination.getClass().getName());
            Class clsSource = Class.forName(source.getClass().getName());
            Field[] declaredFields = clsDestination.getDeclaredFields();
            for (Field field : declaredFields){
                field.setAccessible(true);
                String fieldName = field.getName();
                try{
                    if("serialVersionUID".equals(fieldName)){
                        continue;
                    }
                    Field sourceField = clsSource.getDeclaredField(fieldName);
                    sourceField.setAccessible(true);
                    field.set(destination,sourceField.get(source));
                }catch (NoSuchFieldException e){
                    // continue;
                }
            }
        } catch (Exception e) {
            log.error(String.format("PropertyUtils error: source (%s),destination (%s)",source,destination));
            throw new RuntimeException("PropertyUtils error");
        }

    }

    /**
     * 将source对象的属性填充到destination对象对应属性中
     * @param source 原始对象
     * @param destination 目标对象
     * @param ignoreProperties 不转换的属性
     */
    public static  <S,D> void copyProperties(S source, D destination,String... ignoreProperties)  {
        Class clsDestination ;
        try {
            clsDestination = Class.forName(destination.getClass().getName());

            Class clsSource = Class.forName(source.getClass().getName());

            Field[] declaredFields = clsDestination.getDeclaredFields();

            for (Field field : declaredFields){
                String fieldName = field.getName();
                Set<String> collect = Stream.of(ignoreProperties).collect(Collectors.toSet());
                collect.add("serialVersionUID");
                if(collect.contains(fieldName)){
                    continue;
                }
                try{
                    field.setAccessible(true);
                    Field sourceField = clsSource.getDeclaredField(fieldName);
                    sourceField.setAccessible(true);
                    field.set(destination,sourceField.get(source));
                }catch (NoSuchFieldException e){
                    // 没有对应属性跳过;
                }
            }
        } catch (Exception e) {
            log.error(String.format("PropertyUtils error: source (%s),destination (%s),ignoreProperties (%s)",source,destination,ignoreProperties.toString()));
            throw new RuntimeException("PropertyUtils error");
        }

    }
}
