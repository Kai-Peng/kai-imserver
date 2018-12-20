package im.kai.server.service.user.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import im.kai.server.constant.AesConstant;
import im.kai.server.domain.ApiResult;
import im.kai.server.exception.CustomerErrorException;
import im.kai.server.service.user.constraint.annotation.NumValueFormat;
import im.kai.server.service.user.domain.dto.*;
import im.kai.server.service.user.domain.req.*;
import im.kai.server.service.user.domain.resp.*;
import im.kai.server.service.user.executor.CommonExecutor;
import im.kai.server.service.user.executor.DefaultCommonExecutor;
import im.kai.server.service.user.executor.ExceptionExecutor;
import im.kai.server.service.user.filter.annotation.AuthToken;
import im.kai.server.service.user.filter.annotation.NowSession;
import im.kai.server.service.user.service.*;
import im.kai.server.service.user.utils.*;
import im.kai.server.service.user.constraint.annotation.NumLimited;
import im.kai.server.utils.AesUtils;
import im.kai.server.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static im.kai.server.constant.ApiFailCode.INVALID_JSON;
import static im.kai.server.constant.ApiFailCode.INVALID_PARAMETER;
import static im.kai.server.constant.ApiFailCode.UserManage.*;
import static im.kai.server.service.user.Product.BOOL_AUTH_HEADER;

@Slf4j
@RestController
@RequestMapping("/v1/friends")
@Validated
public class FriendController extends BaseController {

    @Autowired
    private UserService userService ;

    @Autowired
    private UserFriendsService userFriendsService;
    @Autowired
    private UserProfileService userProfileService ;
    @Autowired
    private UserPendingFriendsService userPendingFriendsService ;

