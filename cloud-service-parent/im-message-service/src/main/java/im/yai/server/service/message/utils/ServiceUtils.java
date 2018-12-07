package im.kai.server.service.message.utils;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

public class ServiceUtils {
    //本机的TCP服务名以及监听消息队列名，暂定本机IP地址
    public final static String LOCAL_TCP_SERVICE_NAME = getLocalIpAddr();

    //IM_ROOM_SERVICE
    public static String IM_ROOM_SERVICE = "http://im-room-service/v1/rooms/" ;

    /***
     * 创建获取群成员列表的URI
     * @param roomId
     * @return URI
     */
    public static URI buildGetMembersURI(String roomId) {
        return URI.create(IM_ROOM_SERVICE.concat("members/").concat(roomId)) ;
    }

    private static String getLocalIpAddr(){
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostAddress().toString(); //获取本机ip
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "";
        }
    }
}
