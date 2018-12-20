package im.kai.server.service.user.controller;

import com.alibaba.fastjson.JSONObject;
import im.kai.server.api.vcode.VerificationCodeService;
import im.kai.server.constant.AesConstant;
import im.kai.server.domain.ApiResult;
import im.kai.server.exception.CustomerErrorException;
import im.kai.server.service.user.common.KeyPair;
import im.kai.server.service.user.constraint.annotation.RegexMatch;
import im.kai.server.service.user.domain.dto.User;
import im.kai.server.service.user.domain.dto.UserDevice;
import im.kai.server.service.user.domain.dto.UserOnlineDevice;
import im.kai.server.service.user.domain.dto.UserProfile;
import im.kai.server.service.user.domain.req.UserDeviceReq;
import im.kai.server.service.user.domain.req.UserLoginReq;
import im.kai.server.service.user.domain.req.UserProfileReq;
import im.kai.server.service.user.domain.req.UserRequest;
import im.kai.server.service.user.domain.resp.UserLoginResp;
import im.kai.server.service.user.domain.resp.UserSearchResp;
import im.kai.server.service.user.executor.CommonExecutor;
import im.kai.server.service.user.executor.ConditionExecutor;
import im.kai.server.service.user.executor.DefaultCommonExecutor;
import im.kai.server.service.user.executor.ExceptionExecutor;
import im.kai.server.service.user.filter.annotation.AuthToken;
import im.kai.server.service.user.filter.annotation.NowSession;
import im.kai.server.service.user.service.UserDeviceService;
import im.kai.server.service.user.service.UserOnlineDeviceService;
import im.kai.server.service.user.service.UserProfileService;
import im.kai.server.service.user.service.UserService;
import im.kai.server.service.user.utils.*;
import im.kai.server.utils.AesUtils;
import im.kai.server.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static im.kai.server.constant.ApiFailCode.*;
import static im.kai.server.constant.ApiFailCode.LoginCode.DECRYPT_ERR;
import static im.kai.server.constant.ApiFailCode.UserManage.INVALID_KEY_WORD;
import static im.kai.server.constant.ApiFailCode.UserManage.NOT_FIND_FRIEND;
import static im.kai.server.constant.ApiFailCode.VCode.VERIFY_CODE_INTERNAL_ERROR;
import static im.kai.server.service.user.Product.BOOL_AUTH_HEADER;
import static im.kai.server.service.user.utils.AssertUtils.assertIfTrue;
/**
 * 用户Controller
 *
 * TODO 验证获取问题，验证码验证问题
 *
 */
