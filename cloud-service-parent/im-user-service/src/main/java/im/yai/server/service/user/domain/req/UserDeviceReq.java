package im.kai.server.service.user.domain.req;

import com.alibaba.fastjson.annotation.JSONField;
import im.kai.server.service.user.constraint.annotation.NotBlank;
import im.kai.server.service.user.constraint.annotation.XNotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static im.kai.server.constant.ApiFailCode.Device.*;


/**
 * 用户登陆设备信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDeviceReq implements Serializable {

    /**
     * 设备的编号，采用
     *
     * md5(subtype+name+gcm_id+apn_id+os+imei)
     *
     * 计算得出
     */
    @JSONField(name = "code")
    private String code ;

    /**
     * 设备类型
     *
     * 1 = 手机
     *
     * 2 =平板
     *
     * 3 = PC
     */
    @JSONField(name = "type")
    @XNotNull(code = UNKNOWN_DEVICE_TYPE)
    private Byte type ;

    /**
     * 设备详细类型
     *
     * 0 = 未知
     *
     * 11 = 安卓手机
     *
     * 12 = IOS手机
     *
     * 21 = 安卓平板
     *
     * 22 = IOS平板
     *
     * 31 = PC
     */
    @JSONField(name = "subtype")
    @NotBlank(code = INVALID_DEVICE_INFO)
    private String subtype ;

    /**
     * 设备名称
     */
    @JSONField(name = "name")
    @NotBlank(code = INVALID_DEVICE_NAME)
    private String name ;

    /**
     * app版本
     */
    @JSONField(name = "version")
    //@NotBlank(code = INVALID_APP_VERSION)
    private String version ;


    //app语言
    @JSONField(name = "lang")
    private String lang ;


    /**
     * Google推送服务id
     */
    @JSONField(name = "gcm_id")
    private String gcm_id ;

    /**
     * Apple推送服务id
     */
    @JSONField(name = "apn_id")
    private String apn_id ;

    /**设备的运行系统*/
    @JSONField(name = "os")
    @NotBlank(code = INVALID_OS_VALID)
    private String os ;

    /**设备的imei号*/
    @JSONField(name = "imei")
    private String imei ;

    /**
     * 设备的唯一标记号
     */
    @JSONField(name = "uuid")
    private String uuid ;


}
