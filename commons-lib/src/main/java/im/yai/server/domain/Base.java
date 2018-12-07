package im.kai.server.domain;

import im.kai.server.utils.Utils;

import java.io.Serializable;

/**
 * 领域数据模型的基类
 */
public abstract class Base implements Serializable {
    /**
     * 拷贝数据
     * @param cls 目标对象类
     * @param <T> 目标类型
     * @return 拷贝失败则返回null，否则会生成新的对象，并且拷贝源对象与目标对象共有的属性字段值
     */
    public <T> T copyTo(Class<T> cls){
        return Utils.copyTo(this, cls);
    }
}
