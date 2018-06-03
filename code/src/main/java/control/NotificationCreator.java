/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import boundary.CONST;
import entity.Event;
import entity.Location;
import entity.WarningType;
import entity.Weather;
import entity.WeatherAdvice;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 * Handle the cration of notification such as allert and weather advice
 * @author andrea
 */
@Singleton
@Startup
public class NotificationCreator {
    
    @Inject
    LocationManager lm;
    
    @Inject
    NotificationManager nm;
    
    @Inject
    EventManager em;
    
    @Inject 
    WeatherManager wm;
    
    @Inject
    WeatherUpdater wu;
    
    @Inject
    WeatherAdviceCreator wac;
    
    @Inject
    LocationLoader ll;
    
    private Long lastUpdate;
    private final int DAYS_ALLERT = 1;
    private final int DAYS_WEATHER_ADVICE = 3;
    
    /**
     * when the server is started a weather update is request
     */
    @PostConstruct
    public void init(){
        ll.loadLocation();
        createNotification();
    }
    /**
     *Every two hours start and create allert notification, 
     * update weather and create weather advice notification
     */
    @Schedule(second = "0", minute = "0", hour = "*/2", persistent = false)
    public void createNotification(){
        lastUpdate = Tool.ToolDate.getTodayDate();
        updateWeather();
        createAllert();       
        createWeatherWarning();
    }
    
    /**
     * Get all the events which start in the next day
     * then create an allert for all the participants,
     * also for the owner
     */
    public void createAllert(){
        //get when
        Long endDate = Tool.ToolDate.getNDaysAhead(DAYS_ALLERT);
        //get who
        List<Event> tomorrowEvent = em.findEventEveryBetweenTwoDate(lastUpdate,endDate);
        //create Warning
        for(Event x: tomorrowEvent){
           nm.createWarninForOwner(x, WarningType.ALLERT);
           nm.createWarninForAllParticipants(x, WarningType.ALLERT);
           em.setAllerted(x);
        }        
    }
    
    
    
    /**
     * Get all the outside events which start in the next three days
     * then create a weather advice in case of bad weather
     */
    public void createWeatherWarning() {
         //get when
        Long endDate = Tool.ToolDate.getNDaysAhead(DAYS_WEATHER_ADVICE);
        //get who
        List<Event> threeDayEvent = em.findEventEveryBetweenTwoDateOutside(lastUpdate,endDate);
        //create notifications
        for(Event x: threeDayEvent){
            WeatherAdvice weatherAdvice = wac.getWeatherAdvice(x);
            if(weatherAdvice != null){
                nm.createWeatherAdvice(weatherAdvice);
                em.setAdviced(x);
            }             
        }
        
    }
        
    private void updateWeather(){
        
        //for every Location update the weather  
        List<Location> allLocation = lm.findLocation();      
        for(Location x: allLocation){
            try {
                List<Weather> updatedWeather = wu.dailyForecastForLocation(x);
                if(updatedWeather.size() > 0){
                    wm.deleteWeather(x.getLocationID());
                    wm.updateWeather(updatedWeather, x.getLocationID());
                }
            } catch (IOException ex) {
                CONST.PrintError("WeatherUpdateError", "Failed to update weather for: " + x.getLocationName());
            }
        }
        
    }
    

    
}
