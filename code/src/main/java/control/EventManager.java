/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Event;
import entity.Event_;
import entity.User;
import entity.User_;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
  * Handle the events of the db, such as updating, creating
 * deleting them and query the db
 * @author andrea
 */

@Stateless
public class EventManager {
    
    @PersistenceContext
    EntityManager em;
  
    /**
     *
     * @param id
     * @return
     */
    public Event loadSpecificevent(long id){
      return em.find(Event.class, id);
    }
    
    /**
     * Receiive the mail of a user and two date, return a list of event
     * in wich the user participates bounded between the two date
     * @param mail
     * @param endDate
     * @param startDate
     * @return
     */
    public List<Event> findEventBetweenTwoDateForSpecificUser(String mail, Long endDate, Long startDate){
        CriteriaBuilder cb= em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        
        Root<Event> eventRoot = cq.from(Event.class);
        Join<Event,User> join = eventRoot.join(Event_.participants);
  
        cq.select(eventRoot);
        
        Predicate mailWhere = cb.equal(join.get(User_.email), mail);
        Predicate cancelledWhere = cb.isFalse(eventRoot.get(Event_.cancelled));
        Predicate datewhere = cb.or(cb.between(eventRoot.get(Event_.startdate), startDate, endDate),
                                        cb.between(eventRoot.get(Event_.enddate), startDate, endDate));
        
        cq.where(cb.and(mailWhere,cancelledWhere,datewhere));
        
        
                
        return em.createQuery(cq).getResultList();
    }
    
    /**
     * Receive two date, retuen a list of event bounded between the two date
     * @param endDate
     * @param startDate
     * @return
     */
    public List<Event> findEventBetweenTwoDate(Long endDate, Long startDate){
        CriteriaBuilder cb= em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        
        Root<Event> eventRoot = cq.from(Event.class);
  
        cq.select(eventRoot);
        
        
        Predicate publicEventWhere = cb.isTrue(eventRoot.get(Event_.publicEvent));
        Predicate cancelledWhere = cb.isFalse(eventRoot.get(Event_.cancelled));
        Predicate datewhere = cb.or(cb.between(eventRoot.get(Event_.startdate), startDate, endDate),
                                        cb.between(eventRoot.get(Event_.enddate), startDate, endDate));
        
        cq.where(cb.and(cancelledWhere,datewhere,publicEventWhere));
        
        
                
        return em.createQuery(cq).getResultList();
    
     }

    /**
     * Receiive the mail of a user, return a list of event
     * in wich the user participates 
     * @param mail
     * @return
     */
    public List<Event> findEventForSpecificUser(String mail){
        CriteriaBuilder cb= em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        
        Root<Event> eventRoot = cq.from(Event.class);
        Join<Event,User> join = eventRoot.join(Event_.participants);
  
        cq.select(eventRoot);
        cq.where(cb.and(cb.equal(join.get(User_.email), mail),cb.isFalse(eventRoot.get(Event_.cancelled))));
                
        return em.createQuery(cq).getResultList();
    }
    
    /**
     * Return a list of event
     * @param firstResult
     * @param maxResult
     * @param bounded
     * @return
     */
    public List<Event> findPublicEvent(int firstResult, int maxResult,boolean bounded){
        CriteriaBuilder cb= em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        
        Root<Event> root = cq.from(Event.class);
        
        cq.select(root);
        cq.where(cb.and(cb.equal(root.get(Event_.publicEvent), true),cb.isFalse(root.get(Event_.cancelled))));

        TypedQuery tq = em.createQuery(cq);
        
        if(!bounded){
            firstResult = tq.getFirstResult();
            maxResult = tq.getMaxResults();            
        }
        
        cq.orderBy(cb.desc(root.get(Event_.startdate)));
        
        return tq.setFirstResult(firstResult).setMaxResults(maxResult).getResultList();    
    }
    
