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
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author andrea
 */
@RunWith(Arquillian.class)
public class UserDataLoaderIT {
    
    @Inject
    UserDataLoader udl;
    
    @Inject
    UserManager um;
    
    @Inject
    EventManager em;
    
    @Inject
    NotificationManager nm;
    
    @Inject
    LocationManager lm;
    
    @Inject
    WeatherManager wm;
    
    
    
    
    @Deployment
    public static WebArchive createArchiveAndDeploy() {

        
         return ShrinkWrap.create(WebArchive.class)
                .addClass(UserDataLoader.class)
                .addClass(NotificationManager.class)
                .addClass(UserManager.class)
                .addClass(EventManager.class)
                .addClass(LocationManager.class)
                .addClass(WeatherManager.class)
                .addClass(WeatherAdviceCreator.class)
                .addPackage(Event.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));        
    }
    

    
    @Test
    public void test(){
        Location location = new Location();
        location.setLocationID(new Long(999));
        location.setLocationName("Milan");
        
        lm.saveLocation(location);
        
        
        String email = "a@a.a";
        User user = new User();
        user.setEmail(email);
        user.setGroupName("");
        user.setLiveIn(location);
        user.setName("");
        user.setPassword("");
        user.setPublicCalendar(true);
        
        um.save(user);
        
        Event event = new Event();
        event.setEventID(new Long(1));
        event.setAddress("a");
        event.setAllDay(false);
        event.setDescription("a");
        event.setEnddate(Long.MAX_VALUE);
        event.setStartdate(Long.MIN_VALUE);
        event.setPublicEvent(Boolean.TRUE);
        event.setEventName("a");
        event.setOutside(Boolean.TRUE);
        event.setOwner(user);
        

        
        event.setLocation(location);
        em.modifyEvent(event);
        
        Event event2 = new Event();
        event2.setEventID(new Long(1));
        event2.setAddress("b");
        event2.setAllDay(false);
        event2.setDescription("a");
        event2.setEnddate(Long.MAX_VALUE);
        event2.setStartdate(Long.MIN_VALUE);
        event2.setPublicEvent(Boolean.TRUE);
        event2.setEventName("a");
        event2.setOutside(Boolean.TRUE);
        event2.setLocation(location);
        event2.setOwner(user);
  
                
        
        User user2 = new User();
        user2.setEmail("c@c.c");
        user2.setGroupName("");
        user2.setLiveIn(location);
        user2.setName("");
        user2.setPassword("");
        user2.setPublicCalendar(true);
        
        um.save(user2);
        
        
        //assertEquals(WarningType.CHANGE_ADDRESS,udl.findWhatCahnged(event2));
        
        List<User> listInvit = new ArrayList<User>();
        listInvit.add(user2);
        
        udl.modifyEvent(event2, listInvit);
        
    }
    
    
    @Test
    public void weatherAdviceShouldBeCreated(){
        
        Long startDate = Tool.ToolDate.getTodayDate();
        Long date1 = Tool.ToolDate.getNDaysAhead(1);
        Long date2 = Tool.ToolDate.getNDaysAhead(2);
        Long date3 = Tool.ToolDate.getNDaysAhead(3);
        Long endDate = Tool.ToolDate.getNDaysAhead(4);
    
        String email=  "b@a.a"; 
        User user = new User();
        user.setEmail(email);
        user.setGroupName("");
        user.setName("");
        user.setPassword("");
        user.setPublicCalendar(true);
      
        
        
                
        Location location = new Location();
        location.setLocationID(new Long(1001));
        location.setLocationName("Milan");
        
        Long evtId = new Long(1); 
        Event event = new Event();
        event.setEventID(evtId);
        event.setAddress("a");
        event.setAllDay(false);
        event.setDescription("a");
        event.setEnddate(endDate);
        event.setStartdate(startDate);
        event.setPublicEvent(Boolean.TRUE);
        event.setEventName("a");
        event.setOutside(Boolean.TRUE);
        event.setLocation(location);
        event.setOwner(user);
        event.setAdviced(Boolean.FALSE);
        event.setAllerted(Boolean.FALSE);

        
        Weather weather1,weather2,weather3,weather4,weather5;
        
        weather1 = new Weather();
        weather2 = new Weather();
        weather3 = new Weather();
        weather4 = new Weather();
        weather5 = new Weather();
        
        weather1.setWeatherDate(Tool.ToolDate.removeHours(startDate));        
        weather2.setWeatherDate(Tool.ToolDate.removeHours(date1));
        weather3.setWeatherDate(Tool.ToolDate.removeHours(date2));
        weather4.setWeatherDate(Tool.ToolDate.removeHours(date3));
        weather5.setWeatherDate(Tool.ToolDate.removeHours(endDate));
        
        weather1.setTargetLocation(location);
        weather2.setTargetLocation(location);
        weather3.setTargetLocation(location);
        weather4.setTargetLocation(location);
        weather5.setTargetLocation(location);
        
        weather1.setGoodWeather(Boolean.TRUE);
        weather2.setGoodWeather(Boolean.FALSE);
        weather3.setGoodWeather(Boolean.TRUE);
        weather4.setGoodWeather(Boolean.TRUE);
        weather5.setGoodWeather(Boolean.TRUE);
        
        
        lm.saveLocation(location);
        um.save(user);
        em.modifyEvent(event);
        wm.updateWeather(weather1);
        wm.updateWeather(weather2);
        wm.updateWeather(weather3);
        wm.updateWeather(weather4);
        wm.updateWeather(weather5);
        
        udl.modifyevent(event);
        
        
        
           
    }

    
}
