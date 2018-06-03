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
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 *
 * @author andrea
 */
public class EventManagerTest {
    
   
   private EventManager evtMang;
   private CriteriaBuilder cb;
   private CriteriaQuery<Event>  cq;
   private Root<Event> root; 
   private TypedQuery q; 
   private MapJoin<Event,String,User> joinMap;
   private final int firstResult = Integer.MAX_VALUE;
   private final int maxResult = Integer.MAX_VALUE;
   
    
    @Before
    public void setUp() {
        evtMang = new EventManager();
        evtMang.em = Mockito.mock(EntityManager.class);
        cb = Mockito.mock(CriteriaBuilder.class);
        cq = Mockito.mock(CriteriaQuery.class);
        root = Mockito.mock(Root.class);
        q = mock(TypedQuery.class);
        joinMap = Mockito.mock(MapJoin.class);
        this.beforeQuery();
    }
    
    @Test
    public void testFindEventForSpecificUser(){

        this.returnListEvent();
        
        Mockito.when(root.join(Event_.participants)).thenReturn(joinMap);

        
        evtMang.findEventForSpecificUser("");
        
        verify(cq,times(1)).select(root);
        verify(cq,times(1)).where(cb.equal(joinMap.get(User_.email), ""));
    }
    
   @Test
    public void testFindPublicEventBounded(){
        
        this.returnReducedListEvent();      
      
        evtMang.findPublicEvent(firstResult,maxResult,true);
        
        verify(cq,times(1)).select(root);
        verify(cq,times(1)).where(cb.and(cb.equal(root.get(Event_.publicEvent), true),cb.isFalse(root.get(Event_.cancelled))));
    }
    
    //@Test
    public void testFindPublicEventNotBounded(){
        
        this.returnReducedListEvent();      
      
        evtMang.findPublicEvent(firstResult,maxResult,false);
        
        verify(cq,times(1)).select(root);
        verify(cq,times(1)).where(cb.and(cb.equal(root.get(Event_.publicEvent), true),cb.isFalse(root.get(Event_.cancelled))));
    }
    
    //@Test
    public void testFindEventBetweenTwoDate(){
       
        this.returnListEvent();
        
        Long startDate = new Long(Long.MAX_VALUE);
        Long endDate = new Long(Long.MAX_VALUE); 
        
        Predicate publicEventWhere = cb.isTrue(root.get(Event_.publicEvent));
        Predicate cancelledWhere = cb.isFalse(root.get(Event_.cancelled));
        Predicate datewhere = cb.or(cb.between(root.get(Event_.startdate), startDate, endDate),
                                        cb.between(root.get(Event_.enddate), startDate, endDate));
        
        
        evtMang.findEventBetweenTwoDate(startDate, startDate);
        
        verify(cq,times(1)).select(root);
        verify(cq,times(1)).where(cb.and(cancelledWhere,datewhere,publicEventWhere));
        
    }
    
    
    @Test
    public void testfindEventBetweenTwoDateForSpecificUser(){
        
        
        this.returnListEvent();
        Mockito.when(root.join(Event_.participants)).thenReturn(joinMap);
        
        Long startDate = new Long(Long.MAX_VALUE);
        Long endDate = new Long(Long.MAX_VALUE);
        String mail = "";
        
        Predicate mailWhere = cb.equal(joinMap.get(User_.email), mail);
        Predicate cancelledWhere = cb.isFalse(root.get(Event_.cancelled));
        Predicate datewhere = cb.or(cb.between(root.get(Event_.startdate), startDate, endDate),
                                        cb.between(root.get(Event_.enddate), startDate, endDate));
        
        evtMang.findEventBetweenTwoDateForSpecificUser(mail, endDate, startDate);
        
        verify(cq,times(1)).select(root);
        verify(cq,times(1)).where(cb.and(mailWhere,cancelledWhere,datewhere));
        
    }
    
    @After
    public void end(){}
    
 
    

    /*-----------------------------------------------------------------*/
    
    private void beforeQuery(){
        Mockito.when(evtMang.em.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(Event.class)).thenReturn(cq);
        Mockito.when(cq.from(Event.class)).thenReturn(root);
    }
    
    private void returnListEvent(){
        Mockito.when(evtMang.getEntityManager().createQuery(cq)).thenReturn(q);  
        Mockito.when(q.getResultList()).thenReturn(new ArrayList<Event> ());
    }
    
    private void returnReducedListEvent(){
        Mockito.when(evtMang.em.createQuery(cq)).thenReturn(q);
        Mockito.when(q.setFirstResult(firstResult)).thenReturn(q);
        Mockito.when(q.setMaxResults(maxResult)).thenReturn(q);
        Mockito.when(q.getResultList()).thenReturn(new ArrayList<Event> ());
    }


}
