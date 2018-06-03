/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tool;

import java.util.Date;
import org.junit.Test;

/**
 *
 * @author andrea
 */
public class ToolDateTest {
    
    @Test
    public void test1(){
        long dt = Tool.ToolDate.shiftHours(Tool.ToolDate.getTodayDate(),2);
        
        Date dtDate = new Date(dt);
        
        long dt2 = Tool.ToolDate.shiftDay(Tool.ToolDate.getTodayDate(),2);
        
        Date dt2Date = new Date(dt2);
        
        Date dt3 = new Date(Tool.ToolDate.removeHours(dt2));
        
        long ddtg = Tool.ToolDate.removeHours(dt);
        
        Date ddtgt = new Date(ddtg);
        
        dt2++;
    }
    
    
}
