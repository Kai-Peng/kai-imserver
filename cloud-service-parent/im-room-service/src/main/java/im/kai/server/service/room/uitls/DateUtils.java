package im.kai.server.service.room.uitls;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期时间格式化、比较处理类
 */
public class DateUtils {

    /** 全格式 */
    public static final String DAY_MIN_SECOND = "dd日HH:mm";
    public static final String ALL_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String ALL_FORMAT3 ="yyyyMMdd";
    public static final String YMD_FORMAT = "yyyy-MM-dd";

    /** 获取当前时分 */
    public static String getCurrentDayHourMin(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(DAY_MIN_SECOND);
        return dateFormat.format(new Date(System.currentTimeMillis()));
    }
    /** 获取当前日期 */
    public static String getCurrentDay(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(YMD_FORMAT);
        return dateFormat.format(new Date(System.currentTimeMillis()));
    }
    /**
     * 是否是周末
     */
    public static boolean isWeek(Date date) {
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY ||
                cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY){
            return true;
        }
        return false;
    }

    /**
     * 获取今天几点
     */
    public static Date getTodayTime(int num){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, num);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

  /**
   * 获取今天几点
   */
  public static Date getTimeByDay(int num,long day){
    Date dayTime = getTime3(day);
    Calendar c = Calendar.getInstance();
    c.setTime(dayTime);
    c.set(Calendar.HOUR_OF_DAY, num);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    return c.getTime();
  }

  /**
   * 获取今天几点
   */
  public static Date getDayTime(String YMD,int num){
    String [] items = YMD.split("-");
    Calendar c = Calendar.getInstance();
    c.set(Integer.parseInt(items[0]),
            Integer.parseInt(items[1])-1,
            Integer.parseInt(items[2]));
    c.set(Calendar.HOUR_OF_DAY, num);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    return c.getTime();
  }

    /***
     * 获取指定日期时间搓
     */
    public static long getTime(String time) {
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyyMMddHHmmss").parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1.getTime();
    }

    /***
     * 获取指定日期时间搓
     */
    public static long getTime(String time,String format) {
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat(format).parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1.getTime()/1000;
    }

    /***
     * 获取指定日期时间搓
     */
    public static Date getTime3(String time) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    /**
     * 根据时间戳获取日期
     */
  public static Date getTime3(long time) {
    String strTime = DateUtils.getTimeFormat(time,DateUtils.ALL_FORMAT3);
    Date date = null;
    try {
      date = new SimpleDateFormat("yyyyMMdd").parse(strTime);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date;
  }

    public static Date getTime3(String time,String format) {
        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /***
     * 获取当前日期时间搓
     */
    public static long getTime() {
        String time = getCurrentDay();
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat(YMD_FORMAT).parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1.getTime();
    }
    /***
     * 获取当前日期时间搓
     */
    public static long getTimeHMS(String time) {
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat(ALL_FORMAT).parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1.getTime()/1000;
    }
    /**
     * 当前是AM   OR   PM
     * true am   false  pm
     * */
    public static boolean isAM(){
        GregorianCalendar ca = new GregorianCalendar();
        int num = ca.get(GregorianCalendar.AM_PM);
        return "0".equals(num+"");  //0”是上午  “1”是下午
    }
    public static boolean isPM(){
        GregorianCalendar ca = new GregorianCalendar();
        int num = ca.get(GregorianCalendar.AM_PM);
        return "1".equals(num+"");  //0”是上午  “1”是下午
    }

    public static boolean isAM(long time){
        GregorianCalendar ca = new GregorianCalendar();
        int num = ca.get(GregorianCalendar.AM_PM);
        return "0".equals(num+"");  //0”是上午  “1”是下午
    }

    public static boolean isAM(Date time){
        //        GregorianCalendar ca = new GregorianCalendar();
        Calendar cal=Calendar.getInstance();
        cal.setTime(time);
        int num = cal.get(GregorianCalendar.AM_PM);
        return "0".equals(num+"");  //0”是上午  “1”是下午
    }
    /** 获取当前格式化的时间 */
    public static String getCurrentTimeWithFormat(boolean falg){
        SimpleDateFormat dateFormat = new SimpleDateFormat(falg?ALL_FORMAT:YMD_FORMAT);
        return dateFormat.format(new Date(System.currentTimeMillis()));

    }

    /** 获取当前格式化的时间 */
    public static String getTimeWithFormat(Long time){
        if (time == 0) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DAY_MIN_SECOND);
        return dateFormat.format(new Date(time));

    }
    /** 获取当前格式化的时间 */
    public static String getTimeWithFormat(Long time,String format){
        if (time == 0) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(time*1000));

    }
    /**
     * 时间格式化
     */
    public static String getTimeFormat(Date date){
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    /**
     * 时间格式化
     */
    public static String getTimeFormat(Date date,String format){
        return new SimpleDateFormat(format).format(date);
    }
    //获取当前月的天数
    public static int getDayOfMonth(){
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        int day=aCalendar.getActualMaximum(Calendar.DATE);
        return day;
    }
    /**
     * 时间格式化
     */
    public static String getTimeFormat(Long time,String format){
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        String sd = sdf.format(new Date(time*1000));
        //        return new SimpleDateFormat("yyyy-MM-dd").format(date);
        return sd;
    }
    //获取当前月每天的日期
    public static List getDayListOfMonth() {
        List list = new ArrayList();
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        int year = aCalendar.get(Calendar.YEAR);//年份
        int month = aCalendar.get(Calendar.MONTH);//月份
        int day = aCalendar.getActualMaximum(Calendar.DATE);
        for (int i = 1; i <= day; i++) {
            String aDate = String.valueOf(year)+"/"+month+i;
            list.add(aDate);
        }
        return list;
    }

    /**今天周几*/
    public static int getWeekDay() {
        Date today = new Date();
        Calendar c=Calendar.getInstance();
        c.setTime(today);
        int weekday=c.get(Calendar.DAY_OF_WEEK);
        return weekday-1;
    }

    /**当前年月 字符串*/
    public static String getYM() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM");
        String toDay = format.format(date);  //将字符串转化为指定的日期格式
        toDay = toDay.replace('-','年')+"月";
        return toDay;
    }

    /**当前日*/
    public static  String getD() {
        Calendar now = Calendar.getInstance();
        String d = now.get(Calendar.DAY_OF_MONTH)+"";
        if (d.length() == 1) d = "0"+d;
        return d;
    }

    /**当前日*/
    public static  int getNumD() {
        Calendar now = Calendar.getInstance();
        int d = now.get(Calendar.DAY_OF_MONTH);
        return d;
    }
    public static  int getDay() {
        Calendar now = Calendar.getInstance();
        int d = now.get(Calendar.DAY_OF_MONTH);
        return d;
    }

    /**前几日*/
    public static  String getLastD(int num) {
        Calendar now = Calendar.getInstance();
        int d = now.get(Calendar.DAY_OF_MONTH)-num;
        String strD = String.valueOf(d);
        if (d < 10) {
            if (d == 0) {
                int lastMonthDays = getDaysByMonth(getNumM()-1);
                return String.valueOf(lastMonthDays);
            }else if (d < 0) {
                int lastMonthDays = getDaysByMonth(getNumM()-1)+d;
                return String.valueOf(lastMonthDays);
            }
            strD = String.format("%s%s","0",strD);
        }
        return strD;
    }

    /**后几日*/
    public static  String getAfterD(int num) {
        Calendar now = Calendar.getInstance();
        String d = now.get(Calendar.DAY_OF_MONTH)+num+"";
        if (d.length() == 1) d = "0"+d;
        return d;
    }

    /**获取当前年*/
    public static String getY() {
        Calendar now = Calendar.getInstance();
        return  now.get(Calendar.YEAR)+"";
    }

    /**获取当前年*/
    public static int getNumY() {
        Calendar now = Calendar.getInstance();
        return  now.get(Calendar.YEAR);
    }

    /***获取当前月*/
    public static String getM() {
        Calendar now = Calendar.getInstance();
        String m = now.get(Calendar.MONTH) + 1+"";
        if (m.length() == 1) m = "0"+m;
        return m;
    }

    /***获取当前月*/
    public static int getNumM() {
        Calendar now = Calendar.getInstance();
        int m = now.get(Calendar.MONTH) + 1;
        return m;
    }

    /***获取前几月*/
    public static String getLastM(int num) {
        Calendar now = Calendar.getInstance();
        String m = now.get(Calendar.MONTH) + 1 - num +"";
        if (m.length() == 1) m = "0"+m;
        return m;
    }


    /***获取当前时间几个月后的日期*/
    public static Date getAppointMonth(int num) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date now = null;
        try {
            now = sdf.parse(getTimeFormat(new Date(),DateUtils.YMD_FORMAT));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MONTH, num);
        return calendar.getTime();
    }
    /***获取当前时间几个月后的日期*/
    public static Date getAppointMonth2( int num) {
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();// 取得六个月后时间
         cal.add(Calendar.MONTH, num);
         System.out.print(df.format(cal.getTime()));
         return cal.getTime();
    }
    /***获取前几月*/
    public static int getNumM(int num) {
        Calendar now = Calendar.getInstance();
        int m = now.get(Calendar.MONTH) + 1 - num ;
        return m;
    }

    /**分割年月日*/
    public static String[] getYMD(String time) {
        String [] times = new String[3];
        times[0] = time.substring(0,4);
        times[1] = time.substring(5,7);
        times[2] = time.substring(8,time.length());
        if (times[2].length() == 1) times[2] = "0"+times[2];
        return times;
    }

    /**
     * 根据 月 获取对应的月份 天数
     * */
    public static int getDaysByMonth( int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 根据日期 找到对应日期的 星期
     */
    public static String getDayOfWeekByDate(String date) {
        String dayOfweek = "-1";
        try {
            SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Date myDate = myFormatter.parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat("E");
            String str = formatter.format(myDate);
            dayOfweek = str;

        } catch (Exception e) {
            System.out.println("错误!");
        }
        return dayOfweek;
    }

    /**
     *
     * @param moth
     * @param day
     */
    public static long getDay4Month(int moth,String day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        calendar.add(Calendar.MONTH, moth);
        return calendar.getTime().getTime();
    }

    /**
     * 根据当前日期获得上周的日期区间（上周周一和周日日期）
     *
     * @return
     * @author zhaoxuepu
     */
    public static String getLastTimeInterval() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
        int offset1 = 1 - dayOfWeek;
        int offset2 = 7 - dayOfWeek;
        calendar1.add(Calendar.DATE, offset1 - 7);
        calendar2.add(Calendar.DATE, offset2 - 7);
        String lastBeginDate = sdf.format(calendar1.getTime());
        String lastEndDate = sdf.format(calendar2.getTime());
        return lastBeginDate + "," + lastEndDate;
    }
    /**
     * 根据当前日期获得所在周的日期区间（周一和周日日期）
     *
     * @return
     * @author zhaoxuepu
     * @throws ParseException
     */
    public static String getTimeInterval(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        String imptimeBegin = sdf.format(cal.getTime());
        // System.out.println("所在周星期一的日期：" + imptimeBegin);
        cal.add(Calendar.DATE, 6);
        String imptimeEnd = sdf.format(cal.getTime());
        // System.out.println("所在周星期日的日期：" + imptimeEnd);
        return imptimeBegin + "," + imptimeEnd;
    }

    /**
     * 得到几天后的时间
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d,int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    public static List<Date> findDates(Date dBegin, Date dEnd) {
        List lDate = new ArrayList();
        lDate.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime()))
        {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        return lDate;
    }

    /****
     * 传入具体日期 ，返回具体日期增加一个月。
     * @param date 日期(2017-04-13)
     * @throws ParseException
     */
    public static  String subMonth(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = sdf.parse(date);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.MONTH, 1);
        Date dt1 = rightNow.getTime();
        String reStr = sdf.format(dt1);
        return reStr;
    }
    /****
     * 传入具体日期 ，返回具体日期增加一个月。
     * @param date 日期(2017-04-13)
     * @throws ParseException
     */
    public static  long subMonth(long date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = sdf.parse(DateUtils.getTimeFormat(date,DateUtils.YMD_FORMAT));
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.MONTH, 1);
        Date dt1 = rightNow.getTime();
        String reStr = sdf.format(dt1);
        return dt1.getTime()/1000-86400;
    }

}
