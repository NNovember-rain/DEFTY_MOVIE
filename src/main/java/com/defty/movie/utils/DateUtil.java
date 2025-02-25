package com.defty.movie.utils;

import com.defty.movie.exception.CustomDateException;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtil {
    public static Date stringToSqlDate(String s){
        Date result = null;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            result = sdf.parse(s);
        }
        catch (Exception e){
            throw new CustomDateException("please enter the right date format: dd/MM/yyyy");
        }
        return result;
    }

    public static String dateToString(Date inputDate) {
        SimpleDateFormat outputFormatter = new SimpleDateFormat("dd-MM-yyyy");
        return outputFormatter.format(inputDate);
    }
}
