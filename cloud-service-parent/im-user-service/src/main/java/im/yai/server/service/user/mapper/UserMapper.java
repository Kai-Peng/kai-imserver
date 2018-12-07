package im.kai.server.service.user.mapper;

import im.kai.server.service.user.domain.dto.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    /**
     * 根据User表主键id 来删除记录
     * @param id
     * @return 影响数量
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入一条User 表记录
     * @param record User 实例
     * @return 影响数量
     */
    int insert(User record);

    /**
     * 插入一条User 表记录 , 只操作非null的字段
     * @param record
     * @return 影响数量
     */
    int insertSelective(User record);

    /**
     * 根据User表主键id 来查询User实体
     * @param id
     * @return User
     */
    User selectByPrimaryKey(Long id);

    /**
     * 根据用户表主键ID，更新非null字段
     * @param record
     * @return 影响数量
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * 根据用户表主键ID，更新User实体
     * @param record
     * @return 影响数量
     */
    int updateByPrimaryKey(User record);

    /**
     * 根据用户手机号码获取用户实体
     * @param mobile
     * @return  NowSession 实例
     */
    User selectByMobile(String mobile) ;

    /**
     * 根据im 号获取用户实体
     * @param im_number
     * @return NowSession 实例
     */
    User selectByIMNum(String im_number) ;

    /**
     * 获取指定手机号码的记录数量
     * @param mobile
     * @return 记录数
     */
    int getCount(@Param("mobile") String mobile) ;

    List<Long> checkUsers(@Param("users") List<Long> users);
}