package im.kai.server.service.room.manager;

/**
 * @AUTHER Pengp
 * @DATE 2018/11/2 10:10
 */
public class CacheManager {

    //判断list为空或者为0
    public static final String CONDITION_LIST_NULL_OR_0 = "#result eq null or #result.size() eq 0";

    public static final String FIND_BY_ID_VALUE = "method_find_by_id";
    public static final String FIND_BY_ID_KEY   = "method_find_by_id";
    public static final String UPDATE_VALUE     = "method_update";
    public static final String UPDATE_KEY       = "method_update";
    public static final String USER_ROOMS       = "user_rooms";
    public static final String ROOM             = "room";
}
