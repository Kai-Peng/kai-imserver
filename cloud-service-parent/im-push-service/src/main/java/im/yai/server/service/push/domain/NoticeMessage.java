package im.kai.server.service.push.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;


/**
 * @AUTHER Pengp
 * @DATE 2018/11/17 15:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeMessage implements Serializable {

    /**弃用*/
    private String              apnToken;
    /**通知*/
    private String              noticeMessage;
    /***/
    private Integer             badge;
    /**声音*/
    private String              sound;
    /**token*/
    private String[]            token;
    /**自定义*/
    private Map<String,String>  customDictionary;
}
