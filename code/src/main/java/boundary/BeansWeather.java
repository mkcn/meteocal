/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import control.DataLoader;
import entity.Weather;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

/**
 * class to handle the weather
 * @author Mirko
 */
@ManagedBean
@RequestScoped
public class BeansWeather implements Serializable{
    
    @Inject
    private DataLoader dl;
    
    private List<Weather> meteoList;

    /**
     * get an array of weather obj given the following parameters
     * @param loc a location
     * @param from a date 
     * @param to a date
     * @return a list 
     */
    public List<Weather> getMeteoList(Long loc, Long from , Long to) {
        //? JUST FOR TEST
        CONST.PrintMessage("Meteo request", "Loc:" + loc 
                + " From:" + new Date(from).toGMTString() + "(" + from.toString() + ")"
                +" To:" + new Date(to).toGMTString() + "(" + to.toString() + ")");
        
        this.meteoList =  dl.findWeatherForLocationBetweenTwodate(loc, from, to);
        CONST.PrintMessage("Meteo reply", this.meteoList.size() + "");
        return meteoList;
    }

    /* getter and setter */
    
    public void setMeteoList(List<Weather> meteoList) {
        this.meteoList = meteoList;
    }
}
