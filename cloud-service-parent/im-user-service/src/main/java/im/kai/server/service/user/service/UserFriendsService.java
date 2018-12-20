package im.kai.server.service.user.service;

import im.kai.server.service.user.domain.dto.UserFriends;
import im.kai.server.service.user.mapper.UserFriendsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFriendsService {

    private @Autowired UserFriendsMapper userFriendsMapper;
    public int deleteByPrimaryKey(Long id) {

        return userFriendsMapper.deleteByPrimaryKey(id) ;
    }

    public int insert(UserFriends record){
        return userFriendsMapper.insert(record) ;
    }

    public int insertSelective(UserFriends record){
        return userFriendsMapper.insertSelective(record) ;
    }

    public UserFriends selectByPrimaryKey(Long id){
        return userFriendsMapper.selectByPrimaryKey(id) ;
    }

    public int updateByPrimaryKeySelective(UserFriends record){
        return userFriendsMapper.updateByPrimaryKeySelective(record) ;
    }

    public int updateByPrimaryKey(UserFriends record){
        return userFriendsMapper.updateByPrimaryKey(record) ;
    }

    public List<UserFriends> getFriends(long user_id , int start , int end) {

        return userFriendsMapper.getFriends(user_id , start , end) ;

    }

    public int updateFriend(UserFriends record) {

        return userFriendsMapper.updateFriend(record) ;

    }

    /**
     * 插入用户表,互相添加用户
     * 不能自己添加自己，
     * @param friends
     * @return
     */
    public int addFriend(UserFriends friends) {


        userFriendsMapper.insert(friends) ;


        //用户id和朋友id互换
        UserFriends uFriend = new UserFriends() ;
        uFriend.setFriendId(friends.getUserId());
        uFriend.setUserId(friends.getFriendId());
        uFriend.setBlocked(false);
        uFriend.setType(1);
        uFriend.setDnd_mode((byte)0);
        uFriend.setCreated(friends.getCreated());

        userFriendsMapper.insert(uFriend) ;

        return 1 ;

    }

    public int isFriendOf(Long userId , Long friendId) {
        return userFriendsMapper.isFriendOf(userId , friendId);
    }

    /**
     * 删除好友记录，双方相互删除
     * @param userId
     * @param friendId
     * @return
     */
    public boolean deleteMyFriend(Long userId , Long friendId) {

        int count = userFriendsMapper.deleteMyFriend(userId , friendId) ;
        int count2 = userFriendsMapper.deleteMyFriend(friendId , userId) ;

        return count > 0 && count2 > 0 ;

    }

    /**
     *@描述 检查用户好友，返回正确好友
     *@参数 userId 用户id    friends 用户好友ids
     *@返回值 正确好友
     *@创建人 Pengp
     *@创建时间 2018/11/23
     */
    public List<Long> checkUserFriends(long userId, List<Long> friends) {
        friends = userFriendsMapper.checkUserFriends(userId,friends);
        return friends;
    }
}