    @Autowired
    private SyncDataVersionService syncDataVersionService ;
    /**
     *
     * v1 : 已完成
     * 提交好友申请验证信息
     *
     * TODO 添加到im_pending_user 表 ，
     *
     * @param request
     * @return ApiResult instance
     */
    @AuthToken(mustDoAuth = BOOL_AUTH_HEADER)
    @PostMapping(value = "/handshake" , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResult submitInvitationVerification(HttpServletRequest               request ,
                                                  @RequestBody UserVerifyMessageReq friend ,
                                                  @NowSession  UserRequest userRequest   ) {

        Long currentUserId = userRequest.getId() ;
        ConditionUtils.create().ifTrue(friend.getUserId().equals(currentUserId) , NOT_ALLOWED_SELF , "不能自己添加自己")
        .elseIf(userFriendsService.isFriendOf(currentUserId , friend.getUserId()) > 0 , FORBID_REPEAT_ADD_FRIEND , "不能重复添加")
        .elseIf(XObjectUtils.isNull(userProfileService.selectByPrimaryKey(friend.getUserId())) , UNKNOWN_USER_ID , "未知用户ID");

        UserPendingFriends pendingFriend = new UserPendingFriends() ;

        pendingFriend.setMessage(friend.getMessage());
        pendingFriend.setUserId(currentUserId);
        pendingFriend.setFriendId(friend.getUserId());
        pendingFriend.setCreated(TimeUtils.nowTimeStamp());

        return ResponseUtils.returnFor(userPendingFriendsService.insert(pendingFriend) > 0  ,
                                            ApiResult.success() , ApiResult.fail(ADD_VERIFY_FAILED)) ;

    }

    /**
     * 更新用户好友通讯录版本号
     * @param currentUserId
     */
    private ApiResult resetSynSettingVersion(Long currentUserId , boolean success , int failCode , String message)
            throws Exception {

        return AssertUtils.assertExecutor(new ExceptionExecutor<ApiResult, Boolean>() {

            @Override
            public ApiResult execute(Boolean b) {
                if(!b) {
                    return null ;
                }
                SyncDataVersion syncDataVersion = new SyncDataVersion() ;
                syncDataVersion.setUserId(currentUserId);
                syncDataVersion.setFriendsVersion(System.currentTimeMillis());
                syncDataVersionService.updateByPrimaryKeySelective(syncDataVersion) ;
                return ApiResult.success();
            }

            @Override
            public Exception exceptionResult(Exception e) {

                return new CustomerErrorException(failCode , message);
            }
        } , success) ;



    }
    /**
     * v1 ：已完成
     * 同意好友申请
     * @param friendId
     * @return ApiResult
     */
    @AuthToken(mustDoAuth = BOOL_AUTH_HEADER)
    @RequestMapping(value = "/handshake/{friend_id}" , method = RequestMethod.PUT)
    @Transactional
    public ApiResult agreeFriendApplication(HttpServletRequest request , @PathVariable(name="friend_id")
                                                  @NumValueFormat(code = UNKNOWN_USER_ID) String friendId ,
                                                  @NowSession  UserRequest userRequest) throws Exception {

        long currentUserId = userRequest.getId() ;
        Long friendId2 = Long.parseLong(friendId) ;
        UserPendingFriends pendingFriends = userPendingFriendsService.getOnePendingFriend(friendId2 , currentUserId) ;

        ConditionUtils.create()
        .ifTrue(XObjectUtils.isNull(pendingFriends) ,  NOT_FOUND_INVITATION , "没有找到好友申请")
        .elseIf(XObjectUtils.isNull(userProfileService.selectByPrimaryKey(friendId2)) , NOT_FIND_FRIEND ,"没有找到指定好友")
        .elseIf(userFriendsService.isFriendOf(currentUserId , friendId2) > 0 , FORBID_REPEAT_ADD_FRIEND ,"不能重复添加好友");

        UserFriends friends = new UserFriends() ;
        friends.setType(1);
        friends.setBlocked(false);
        friends.setUserId(currentUserId);
        friends.setFriendId(friendId2);
        friends.setDnd_mode((byte)0);

        userFriendsService.addFriend(friends) ;
        userPendingFriendsService.deletePendingFriend(friendId2 , currentUserId) ;

        resetSynSettingVersion(currentUserId , true , AGREE_APPLICATION_FAILED ,"同意好友申请失败");

        return ApiResult.success() ;
    }

    /**
     *
     * v1 : 已完成
     * 拒绝好友申请
     * @param friendId
     * @return ApiResult instance
     */
    @AuthToken(mustDoAuth = BOOL_AUTH_HEADER)
    @RequestMapping(value = "/handshake/{friend_id}" , method = RequestMethod.DELETE)
    public ApiResult rejectFriendApplication(HttpServletRequest request , @PathVariable(name="friend_id")
                                                 @NumValueFormat(code = UNKNOWN_USER_ID) String friendId ,
                                                 @NowSession  UserRequest userRequest                  ) {

        Long friendId2 = Long.parseLong(friendId) ;
        boolean success = userPendingFriendsService.rejectInvitation(friendId2 , userRequest.getId()) > 0 ;
        return ResponseUtils.returnFor(success , ApiResult.success() ,  ApiResult.fail(REJECT_INVITATION_FAILED)) ;

    }

    /**
     *
     *  v1 ：已完成
     * 直接添加好友
     *  TODO 需要更新版本号？
     * 需要判断是否可以直接添加好友
     *
     * 好友关系一旦成立，双方的申请记录应该删除。
     *
     * @param friendId 要添加的用户Id
     * @return ApiResult
     */
    @AuthToken(mustDoAuth = BOOL_AUTH_HEADER)
    @RequestMapping(value = "/relation/{friend_id}" , method = RequestMethod.PUT)
    public ApiResult addFriendWithId(HttpServletRequest request ,
                                     @NumValueFormat(code = UNKNOWN_USER_ID)
                                     @PathVariable(name="friend_id")  String friendId ,
                                     @NowSession  UserRequest userRequest) throws Exception {

        Long friendId2 = Long.parseLong(friendId) ;

        UserProfile userProfile = userProfileService.selectByPrimaryKey(friendId2) ;

        ConditionUtils.create().ifTrue(XObjectUtils.isNull(userProfile) , NOT_FIND_FRIEND , "用户不存在")
        .elseIf(userProfile.getFriendMode() != 1 , NON_VERIFY_INVITATION_INFO , "需要验证")
        .elseIf(userFriendsService.isFriendOf(userRequest.getId() , friendId2) > 0 , FORBID_REPEAT_ADD_FRIEND , "已经是好友，不能重复添加") ;

        UserFriends userFriends = new UserFriends() ;
        //手机号码备注作为好友备注
        //userFriends.setRealName(userProfile.getNickName());
        userFriends.setFriendId(friendId2);
        userFriends.setType(1);                             //好友
        userFriends.setBlocked(false);
        userFriends.setUserId(userRequest.getId());

        userFriendsService.addFriend(userFriends);

        /** 删除相互的申请关系*/
        userPendingFriendsService.deletePendingFriend(friendId2 , userRequest.getId()) ;
        userPendingFriendsService.deletePendingFriend(userRequest.getId() , friendId2) ;

        return resetSynSettingVersion(userRequest.getId() , true , ADD_DIRECT_USER_FAILED ,"直接添加好友失败") ;
    }

    /**
     * v1 ：已完成
     * 删除好友
     *
     * TODO 需要确定是否双方关系，两条记录一起删除
     *
     * @param friendId 要添加的用户Id
     * @return ApiResult
     */
    @Transactional
    @AuthToken(mustDoAuth = BOOL_AUTH_HEADER)
    @RequestMapping(value = "/relation/{friend_id}" , method = RequestMethod.DELETE)
    public ApiResult delFriendWithId(HttpServletRequest     request ,  @PathVariable(name="friend_id")
                                     @NumValueFormat(code = UNKNOWN_USER_ID) String friendId ,
                                     @NowSession  UserRequest userRequest) throws Exception {

        boolean success = userFriendsService.deleteMyFriend(userRequest.getId() , Long.parseLong(friendId));

        ApiResult apiResult = resetSynSettingVersion(userRequest.getId() , success , REMOVE_FRIEND_FAILED ,"移除好友失败") ;
        log.info("apiResult del friend: " + apiResult);
        return apiResult ;

    }




    /**
     *
     * v1: 已完成
     * 更新好友备注信息 ，备注名称，备注描述
     *
     * 注意: 可能客户端可以更新的字段都没有更新。
     *
     * TODO 注意friend_id的特殊性，可能往后，不再是整形的字符串
     * @param request
     * @return ApiResult
     */
    @AuthToken(mustDoAuth = BOOL_AUTH_HEADER)
    @RequestMapping(value = "/mark" , method = RequestMethod.POST)
    public ApiResult updateFriendMarkInfo(HttpServletRequest request , @RequestBody UserMarkReq mark ,
                                          @NowSession  UserRequest userRequest                      ) {

        //全部为null，不作更新，直接返回
        if(XStringUtils.allOfNull(mark.getDescription() ,mark.getRealName() , mark.getMobile())) {
            return ApiResult.success() ;
        }

        UserFriends userFriend = Utils.copyTo(mark , UserFriends.class);
        userFriend.setFriendId(mark.getFriendId());
        userFriend.setUserId(userRequest.getId());

        return ResponseUtils.affectedOk(userFriendsService.updateFriend(userFriend) ,
                    ApiResult.success() , ApiResult.fail(MARK_FRIEND_INFO_FAILED)) ;

    }

    /**
     *
     * v1 : 已完成
     * TODO 获取申请添加好友的信息
     * 获取好友申请列表
     *
     * @return ApiResult instance
     *
     */
    @AuthToken(mustDoAuth = BOOL_AUTH_HEADER)
    @RequestMapping(value = "/pending" , method = RequestMethod.GET)
    public ApiResult getInvitation( HttpServletRequest  request ,
                                    @RequestParam("start")
                                    @NumLimited(code = INVALID_PARAMETER , nullable = false, min = 0 )  Integer start ,

                                    @RequestParam(value = "limit" , required = false)
                                    @NumLimited(code = INVALID_PARAMETER , nullable = false , min = 0 , max = 200) Integer limit ,

                                    @NowSession  UserRequest userRequest) throws Exception {


        PendingFriendsBodyResp bodyResp = new PendingFriendsBodyResp();
        List<UserPendingFriends> pendingFriends =
                userPendingFriendsService.getPendingFriends(userRequest.getId() , start , limit) ;

        List<PendingFriendsResp> pendingFriendsRespList = new ArrayList<PendingFriendsResp>() ;

        for(int i = 0 ; i < pendingFriends.size() ; i ++) {

            UserPendingFriends item = pendingFriends.get(i) ;
            UserProfile profile = userProfileService.selectByPrimaryKey(item.getUserId()) ;

            if(profile == null) {
                continue ;
            }
            PendingFriendsResp pending = Utils.copyTo(profile ,PendingFriendsResp.class) ;
            pending.setCreated(item.getCreated());
            pending.setMessage(item.getMessage());

            pendingFriendsRespList.add(pending) ;

        }

        if(pendingFriends.size() > 0) { //降序排序，取最后一个ID值
            bodyResp.nextStart = pendingFriends.get(pendingFriends.size() - 1).getId() ;
            bodyResp.hasMore = pendingFriends.size() >= limit ;
        } else {
            bodyResp.nextStart = 0L ;   //如果没有更多的数据，将从0开始重新计数
        }

        bodyResp.setFriends(pendingFriendsRespList);

        return ApiResult.success(bodyResp) ;

    }

    /**
     * v1：已完成
     * 屏蔽好友
     *
     * {friend_id} 好友id
     * @return  ApiResult instance
     */
    @RequestMapping(value = "/blacklist/{friend_id}" , method = RequestMethod.PUT)
    public ApiResult blockFriend(HttpServletRequest request ,
                                 @PathVariable(name="friend_id")
                                 @NumValueFormat(code = UNKNOWN_USER_ID) String friendId ,
                                 @NowSession  UserRequest userRequest) throws Exception {

        int count = userFriendsService.updateFriend(blacklist(Long.parseLong(friendId) ,true , userRequest.getId())) ;

        return resetSynSettingVersion(userRequest.getId() , count > 0 , BLOCK_USER_FAILED , "屏蔽好友失败") ;

    }

    /**
     * 屏蔽或取消屏蔽好友
     * @param friendId 好友id
     * @param isBlock 是否屏蔽，true : 屏蔽 ， false : 不屏蔽
     * @return UserFriends
     */
    private UserFriends blacklist(long friendId , boolean isBlock , Long userId) {

        UserFriends userFriendsEntity = new UserFriends() ;
        userFriendsEntity.setUserId(userId);
        userFriendsEntity.setBlocked(isBlock);
        userFriendsEntity.setFriendId(friendId);

        return userFriendsEntity ;

    }

    /**
     * 取消屏蔽
     * v1 : 已完成
     * @param request
     * @param friendId
     * @return
     */
    @AuthToken(mustDoAuth = BOOL_AUTH_HEADER)
    @RequestMapping(value = "/blacklist/{friend_id}" , method = RequestMethod.DELETE)
    public ApiResult unBlockFriend(HttpServletRequest request ,
                                   @PathVariable(name="friend_id")
                                   @NumValueFormat(code = UNKNOWN_USER_ID) String friendId ,
                                   @NowSession  UserRequest userRequest                   ) throws Exception {

        UserFriends f = blacklist(Long.parseLong(friendId),false , userRequest.getId()) ;
        int count = userFriendsService.updateFriend(f) ;

        return resetSynSettingVersion(userRequest.getId() , count > 0 , UNBLOCK_USER_FAILED , "取消屏蔽失败") ;


    }

    /**
     * 好友通讯录数据同步接口
     *
     * ATTENTION :
     *
     * 1. 如果本地版本与服务端的版本号一样，则此列表无数据，为null，
     * 2. 表示客户端的好友列表已是最新；如果没有任何好友，则为空数组
     *
     * @param request
     * @param version 客户端传递的好友列表版本号
     * @return ApiResult instance
     */
    @AuthToken(mustDoAuth = BOOL_AUTH_HEADER)
    @RequestMapping(value = "/sync/{version}" , method = RequestMethod.GET)
    public ApiResult synFriendList(HttpServletRequest request ,
                                  @PathVariable(name="version")
                                  @NumValueFormat(code = INVALID_VERSION) String version ,
                                  @NowSession  UserRequest userRequest ) throws Exception {

        String lastVersionNo = lastVersionNo(userRequest.getId()) ;

        //返回统一处理
        CommonExecutor<ApiResult ,List> commonExecutor = new DefaultCommonExecutor<ApiResult, List>() {
            @Override
            public ApiResult doExecutor(List list)  {

                UserFriendListResp resp = new UserFriendListResp() ;
                resp.setVersion(Long.parseLong(lastVersionNo));
                resp.setFriends(list);

                return ApiResult.success(resp) ;
            }

        } ;

        if(lastVersionNo.equals(version)) {//版本号相同 , 返回null

            return commonExecutor.doExecutor(null) ;
        }

        //版本号不同，搜索。5000个好友限制的
        List<UserFriends> userFriendsList = userFriendsService.getFriends(userRequest.getId() , 0 , 5000) ;

        if(userFriendsList == null && userFriendsList.size() == 0) {
            return commonExecutor.doExecutor(new ArrayList()) ;
        }

        List<UserFriendResp> userFriendList = new ArrayList<UserFriendResp>() ;
        for(int i = 0 ; i < userFriendsList.size() ; i ++) {

            UserFriends friend = userFriendsList.get(i) ;
            UserProfile userProfile = userProfileService.selectByPrimaryKey(friend.getFriendId()) ;

            UserFriendResp userFriendResp = new UserFriendResp() ;

            userFriendResp.setFriendId(friend.getFriendId());
            userFriendResp.setAvatar(userProfile.getAvatar());
            userFriendResp.setBlocked(friend.getBlocked());
            userFriendResp.setCreated(friend.getCreated());
            userFriendResp.setDescription(friend.getDescription());
            userFriendResp.setNickName(userProfile.getNickName());
            userFriendResp.setRealName(friend.getRealName());

            userFriendResp.setType(friend.getType());
            userFriendList.add(userFriendResp) ;

        }

        return commonExecutor.doExecutor(userFriendList) ;

    }

    /**
     * v1 ：已完成
     * 批量导入通讯录好友接口
     *
     *  批量导入手机通讯录中已注册APP且未开启“加好友需要验证”功能的好友
     *
     *  不能重复添加
     *  关键字 : 不是好友, 对方没有设置好友验证
     *
     *  不能自己添加自己
     *
     * @return ApiResult
     */
    @Transactional
    @AuthToken(mustDoAuth = BOOL_AUTH_HEADER)
    @RequestMapping(value = "/import" , method = RequestMethod.POST)
    public ApiResult importUser(HttpServletRequest request , @RequestBody UserImportReq userImportReq ,
                                            @NowSession  UserRequest userRequest ) throws Exception {

        List<UserImportItem> users = AssertUtils.assertExecutor(new ExceptionExecutor<List<UserImportItem>, String>() {
            @Override
            public List<UserImportItem> execute(String p) {
                return JSON.parseObject(p , new TypeReference<List<UserImportItem>>(){});
            }

            @Override
            public Exception exceptionResult(Exception e) {
                return new CustomerErrorException(INVALID_JSON , "无效JSON");
            }

        } , AesUtils.decrypt(userImportReq.getFriends() , AesConstant.DEBUG_AES_KEY));


        for(int i = 0 ; i < users.size() ; i ++) {

            UserImportItem newUserItem = users.get(i) ;

            if(StringUtils.isBlank(newUserItem.getNumber()) ||  //手机号码或名称为空，不添加
                                StringUtils.isBlank(newUserItem.getName())) {
                continue ;
            }

            User newUser = userService.getWithMobile(newUserItem.getNumber()) ;

            if(XObjectUtils.isNull(newUser)) {                  //用户不存在
                log.info("user is not exist");
                continue ;
            }

            int count = userFriendsService.isFriendOf(userRequest.getId() , newUser.getId()) ;

            if(count > 0) {                                     //已添加过
                continue ;
            }

            UserProfile userProfile = userProfileService.selectByPrimaryKey(newUser.getId()) ;
            Byte friendMode = userProfile.getFriendMode() ;
            if(friendMode.equals(2)) {                          //需要验证
                continue ;
            }

            UserFriends userFriends = new UserFriends() ;
            //手机号码备注作为好友备注
            userFriends.setRealName(newUserItem.getName());
            userFriends.setFriendId(newUser.getId());
            userFriends.setType(1); //好友
            userFriends.setBlocked(false);
            userFriends.setUserId(userRequest.getId());
            userFriends.setCreated(TimeUtils.nowTimeStamp());
            /**
             *
             */
            userFriendsService.addFriend(userFriends);


        }

        return ApiResult.success() ;

    }


    /**
     * 批量验证手机号是否注册接口
     * @param request
     * @param userRegisterReq
     * @return ApiResult
     */
    @AuthToken(mustDoAuth = BOOL_AUTH_HEADER)
    @RequestMapping(value = "/register" , method = RequestMethod.PUT)
    public ApiResult checkRegister(HttpServletRequest request , @RequestBody UserRegisterReq userRegisterReq) throws Exception {

        JSONArray numbers = AssertUtils.assertExecutor(new ExceptionExecutor<JSONArray, String>() {

            @Override
            public JSONArray execute(String p) {
                return JSON.parseArray(p);
            }

            @Override
            public Exception exceptionResult(Exception e) {
                return new CustomerErrorException(INVALID_NUMBER_LIST ,"无效手机列表");
            }

        } , AesUtils.decrypt(userRegisterReq.getFriends() , AesConstant.DEBUG_AES_KEY));

        //超过300个的话，截断300个之后的数据
        List<Object> numberList = numbers.size() > 300 ? numbers.subList(0 , 299) : numbers.toJavaList(Object.class) ;

        List<UserRegisterItemResp>  registerItemRespList = new ArrayList<UserRegisterItemResp>();
        for(int i = 0 ; i < numberList.size() ; i ++) {

            Object mobile = numberList.get(i) ;
            if(XObjectUtils.isNull(mobile)) {
                continue ;
            }
            String _mobile = String.valueOf(mobile) ;
            if(!StringUtils.isBlank(_mobile)) {
                UserRegisterItemResp userRegisterItemResp = new UserRegisterItemResp() ;
                userRegisterItemResp.number = _mobile ;
                userRegisterItemResp.register = userService.getCount(_mobile) > 0 ;
                registerItemRespList.add(userRegisterItemResp) ;
            }
        }

        numberList.clear();

        return ApiResult.success(registerItemRespList) ;

    }


    /**
     * 比较版本号
     * @return 返回最新的版本号
     */
    private String lastVersionNo(Long userId) {


        SyncDataVersion syncDataVersion = syncDataVersionService.selectByPrimaryKey(userId) ;
        if(syncDataVersion == null) {
            return  "0";
        }
        /**
         * TODO 处理版本号查询比较，返回是否需要更新
         */
        return  syncDataVersion.getFriendsVersion().toString() ;
    }

    /**
     *@描述 检查用户好友，返回正确好友
     *@参数 userId 用户id friends 用户好友ids
     *@返回值 正确好友
     *@创建人 Pengp
     *@创建时间 2018/11/23
     */
    @PostMapping(value = "/check/user/{userId}/friends")
    public ApiResult checkUserFriends(@PathVariable long userId,@RequestBody List<Long> friends){
        friends = userFriendsService.checkUserFriends(userId,friends);
        return ApiResult.success(friends);
    }
}
