/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tool;

import boundary.CONST;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author andrea
 */
public class ToolDate {
    
    /**
     *
     * @return
     */
    public static Long getTodayDate(){
       
        Date todayDate = new Date();
        return  todayDate.getTime() ;
    }
    
    /**
     * 
     * @param numberOfDays
     * @return 
     */
    public static Long getNDaysAhead(int numberOfDays){
        
        return ToolDate.shiftDay(ToolDate.getTodayDate(), numberOfDays);
    }
    
    /**
     * Receive a date and shift it a given number of
     * day
     * @param date
     * @param numberOfDays
     * @return 
     */
    public static Long shiftDay(Long date, int numberOfDays){
        Calendar c = Calendar.getInstance(Locale.ITALY); 
        c.setTimeInMillis(date); 
        c.add(Calendar.DATE, numberOfDays);
        
        return c.getTimeInMillis();
    }
    
    
    public static Long shiftHours(Long date, int numberOfDays){
        Calendar c = Calendar.getInstance(Locale.ITALY);  
        c.setTimeInMillis(date); 
        c.add(Calendar.HOUR, numberOfDays);
        
        return c.getTimeInMillis(); 
    }
    
    /**
     * Receive a long a value representing a date
     * return the the same date with hours, minute and second set to 0
     * @param date
     * @return 
     */
    public static Long removeHours(Long date){        
        Calendar c = Calendar.getInstance(Locale.ITALY); 
        c.setTimeInMillis(date);
        
        Date dt = new Date(date);
        

        
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        
        
        
        return c.getTimeInMillis();
    }
    
}
