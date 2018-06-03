/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author andrea
 */
@Entity
public class Weather implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /*---------------------primary key-----------------------------------------*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long weatherID;
    
    /*---------------------attribute-----------------------------------------*/
    private Long weatherDate;
    private Boolean goodWeather;
    private float teamAvg;
    private float tempMin;
    private float tempMax;
    private int weatherType;
    private String iconName;
    private float rain;
    private float snow;

    /*---------------------foreign key-----------------------------------------*/
    
    @ManyToOne(targetEntity = Location.class)
    private Location targetLocation;

    /*---------------------getter & setter-----------------------------------------*/
    public Long getWeatherDate() {
        return weatherDate;
    }

    public void setWeatherDate(Long weatherDate) {
        this.weatherDate = weatherDate;
    }
    
    public void setWeatherID(Long id){
        this.weatherID = id;
    }

    public Long getWeatherID() {
        return weatherID;
    }

    public Location getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(Location targetLocation) {
        this.targetLocation = targetLocation;
    }

    public Boolean getGoodWeather() {
        return goodWeather;
    }

    public void setGoodWeather(Boolean goodWeather) {
        this.goodWeather = goodWeather;
    }

    public float getTeamAvg() {
        return teamAvg;
    }

    public void setTeamAvg(float teamAvg) {
        this.teamAvg = teamAvg;
    }

    public float getTempMin() {
        return tempMin;
    }

    public void setTempMin(float tempMin) {
        this.tempMin = tempMin;
    }

    public float getTempMax() {
        return tempMax;
    }

    public void setTempMax(float tempMax) {
        this.tempMax = tempMax;
    }

    public int getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(int weatherType) {
        this.weatherType = weatherType;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public float getRain() {
        return rain;
    }

    public void setRain(float rain) {
        this.rain = rain;
    }

    public float getSnow() {
        return snow;
    }

    public void setSnow(float snow) {
        this.snow = snow;
    }
    
    


    /*------------------------------------------------------------------------*/
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (weatherID != null ? weatherID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Weather)) {
            return false;
        }
        Weather other = (Weather) object;
        if ((this.weatherID == null && other.weatherID != null) || (this.weatherID != null && !this.weatherID.equals(other.weatherID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Weather[ id=" + weatherID + " ]";
    }
    
}
