package im.kai.server.service.push.mq.config;

/**
 * @AUTHER Pengp
 * @DATE 2018/11/21 16:36
 * 定义数据交换方式枚举
 */
public enum RabbitMqExchangeEnum {

    EXCHANGE_DIRECT("EXCHANGE_DIRECT", "点对点"),

    EXCHANGE_TOPIC("EXCHANGE_TOPIC","消息订阅"),

    EXCHANGE_FANOUT("EXCHANGE_FANOUT","消息分发");

    private String code;

    private String desc;

    RabbitMqExchangeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}