/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Event;
import entity.Notification;
import entity.User;
import entity.WarningType;
import entity.WeatherAdvice;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * retrive information about the logged user, such as its events
 * its notification and so on.
 * @author andrea
 */
@Named
@Stateless
public class UserDataLoader implements Serializable{
    
   
    
    @Inject 
    EventManager eventManger;
    
    @Inject
    UserManager  userManager;
    
    @Inject
    NotificationManager notificationManager;
    
    @Inject
    WeatherAdviceCreator wac;
    

    
    /**
     * Should be used to get the logged user
     * @return
     */
    public User getloggedUser(){
     return userManager.getLoggedUser();
    }
    
    /**
     * Receive an event and a list of users, persist the event
     * and send invitations
     * @param eventEntity
     * @param invited
     * @return event ID
     */
    public long createEvent(Event eventEntity,List<User> invited){
        
        eventManger.createEvent(eventEntity, this.getloggedUser());
        if(invited.size() > 0){
           notificationManager.createInvitation(invited,eventEntity);
        }
        
        createNotificationAllertAndWeather(eventEntity);
        
        //Create allert and weather
        
        return eventEntity.getEventID();
    }
    
    private void createNotificationAllertAndWeather(Event evt){
        Event evtPersisted = eventManger.findSpecificEvent(evt.getEventID());
        
        if(evtPersisted.getStartdate() <= Tool.ToolDate.getNDaysAhead(1)
                && !evtPersisted.getAllerted()){
           notificationManager.createWarninForOwner(evtPersisted, WarningType.ALLERT);
           notificationManager.createWarninForAllParticipants(evtPersisted, WarningType.ALLERT);
           eventManger.setAllerted(evtPersisted);
           
        }
        
        if(evtPersisted.getStartdate() <= Tool.ToolDate.getNDaysAhead(3)
                && !evtPersisted.getAdviced()
                && evtPersisted.getOutside()){
            WeatherAdvice weatherAdvice = wac.getWeatherAdvice(evtPersisted);
            if(weatherAdvice != null){
                notificationManager.createWeatherAdvice(weatherAdvice);
                eventManger.setAdviced(evtPersisted);
            }
        }
        
    }
    
    /**
     * Should be used to delete an event.
     * -If the logged user is also the owner, the event is setted 
     *  to cancelled and ll the participants are warned
     * -If the logged user is not the owner then he will be removed
     *  from the participants and the owner will be warned
     * @param eventEntity
     */
    public void deleteEvent(Event eventEntity){
        
        if(eventEntity.getOwner().getEmail().equals(this.getloggedUser().getEmail())){
            eventManger.deleteEvent(eventEntity);
            notificationManager.createWarninForAllParticipants(eventEntity, WarningType.EVENTDELETED);
        } else {
            eventManger.removeParticipant(eventEntity, this.getloggedUser());
            notificationManager.createWarninForOwner(eventEntity, WarningType.PARTICIPANTREMOVED);
        }
    }
    
    /**
     * Should be used to modify an event and its 
     * participants
     * @param eventEntity
     * @param invited 
     */
    public void modifyEvent(Event eventEntity,List<User> invited){
        this.modifyevent(eventEntity);
        
        if(invited.size() > 0){
           notificationManager.createInvitation(invited,eventEntity);
        }
    }
    

    
    /**
     * Should be used to modify an event, a notification
     * about the type of modification
     * @param eventEntity
     */
    public void modifyevent(Event eventEntity){       
        WarningType type = this.findWhatCahnged(eventEntity);
        eventManger.modifyEvent(eventEntity);
        if(type != null){
          notificationManager.createWarninForAllParticipants(eventEntity, type); 
        }
        createNotificationAllertAndWeather(eventEntity);
    }
    
    /**
     * Should be used to get notification of the logged user,
     * that list contain un-seen notification and the lasts
     * already read
     * @return a list of notification
     */
    public List<Notification> getMyNotification(){        
        return notificationManager.findAll(this.getloggedUser().getEmail());
    }
    
