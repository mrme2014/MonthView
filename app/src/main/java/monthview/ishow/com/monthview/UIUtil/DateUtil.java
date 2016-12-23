package monthview.ishow.com.monthview.UIUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static String parseDate2Str(Long d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(d));
    }

    public static String parseDate2Str(Long d, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(d));
    }

    public static String parseSecond2Str(Long d, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(d * 1000));
    }

    public static String parseSecond2Str(Long d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(d * 1000));
    }

    public static Long parseDate2Second(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d = sdf.parse(date);
            return d.getTime() / 1000L;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static String parseTimelineDate(long parseDate) {
        long s = System.currentTimeMillis() / 1000 - parseDate;
        if (s < 10) {
            return "刚刚";
        } else if (s < 60) {
            return s + "秒前";
        } else if (s < 60 * 60) {
            return s / 60 + "分钟前";
        } else if (s < 60 * 60 * 24) {
            return s / (60 * 60) + "小时前";
        } else {
            return parseDate2Str(parseDate * 1000, "yyyy-MM-dd HH:mm");
        }
    }

    /**
     * 时区问题，不能用day数相减
     *
     * @param sdate 单位：微秒
     * @param bdate
     * @return
     */
    public static int daysBetween(long sdate, long bdate) {
        long between_days = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date sd = sdf.parse(sdf.format(new Date(sdate * 1000)));
            Date bd = sdf.parse(sdf.format(new Date(bdate * 1000)));
            Calendar cal = Calendar.getInstance();
            cal.setTime(sd);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bd);
            long time2 = cal.getTimeInMillis();
            between_days = (time2 - time1) / (1000 * 3600 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int) between_days;
    }


    public static int date2Second(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int unix = 0;
        try {
            Date date = sdf.parse(String.valueOf(time));
            unix = (int) (date.getTime() / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //LogUtil.e("date2Second"+unix);
        return unix;
    }

    public static int date2Second(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        int unix = 0;
        try {
            Date date = sdf.parse(String.valueOf(time));
            unix = (int) (date.getTime() / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return unix;
    }


    /**
     * 获取指定日期开始时间的毫秒数
     *
     * @return
     */
    public static Long getStartTime(final Date date) {
        Calendar calendar = Calendar.getInstance();
        //  Calendar.HOUR_OF_DAY是24小时制
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 获取指定日期截止时间的毫秒数
     *
     * @return
     */
    public static Long getEndTime(final Date date) {
        Calendar calendar = Calendar.getInstance();
        //  Calendar.HOUR是12小时制
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime().getTime();
    }
}
