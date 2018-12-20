package im.kai.server.service.message.websocket.message;

import com.google.protobuf.ByteString;
import im.kai.server.service.message.protocol.WebSocketProtos;
import org.springframework.web.socket.BinaryMessage;

import java.nio.ByteBuffer;

/**
 * websocket网络数据包
 */
public abstract class WebSocketMessage {
    private long id;
    private float version;
    private WebSocketProtos.WebSocketMessage.Type type;
    private ByteBuffer data;

    /**
     * 根据类型实例化网络数据包
     * @param type
     */
    WebSocketMessage(WebSocketProtos.WebSocketMessage.Type type){
        this(type, 1.0F);
    }
    /**
     * 根据类型及版本实例化网络数据包
     * @param type
     */
    WebSocketMessage(WebSocketProtos.WebSocketMessage.Type type, float version){
        this.id = this.generateId();
        this.type = type;
        this.version = version;
    }
    /**
     * 根据id,类型及版本实例化网络数据包
     * @param type
     */
    WebSocketMessage(long id, WebSocketProtos.WebSocketMessage.Type type, float version){
        this.id = id;
        this.type = type;
        this.version = version;
    }

    /**
     * 生成数据包id
     * @return
     */
    long generateId(){
        return System.currentTimeMillis();
    }

    /**
     * 获取数据
     * @return
     */
    abstract ByteString getData();

    /**
     * 消息id
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * 消息类型
     * @return
     */
    public WebSocketProtos.WebSocketMessage.Type getType() {
        return type;
    }

    /**
     *返回Websocket BinaryMessage格式消息，用于WebSocket传输
     * @return
     */
    public final BinaryMessage build(){
        return new BinaryMessage(buildBytesMsg());
    }

    /**
     *返回protobuf的字节流，用于MQ消息
     * @return
     */
    public final byte[] buildBytesMsg(){
        WebSocketProtos.WebSocketMessage.Builder builder = WebSocketProtos.WebSocketMessage.newBuilder();
        builder.setType(this.type);
        builder.setVersion(version);
        builder.setId(this.id);
        builder.setData(getData());

        return builder.build().toByteArray();
    }
}
