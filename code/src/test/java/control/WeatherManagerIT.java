/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Location;
import entity.Weather;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author andrea
 */
@RunWith(Arquillian.class)
public class WeatherManagerIT {
    
    @Inject
    WeatherManager wm;
    
    @Inject
    LocationManager lm;
    
    
    Long date = Tool.ToolDate.getTodayDate();
    Long dateMinusOne = Tool.ToolDate.shiftDay(date, -1);
    Long dateplusOne = Tool.ToolDate.shiftDay(date, 1);
    
    @Deployment
    public static WebArchive createArchiveAndDeploy() {
        
        return ShrinkWrap.create(WebArchive.class)
                .addClass(WeatherManager.class)
                .addClass(LocationManager.class)
                .addPackage(Weather.class.getPackage())
                .addAsResource("test-persistence.xml","META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
    }
    
    
    @Test
    public void ITGetWeatherFromLocationAndDate(){
        Long id = new Long(1001);
        Location location = new Location();
        location.setLocationID(id);
        location.setLocationName("Milan");
        
        Long id2 = new Long(1002);
        Location location2 = new Location();
        location2.setLocationID(id2);
        location2.setLocationName("Rome");
        
        lm.saveLocation(location);
        lm.saveLocation(location2);
        
        Weather weather,weather1,weather2;
        weather = new Weather();        
        weather.setWeatherDate(date);
        weather.setTargetLocation(location);
        
        weather1 = new Weather();
        weather1.setWeatherDate(date);
        weather1.setTargetLocation(location2);
        
        weather2 = new Weather();
        weather2.setWeatherDate(Tool.ToolDate.getNDaysAhead(2));
        weather2.setTargetLocation(location2);
            
        wm.updateWeather(weather);
        wm.updateWeather(weather1);
        wm.updateWeather(weather2);
        
        
        
        Weather weather3 = wm.getWeatherFromLocationAndDate(id,date);
        
        //add assert equals on weather 3 and weather      
        
        
    }
    
    @Test
    public void ITGetWeatherFromLocationBetweenTwodate(){
        Long id = new Long(1003);
        String locationName = "Rome";
        Location location = new Location();
        location.setLocationID(id);
        location.setLocationName(locationName);
        
        lm.saveLocation(location);
        
        Long midDate = Tool.ToolDate.shiftDay(date, 1);
        Long endDate = Tool.ToolDate.shiftDay(date, 2);
        Long plusendDate = Tool.ToolDate.shiftDay(date, 4);
        
        Weather weather1 = new Weather();  
        weather1.setWeatherDate(Tool.ToolDate.removeHours(date));
        weather1.setTargetLocation(location);
        
        Weather weather2 = new Weather();
        weather2.setWeatherDate(Tool.ToolDate.removeHours(midDate));
        weather2.setTargetLocation(location);
        
        Weather weather3 = new Weather();
        weather3.setWeatherDate(Tool.ToolDate.removeHours(endDate));
        weather3.setTargetLocation(location);
        
        Weather weather4 = new Weather();
        weather4.setWeatherDate(Tool.ToolDate.removeHours(plusendDate));
        weather4.setTargetLocation(location); 
        
        wm.updateWeather(weather2);
        wm.updateWeather(weather1);
        wm.updateWeather(weather3);
        wm.updateWeather(weather4);
        
        assertEquals(wm.getWeatherFromLocationBetweenTwodate(id, date, endDate).size(),3);
       
    }
    
    //@Test
    public void ITWeatherFromLocation(){
        Long id = new Long(1004);
        String locationName = "Rome";
        Location location = new Location();
        location.setLocationID(id);
        location.setLocationName(locationName);
        
        lm.saveLocation(location);
        
        assertEquals(0,wm.getWeatherFromLocation(id).size());
        
        Weather weather1 = new Weather(); 
        weather1.setTargetLocation(location);
        
        Weather weather2 = new Weather();
        weather2.setTargetLocation(location);
        
        Weather weather3 = new Weather();
        weather3.setTargetLocation(location);
        
        wm.updateWeather(weather3);
        wm.updateWeather(weather2);
        wm.updateWeather(weather1);
        
        assertEquals(3,wm.getWeatherFromLocation(id).size());
        
        List<Weather> weathers = new ArrayList<Weather>();
        weathers.add(weather1);
        
        wm.updateWeather(weathers,id);
        
        assertEquals(1,wm.getWeatherFromLocation(id).size());

    }
    

    
    
    
}
