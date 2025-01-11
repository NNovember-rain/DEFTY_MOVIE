package com.defty.movie.utils;

import com.defty.movie.exception.CustomDateException;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class DateUtil {
    public static Date stringToSqlDate(String s){
        Date result = null;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            result = sdf.parse(s);

//            Calendar startCal = Calendar.getInstance();
//            startCal.setTime(result);
//            startCal.set(Calendar.HOUR_OF_DAY, 0);
//            startCal.set(Calendar.MINUTE, 0);
//            startCal.set(Calendar.SECOND, 0);
//            startCal.set(Calendar.MILLISECOND, 0);
//            result = startCal.getTime();
        }
        catch (Exception e){
            throw new CustomDateException("please enter the right date format: dd/MM/yyyy");
        }
        return result;
    }
}