    /**
     * Should be used to get the number of the unseen notification
     * of the logged user
     * @return 
     */
    public int getMyNumberNotification(){
        return this.notificationManager.getNumberUnseen(this.getloggedUser().getEmail());
    }
 
    
    /**
     * Should be used to get the events of the logged user
     * @return a list of event
     */
    public List<Event> getMyEvent(){
        eventManger.findEventForSpecificUser(this.getloggedUser().getEmail());
        return null;
    }
    
    /**
     * Should be used to get the events of the logged user,
     * the events returned are bounded between the two given date
     * @param startDate
     * @param endDate
     * @return 
     */
    public List<Event> getMyLazyEvent(Long startDate, Long endDate){
        return this.eventManger.
                findEventBetweenTwoDateForSpecificUser(this.getloggedUser().getEmail(), endDate, startDate);
    }
    
   /**
    * Should be used to update the user' s data in the DB
    * @param user 
    */
    public void updateUser(User user){
        this.userManager.update(user);
    }
   
    
//------------------------------------------------------------------------------------------------
    /**
     * Get an entity event and find what is changed before the modification,
     * 
     * @param eventEntity
     * @return type 
     */
    protected WarningType findWhatCahnged(Event eventEntity){
        Event oldEvent =this.eventManger.findSpecificEvent(eventEntity.getEventID());
        WarningType type = null;
        
        
        if(oldEvent.getOutside() != eventEntity.getOutside()){
            type = WarningType.CHANGE_OUSIDE;
        }
        
        
        if(oldEvent.getPublicEvent() != eventEntity.getPublicEvent()){
            if(type == null){
              type = WarningType.CHANGE_PUBLIC;
            }else{
              type = WarningType.MANY_CHANGE;
            }
        }
        
        
        if(oldEvent.getAddress() == null){
            if(eventEntity.getAddress() != null){
                if(type == null){
                    type = WarningType.CHANGE_ADDRESS;
                }else{
                    type = WarningType.MANY_CHANGE;
                }
            }
        }
        
        if(oldEvent.getAddress() != null){
                if (!oldEvent.getAddress().equals(eventEntity.getAddress())){
                    if(type == null){
                         type = WarningType.CHANGE_ADDRESS;
                    }else{
                        type = WarningType.MANY_CHANGE;
                    }
                }
        }
            
        
        
        if(oldEvent.getDescription()== null){
            if(eventEntity.getDescription()!= null){
                if(type == null){
                    type = WarningType.CHANGE_DESCRIPTION;
                }else{
                    type = WarningType.MANY_CHANGE;
                }
           }
        }
                
        
        if(oldEvent.getDescription()!= null){
                if (!oldEvent.getDescription().equals(eventEntity.getDescription())){
                    if(type == null){
                         type = WarningType.CHANGE_DESCRIPTION;
                    }else{
                        type = WarningType.MANY_CHANGE;
                    }
                }
        }
        
        if(!(oldEvent.getEnddate().equals(eventEntity.getEnddate()))
             || !(oldEvent.getStartdate().equals(eventEntity.getStartdate()))){
            if(type == null){
              type = WarningType.CHANGE_DATE;
              //if  the event has been postponed 
              if(oldEvent.getStartdate() < eventEntity.getStartdate()){
                  eventEntity.setAdviced(Boolean.FALSE);
                  eventEntity.setAllerted(Boolean.FALSE);
              }
            }else{
              type = WarningType.MANY_CHANGE;
            }
        }
        
        
        if(!oldEvent.getEventName().equals(eventEntity.getEventName())){
            if(type == null){
              type = WarningType.CHANGE_NAME;
            }else{
              type = WarningType.MANY_CHANGE;
            }
        }
        
        
        if(oldEvent.getLocation().getLocationID()!= eventEntity.getLocation().getLocationID()){
            if(type == null){
              type = WarningType.CHANGE_LOCATON;
            }else{
              type = WarningType.MANY_CHANGE;
            }
        }
        
        
        return type;
    }
    
    
   
}
