/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Event;
import entity.Event_;
import entity.Invitation;
import entity.Notification;
import entity.Notification_;
import entity.User;
import entity.User_;
import entity.Warning;
import entity.WarningType;
import entity.WeatherAdvice;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
  * Handle the notification of the db, such as updating, creating
 * deleting them and query the db 
 * @author andrea
 */
@Stateless
public class NotificationManager {
    
    @PersistenceContext
    EntityManager em;
    
    private final int PARTICIPANTS_EXCEPT_OWNER = 1;
    

    
    /**
     * Return an object notification find in the DB
     * related to the given id
     * @param id
     * @return 
     */
    public Notification findSpecificNotification(Long id){
        return em.find(Notification.class, id);
    }
    
    /**
     * Update a notification
     * @param notification 
     */
    public void updateNotification(Notification notification){
        em.merge(notification);
    }
    
    public void setAccepted(Invitation invitation){
        invitation.setAccepted(true);
        this.updateNotification(invitation);
    }
    
    /**
     * Return the list of notification not seen
     * @param mail
     * @return
     */   
     public List<Notification> findAll(String mail) {
         
        CriteriaBuilder cb= em.getCriteriaBuilder();
        CriteriaQuery<Notification> cq = cb.createQuery(Notification.class);
        
        Root<Notification> root = cq.from(Notification.class);
        Join<Notification,User> join = root.join(Notification_.receiver);
        
        Predicate equalMail= cb.equal(join.get(User_.email), mail);
        
        cq.select(root);
        cq.where(equalMail);
        
        cq.orderBy(cb.desc(root.get(Notification_.sendDate)));
                
        return em.createQuery(cq).getResultList();
    }  
     
    /**
     * Return the number of notification not already seen of the given user
     * @param mail
     * @return 
     */
    public int getNumberUnseen(String mail){
        CriteriaBuilder cb= em.getCriteriaBuilder();
        CriteriaQuery<Notification> cq = cb.createQuery(Notification.class);
        
        Root<Notification> root = cq.from(Notification.class);
        Join<Notification,User> join = root.join(Notification_.receiver);
        
        Predicate equalMail= cb.equal(join.get(User_.email), mail);
        Predicate unSeen = cb.isFalse(root.get(Notification_.seen));
        
        cq.select(root);
        cq.where(cb.and(equalMail,unSeen));
                
        return em.createQuery(cq).getResultList().size();
    }
    
    /**
     * Return a list of notification related to a
     * givn event id and a given user email
     * @param mail
     * @param eventId
     * @return 
     */
    public List<Notification> invitationForEventAndUser(String mail,Long eventId){
        CriteriaBuilder cb= em.getCriteriaBuilder();
        CriteriaQuery<Notification> cq = cb.createQuery(Notification.class);
        
        Root<Notification> root = cq.from(Notification.class);
        Join<Notification,User> join = root.join(Notification_.receiver);
        Join<Notification,Event> join2 = root.join(Notification_.About);
        
        Predicate eventWhere = cb.equal(join2.get(Event_.eventID), eventId);
        Predicate userWhere = cb.equal(join.get(User_.email), mail);
        
        cq.where(cb.and(eventWhere,userWhere));
                
        List<Notification> resutList = em.createQuery(cq).getResultList();
        
        return resutList;
    }
    
    
    /**
     * Receive a list of user (invited to an event) and an event.
     * Create a notification related to he received event for every invited user
     * @param invited
     * @param event
     */
    public void createInvitation(List<User> invited,Event event){
                           
       for(User x : invited){
           this.createInvitation(event, x);
        }
      
    }

    
    
    /**
     * Create a warning for all the participant for the given evet, excluded the owner
     * @param event
     * @param type
     */
    public void createWarninForAllParticipants(Event event,WarningType type){

       if(event.getParticipants().size() > PARTICIPANTS_EXCEPT_OWNER){  
       Map<String,User> participants = this.removeOwnerfromParticipants(event);
       for(User x : participants.values()){
           this.createWarning(event, type, x);
          }
       }
    }
    
   /**
     * Create a warning for he owner of the given event
     * @param event
     * @param type
     */
    public void createWarninForOwner(Event event,WarningType type){
        this.createWarning(event, type, event.getOwner());
    }
    
    /**
     * Persist a weather advice
     * @param weatherAdvice
     */
    public void createWeatherAdvice(WeatherAdvice weatherAdvice){
        weatherAdvice.setSeen(false);
        weatherAdvice.setSendDate(Tool.ToolDate.getTodayDate());
        em.persist(weatherAdvice);
    }
//---------------------------------------------------------------------------------//    

    private void createWarning(Event event,WarningType type,User receiver)  {
        Warning warning = new Warning();
        warning.setAbout(event);
        warning.setReceiver(receiver);
        warning.setSeen(false);
        warning.setSendDate(Tool.ToolDate.getTodayDate());
        warning.setType(type);
        em.persist(warning);
    }    
    
        
    private void createInvitation(Event event,User participant){
        Long today = Tool.ToolDate.getTodayDate();
        Invitation n = new Invitation();
             n.setReceiver(participant);
             n.setSeen(false);
             n.setAbout(event);
             n.setSendDate(today);
             n.setAccepted(false);
             em.persist(n);             
    }
    

    
    private Map <String, User> removeOwnerfromParticipants(Event event){      
        Map<String,User> participants;
        participants  = event.getParticipants();
        participants.remove(event.getOwner().getEmail());
        return participants;
    }
    
    /**
     *
     * @param em
     */
    public void setEntityManager(EntityManager em){
        this.em = em;
    }
    
    /**
     *
     * @return
     */
    public EntityManager getEntityManager(){
        return this.em;
    }

    
    
}
