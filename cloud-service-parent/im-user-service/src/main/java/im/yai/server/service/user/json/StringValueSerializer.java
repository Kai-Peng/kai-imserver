package im.kai.server.service.user.json;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * fastjson 序列化为字符串的操作类
 */
@Slf4j
public class StringValueSerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType , int features)
            throws IOException {

        serializer.write(String.valueOf(object));

    }
}