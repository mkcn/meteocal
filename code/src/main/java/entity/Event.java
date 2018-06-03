/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;

/**
 *
 * @author andrea
 */
@Entity
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    /*---------------------primary key-----------------------------------------*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long eventID;

    /*---------------------attribute-----------------------------------------*/
    private String eventName;
    private Long startdate;
    private Long enddate;
    private Boolean allDay = false;
    private String address;
    private String description;
    private Boolean cancelled;
    private Boolean publicEvent = false;
    private Boolean outside = false;
    private Boolean allerted = false;
    private Boolean adviced = false;

    /*---------------------foreign key-----------------------------------------*/
    @ManyToOne(targetEntity = Location.class)
    private Location location;

    @ManyToOne(targetEntity = User.class)
    private User owner;

    @ManyToMany(targetEntity = User.class)
    @MapKey
    @JoinColumn
    private Map<String, User> participants = new HashMap<String, User>();

    /*---------------------getter & setter-----------------------------------*/

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }
    
    public Boolean getOutside() {
        return outside;
    }

    public void setOutside(Boolean outside) {
        this.outside = outside;
    }
    
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Boolean getPublicEvent() {
        return publicEvent;
    }

    public void setPublicEvent(Boolean publicEvent) {
        this.publicEvent = publicEvent;
    }

    public Long getEventID() {
        return eventID;
    }

    public void setEventID(Long eventID) {
        this.eventID = eventID;
    }

    public boolean isPublicEvent() {
        return publicEvent;
    }

    public void setPublicEvent(boolean publicEvent) {
        this.publicEvent = publicEvent;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public Long getStartdate() {
        return startdate;
    }

    public void setStartdate(Long startdate) {
        this.startdate = startdate;
    }

    public Long getEnddate() {
        return enddate;
    }

    public void setEnddate(Long enddate) {
        this.enddate = enddate;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Map<String, User> getParticipants() {
        return participants;
    }

    public void addPArticipant(User newParticipant) {
        this.participants.put(newParticipant.getEmail(), newParticipant);
    }

    public Boolean getAllerted() {
        return allerted;
    }

    public void setAllerted(Boolean allerted) {
        this.allerted = allerted;
    }

    public Boolean getAdviced() {
        return adviced;
    }

    public void setAdviced(Boolean adviced) {
        this.adviced = adviced;
    }
    
    

}
