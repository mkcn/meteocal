/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Event;
import entity.Weather;
import entity.WeatherAdvice;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Handle the creation of weather advice notification
 * @author andrea
 */
@Stateless
public class WeatherAdviceCreator {
    
    @Inject
    WeatherManager wm;
    
    /**
     * receive an event and return a waether advice if it needed
     * @param event
     * @return a weather advice or null if there is good weather
     */
    public WeatherAdvice getWeatherAdvice(Event event){
       
       
        
      Long needed = isWeatherAdviceNeeded(event);
      
      if(needed != 0){
         return createWeatherAdvice(event,needed); 
      }else{
        return null;   
      }
    }
    
    private WeatherAdvice createWeatherAdvice(Event event,Long needed){
        WeatherAdvice weatherAdvice = new WeatherAdvice();
        Long locId = event.getLocation().getLocationID();
        
       //giving Long.MAX_VALUE i am sure to take all the possible weather
        //for locId starting from the date found before
        List<Weather> weatherList =   
                wm.getWeatherFromLocationBetweenTwodate(locId,needed,Long.MAX_VALUE);
        
        
        for(Weather x: weatherList){
            if(x.getGoodWeather()){
                weatherAdvice.setAbout(event);
                weatherAdvice.setBestDate(x.getWeatherDate());
                weatherAdvice.setReceiver(event.getOwner());
                return weatherAdvice;
            }
        }
        
        //return null in case there is no godd weather
        return null;
    }
    
    private Long isWeatherAdviceNeeded(Event event){
       
        Long locId = event.getLocation().getLocationID();
        Long startDate = event.getStartdate();
        Long endDate = event.getEnddate();
        
        List<Weather> weatherList =   
                wm.getWeatherFromLocationBetweenTwodate(locId,startDate,endDate); 
        
        for(Weather x: weatherList){
            if(!x.getGoodWeather()){
                return x.getWeatherDate();
            }
        }
        
        return new Long(0);
    }
    
}
