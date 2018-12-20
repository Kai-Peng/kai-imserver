package im.kai.server.domain.req.user;

import im.kai.server.domain.user.DeviceType;
import lombok.Data;
import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * 设备信息
 */
public class Device {

    private Device(String data){
        this.data = data;
    }

    private String data;

    /**
     * APP版本号
     */
    @Getter
    private String appVersion;
    /**
     * 设备语言
     */
    @Getter
    private String language;
    /**
     * 设备类型
     */
    @Getter
    private DeviceType.SubType deviceSubType;
    /**
     * 设备ID
     */
    @Getter
    private String deviceId;

    @Override
    public String toString(){
       return this.data;
    }
    @Override
    public int hashCode(){
        return this.data.hashCode();
    }

    /**
     * 构造器
     */
    public static class Builder{
        /**
         * 生成Device对象数据
         * @return
         * @throws IllegalArgumentException
         * @throws IndexOutOfBoundsException
         */
        public static Device build(String data)
                throws IllegalArgumentException, IndexOutOfBoundsException{
            if(StringUtils.isEmpty(data)) throw new IllegalArgumentException("device");
            String[] splitData = data.split(",");

            String appVersion = splitData[0];
            if(splitData.length < 2) throw new IndexOutOfBoundsException("language");
            String language = splitData[1];
            if(splitData.length < 3) throw new IndexOutOfBoundsException("subType");
            String subType = splitData[2];
            if(splitData.length < 4) throw new IndexOutOfBoundsException("deivceId");
            String deviceId = splitData[3];

            Device device = new Device(data);
            device.appVersion = appVersion;
            device.language = language;
            try {
                device.deviceSubType = DeviceType.SubType.parse(Integer.valueOf(subType));
                device.deviceId = StringUtils.isEmpty(deviceId) || "*".equals(deviceId) ? null : Long.valueOf(deviceId).toString();
            }catch (NumberFormatException n){
                throw new IllegalArgumentException("device");
            }
            return device;
        }
    }
}
