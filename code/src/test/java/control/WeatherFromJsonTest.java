/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Location;
import entity.Weather;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author andrea
 */
public class WeatherFromJsonTest {
    
    private final String response = "{\"cod\":\"200\",\"message\":0.0037,\"city\":{\"id\":3164527,\"name\":\"Verona\",\"coord\":{\"lon\":10.99779,\"lat\":45.434189},\"country\":\"IT\",\"population\":0,\"sys\":{\"population\":0}},\"cnt\":5,\"list\":[{\"dt\":1421578800,\"temp\":{\"day\":1.91,\"min\":-0.15,\"max\":1.91,\"night\":0.13,\"eve\":1.91,\"morn\":1.91},\"pressure\":1028.12,\"humidity\":90,\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"sky is clear\",\"icon\":\"01n\"}],\"speed\":1.21,\"deg\":25,\"clouds\":0},{\"dt\":1421665200,\"temp\":{\"day\":5.2,\"min\":0.32,\"max\":5.63,\"night\":0.32,\"eve\":1.87,\"morn\":1.51},\"pressure\":1025,\"humidity\":100,\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02d\"}],\"speed\":2.91,\"deg\":286,\"clouds\":12},{\"dt\":1421751600,\"temp\":{\"day\":4.38,\"min\":-0.74,\"max\":5.14,\"night\":1.25,\"eve\":1.67,\"morn\":-0.74},\"pressure\":1023.97,\"humidity\":100,\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02d\"}],\"speed\":2.06,\"deg\":312,\"clouds\":20},{\"dt\":1421838000,\"temp\":{\"day\":4.01,\"min\":1.57,\"max\":4.01,\"night\":2.85,\"eve\":2.95,\"morn\":1.57},\"pressure\":1023.27,\"humidity\":100,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"speed\":2.02,\"deg\":311,\"clouds\":92,\"rain\":1},{\"dt\":1421924400,\"temp\":{\"day\":6.1,\"min\":3.35,\"max\":6.17,\"night\":5.09,\"eve\":5.44,\"morn\":3.35},\"pressure\":1015.25,\"humidity\":100,\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"speed\":3.27,\"deg\":51,\"clouds\":92,\"rain\":7}]}";
    private WeatherFromJson wfj;
    private Location loc;
    
    @Test 
    public void testGetWeatherList(){
     
        loc = new Location();
        loc.setLocationName("Verona");
        wfj = new WeatherFromJson();
        
        List<Weather> weatherList = wfj.getWeatherList(response,loc);

        
        Assert.assertEquals(5, weatherList.size());
    }
    
}
