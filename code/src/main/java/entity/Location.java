/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 *
 * @author andrea
 */
@Entity
public class Location implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /*---------------------primary key-----------------------------------------*/ 
    @Id
    private Long locationID;
    
     /*---------------------attribute-----------------------------------------*/
    @NotNull(message = "May not be empty")
    private String locationName;
    
    /*---------------------getter & setter-----------------------------------------*/

    public Long getLocationID() {
        return locationID;
    }

    public void setLocationID(Long locationID) {
        this.locationID = locationID;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    
    


    
}
