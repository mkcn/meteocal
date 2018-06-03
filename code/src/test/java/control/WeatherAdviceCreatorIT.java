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
import entity.WeatherAdvice;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author andrea
 */
@RunWith(Arquillian.class)
public class WeatherAdviceCreatorIT {
    
    @Inject
    UserManager um;
    
    @Inject
    EventManager em;
    
    @Inject
    WeatherManager wm;
    
    @Inject
    LocationManager lm;
    
    @Inject
    WeatherAdviceCreator wac;
    
    
    @Deployment
    public static WebArchive createArchiveAndDeploy() {
        
        return ShrinkWrap.create(WebArchive.class)
                .addClass(EventManager.class)
                .addClass(WeatherManager.class)
                .addClass(UserManager.class)
                .addClass(WeatherAdviceCreator.class)
                .addClass(LocationManager.class)
                .addPackage(Weather.class.getPackage())
                .addAsResource("test-persistence.xml","META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
    }
    
    
    
    
    @Test
    public void notNeededWeatherAdvice(){
        Long date = Tool.ToolDate.getTodayDate();
        Long firstDate = Tool.ToolDate.shiftDay(date, 1);
        Long secondDate = Tool.ToolDate.shiftDay(date, 2);
        Long thirdDate = Tool.ToolDate.shiftDay(date, 3);
        
        User user = new User();
        Event event = new Event();
        Location location = new Location();
        List<Weather> list = new ArrayList<Weather>();
        
        Weather weather1,weather2,weather3;
        
        user.setEmail("a@a.a");
        user.setGroupName("");
        user.setPassword("password");
        user.setName("a");
        user.setPublicCalendar(false);
        um.save(user);
        
        location.setLocationID(new Long(1001));
        location.setLocationName("Milan");
        lm.saveLocation(location);
        
        event.setStartdate(firstDate);
        event.setEnddate(thirdDate);
        event.setLocation(location);
        em.createEvent(event, user);
        
        weather1 = new Weather();
        weather1.setGoodWeather(true);
        weather1.setWeatherDate(Tool.ToolDate.removeHours(firstDate));
        weather1.setTargetLocation(location);
        wm.updateWeather(weather1);
        
        weather2 = new Weather();
        weather2.setGoodWeather(true);
        weather2.setWeatherDate(Tool.ToolDate.removeHours(secondDate));
        weather2.setTargetLocation(location);
        wm.updateWeather(weather2);
        
        weather3 = new Weather();
        weather3.setGoodWeather(true);
        weather3.setWeatherDate(Tool.ToolDate.removeHours(thirdDate));
        weather3.setTargetLocation(location);
        wm.updateWeather(weather3);
        
        WeatherAdvice wacE = wac.getWeatherAdvice(event);
        
        Assert.assertNull(wacE);

    }
    
    
    @Test
    public void neededButNotGoodWeather(){
        Long date = Tool.ToolDate.getTodayDate();
        Long firstDate = Tool.ToolDate.shiftDay(date, 1);
        Long secondDate = Tool.ToolDate.shiftDay(date, 2);
        Long thirdDate = Tool.ToolDate.shiftDay(date, 3);
        
        User user = new User();
        Event event = new Event();
        Location location = new Location();
        List<Weather> list = new ArrayList<Weather>();
        
        Weather weather1,weather2,weather3;
        
        user.setEmail("b@b.b");
        user.setGroupName("");
        user.setPassword("password");
        user.setName("a");
        user.setPublicCalendar(false);
        um.save(user);
        
        location.setLocationID(new Long(1002));
        location.setLocationName("Milan");
        lm.saveLocation(location);
        
        event.setStartdate(firstDate);
        event.setEnddate(thirdDate);
        event.setLocation(location);
        em.createEvent(event, user);
        
        weather1 = new Weather();
        weather1.setGoodWeather(false);
        weather1.setWeatherDate(Tool.ToolDate.removeHours(firstDate));
        weather1.setTargetLocation(location);
        wm.updateWeather(weather1);
        
        weather2 = new Weather();
        weather2.setGoodWeather(false);
        weather2.setWeatherDate(Tool.ToolDate.removeHours(secondDate));
        weather2.setTargetLocation(location);
        wm.updateWeather(weather2);
        
        weather3 = new Weather();
        weather3.setGoodWeather(false);
        weather3.setWeatherDate(Tool.ToolDate.removeHours(thirdDate));
        weather3.setTargetLocation(location);
        wm.updateWeather(weather3);
        
        WeatherAdvice wacE = wac.getWeatherAdvice(event);
        
        Assert.assertNull(wacE);

    }
    
    @Test
    public void neededNotNull(){
        Long date = Tool.ToolDate.getTodayDate();
        Long firstDate = Tool.ToolDate.shiftDay(date, 1);
        Long secondDate = Tool.ToolDate.shiftDay(date, 2);
        Long thirdDate = Tool.ToolDate.shiftDay(date, 3);
        
        User user = new User();
        Event event = new Event();
        Location location = new Location();
        List<Weather> list = new ArrayList<Weather>();
        
        Weather weather1,weather2,weather3;
        
        user.setEmail("c@c.c");
        user.setGroupName("");
        user.setPassword("password");
        user.setName("a");
        user.setPublicCalendar(false);
        um.save(user);
        
        location.setLocationID(new Long(1003));
        location.setLocationName("Milan");
        lm.saveLocation(location);
        
        event.setStartdate(firstDate);
        event.setEnddate(thirdDate);
        event.setLocation(location);
        em.createEvent(event, user);
        
        weather1 = new Weather();
        weather1.setGoodWeather(false);
        weather1.setWeatherDate(Tool.ToolDate.removeHours(firstDate));
        weather1.setTargetLocation(location);
        wm.updateWeather(weather1);
        
        weather2 = new Weather();
        weather2.setGoodWeather(true);
        weather2.setWeatherDate(Tool.ToolDate.removeHours(secondDate));
        weather2.setTargetLocation(location);
        wm.updateWeather(weather2);
        
        weather3 = new Weather();
        weather3.setGoodWeather(true);
        weather3.setWeatherDate(Tool.ToolDate.removeHours(thirdDate));
        weather3.setTargetLocation(location);
        wm.updateWeather(weather3);
        
        WeatherAdvice wacE = wac.getWeatherAdvice(event);
        
        Assert.assertNotNull(wacE);

    }
    
}
