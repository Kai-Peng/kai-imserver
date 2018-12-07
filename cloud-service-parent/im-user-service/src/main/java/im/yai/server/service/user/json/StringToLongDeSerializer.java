package im.kai.server.service.user.json;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;


/**
 *
 * fastjson 反序列化字符串反转类型为long
 * 字符串转换成整形,
 *
 * 注意 : 传递的数据类型要匹配到  文档要求，否则会传递失败
 */
@Slf4j
public class StringToLongDeSerializer implements ObjectDeserializer {


    @Override
    public  Long deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o) {
        try {
            return Long.parseLong(defaultJSONParser.lexer.stringVal()) ;
        } catch (NumberFormatException e) {
            return null ;
        }
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}