package im.kai.server.service.user.mapper;

import im.kai.server.service.user.domain.dto.UserFriends;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 处理好友Mapper
 */
public interface UserFriendsMapper {

    /**
     * 根据主键ID 删除记录
     * @param id
     * @return 影响数量
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入一条记录
     * @param record UserFriends 实体
     * @return 影响数量
     */
    int insert(UserFriends record);

    /**
     * 插入一条记录，只操作非null的数据
     * @param record  UserFriends 实体
     * @return 影响数量
     */
    int insertSelective(UserFriends record);

    /**
     * 根据主键ID 查询UserFriends 实体
     * @param id
     * @return UserFriends
     */
    UserFriends selectByPrimaryKey(Long id);

    /**
     * 根据主键ID 更新 UserFriends 实体，只操作非null的字段
     * @param record
     * @return 影响数量
     */
    int updateByPrimaryKeySelective(UserFriends record);

    /**
     * 根据主键ID 更新 UserFriends 实体，包括null的字段
     * @param record
     * @return 影响数量
     */
    int updateByPrimaryKey(UserFriends record);

    /**
     * 获取好友列表
     * @param userId 当前用户id
     * @param start id偏移值
     * @param end id 结束值
     * @return
     */
    List<UserFriends> getFriends(@Param("user_id") long userId , @Param("start") int start ,
                                 @Param("end") int end) ;

    /**
     * 根据自身id和好友id作为条件进行信息更新
     * @param record
     * @return
     */
    public int updateFriend(UserFriends record) ;

    /**
     * 是否为好友关系
     * @param userId
     * @param friendId
     * @return 如果是 > 0 ，已存在还有关系
     */
    public int isFriendOf(@Param("userId") Long userId , @Param("friendId") Long friendId) ;

    /**
     * 删除用户的好友
     * @param userId
     * @param friendId
     * @return  如果是 > 0 ，删除成功，否则失败
     */
    public int deleteMyFriend(@Param("userId") Long userId , @Param("friendId") Long friendId) ;

    /**
     *@描述 检查用户好友， 返回正确好友
     *@参数 userid  用户id   friends 用户好友ids
     *@返回值
     *@创建人 Pengp
     *@创建时间 2018/11/23
     */
    List<Long> checkUserFriends(@Param("userId") long userId,@Param("friends") List<Long> friends);
}