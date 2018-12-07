package im.kai.server.service.verificationcode.service;

/**
 * 验证码生成器
 */
public interface VCodeGenerator {
    /**
     * 生成验证码
     * @return
     */
    String generate();
}
