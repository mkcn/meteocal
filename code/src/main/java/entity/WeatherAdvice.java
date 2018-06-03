/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;


import java.sql.Date;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


/**
 *
 * @author andrea
 */
@Entity
@DiscriminatorValue("WE")
public class WeatherAdvice extends Notification{
    /*---------------------attribute----------------------------------------*/
    private Long bestDate;

    public Long getBestDate() {
        return bestDate;
    }

    public void setBestDate(long bestDate) {
        this.bestDate = bestDate;
    }
    
}
