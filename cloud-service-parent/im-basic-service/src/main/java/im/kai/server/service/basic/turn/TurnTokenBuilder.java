package im.kai.server.service.basic.turn;

import im.kai.server.exception.ServerErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * WebRTC会话构建器
 */
@Component
@Slf4j
public class TurnTokenBuilder {
    private final byte[] secretKeys;
    private final List<String> hosts;

    /**
     *
     * @param configuration
     */
    public TurnTokenBuilder(TurnProperties configuration){
        this.secretKeys = configuration.getSecret().getBytes();
        this.hosts = configuration.getHosts();
    }

    /**
     * 生成一个turn会话
     * @return
     */
    public TurnToken build(){
        try {
            Mac mac                = Mac.getInstance("HmacSHA1");
            long   validUntilSeconds  = (System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)) / 1000;
            long   user               = Math.abs(new SecureRandom().nextInt());
            String username           = validUntilSeconds + ":"  + user;

            mac.init(new SecretKeySpec(this.secretKeys, "HmacSHA1"));
            String password = Base64Utils.encodeToString(mac.doFinal(username.getBytes()));

            return new TurnToken(username, password, hosts);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new ServerErrorException("构造WebRTC turn会话时出错", e);
        }
    }
}
