package im.kai.server.constant;

/**
 * API请求失败响应码
 */

public interface ApiFailCode {
    /**
     * 账号被禁用
     */
    int ACCOUNT_IS_LOCKED = 99999;
    /**
     * Token无效
     */
    int INVALID_AUTH_TOKEN = 99998;

    /**
     * 无效的请求参数
     */
    int INVALID_REQUEST_PARAMETER = 30000;
    /**
     * 无效手机号码
     */
    int INVALID_MOBILE_NUMBER = 30001;

    /**无效的json 数据*/
    int INVALID_JSON = 30002 ;

    /**请求头验证失败*/
    int HEADER_AUTH_FAILED = 30003 ;

    /**操作权限不足*/
    int PERMISSION_DENIED = 30004 ;

    /**数据量太大，超出了接收的范围限制 ，请检查参数的数据长度限制*/

    int DATA_TOO_LONG = 30005 ;

    /**当每页记录的数量太大的时候，返回此返回码*/
    int PAGE_RECORD_TOO_LARGE = 30006 ;

    /**无效参数值，通常是传递了一些没有意义的参数值*/
    int INVALID_PARAMETER = 30007 ;



   /**
     * 验证码相关
     */
    interface VCode{

        /**
         * 无效的验证码
         */
        int INVALID_CODE = 31001;
        /**验证 验证码内部发生错误，可能是因为无法连接到验证服务*/
        int VERIFY_CODE_INTERNAL_ERROR = 31002 ;

    }

    interface LoginCode {

        /**缺少密码或验证码*/
        int EMPTY_VERIFICATION_STRING = 41001 ;
        /**请求头部 数据结构错误*/
        int HEADER_AUTHORIZATION_ERROR = 41002 ;

        /**用户名不存在或密码错误*/
        int ACCOUNT_NOT_EXIST = 41003 ;
        /**无效的设备信息*/
        int INVALID_DEVICE_INFO = 41004 ;

        /**解密发生错误，请检查加密的正确性*/
        int DECRYPT_ERR = 41005 ;
    }

    interface UserManage {


        /**屏蔽好友操作失败*/
        int BLOCK_USER_FAILED = 42001 ;
        /**取消屏蔽好友操作失败*/
        int UNBLOCK_USER_FAILED = 42002 ;

        /**没有指定用户的ID*/
        int UNKNOWN_USER_ID = 42003;

        /**没有找到指定的用户*/
        int NOT_FIND_FRIEND = 42004 ;

        /**已经是好友，不同重复添加*/
        int FORBID_REPEAT_ADD_FRIEND = 42005 ;

        /**添加好友，需要验证信息时，没有提供验证信息*/
        int NON_VERIFY_INVITATION_INFO = 42006 ;

        /**同意好友申请操作失败，需要重试*/
        int AGREE_APPLICATION_FAILED = 42007 ;

        /**更新好友备注信息失败 , 可能是因为用户跨权操作*/
        int MARK_FRIEND_INFO_FAILED = 42008 ;

        /**添加好友验证没有加入验证信息*/
        int EMPTY_VERIFY_MESSAGE = 42009 ;

        /**添加好友验证失败，可能需要重试*/
        int ADD_VERIFY_FAILED = 42010 ;

        /**用户可能已经拒绝过了，或者好友申请本身就不存在*/
        int REJECT_INVITATION_FAILED = 42011 ;

        /**没有更多的好友申请*/
        int LESS_INVITATION = 42012 ;

        /**删除好友失败，需要重试*/
        int REMOVE_FRIEND_FAILED = 42013 ;
        /**无效关键字，用户通过手机号码，id，或者其他搜索关键字搜索*/
        int INVALID_KEY_WORD = 42014 ;

        /**无效的版本号值，不能为空或者不合法*/
        int INVALID_VERSION = 42015 ;

        /**没有找到对应的好友申请*/
        int NOT_FOUND_INVITATION = 42016 ;
        /**不能添加自己*/
        int NOT_ALLOWED_SELF = 42017 ;

        /**批量验证手机号是否注册接口 , 传递的手机列表无效*/
        int INVALID_NUMBER_LIST = 42018 ;

        //没有登陆
        int NO_SIGN_IN = 42019 ;

        /**直接添加好友失败*/
        int ADD_DIRECT_USER_FAILED = 42020 ;

        /**好友模式值无效*/
        int FRIEND_MODE_INVALID = 42021 ;

        /**错误的好友关系*/
        int ERROR_FRIENDS = 42022;
    }

    interface Device {


        /**无效设备信息*/
        int INVALID_DEVICE = 43014 ;

        /**设备名称不能为空，或无效*/
        int INVALID_DEVICE_NAME = 43015 ;

        /**App 版本不能为空，或无效*/
        int INVALID_APP_VERSION = 43016 ;

        /**app语言不能为空，或无效*/
        int INVALID_APP_LANGUAGE = 43017 ;

        /**Google推送服务id不能为空，或无效*/
        int INVALID_GCM_ID = 43018 ;
        /**Apple推送服务id不能为空，或无效*/
        int INVALID_APN_ID = 43019 ;

        /**无效OS值*/
        int INVALID_OS_VALID = 43020;

        /**无效设备的imei号*/
        int INVALID_OS_IMEI = 43021;
        /**无效的UUID值*/
        int INVALID_UUID = 43022;
        /**未知设备类型*/
        int UNKNOWN_DEVICE_TYPE = 43023 ;

        /**无效设备信息*/
        int INVALID_DEVICE_INFO = 43024 ;
        /**无效token*/
        int INVALID_TOKEN = 43025 ;



    }

     interface RoomManage {
        /**非群成员*/
        int NOT_GROUP_MEMBERS = 51001 ;
        /**群主不能退出群組*/
        int GROUP_MANAGER_CANNOT_QUIT = 51002 ;
        /**不是群主*/
        int NOT_GROUP_MANAGER = 51003 ;
        /**群成员已加入群组中*/
        int GROUP_MEMBER_ALREADY_EXIST = 51004 ;
        /**群组已锁定*/
        int ROOM_HAD_LOCKED = 52001;
        /**无效群組id*/
        int INVALID_ROOM_ID = 52002;
    }

    interface Message {
        /** 没有任何聊天服务器 **/
        int EMPTY_MESSAGER_SERVER_LIST = 55001;
    }
}
