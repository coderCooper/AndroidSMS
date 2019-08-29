package lollipop.com.sms.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static final String SMS_ACTION = "com.android.TinySMS.RESULT";

    public static String formatDateTime(long time) {
//        return formatDateTime("yyyy-MM-dd HH:mm:ss",time);

        return getDataTime(time);
    }

    public static String formatDateTime(String pattern, long time) {

        SimpleDateFormat format = new SimpleDateFormat(pattern);

        Date date = new Date(time);

        return format.format(date);
    }

    public static String getDataTime(long timeStamp){
        if (timeStamp==0) return "";
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp);
        Date currenTimeZone = inputTime.getTime();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(inputTime)){
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(currenTimeZone);
        }
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        if (calendar.before(inputTime)){
            DecimalFormat df = new DecimalFormat("#00");
            return "昨天 " + inputTime.get(Calendar.HOUR_OF_DAY) + " : " + df.format(inputTime.get(Calendar.MINUTE));
        }else{
            calendar.add(Calendar.DAY_OF_MONTH, -5);
            if (calendar.before(inputTime)){
                DecimalFormat df = new DecimalFormat("#00");
                return getWeekDayStr(inputTime.get(Calendar.DAY_OF_WEEK)) + " " + inputTime.get(Calendar.HOUR_OF_DAY)   + " : " +  df.format(inputTime.get(Calendar.MINUTE));
            } else {
                calendar.add(Calendar.YEAR, -1);
                if (calendar.before(inputTime)){
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
                    return sdf.format(currenTimeZone);
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    return sdf.format(currenTimeZone);
                }
            }
        }
    }

    private static String getWeekDayStr(int indexOfWeek){
        String weekDayStr = "";
        switch (indexOfWeek)
        {
            case 1:
                weekDayStr = "周日";
                break;
            case 2:
                weekDayStr = "周一";
                break;
            case 3:
                weekDayStr = "周二";
                break;
            case 4:
                weekDayStr = "周三";
                break;
            case 5:
                weekDayStr = "周四";
                break;
            case 6:
                weekDayStr = "周五";
                break;
            case 7:
                weekDayStr = "周六";
                break;
        }
        return weekDayStr;
    }
}
