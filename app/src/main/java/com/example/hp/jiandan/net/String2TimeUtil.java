package com.example.hp.jiandan.net;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by hp on 2016/3/15.
 */
public class String2TimeUtil {
    /**
     * 转换日期格式到用户体验好的时间格式
     * @param time 2015-04-11 12:45:06
     * @return
     */

    public static String dateString2GoodExperienceFormat(String time){

        if(isNullString(time)){
            return "";
        }else {
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //设置时区
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));

            try {
                String timeString;
                Date parse=dateFormat.parse(time);
                long distanceTime=new Date().getTime()-parse.getTime();

                if(distanceTime<0L){

                    timeString = "0 mins ago";
                }else {
                    long n2 = distanceTime / 60000L;
                    if(n2<60L){
                        timeString=String.valueOf(n2)+" mins ago";

                    }else if(n2<720L){
                        timeString = String.valueOf(n2 / 60L) + " hours ago";

                    }else {

                        timeString=dateFormat.format(parse);
                    }


                }
                return  timeString;
            }catch (Exception e){
                e.printStackTrace();
                return "";
            }


        }

    }
    public static boolean isNullString(String s) {
        return s == null || s.equals("") || s.equals("null");
    }

}
