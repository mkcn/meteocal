/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author andrea
 */
@Entity
@DiscriminatorValue("WA")
public class Warning extends Notification {
   
     /*---------------------attribute----------------------------------------*/
    private String type;
    
    public void setType(WarningType type){
      this.type = type.toString();
    }
    
    public String getType(){
      return this.type;
    }
    
}
