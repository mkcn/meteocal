/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Event;
import entity.Location;
import entity.User;
import entity.Weather;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author andrea
 */
public class WeatherAdviceCreatorTest {
    
    private WeatherAdviceCreator wac;
    
    private Long date;
    private Location location;
    private Event event;
    
    @Before
    public void init(){
        date = Tool.ToolDate.getTodayDate();
        wac = new WeatherAdviceCreator();
        wac.wm = Mockito.mock(WeatherManager.class);
        setEvent();
        
    }
    
    //@Test
    public void testGetWeatherAdviceNeededNotNull(){
        List<Weather> weatherList = null;
        weatherList = this.setWeatherList();
        
        
        Mockito.when(wac.wm.getWeatherFromLocationBetweenTwodate(Mockito.anyLong(), 
                Mockito.anyLong(), Mockito.anyLong())).thenReturn(weatherList);
        
       assertNotNull(wac.getWeatherAdvice(event));
    }
    
    @Test
    public void testGetWeatherAdviceNeededNull(){
        List<Weather> weatherList = null;
        weatherList = this.setWeatherList2();
        
        
        Mockito.when(wac.wm.getWeatherFromLocationBetweenTwodate(Mockito.anyLong(), 
                Mockito.anyLong(), Mockito.anyLong())).thenReturn(weatherList);
        
       assertNull(wac.getWeatherAdvice(event));
    }
    
    @Test
    public void testGetWeatherAdviceNotNeeded(){
        List<Weather> weatherList = null;
        weatherList = this.setWeatherList3();
        
        
        Mockito.when(wac.wm.getWeatherFromLocationBetweenTwodate(Mockito.anyLong(), 
                Mockito.anyLong(), Mockito.anyLong())).thenReturn(weatherList);
        
       assertNull(wac.getWeatherAdvice(event));
    }
    

    
    private void setEvent(){
        event = Mockito.mock(Event.class);
        location = Mockito.mock(Location.class);
        Mockito.when(event.getOwner()).thenReturn(new User());
        Mockito.when(event.getStartdate()).thenReturn(date);
        Mockito.when(event.getEnddate()).thenReturn(date);
        Mockito.when(event.getLocation()).thenReturn(location);
        Mockito.when(location.getLocationID()).thenReturn(date);
    }

    
    private List<Weather> setWeatherList(){
        date = Long.MAX_VALUE;
        List<Weather> weatherList  = new ArrayList<Weather>();
        
        
        Weather weather1 = new Weather();  
        weather1.setWeatherDate(date);
        weather1.setTargetLocation(location);
        weather1.setGoodWeather(false);
        
        Weather weather2 = new Weather();
        weather2.setWeatherDate(date);
        weather2.setTargetLocation(location);
        weather2.setGoodWeather(true);
        
        Weather weather3 = new Weather();
        weather3.setWeatherDate(date);
        weather3.setTargetLocation(location);
        weather3.setGoodWeather(true);
        
        Weather weather4 = new Weather();
        weather4.setWeatherDate(date);
        weather4.setTargetLocation(location);
        weather3.setGoodWeather(true);
        
        Weather weather5 = new Weather();
        weather5.setWeatherDate(date);
        weather5.setTargetLocation(location);
        weather5.setGoodWeather(true);
        
        Weather weather6 = new Weather();
        weather6.setWeatherDate(date);
        weather6.setTargetLocation(location);
        weather6.setGoodWeather(true);
        
        weatherList.add(0,weather1);
        weatherList.add(1,weather2);
        weatherList.add(2,weather3);
        weatherList.add(3,weather4);
        weatherList.add(4,weather5);
        weatherList.add(5,weather6);
        
        
        return weatherList;
        
    }
    
    private List<Weather> setWeatherList2(){
        date = Long.MAX_VALUE;
        List<Weather> weatherList  = new ArrayList<Weather>();
        
        
        Weather weather1 = new Weather();  
        weather1.setWeatherDate(date);
        weather1.setTargetLocation(location);
        weather1.setGoodWeather(false);
        
        Weather weather2 = new Weather();
        weather2.setWeatherDate(date);
        weather2.setTargetLocation(location);
        weather2.setGoodWeather(false);
        
        Weather weather3 = new Weather();
        weather3.setWeatherDate(date);
        weather3.setTargetLocation(location);
        weather3.setGoodWeather(false);
        
        Weather weather4 = new Weather();
        weather4.setWeatherDate(date);
        weather4.setTargetLocation(location);
        weather4.setGoodWeather(false);
        
        Weather weather5 = new Weather();
        weather5.setWeatherDate(date);
        weather5.setTargetLocation(location);
        weather5.setGoodWeather(false);
        
        Weather weather6 = new Weather();
        weather6.setWeatherDate(date);
        weather6.setTargetLocation(location);
        weather6.setGoodWeather(false);
        
        weatherList.add(0,weather1);
        weatherList.add(1,weather2);
        weatherList.add(2,weather3);
        weatherList.add(3,weather4);
        weatherList.add(4,weather5);
        weatherList.add(5,weather6);
        
        
        return weatherList;
        
    }
    
    private List<Weather> setWeatherList3(){
        date = Long.MAX_VALUE;
        List<Weather> weatherList  = new ArrayList<Weather>();
        
        
        Weather weather1 = new Weather();  
        weather1.setWeatherDate(date);
        weather1.setTargetLocation(location);
        weather1.setGoodWeather(true);
        
        Weather weather2 = new Weather();
        weather2.setWeatherDate(date);
        weather2.setTargetLocation(location);
        weather2.setGoodWeather(true);
        
        Weather weather3 = new Weather();
        weather3.setWeatherDate(date);
        weather3.setTargetLocation(location);
        weather3.setGoodWeather(true);
        
        Weather weather4 = new Weather();
        weather4.setWeatherDate(date);
        weather4.setTargetLocation(location);
        weather4.setGoodWeather(true);
        
        Weather weather5 = new Weather();
        weather5.setWeatherDate(date);
        weather5.setTargetLocation(location);
        weather5.setGoodWeather(true);
        
        Weather weather6 = new Weather();
        weather6.setWeatherDate(date);
        weather6.setTargetLocation(location);
        weather6.setGoodWeather(true);
        
        weatherList.add(0,weather1);
        weatherList.add(1,weather2);
        weatherList.add(2,weather3);
        weatherList.add(3,weather4);
        weatherList.add(4,weather5);
        weatherList.add(5,weather6);
        
        
        return weatherList;
        
    }
}
