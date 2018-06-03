/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author andrea
 */
@Entity
@Inheritance
@DiscriminatorColumn(name="NOTIFICATION_TYPE")
public abstract class Notification implements Serializable {
    /*---------------------primary key-----------------------------------------*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "NOTIFICATION_ID")
    private Long id;
    /*---------------------attribute----------------------------------------*/
    
    private Long sendDate;
    
    private Boolean seen;

    
    /*---------------------foreign key-----------------------------------------*/
    @OneToOne(targetEntity = User.class)    
    //@PrimaryKeyJoinColumn(name="NOTIFICATION_ID", referencedColumnName="USER_ID" )
    private User receiver;
    
    @ManyToOne(targetEntity = Event.class)
    private Event About;
    
    
    /*---------------------getter & setter-----------------------------------------*/
    public Event getAbout() {
        return About;
    }

    public void setAbout(Event About) {
        this.About = About;
    }
    
    
    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
    

    public long getSendDate() {
        return sendDate;
    }

    public void setSendDate(Long sendDate) {
        this.sendDate = sendDate;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notification)) {
            return false;
        }
        Notification other = (Notification) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Warning[ id=" + id + " ]";
    }
    
}
