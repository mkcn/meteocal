/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import boundary.CONST;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entity.Location;
import entity.Weather;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author andrea
 */
@Stateless
public class WeatherFromJson {
    
    private static final String JSON_LIST = "list";
    private static final String JSON_TEMP     = "temp";
    private static final String JSON_TEMP_MIN = "min";
    private static final String JSON_TEMP_MAX = "max";
    private static final String JSON_TEMP_AVG = "day";   
    private static final String JSON_WEATHER = "weather";
    private static final String JSON_WEATHER_ID = "id";
    private static final String JSON_WEATHER_ICON = "icon";
    private static final String JSON_RAIN = "rain"; 
    private static final String JSON_SNOW = "snow";
    
    
  
    /**
     * Receive a string, parse it as a json objet and create a list of weather
     * @param response
     * @param location
     * @return 
     */
    public List<Weather> getWeatherList(String response,Location location){
        
        JsonParser parser = new JsonParser();        
        List<Weather> weatherList = new ArrayList<Weather>();
        
        try{
            //Create a json object
            JsonObject resposeJson = (JsonObject) parser.parse(response);
            
            if(resposeJson.has(JSON_LIST)){
                JsonArray jsonArray = resposeJson.get(JSON_LIST).getAsJsonArray();
        
        
                for(int i=0; i<jsonArray.size(); i++){
                    JsonObject oneDayForecast = jsonArray.get(i).getAsJsonObject();
                    //date
                    //NOT-WOARKING long date = oneDayForecast.get(JSON_DATE_TIME).getAsLong();
                    long date = Tool.ToolDate.removeHours(Tool.ToolDate.getNDaysAhead(i));
                    //temperature
                    JsonObject temp = oneDayForecast.get(JSON_TEMP).getAsJsonObject();
                    float tempMin = temp.get(JSON_TEMP_MIN).getAsFloat();
                    float tempMmax = temp.get(JSON_TEMP_MAX).getAsFloat();
                    float tempAvg = temp.get(JSON_TEMP_AVG).getAsFloat();
                    //weather
                    JsonArray weatherJson = oneDayForecast.get(JSON_WEATHER).getAsJsonArray();
                    JsonObject weatherJsonObj = weatherJson.get(0).getAsJsonObject();
                    int id = weatherJsonObj.get(JSON_WEATHER_ID).getAsInt();
                    String iconName = weatherJsonObj.get(JSON_WEATHER_ICON).getAsString() + ".png";
            
                    //Rain
                    float rain;
                    if(oneDayForecast.has(JSON_RAIN)){
                        rain = oneDayForecast.get(JSON_RAIN).getAsFloat();
                    }else{
                        rain = 0;
                    }

                    //Snow
                    float snow;
                    if(oneDayForecast.has(JSON_SNOW)){
                        snow =oneDayForecast.get(JSON_SNOW).getAsFloat();
                    }else{
                        snow = 0;
                    }
             
            
                    //create a new entity and set-up it
                    Weather weatherEntity = new Weather();
                    weatherEntity.setIconName(iconName);
                    weatherEntity.setRain(rain);
                    weatherEntity.setSnow(snow);
                    weatherEntity.setTargetLocation(location);
                    weatherEntity.setTeamAvg(tempAvg);
                    weatherEntity.setTempMax(tempMmax);
                    weatherEntity.setTempMin(tempMin);
                    weatherEntity.setWeatherDate(date);
                    weatherEntity.setWeatherType(id);
                    weatherEntity.setGoodWeather((rain == 0) && (snow == 0));
            
                    //add the weather to the list
                    weatherList.add(weatherEntity);
                }
            }
        } catch(Exception e){
            CONST.PrintError("Could not parse json", "");
        }
        
        return weatherList;
        
    }


        
        
    
}
