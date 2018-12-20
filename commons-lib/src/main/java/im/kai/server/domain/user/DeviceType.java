package im.kai.server.domain.user;

import java.util.Arrays;

/**
 * 设备类型
 */
public enum DeviceType {
    UNKNOWN(0, "未知"),
    PHONE(1, "手机"),
    PAD(2, "平板"),
    PC(3, "PC");
     /**
     * id
     */
    private int id;
    /**
     * 名称
     */
    private String name;

    /**
     * id
     */
    public int getId() {
        return id;
    }

    /**
     * 名称
     */
    public String getName() {
        return name;
    }

    DeviceType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static DeviceType parse(int type){
        for (DeviceType v : DeviceType.values()) {
            if(v.getId() == type) return v;
        }
        throw new IllegalArgumentException("type");
    }
    /**
     * 设备子类型
     */
    public enum SubType {
        UNKNOWN(0, "未知"),
        ANDROID_PHONE(11, "安卓手机"),
        APPLE_PHONE(12, "苹果手机"),
        ANDROID_PAD(21, "安卓平板"),
        APPLE_PAD(21, "IOS平板"),
        PC(31, "PC");

        /**
         * id
         */
        public int getId() {
            return id;
        }

        /**
         * 名称
         */
        public String getName() {
            return name;
        }

        /**
         * id
         */
        private int id;
        /**
         * 名称
         */
        private String name;

        SubType(int id, String name) {
            this.id = id;
            this.name = name;
        }

        /**
         * 获取设备类型
         * @return
         */
        public DeviceType getDeviceType(){
            switch (this) {
                case ANDROID_PHONE:
                case APPLE_PHONE:
                    return DeviceType.PHONE;
                case ANDROID_PAD:
                case APPLE_PAD:
                    return DeviceType.PAD;
                case PC:
                    return DeviceType.PC;
                default:
                    return DeviceType.UNKNOWN;
            }
        }
        public static SubType parse(int subType){
            for (SubType v : SubType.values()) {
                if(v.getId() == subType) return v;
            }
            throw new IllegalArgumentException("subType");
        }
    }
}