@Slf4j
@RestController
@RequestMapping("/v1")
@Validated
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserDeviceService userDeviceService ;

    @Autowired
    private UserOnlineDeviceService onlineDeviceService;

    @Autowired
    private UserRedisUtils userRedisUtils;

    @Autowired
    private VerificationCodeService verificationCodeService ;

    /**
     *
     * v1 ：已完成。
     *
     * 需要判断是不是自己退出登录
     * POST
     *
     * 有可能传递了错误的设备ID
     * <p>
     * 用户登出
     *
     * @return ApiResult instance
     */
    @AuthToken(mustDoAuth = BOOL_AUTH_HEADER)
    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public ApiResult logout(@NowSession UserRequest userRequest ) {

        Long userId   = userRequest.getId() ;
        Long deviceId = userRequest.getDeviceId() ;

        String token  = userRedisUtils.generateUserTokenKey(XStringUtils.toString(userId)  , XStringUtils.toString(deviceId)) ;

        AssertUtils.assertIfTrue(!userRequest.getToken().equals(token) , PERMISSION_DENIED , "没有权限");
        offline(userId , deviceId);
        onlineDeviceService.setOffline(userId  , deviceId);

        return ApiResult.success();

    }

    /**
     * 用户离线 , 清除缓存
     * @param userId 用户ID
     * @param deviceId 设备ID
     */
    private void offline(Long userId , Long deviceId) {

        String uid = String.valueOf(userId) ;

        String strDeviceId = String.valueOf(deviceId) ;
        //String token = userRedisUtils.getCache(userRedisUtils.generateUserTokenKey(uid , deviceId));

        userRedisUtils.clearAllCache(uid , strDeviceId);

    }

    /**
     *
     * v1：已完成
     * 用户登陆
     *
     * 一个用户可能再多个设备上登陆，每次在不同设备登陆时，都要记录登陆设备信息，
     * 并且更新在线设备信息。
     *
     * @param request
     * @param loginReq
     * @return ApiResult
     */
    @AuthToken(mustDoAuth = false)
    @RequestMapping(value = "/login", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Transactional
    public ApiResult login(HttpServletRequest request , @RequestBody UserLoginReq loginReq , @NowSession
                            UserDeviceReq headerDevice) throws Exception {


        /*URI uri = URI.create("http://verification-code-service:8901//v1/vcode/verify/".concat(loginReq.getMobile())
        .concat("/").concat(loginReq.getVerifyCode())) ;
        ResponseEntity<ApiResult> apiResult2 =  restTemplate.exchange(uri, HttpMethod.PUT , null , ApiResult.class);
        log.info("ap : " + JSONObject.toJSON(apiResult2.getBody()));*/

        ApiResult codeResult = validateCode(loginReq.getMobile() , loginReq.getVerifyCode()) ;

        if(XObjectUtils.isNotNull(codeResult) && codeResult.getCode() != 0) {
            return codeResult ;
        }

        User userEntity = userService.getWithMobile(loginReq.getMobile());
        log.info("loginReq.getDevice(): " + loginReq.getDevice());
        String decrypt = AssertUtils.assertExecutor(new ExceptionExecutor<String, String>() {    //解密

            @Override
            public String execute(String p) throws Exception {
                return AesUtils.decrypt(p , AesConstant.DEBUG_AES_KEY);
            }

            @Override
            public Exception exceptionResult(Exception e) { return new CustomerErrorException(DECRYPT_ERR , "秘钥错误" , e); }

        } , loginReq.getDevice());

        log.info("decrypt : " + decrypt);
        //获取设备信息, 如果解析出现问题，那么直接抛出异常
        UserDeviceReq deviceReq = AssertUtils.assertExecutor(new ExceptionExecutor<UserDeviceReq, String>() {
            @Override
            public UserDeviceReq execute(String p) {
                return JSONObject.parseObject(p , UserDeviceReq.class);
            }

            @Override
            public Exception exceptionResult(Exception e) { return new CustomerErrorException(INVALID_JSON , "无效JSON", e); }

        } , decrypt);


        final String KEY_USER = "user";
        final String KEY_USER_PROFILE = "user_profile";
        Map<String , Object> cacheMap = new HashMap <String , Object> ();

        //验证设备信息是否传递完成
        super.checkRequestArguments(deviceReq) ;

        //从请求头中设置
        deviceReq.setVersion(headerDevice.getVersion());
        deviceReq.setLang(headerDevice.getLang());
        deviceReq.setType(headerDevice.getType());

        //考虑到不同设备，缓存需要加入设备识别
        CommonExecutor redisCacheExecutor = new DefaultCommonExecutor<Void , UserLoginResp>() {
            @Override
            public Void doExecutor(UserLoginResp loginResp) throws Exception {

            User user = (User)cacheMap.get(KEY_USER) ;
            UserProfile userProfile =  (UserProfile)cacheMap.get(KEY_USER_PROFILE) ;

            String userId = String.valueOf(user.getId()) ;
            userRedisUtils.cacheUser(userId , user , loginResp.getDeviceId());
            userRedisUtils.cacheUserProfile(userId , userProfile , loginResp.getDeviceId());
            userRedisUtils.cacheUserToken(userId , loginResp.getToken() , loginResp.getDeviceId());

            /*
            log.info("redisUtils : "+ userRedisUtils
                 .getCache(userRedisUtils.generateUserKey(String.valueOf(user.getId()) , loginResp.getDeviceId())));
                            */
            //log.info("redisUtils : "+ userRedisUtils.getCache(generateUserProfileKey(user.getId())));
            //log.info("redisUtils : "+ userRedisUtils.getCache(token));

            return null;

            }
        };

        //最后返回的数据
        CommonExecutor finalCommonExecutor = new CommonExecutor<ApiResult , UserOnlineDevice>() {

            @Override
            public ApiResult doExecutor(UserOnlineDevice onlineDevice) throws Exception {

                //用户登陆操作完成
                UserLoginResp resp = new UserLoginResp();
                resp.setNew(XObjectUtils.isNull(userEntity));
                resp.setSecret(AesUtils.encrypt(createSecret(), AesConstant.DEBUG_AES_KEY));
                resp.setDeviceId(XStringUtils.toString(onlineDevice.getDeviceId()));
                resp.setToken(onlineDevice.getAuthToken());

                nextExecutor().doExecutor(resp);

                return ApiResult.success(resp);
            }

            @Override
            public CommonExecutor nextExecutor() {
                return redisCacheExecutor ;
            }
        } ;

        //构建设备编号
        CommonExecutor<String , UserDeviceReq> buildDeviceCodeExecutor = new DefaultCommonExecutor<String, UserDeviceReq>() {
            @Override
            public String doExecutor(UserDeviceReq p) throws Exception {

                StringBuilder sb = new StringBuilder();
                sb.append(XStringUtils.getString(deviceReq.getSubtype() , "")) ;
                sb.append(XStringUtils.getString(deviceReq.getName() , "")) ;
                sb.append(XStringUtils.getString(deviceReq.getGcm_id() , "")) ;
                sb.append(XStringUtils.getString(deviceReq.getApn_id() , "")) ;
                sb.append(XStringUtils.getString(deviceReq.getOs() , "")) ;
                sb.append(XStringUtils.getString(deviceReq.getImei() , "")) ;

                return MD5Utils.md5(sb.toString()) ;
            }


        } ;
        //注册在线用户设备
        CommonExecutor onlineDeviceExecutor = new CommonExecutor<UserOnlineDevice , UserDevice>() {
            @Override
            public UserOnlineDevice doExecutor(UserDevice userDevice)            throws Exception {

                log.info("user_id : " + userDevice.getUserId() + " , type : " + userDevice.getType());
                //同一平台设备在线
                UserOnlineDevice userOnlineDevice =
                        onlineDeviceService.getOnlineDevice(userDevice.getUserId() , String.valueOf(userDevice.getType())) ;

                //踢掉上次登陆的用户 , 更新
                if(XObjectUtils.isNotNull(userOnlineDevice)) {
                    offline(userOnlineDevice.getUserId() , userOnlineDevice.getDeviceId());

                    //设置新的设备ID
                    userOnlineDevice.setDeviceId(userDevice.getId());
                    userOnlineDevice.setLastSeen(TimeUtils.nowTimeStamp());

                    String strDeviceId = XStringUtils.toString(userOnlineDevice.getDeviceId()) ;
                    userOnlineDevice.setAuthToken(TokenUtils.createToken(strDeviceId));

                    onlineDeviceService.updateByPrimaryKeySelective(userOnlineDevice) ;

                    return userOnlineDevice ;

                }

                //新设备
                userOnlineDevice = new UserOnlineDevice() ;
                userOnlineDevice.setLastSeen(TimeUtils.nowTimeStamp());
                userOnlineDevice.setDeviceId(userDevice.getId());
                userOnlineDevice.setDeviceType(userDevice.getType());
                userOnlineDevice.setUserId(userDevice.getUserId());

                String strDeviceId = XStringUtils.toString(userOnlineDevice.getDeviceId()) ;
                //使用设备iD 来生成token
                userOnlineDevice.setAuthToken(TokenUtils.createToken(strDeviceId));

                onlineDeviceService.insert(userOnlineDevice) ;

                return userOnlineDevice ;
            }

            @Override
            public CommonExecutor nextExecutor() {
                return finalCommonExecutor ;
            }
        } ;

        final String KEY_DEVICE = "device" ;
        final String KEY_USER_ID = "user_id" ;

        //如果是新用户
        ConditionExecutor<ApiResult , CommonExecutor<ApiResult , KeyPair>> ifNewUserExecutor
                = new ConditionExecutor<ApiResult , CommonExecutor<ApiResult , KeyPair>>() {

            @Override
            public ApiResult execute(CommonExecutor<ApiResult , KeyPair> commonExecutor) throws Exception {

                User user = new User();                                             /*加入两张表记录 , 用户表和信息表*/
                user.setImNumber(createIMId());
                user.setMobile(loginReq.getMobile());
                userService.insertUserAndSynData(user) ;

                UserProfile userProfile = new UserProfile() ;                       /*增加用户资料表*/
                userProfile.setUserId(user.getId());

                userProfile.setNickName(createRandomName(user.getMobile()));        /**随机两个字母 ，后4位号码*/
                userProfile.setCreated(TimeUtils.nowTimeStamp());
                userProfile.setPassword(MD5Utils.md5(CommonUtils.randomValue()));
                userProfile.setGender((byte)1);                                     /*默认设置为未知的性别*/
                userProfile.setLastUpdated(TimeUtils.nowTimeStamp());
                userProfile.setCreated(TimeUtils.nowTimeStamp());
                userProfile.setFriendMode((byte)1);
                userProfileService.insert(userProfile) ;

                cacheMap.put(KEY_USER , user) ;
                cacheMap.put(KEY_USER_PROFILE , userProfile) ;

                log.info("deviceReq : " + JSONObject.toJSON(deviceReq));
                KeyPair keyPair = KeyPair.create() ;
                keyPair.set(KEY_DEVICE , deviceReq);
                keyPair.set(KEY_USER_ID , user.getId());

                return commonExecutor.doExecutor(keyPair);

            }

        } ;


        //不是新用户
        ConditionExecutor<ApiResult , CommonExecutor<ApiResult , KeyPair>> elseUserExistExecutor
                = new ConditionExecutor<ApiResult , CommonExecutor<ApiResult , KeyPair>>() {

            @Override
            public ApiResult execute(CommonExecutor<ApiResult , KeyPair> commonExecutor) throws Exception {

                cacheMap.put(KEY_USER , userEntity) ;
                cacheMap.put(KEY_USER_PROFILE , userProfileService.selectByPrimaryKey(userEntity.getId())) ;

                String deviceCode = buildDeviceCodeExecutor.doExecutor(deviceReq);

                log.info("deviceCode : " + deviceCode);

                UserDevice userDevice = userDeviceService.getDeviceWithCode(deviceCode , userEntity.getId()) ;

                if(XObjectUtils.isNull(userDevice)) {    //新设备登陆，添加用户新设备

                    KeyPair keyPair = KeyPair.create() ;
                    keyPair.set(KEY_DEVICE , deviceReq);
                    keyPair.set(KEY_USER_ID , userEntity.getId());

                    return commonExecutor.doExecutor(keyPair);

                }

                //如果是旧设备登陆的话，需要添加在线记录
                CommonExecutor nextExecutor = commonExecutor.nextExecutor() ;
                UserOnlineDevice userOnlineDevice = (UserOnlineDevice) nextExecutor.doExecutor(userDevice);

                return (ApiResult) nextExecutor.nextExecutor().doExecutor(userOnlineDevice);

            }
        } ;

        //增加用户设备表
        CommonExecutor<ApiResult , KeyPair> commonExecutor = new CommonExecutor<ApiResult , KeyPair>() {

            @Override
            public ApiResult doExecutor(KeyPair map) throws Exception {

                UserDeviceReq userDeviceReq = (UserDeviceReq) map.get(KEY_DEVICE);
                Long user_id = (Long) map.get(KEY_USER_ID);

                //log.info("userDeviceReq :"  + JSONObject.toJSON(userDeviceReq));
                UserDevice userDevice = Utils.copyTo(userDeviceReq , UserDevice.class) ;

                //log.info("userDevice :"  + JSONObject.toJSON(userDevice));
                userDevice.setCreated(TimeUtils.nowTimeStamp());
                userDevice.setLastSeen(userDevice.getCreated());
                userDevice.setUserId(user_id);
                userDevice.setCode(buildDeviceCodeExecutor.doExecutor(userDeviceReq));

                userDeviceService.insert(userDevice) ;

                //log.info("userDevice : " + JSONObject.toJSON(userDevice));

                CommonExecutor nextExecutor = nextExecutor() ;
                UserOnlineDevice userOnlineDevice = (UserOnlineDevice) nextExecutor.doExecutor(userDevice);

                return (ApiResult) nextExecutor.nextExecutor().doExecutor(userOnlineDevice);

            }

            @Override
            public CommonExecutor nextExecutor() {
                return onlineDeviceExecutor ;
            }

        } ;


        //分支执行。
        ApiResult apiResult = BranchUtils
                .execute(XObjectUtils.isNull(userEntity) , commonExecutor , ifNewUserExecutor , elseUserExistExecutor );

        //clear data
        cacheMap.clear();

        return apiResult ;

    }

    /**
     * 生成随机昵称
     * 两个字母随机  + 手机号码后四位
     * TODO
     * @return
     */
    private String createRandomName(String mobile) {

        int diff = 90 - 65 ;
        int diff2 = 122 - 97 ;
        char ch = (char)(65 + (int)(Math.random() * diff));
        char ch2 = (char)(97 + (int)(Math.random() * diff2));

        return String.valueOf(ch).concat(String.valueOf(ch2)).concat(mobile.substring(mobile.length() - 4));
    }
    /**
     * 获取用户信息
     *
     *  v1 ：已完成
     * header
     * X-TOKEN : 登陆后token值
     * X-DEVICE-DATA : 见文档
     *
     * @return ResponseEntity
     */
    @AuthToken(mustDoAuth = BOOL_AUTH_HEADER)
    @RequestMapping(value = "/user/profile" , method = RequestMethod.GET)
    public ApiResult getUserInfo(HttpServletRequest req , @NowSession UserRequest userRequest) {

        //UserProfile apiResult = userApiService.searchUser("+8618802001702");
        //log.info("apiResult search: " + apiResult);

        return ApiResult.success(userProfileService.selectByPrimaryKey(userRequest.getId()));

    }

    /**
     * 更新我的个人信息
     * @param userProfileReq 待更新信息
     * @return
     */
    @AuthToken(mustDoAuth = BOOL_AUTH_HEADER)
    @PutMapping("/user/profile")
    public ApiResult updateUserInfo(@RequestBody UserProfileReq userProfileReq , @NowSession UserRequest userRequest) throws Exception {

        if(XObjectUtils.isNull(userProfileReq.getArea()) &&
            XObjectUtils.isNull(userProfileReq.getGender()) &&
            XObjectUtils.isNull(userProfileReq.getNickName()) &&
            XObjectUtils.isNull(userProfileReq.getSignature())) {
            return ApiResult.success() ;
        }
        UserProfile userProfile = Utils.copyTo(userProfileReq , UserProfile.class);

        //log.info("userProfile : " + JSONObject.toJSON(userProfile));
        userProfileService.updateUserInfo(userRequest.getId() , userProfile);

        return ApiResult.success();
    }

    /**
     * v1 : 已完成
     *
     * @param request
     * @param keyword
     * @return ApiResult
     * @throws Exception
     */
    @AuthToken(mustDoAuth = BOOL_AUTH_HEADER)
    @RequestMapping(value = "/search/{keyword}" , method = RequestMethod.GET)
    public ApiResult searchFriend(HttpServletRequest request ,
                                  @PathVariable(name="keyword")
                                  @RegexMatch(regex="[-|+]+(\\S)+" , code = INVALID_KEY_WORD , nullable = false)
                                  String keyword)  throws Exception {

        boolean isMobile = keyword.startsWith("+") ;
        assertIfTrue(isMobile && !Utils.isValidMobileNumber(keyword) , INVALID_MOBILE_NUMBER , "无效手机号码");

        //return userProfileService.selectByPrimaryKey(35L) ;
        CommonExecutor<ApiResult , User> executor = new DefaultCommonExecutor<ApiResult, User>() {

            @Override
            public ApiResult doExecutor(User user) throws Exception {

                if (XObjectUtils.isNull(user)) {
                    return ApiResult.fail(NOT_FIND_FRIEND);
                }

                UserProfile profile = userProfileService.selectByPrimaryKey(user.getId());
                UserSearchResp userSearchResp = Utils.copyTo(profile, UserSearchResp.class);

                return ApiResult.success(userSearchResp);
            }


        }  ;

        if(isMobile) {
            User userEntity = userService.getWithMobile(keyword) ;
            return  executor.doExecutor(userEntity) ;
        }

        Long user_id = Long.parseLong(keyword.substring(keyword.indexOf('-') + 1)) ;
        User userEntity = userService.get(user_id) ;
        return  executor.doExecutor(userEntity) ;

    }

    /**
     * 检查验证码是否正确
     * @param mobile 手机号码
     * @param checkCode 用户传递的验证码
     * @return true 正确， false 错误/过期
     */
    private ApiResult validateCode(String mobile , String checkCode) throws Exception {

        ApiResult apiResult = AssertUtils.assertExecutor(new ExceptionExecutor<ApiResult , Void>() {

            @Override
            public ApiResult execute(Void uri) {
                return verificationCodeService.checkVerificationCode(mobile , checkCode) ;
            }

            @Override
            public Exception exceptionResult(Exception e) {
                return new CustomerErrorException(VERIFY_CODE_INTERNAL_ERROR , "验证码服务可能发生了错误" , e);
            }
        } , null) ;

       return apiResult ;


    }
    /**
     * 生成secret
     * @return
     */
    private String createSecret() {

        JSONObject json = new JSONObject();
        json.put("mode","AES-128-ECB");
        json.put("private_key","0YYDnCQd3BZC");
        json.put("public_key","0YYDnCQd3BZC");

        return json.toJSONString() ;
    }

    /**
     * TODO
     * @return
     */
    private String createIMId() {
        return CommonUtils.randomValue() ;
    }

    /**
     *@描述 获取用户存在的用户ids
     *@参数 userIds  用户Ids
     *@返回值
     *@创建人 Pengp
     *@创建时间 2018/11/22
     */
    @PostMapping(value = "/check/users")
    public ApiResult checkUsers(@RequestBody List<Long> userIds){
        List<Long> user = userService.checkUsers(userIds);
        return ApiResult.success(user);
    }
}