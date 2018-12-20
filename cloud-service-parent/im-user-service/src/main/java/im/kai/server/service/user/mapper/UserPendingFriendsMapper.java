package im.kai.server.service.user.mapper;

import im.kai.server.service.user.domain.dto.UserPendingFriends;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserPendingFriendsMapper {


    int deleteByPrimaryKey(Long id);

    int insert(UserPendingFriends record);

    int insertSelective(UserPendingFriends record);

    UserPendingFriends selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserPendingFriends record);

    int updateByPrimaryKey(UserPendingFriends record);

    List<UserPendingFriends> getPendingFriends(@Param("user_id") long userId , @Param("start") int start ,
                                               @Param("limit") int end);

    /**
     * 拒绝好友申请
     * @param userId
     * @param friendId
     * @return 影响的数量值
     */
    public int rejectInvitation(@Param("userId") Long userId , @Param("friendId") Long friendId) ;

    /**
     * 获取好友申请信息
     * @param userId
     * @param friendId
     * @return UserPendingFriends 实例
     */
    public UserPendingFriends getOnePendingFriend(@Param("userId") Long userId , @Param("friendId") Long friendId) ;

    /**
     * 删除好友申请记录
     * @param userId
     * @param friendId
     * @return 删除数量
     */
    public int deletePendingFriend(@Param("userId") Long userId , @Param("friendId") Long friendId) ;


}