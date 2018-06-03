/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Group;
import entity.User;
import entity.User_;
import java.io.Serializable;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Handle the user of the db, such as updating, creating
 * deleting them and query the db
 * @author andrea
 */
@Stateless
public class UserManager  implements Serializable{

    @PersistenceContext
    EntityManager em;
    
    @Inject
    Principal principal;
    
    /**
     * Persist a new user
     * @param user
     */
    public void save(User user) {
        user.setGroupName(Group.USER);
       
        
      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
      Validator validator = factory.getValidator();

      Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);

      if (constraintViolations.size() > 0 ) {
      System.out.println("Constraint Violations occurred..");
      for (ConstraintViolation<User> contraints : constraintViolations) {
            System.err.println(contraints.getRootBeanClass().getSimpleName()+
            "." + contraints.getPropertyPath() + " " + contraints.getMessage());
       }
}
        em.persist(user);
    }

    /**
     * Remove a user from the db
     */
    public void unregister() {
        em.remove(getLoggedUser());
    }
    
    /**
     * Receive a user and update it
     * @param user
     */
    public void update(User user){
        em.merge(user);
    }
    
    /**
     *Receive a mail and find the user realted to 
     * the given mail
     * @param mail
     * @return
     */
    public User findUser(String mail){
        return em.find(User.class, mail);
    }

    /**
     *
     * @return
     */
    public User getLoggedUser() {
        return em.find(User.class, principal.getName());
    }
    
    /**
     *
     * @return a list with all the public users
     */
    public List<User> findPublicUser(){
        CriteriaBuilder cb= em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        
        Root<User> root = cq.from(User.class);
        
        cq.select(root);
        cq.where(cb.isTrue(root.get(User_.publicCalendar)));

                
        return em.createQuery(cq).getResultList();    
    }
    
    /**
     * 
     * @return a list with all the users
     */
    public List<User> findAllUser(){
        CriteriaBuilder cb= em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        
        Root<User> root = cq.from(User.class);
        
        cq.select(root);
        
        String mailLoggedUser = this.getLoggedUser().getEmail();
        Predicate notLoggedUser = cb.notEqual(root.get(User_.email), mailLoggedUser);
        
        cq.where(notLoggedUser);
                
        return em.createQuery(cq).getResultList();
    }
    
    
    //TODO- 
    public List<User> findInvitedNotParticipants(Long eventId){
//        CriteriaBuilder cb= em.getCriteriaBuilder();
//        CriteriaQuery<User> cq = cb.createQuery(User.class);
//        
//        CriteriaQuery<Event> cqEvent = cb.createQuery(Event.class);
//        Subquery<Event> subEvent = cqEvent.subquery(Event.class);
//        Root<Event> rootEvent = cqEvent.from(Event.class);
//        
//        Join<Event,User> joinSubEvent = rootEvent.join(Event_.participants);
//        cqEvent.where(cb.equal(joinSubEvent.get(User_.email), subEvent))
//        cqEvent.multiselect(rootEvent.get(Event_.participants));
//        
//        Root<User> root = cq.from(User.class);
//        
//        Predicate invitedIn =
//        
//        cq.select(root);
//                
//        return em.createQuery(cq).getResultList();
        return null;
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
