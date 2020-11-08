package com.wander.baseframe.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class TimeUtils {

    public static final long SECOND = 1000;
    public static final long MINUTE = SECOND * 60;
    public static final long HOUR = MINUTE * 60;
    public static final long DAY = HOUR * 24;
    public static final long MONTH = DAY * 30;
    public static final long YEAR = MONTH * 12;

    public static final DateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    public static final DateFormat DAY_FORMAT = new SimpleDateFormat("MM-dd");
    public static final String HH_MM = "HH:mm";
    private static ThreadLocal<SimpleDateFormat> mLocalFormater = new ThreadLocal<>();

    public final static String WEEK_INDEX = "日一二三四五六";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD2 = "yyyy.MM.dd";
    public final static int[] WEEK_DAYS = {Calendar.SUNDAY, Calendar.MONDAY,
            Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY,
            Calendar.FRIDAY, Calendar.SATURDAY};

    public static String convertTimeStamp(String timeStr) {
        try {
            if (!TextUtils.isEmpty(timeStr)) {
                long timeL = Long.parseLong(timeStr);
                return convertTimeStamp(timeL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isOneWeekAgo(String timeStr) {

        try {
            long timeL = Long.parseLong(timeStr);
            long delta = TimeUnit.MILLISECONDS.toSeconds(
                    new Date().getTime() - timeL);

            return delta > 7 * DAY;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    /**
     * 是否超过24小时
     *
     * @return
     */
    public static boolean isOut24Hours(Long beforeTime) {
        long delta = TimeUnit.MILLISECONDS.toSeconds(new Date().getTime() - beforeTime);
        return delta > DAY;
    }


    public static String convertTimeStamp(long time) {

        try {
            long nowlMilliSeconds = System.currentTimeMillis();
            long showMillSeconds = nowlMilliSeconds - time;
            DateFormat dateFormatterChina = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
            TimeZone timeZoneChina = TimeZone.getTimeZone("Asia/Shanghai");
            dateFormatterChina.setTimeZone(timeZoneChina);

            long seconds = showMillSeconds / 1000;//秒
            long minitue = seconds / 60;//分
            long hour = minitue / 60;//时;
            long day = hour / 24;//天;

            Date updateDate = new Date(time);
            Date nowDate = new Date(nowlMilliSeconds);


            String updateTime = YEAR_FORMAT.format(updateDate);
            String nowTime = YEAR_FORMAT.format(nowDate);
            if (updateTime.equals(nowTime)) {
                String showTime = TIME_FORMAT.format(updateDate);
                return "更新于" + showTime;
            } else if (day == 0) {
                return "更新于" + "1天前";
            } else if (day >= 1 && day < 11) {
                return "更新于" + day + "天前";
            } else {
                return "更新于" + updateTime;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";

    }


    public static String convertTimeStampForReadingRecord(long time) {

        try {
            Date date0 = new Date(time);
            Date dateCurrent = new Date();//取时间
            long currentTime = dateCurrent.getTime();

            Calendar calendar = new GregorianCalendar();

            calendar.setTime(dateCurrent);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date thisDay0 = calendar.getTime();

            calendar.setTime(dateCurrent);
            calendar.add(Calendar.HOUR_OF_DAY, -1);
            Date oneHourAgo = calendar.getTime();

            if (currentTime < time) {
                return YEAR_FORMAT.format(date0);
            } else if (date0.after(thisDay0) && date0.after(oneHourAgo)) {
                if ((currentTime - time) / MINUTE == 0) {
                    return "刚刚";
                } else {
                    return (currentTime - time) / MINUTE + "分钟前";
                }

            } else if (date0.after(thisDay0) && date0.before(oneHourAgo)) {
                return "今日" + TIME_FORMAT.format(date0);
            } else {
                calendar.setTime(dateCurrent);
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date yesterday = calendar.getTime();

                calendar.setTime(dateCurrent);
                calendar.add(Calendar.DAY_OF_YEAR, -6);//WEEK_OF_YEAR -1是不符合常规说法的，会出现7天前，而7天前算一个周之前
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date oneWeekAgo = calendar.getTime();

                if (date0.after(yesterday)) { //昨天
                    return "昨日" + TIME_FORMAT.format(dateCurrent);
                } else if (date0.before(yesterday) && date0.after(oneWeekAgo)) { //昨天之前，一周以内
                    return (currentTime - time) / DAY + "天前";
                } else { //一周之前
                    return YEAR_FORMAT.format(date0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";

    }

    public static String getFormattedTime(Date pushplishTime) {

        Calendar nowCalendar = Calendar.getInstance();
        int mNowYearPublish = nowCalendar.get(Calendar.YEAR); // 当前时间的年

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(pushplishTime);
        int mPostTodayPublish = calendar.get(Calendar.DAY_OF_MONTH); // 目标时间的日
        int mPostMonthPublish = (calendar.get(Calendar.MONTH) + 1); // 目标时间的月
        int mPostYearPublish = calendar.get(Calendar.YEAR); // 目标时间的年

        // 原始时间差
        long time = nowCalendar.getTime().getTime() - pushplishTime.getTime(); // 时间差
        // 归零的时间差

        String mFormatStr = null;
        if (time < 0 || time / 1000 < 1) {
            mFormatStr = "刚刚";
            return mFormatStr;
        }

        if ((time / 1000) < 60) {
            mFormatStr = time / 1000 + "秒前";
            return mFormatStr;
        }

        // 如果时间差在60min之内 （*分钟前）
        if (time / (1000 * 60) < 60) {
            mFormatStr = time / (1000 * 60) + "分钟前";
            return mFormatStr;
        }

        // 如果时间差在12h之内 （*小时前）
        if (time / (1000 * 60 * 60) < 24) {
            mFormatStr = time / (1000 * 60 * 60) + "小时前";
            return mFormatStr;
        }

        // 如果年份相同，并且相差两天以上，则显示（日期+时间 12月3日 01:00）
        if (mNowYearPublish == mPostYearPublish) {
            mFormatStr = formatInt(mPostMonthPublish) + "-" + formatInt(mPostTodayPublish);
            return mFormatStr;
        }

        // 否则，显示（年月日 2012/12/10）
        if (mNowYearPublish != mPostYearPublish) {
            mFormatStr = mPostYearPublish + "-" + formatInt(mPostMonthPublish) + "-" + formatInt(mPostTodayPublish);
            return mFormatStr;
        }

        return mFormatStr;
    }

    public static String getFormattedCommentTime(long timeLong) {
        Date pushplishTime = new Date(timeLong);

        Calendar nowCalendar = Calendar.getInstance();
        int mNowYearPublish = nowCalendar.get(Calendar.YEAR); // 当前时间的年

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(pushplishTime);
        int mPostTodayPublish = calendar.get(Calendar.DAY_OF_MONTH); // 目标时间的日
        int mPostMonthPublish = (calendar.get(Calendar.MONTH) + 1); // 目标时间的月
        int mPostYearPublish = calendar.get(Calendar.YEAR); // 目标时间的年
        int mPostHourPublish = calendar.get(Calendar.HOUR_OF_DAY); // 目标时间的时
        int mPosMinutePublish = calendar.get(Calendar.MINUTE); // 目标时间的时

        // 原始时间差
        long time = nowCalendar.getTime().getTime() - pushplishTime.getTime(); // 时间差
        // 归零的时间差

        String mFormatStr = null;
        if (time < 0 || time / 1000 <= 60) {
            mFormatStr = "刚刚";
            return mFormatStr;
        }

        // 如果时间差在60min之内 （*分钟前）
        if (time / (1000 * 60) <= 60) {
            mFormatStr = time / (1000 * 60) + "分钟前";
            return mFormatStr;
        }

        // 如果时间差在12h之内 （*小时前）
        if (time / (1000 * 60 * 60) < 24) {
            mFormatStr = time / (1000 * 60 * 60) + "小时前";
            return mFormatStr;
        }

        // 如果年份相同，并且相差两天以上，则显示（日期+时间 12月3日 01:00）
        if (mNowYearPublish == mPostYearPublish) {
            mFormatStr = mPostMonthPublish + "月" + mPostTodayPublish + "日" + " " + formatInt(mPostHourPublish) + ":" + formatInt(mPosMinutePublish);
            return mFormatStr;
        }

        // 否则，显示（年月日 2012/12/10）
        if (mNowYearPublish != mPostYearPublish) {
            mFormatStr = mPostYearPublish + "年" + mPostMonthPublish + "月" + mPostTodayPublish + "日";
            return mFormatStr;
        }

        return mFormatStr;
    }


    /**
     * 2018年05月21日
     *
     * @param timeLong
     * @return
     */
    public static String getFormattedTimeChinese(long timeLong) {
        try {
            Date pushplishTime = new Date(timeLong);

            Calendar nowCalendar = Calendar.getInstance();
            int mNowYearPublish = nowCalendar.get(Calendar.YEAR); // 当前时间的年

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(pushplishTime);
            int mPostTodayPublish = calendar.get(Calendar.DAY_OF_MONTH); // 目标时间的日
            int mPostMonthPublish = (calendar.get(Calendar.MONTH) + 1); // 目标时间的月
            int mPostYearPublish = calendar.get(Calendar.YEAR); // 目标时间的年

            // 原始时间差
            long time = nowCalendar.getTime().getTime() - pushplishTime.getTime(); // 时间差
            // 归零的时间差

            String mFormatStr = null;
            //一秒之内
            if (time < 0 || time / 1000 < 1) {
                mFormatStr = "刚刚";
                return mFormatStr;
            }
            //一分钟之内
            if ((time / 1000) < 60) {
                mFormatStr = "刚刚";
                return mFormatStr;
            }

            // 如果时间差在60min之内 （*分钟前）
            if (time / (1000 * 60) < 60) {
                mFormatStr = time / (1000 * 60) + "分钟前";
                return mFormatStr;
            }

            // 如果时间差在12h之内 （*小时前）
            if (time / (1000 * 60 * 60) < 24) {
                mFormatStr = time / (1000 * 60 * 60) + "小时前";
                return mFormatStr;
            }

            // 如果年份相同，并且相差两天以上，则显示（日期+时间 12月3日 01:00）
            if (mNowYearPublish == mPostYearPublish) {
                mFormatStr = formatInt(mPostMonthPublish) + "月" + formatInt(mPostTodayPublish) + "日";
                return mFormatStr;
            }

            // 否则，显示（年月日 2012/12/10）
            if (mNowYearPublish != mPostYearPublish) {
                mFormatStr = mPostYearPublish + "年" + formatInt(mPostMonthPublish) + "月" + formatInt(mPostTodayPublish) + "日";
                return mFormatStr;
            }

            return mFormatStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String formatInt(int value) {
        return (value < 10 ? "0" : "") + value;
    }


    public static boolean withinDays(String time, int days) {
        try {
            long s = TimeUnit.MILLISECONDS.toSeconds(
                    new Date().getTime() - YEAR_FORMAT.parse(time).getTime());
            long count = s / DAY;
            if (0 < count && count <= days) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int compareTime(String a, String b) {
        try {
            long timeA = TimeUnit.MILLISECONDS.toSeconds(
                    new Date().getTime() - YEAR_FORMAT.parse(a).getTime());
            long timeB = TimeUnit.MILLISECONDS.toSeconds(
                    new Date().getTime() - YEAR_FORMAT.parse(b).getTime());

            return timeA >= timeB ? 1 : -1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int compareDate(String a, String b) {
        try {
            long timeA = YEAR_FORMAT.parse(a).getTime();
            long timeB = YEAR_FORMAT.parse(b).getTime();
            return timeA >= timeB ? 1 : -1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean isSameDate(long date1, long date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        return isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);
    }

    public static List<String> dateToWeek(Date mDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        int b = mDate.getDay();
        if (b == 0) b = 7;
        Date fDate;
        List<String> list = new ArrayList<>();
        Long fTime = mDate.getTime() - (b - 1) * 24 * 3600000;
        for (int a = 1; a <= 7; a++) {
            fDate = new Date();
            fDate.setTime(fTime + ((a - 1) * 24 * 3600000));
            list.add(sdf.format(fDate));
        }
        return list;
    }

    public static boolean isWithInThreeDays(long time) {
        return (System.currentTimeMillis() - time - 3 * 24 * 60 * 60 * 1000) <= 0;
    }

    public static boolean[] diffDate(long time1, long time2) {
        boolean[] ret = {false, false};

        Calendar pre = Calendar.getInstance();
        pre.setTimeInMillis(time1);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time2);

        if (cal.get(Calendar.YEAR) != (pre.get(Calendar.YEAR))) {
            ret[0] = true;
            ret[1] = true;
        } else {
            if (cal.get(Calendar.DAY_OF_YEAR) != pre.get(Calendar.DAY_OF_YEAR)) {
                ret[1] = true;
            }
        }
        return ret;
    }

    public static boolean isToday(long time) {
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        Calendar cal = Calendar.getInstance();
        Date date = new Date(time);
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            return diffDay == 0;
        }
        return false;
    }

    public static boolean isYesterday(long time) {
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = new Date(time);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            return diffDay == -1;
        }
        return false;
    }

    public static String HH_MM(long time) {
        return timeToStr(HH_MM, time);
    }

    public static String MM_DD(long time) {
        return timeToStr("MM-dd", time);
    }

    public static String timeToStr(String template, long time) {
        return timeToStr(template, time, getFormater(), null);
    }

    private static SimpleDateFormat getFormater() {
        SimpleDateFormat fmt = mLocalFormater.get();
        if (fmt == null) {
            fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault());
            mLocalFormater.set(fmt);
        }
        return fmt;
    }

    public static String timeToStr(String template, long time,
                                   SimpleDateFormat formater, TimeZone timeZone) {
        String res = "";
        if (formater == null)
            return res;
        try {
            formater.applyPattern(template);
            if (timeZone != null)
                formater.setTimeZone(timeZone);
            res = formater.format(new Date(time));
        } catch (Exception e) {
        }
        return res;
    }


    public static int getWeekDay(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i < WEEK_DAYS.length; i++)
            if (WEEK_DAYS[i] == day)
                return i;
        return 0;
    }

    public static String getWeekDayStr(long date) {
        int week = getWeekDay(date);
        if (week < 0 || week > 6)
            week = 0;
        return "星期" + WEEK_INDEX.charAt(week);
    }

    public static String YYYY_MM_DD(long time) {
        return timeToStr(YYYY_MM_DD, time);
    }

    /**
     * @param time 时间
     * @return 2018.11.09
     */
    public static String YYYY_MM_DD2(long time) {
        return timeToStr(YYYY_MM_DD2, time);
    }

    public static long dateStringToLong(String strTime, String formatType) throws ParseException {
        Date date = dataStringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    public static Date dataStringToDate(String strTime, String formatType) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        return formatter.parse(strTime);
    }

    public static long dateToLong(Date date) {
        return date.getTime();
    }

    /**
     * duration 转为 02：20时长
     */
    public static String convertDurationTime(long duration) {
        return String.format("%02d:%02d", duration / 60 % 60, duration % 60);
    }

    /**
     * 转换时间格式 2019.5.21
     */
    public static String formatTimeToDrop(long time) {
        String format = "yyyy.MM.dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String date = sdf.format(time);

        return date;
    }

    /**
     * 转换时间格式
     */
    public static String formatTimeYMDHM(long time) {
        String format = "yyyy年MM月dd日 HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String date = sdf.format(time);
        return date;
    }

    /**
     * 转换时间格式
     */
    public static String formatTimeMD(long time) {
        String format = "MM月dd日";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String date = sdf.format(time);
        return date;
    }

    public static String formatTimeHMS(int time) {
        int h = time / 60 / 60;
        int m = (time - h * 60 * 60) / 60;
        int s = (time - m * 60 - h * 60 * 60);
        StringBuilder sb = new StringBuilder();
        if (h > 0) {
            sb.append(formatInt(h) + ":");
        }
        sb.append(formatInt(m) + ":");
        sb.append(formatInt(s));
        return sb.toString();
    }

    //
    public static String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
}
