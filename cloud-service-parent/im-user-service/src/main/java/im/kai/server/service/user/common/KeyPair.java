package im.kai.server.service.user.common;

import java.util.HashMap;
import java.util.Map;

public class KeyPair {

    private Map<String , Object> data = new HashMap <String , Object>();

    private KeyPair () {

    }
    public static KeyPair create() {
        return new KeyPair() ;
    }

    public void set(String key , Object value ) {
        data.put(key , value) ;
    }

    public Object get(String key) {
        return data.get(key) ;
    }

    public void clear() {
        data.clear();
    }
}
