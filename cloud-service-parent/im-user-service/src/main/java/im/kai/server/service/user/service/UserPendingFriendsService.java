package im.kai.server.service.user.service;

import im.kai.server.service.user.domain.dto.UserPendingFriends;
import im.kai.server.service.user.mapper.UserPendingFriendsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户好友申请列表
 */
@Service
public class UserPendingFriendsService {

    private @Autowired UserPendingFriendsMapper userPendingFriendsMapper;

    public int deleteByPrimaryKey(Long id) {
        return userPendingFriendsMapper.deleteByPrimaryKey(id) ;
    }

    public int insert(UserPendingFriends record){
        return userPendingFriendsMapper.insert(record) ;
    }

    public int insertSelective(UserPendingFriends record) {
        return userPendingFriendsMapper.insertSelective(record) ;
    }

    public UserPendingFriends selectByPrimaryKey(Long id) {
        return userPendingFriendsMapper.selectByPrimaryKey(id) ;
    }

    public int updateByPrimaryKeySelective(UserPendingFriends record) {
        return userPendingFriendsMapper.updateByPrimaryKeySelective(record) ;
    }

    public int updateByPrimaryKey(UserPendingFriends record) {
        return userPendingFriendsMapper.updateByPrimaryKey(record) ;
    }

    /**
     * 获取好友列表
     * @param userId 用户ID
     * @param start 申请列表ID 偏移量
     * @param limit 申请列表ID 终点值
     * @return List<UserPendingFriends>
     */
    public List<UserPendingFriends> getPendingFriends(long userId , int start , int limit){
        return userPendingFriendsMapper.getPendingFriends(userId , start , limit) ;
    }

    /**
     * 拒绝好友申请
     * @param userId 用户ID
     * @param friendId 好友ID
     * @return 影响数量
     */
    public int rejectInvitation(Long userId , Long friendId) {
        return userPendingFriendsMapper.rejectInvitation(userId , friendId) ;
    }

    /**
     * 获取一条好友申请记录
     * @param userId 用户id
     * @param friendId 好友id
     * @return UserPendingFriends 实例
     */
    public UserPendingFriends getOnePendingFriend(Long userId , Long friendId) {
        return userPendingFriendsMapper.getOnePendingFriend(userId , friendId) ;
    }

    /**
     * 删除申请记录
     * @param userId 用户id
     * @param friendId 好友oid
     * @return 影响数量
     */
    public int deletePendingFriend(Long userId ,Long friendId) {
        return userPendingFriendsMapper.deletePendingFriend(userId , friendId) ;
    }


}