    /**
     * Return a list of all the event (public and not), between two date
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Event> findEventEveryBetweenTwoDate(Long startDate,Long endDate){
        
        CriteriaBuilder cb= em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        
        Root<Event> eventRoot = cq.from(Event.class);
  
        cq.select(eventRoot);
        

        Predicate cancelledWhere = cb.isFalse(eventRoot.get(Event_.cancelled));
        Predicate datewhere1 = cb.ge(eventRoot.get(Event_.startdate), startDate);
        Predicate dateWhere2 = cb.le(eventRoot.get(Event_.startdate), endDate);
        Predicate notAllerted = cb.isFalse(eventRoot.get(Event_.allerted));

        cq.where(cb.and(cancelledWhere,notAllerted,datewhere1,dateWhere2));
                
         return em.createQuery(cq).getResultList();       
    }
    
    /**
     * Return a list of outside event public and private
     * @param startDate
     * @param endDate
     * @return 
     */
    public List<Event> findEventEveryBetweenTwoDateOutside(Long startDate,Long endDate){
        
        CriteriaBuilder cb= em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        
        Root<Event> eventRoot = cq.from(Event.class);
  
        cq.select(eventRoot);
        

        Predicate cancelledWhere = cb.isFalse(eventRoot.get(Event_.cancelled));
        Predicate datewhere1 = cb.ge(eventRoot.get(Event_.startdate), startDate);
        Predicate dateWhere2 = cb.le(eventRoot.get(Event_.startdate), endDate);
        Predicate outsideWhere = cb.isTrue(eventRoot.get(Event_.outside));
        Predicate notAllerted = cb.isFalse(eventRoot.get(Event_.adviced));

        cq.where(cb.and(cancelledWhere,notAllerted,datewhere1,dateWhere2,outsideWhere));
                
         return em.createQuery(cq).getResultList();       
    }
    
    /**
     * Set an event allerted
     * @param event 
     */
    public void setAllerted(Event event){
        event.setAllerted(Boolean.TRUE);
        this.modifyEvent(event);
    }
        
    /**
     * Set an event adviced
     * @param event 
     */
    public void setAdviced(Event event){
        event.setAdviced(Boolean.TRUE);
        this.modifyEvent(event);
    }
  
    
    /**
     * Return the event related to the givn id
     * @param id
     * @return
     */
    public Event findSpecificEvent(Long id){
        return em.find(Event.class,id);
    }
    
    /**
     * Set up the event, then persist it
     * @param event
     * @param owner
     */
    public void createEvent(Event event, User owner){      
      //set the owner
      event.setOwner(owner);
      //set the owner as participant
      event.getParticipants().put(owner.getEmail(),owner);
      //set the event not cancelled
      event.setCancelled(false);
      //set the event not advice
      event.setAdviced(false);
      //set the event not allerted
      event.setAllerted(false);
      
      if(event.getAddress() == null){
          event.setAddress("");
      }
      
      if(event.getDescription() == null){
          event.setDescription("");
      }
      
      //find possible constraints violation
      this.eventVonstraintsVioltion(event);
      //add the entity in the DB
      em.persist(event);     
    }
    
    /**
     * Modify the vent on the db
     * @param event
     */
    public void modifyEvent(Event event){
       //find possible constraints violation
       this.eventVonstraintsVioltion(event);
       //refresh the event in the DB
       em.merge(event);
    }

    /**
     * Set an event cancelled
     * @param event
     */
    public void deleteEvent(Event event){
      //remove the entity in the DB
      event.setCancelled(true);
      em.merge(event);
    }
    
    /**
     * Remvoe a participant from the given event
     * @param event
     * @param participantToRemove
     */
    public void removeParticipant(Event event,User participantToRemove){
      event.getParticipants().remove(participantToRemove.getEmail());
      
      em.merge(event);
    }
    

    
    /**
     * Add a new Participant to an event
     * @param event
     * @param newParticipant
     */
    public void addPArticiPant(Event event , User newParticipant){
        event.addPArticipant(newParticipant);
        em.merge(event);
    }
    
    /**
     *
     * @param user
     * @param event
     * @return
     */
    public boolean isTheOwner(User user,Event event){
       return user.equals(event.getOwner());
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
    
    
    private void eventVonstraintsVioltion(Event event){
      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
      Validator validator = factory.getValidator();

      Set<ConstraintViolation<Event>> constraintViolations = validator.validate(event);

      if (constraintViolations.size() > 0 ) {
      System.out.println("Constraint Violations occurred..");
      for (ConstraintViolation<Event> contraints : constraintViolations) {
            System.err.println(contraints.getRootBeanClass().getSimpleName()+
            "." + contraints.getPropertyPath() + " " + contraints.getMessage());
          }
        }
    }

    
    
}
