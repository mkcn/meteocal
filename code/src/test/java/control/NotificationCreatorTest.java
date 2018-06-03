/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Event;
import entity.Location;
import entity.Weather;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author andrea
 */
public class NotificationCreatorTest {

    private NotificationCreator nc;
    private List<Location> locations;
    private List<Weather> updatedWeather;
    private List<Event> events;
    
    @Before
    public void setUp(){
        nc = new NotificationCreator();
        nc.lm = Mockito.mock(LocationManager.class);
        nc.nm = Mockito.mock(NotificationManager.class);
        nc.em = Mockito.mock(EventManager.class);
        nc.wac = Mockito.mock(WeatherAdviceCreator.class);
        nc.wm = Mockito.mock(WeatherManager.class);
        nc.wu = Mockito.mock(WeatherUpdater.class);
        
        
        
        setUpLocations();
        setUpweatherList();
        setUpEvents();
        
    }
    
    
    @Test
    public void testNotificationCreator() throws IOException{
        
        Mockito.when(nc.lm.findLocation()).thenReturn(locations);
        
        Mockito.when(nc.wu.dailyForecastForLocation(Mockito.any(Location.class)))
                     .thenReturn(updatedWeather);
        
        Mockito.when(nc.em.findEventEveryBetweenTwoDate(Mockito.anyLong(), 
                                      Mockito.anyLong()))
                                           .thenReturn(events);
        
        Mockito.when(nc.em.findEventEveryBetweenTwoDateOutside(Mockito.anyLong(), 
                                      Mockito.anyLong()))
                                           .thenReturn(events);
        
        
        nc.createNotification();
        
        
    }
    
    
    private void setUpEvents(){
        
        Event  event1,event2;
        
        event1 = new Event();
        event2 = new Event();
        
        events = new ArrayList<Event>();
        events.add(0, event1);
        events.add(1, event2);
        
    }

    private void setUpLocations() {
        
        Location rome = new Location();
        rome.setLocationID(new Long(1));
        rome.setLocationName("Rome");
        
        Location milan = new Location();
        milan.setLocationID(new Long(2));
        milan.setLocationName("Milan");
        
        locations = new ArrayList<Location>();
        this.locations.add(0,rome);
        this.locations.add(1, milan);
               
        
    }

    private void setUpweatherList() {
        
        Weather weather1,weather2,weather3;
        
        weather1 = new Weather();
        weather2 = new Weather();
        weather3 = new Weather();
        
        
        updatedWeather = new ArrayList<Weather>();
        updatedWeather.add(0,weather1);
        updatedWeather.add(1, weather2);
        updatedWeather.add(2, weather3);
    }
    
    
}
