/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import control.NotificationManager;
import entity.Event;
import entity.Notification;
import entity.Notification_;
import entity.User;
import entity.User_;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;

/**
 *
 * @author andrea
 */
public class NotificationManagerTest {
   
   NotificationManager nm;
   CriteriaBuilder cb;
   CriteriaQuery<Notification>  cq;
   Root<Notification> root; 
   TypedQuery q; 
   MapJoin<Notification,String,User> joinMap;
    
    @Before
    public void setUp() {
        nm = new NotificationManager();
        nm.setEntityManager(mock(EntityManager.class));
        cb = Mockito.mock(CriteriaBuilder.class);
        cq = Mockito.mock(CriteriaQuery.class);
        root = Mockito.mock(Root.class);
        q = mock(TypedQuery.class);
        joinMap = Mockito.mock(MapJoin.class);
    }
    
    
    @Test 
    public void findAllUnseen(){
       String mail ="";
        
       Mockito.when(nm.getEntityManager().getCriteriaBuilder()).thenReturn(cb);
       Mockito.when(cb.createQuery(Notification.class)).thenReturn(cq);
       Mockito.when(cq.from(Notification.class)).thenReturn(root);
       Mockito.when(root.join(Notification_.receiver)).thenReturn(joinMap);
       
       Mockito.when(nm.getEntityManager().createQuery(cq)).thenReturn(q);  
       Mockito.when(q.getResultList()).thenReturn(new ArrayList<Notification> ());
       
       nm.findAll(mail);
       
       Predicate equalMail= cb.equal(joinMap.get(User_.email), mail);
       
       Mockito.verify(cq).select(root);
       Mockito.verify(cq).where(equalMail);
    }
    
    @Test
    public void reateInvitation(){
    
     List<User> users = new ArrayList<User>();
     users.add(new User());
     users.add(new User());
     
     nm.createInvitation(users, new Event());
     
     Mockito.verify(nm.getEntityManager(), Mockito.times(users.size())).persist(Mockito.isNotNull());
    }

}
