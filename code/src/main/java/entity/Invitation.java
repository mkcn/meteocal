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
@DiscriminatorValue("IN")
public class Invitation extends Notification {
   /*---------------------attribute----------------------------------------*/
    private boolean accepted;

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
    
}
