/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Event;
import entity.Invitation;
import entity.Location;
import entity.Notification;
import entity.User;
import entity.WarningType;
import entity.Weather;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import meteocalexception.EventException;

/**
 *
 * @author andrea
 */
@Stateless
public class DataLoader {
    
    @Inject 
    EventManager eventManger;
    
    @Inject 
    UserManager  userManager;
    
    @Inject 
    NotificationManager notificationManager;
    
    @Inject 
    LocationManager locationManager;
    
    @Inject
    WeatherManager weatherManager;
    
    

    
    /**
     * Return a list with all the location 
     * persisted
     * @return 
     */
    public List<Location> findAllLocation(){
       return  this.locationManager.findLocation();
    }
    
    /**
     * Receive a location id 
     * @param id
     * @return return a specific location
     */
    public Location findSpecificLocation(Long id){
        return this.locationManager.findLocation(id);
    }
    
   /**
     *
     * Persist the given user  
     * @param newUser
     */
    public void registerUser(User newUser){
        this.userManager.save(newUser);
    }
    
    
    /**
     * Return  a list with all the public user
     * @return
     */
    public List<User> findPublicUser(){
        return userManager.findPublicUser();
    }
    
    /**
     * Return a list with all the user persisted
     * except for the logged one
     * @return 
     */
    public List<User> findUsersExceptLoggedOne(){
        return this.userManager.findAllUser();
    }
    
    /**
     * Return a specific user
     * @param mail
     * @return
     */
    public User findUser(String mail){
        return this.userManager.findUser(mail);
    }
    
    /**
     * Receive first result and max result which are parameters used to bound 
     * the query, they set respectively the element from wich start the list
     * and the number of event
     * @param maxResult
     * @param firstResult
     * @return a list of event bounded
     */
    public List<Event> findPublicEvent(int firstResult,int maxResult){
        return eventManger.findPublicEvent(firstResult,maxResult,true);
    }
    
    /**
     * Return  all the public event
     * @return
     */
    public List<Event> findPublicEvent(){
        return eventManger.findPublicEvent(0,0,false);
    }
    
    /**
     * Receive the event id and return  the event realted to the id
     * @param eventID
     * @return a specific event
     */
    public Event loadSpecificEvent(Long eventID){
        return eventManger.findSpecificEvent(eventID);
    }
    
    /**
     * Add the user to the event
     * @param eventID
     */
    public void joinEvent(Long eventID )throws EventException{
        Event event = eventManger.loadSpecificevent(eventID);
        Invitation invitation = getInvitationForEventAndUser(eventID);
        
        if(!event.getCancelled()){
            eventManger.addPArticiPant(event, userManager.getLoggedUser());
            notificationManager.createWarninForOwner(event, WarningType.NEWPARTICIPANT);
            
            if(invitation != null){
                notificationManager.setAccepted(invitation);
            }
        } else{
            throw new EventException("The event has been cancelled");
        }
            
    }
    
    private Invitation getInvitationForEventAndUser(Long eventId){               
            //Get notification about the event just joined for the logged user
            List<Notification> inviTationList = notificationManager.invitationForEventAndUser(
                    userManager.getLoggedUser().getEmail(), eventId);
            
            //if the user has been invited then set to accepted the invitation
            for(Notification x : inviTationList){
                if(x.getClass() == Invitation.class){
                    return (Invitation) x;
                }
            }
            
            return null;
    }
    
    /**
     *
     * @param mail
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Event> findEventBetweenTwoDateForSpecificUser(String mail,Long startDate,Long endDate){
        return this.eventManger.
                findEventBetweenTwoDateForSpecificUser(mail, endDate, startDate);
    }
    
    /**
     * Return a list of public event
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Event> findPublicEventBetweenTwoDate(Long startDate, Long endDate){
        return this.eventManger.findEventBetweenTwoDate(endDate, startDate);
    }
    
    /**
     * Receive a locan id related to a specific Location and two date
     * @param locationId
     * @param startDate
     * @param endDate
     * @return a list of weather for a specific location between two date
     */
    public List<Weather> findWeatherForLocationBetweenTwodate(Long locationId,Long startDate,Long endDate){
        return this.weatherManager.
                  getWeatherFromLocationBetweenTwodate(locationId, startDate, endDate);
    }
    
    /**
     * Receive a notification and a value for see,
     * update the notification setting seen as the 
     * given value
     * @param not
     * @param seen 
     */
    public  void setSeenForNotification(Notification not, boolean seen){
        Notification notification = this.notificationManager.findSpecificNotification(not.getId());
        notification.setSeen(seen);
        this.notificationManager.updateNotification(notification);
    }
    
    
}
