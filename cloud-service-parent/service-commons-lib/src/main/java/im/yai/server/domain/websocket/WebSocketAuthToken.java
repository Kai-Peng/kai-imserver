package im.kai.server.domain.websocket;

import im.kai.server.domain.req.user.Device;
import im.kai.server.exception.ServerErrorException;
import im.kai.server.utils.AesUtils;
import im.kai.server.utils.HmacSHAUtils;
import lombok.Data;
import org.springframework.util.Base64Utils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * websocket的授权会话值
 */
@Data
public class WebSocketAuthToken {
    /**
     * 用户授权token
     */
    private String token;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户设备数据
     */
    private Device device;
    /**
     * 时间戳
     */
    private Long timestamp;


    /**
     * 授权的会话数据是否已超出30s
     * @return
     */
    public boolean isDirty(){
       Long seconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
                - TimeUnit.MILLISECONDS.toSeconds(this.timestamp);
       return seconds < 0 || seconds > 30;
    }

    /**
     * 构造器
     */
    public static class Builder{
        private String serectKey;
        private String signKey;

        /**
         * 实例化构造器
         * @param serectKey 数据加密密匙
         * @param signKey 数据签名密匙
         */
        public Builder(String serectKey, String signKey){
            this.serectKey = serectKey;
            this.signKey = signKey;
        }

        /**
         * 转为查询参数： token=加密数据&sign=签名
         * @param authToken 需要转换的授权会话对象
         * @return
         * @throws Exception
         */
        public String toQueryString(WebSocketAuthToken authToken) throws Exception {
            StringBuilder builder = new StringBuilder();
            builder.append("token=").append(authToken.getToken()).append("&");
            builder.append("uid=").append(authToken.getUserId()).append("&");
            builder.append("device=").append(authToken.getDevice()).append("&");
            builder.append("timestamp=").append(authToken.getTimestamp());

            String serectData = Base64Utils.encodeToUrlSafeString(AesUtils.encryptToBytes(builder.toString(), this.serectKey));
            builder.setLength(0);
            builder.append("token=").append(serectData);

            String sign = Base64Utils.encodeToUrlSafeString(HmacSHAUtils.hashToBytes(this.signKey, builder.toString()));
            builder.append("&").append("sign=").append(sign);
            return builder.toString();
        }

        /**
         * 从查询参数解析数据
         * @param queryString
         * @return
         */
        public WebSocketAuthToken fromQueryString(String queryString) throws IllegalArgumentException{
            if(StringUtils.isEmpty(queryString)) throw new IllegalArgumentException("queryString");
            MultiValueMap<String, String> queryParams = parseQueryString(queryString);
            if(!queryParams.containsKey("token")){
                throw new IllegalArgumentException("token");
            }
            if(!queryParams.containsKey("sign")){
                throw new IllegalArgumentException("sign");
            }

            String reqToken = queryParams.getFirst("token");
            String reqSign = queryParams.getFirst("sign");

            String sign = null;
            try {
                sign = Base64Utils.encodeToUrlSafeString(
                        HmacSHAUtils.hashToBytes(this.signKey, "token=" + reqToken));
            } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
                throw new ServerErrorException("websocket会话签名计算出错", e);
            }
            if(!sign.equals(reqSign)){
                throw new IllegalArgumentException("sign");
            }
            byte[] tokenData = Base64Utils.decodeFromUrlSafeString(reqToken);

            try {
                MultiValueMap<String, String> dataParams = parseQueryString(AesUtils.decrypt(tokenData, this.serectKey));
                WebSocketAuthToken authToken = new WebSocketAuthToken();
                authToken.setToken(dataParams.getFirst("token"));
                authToken.setUserId(dataParams.getFirst("uid"));
                authToken.setDevice(Device.Builder.build(dataParams.getFirst("device")));
                authToken.setTimestamp(Long.parseLong(dataParams.getFirst("timestamp")));
                return authToken;
            } catch (Exception e) {
                throw new IllegalArgumentException("token", e);
            }
        }

        /**
         * 解析查询参数
         * @param queryString
         * @return
         */
        private MultiValueMap<String, String> parseQueryString(String queryString){
            return UriComponentsBuilder.newInstance().query(queryString).build().getQueryParams();
        }
    }
}
