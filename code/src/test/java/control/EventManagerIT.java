/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Event;
import entity.User;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author andrea
 */
@RunWith(Arquillian.class)
public class EventManagerIT {
    
   @EJB 
   EventManager nm;
   
   @EJB
   UserManager um;
   
   @PersistenceContext  
   EntityManager em;
    
   
    @Deployment
    public static WebArchive createArchiveAndDeploy() {

        
         return ShrinkWrap.create(WebArchive.class)
                .addClass(EventManager.class)
                .addClass(UserManager.class)
                .addPackage(Event.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));        
    }
    
    @Test
    public void newEventShouldBePersist(){
      String email =  "a@a.a";
      
      User user = new User();
      user.setEmail(email);
      user.setGroupName("");
      user.setName("");
      user.setPassword("");
      user.setPublicCalendar(true);
      
      String email2=  "b@a.a"; 
      User user2 = new User();
      user2.setEmail(email2);
      user2.setGroupName("");
      user2.setName("");
      user2.setPassword("");
      user2.setPublicCalendar(true);
      
      um.save(user);
      um.save(user2);
      
      Event newEvent = new Event();
      newEvent.setEventName("eventName");
      newEvent.setPublicEvent(Boolean.TRUE);
      this.nm.createEvent(newEvent, user);
      
      List<Event> events = nm.findEventForSpecificUser(email);
      
      Event event = events.get(0);
      
      nm.addPArticiPant(event, user2);
      
      assertEquals(events.size() , 1 );
      assertEquals(nm.findPublicEvent(0, 0, false).size() , 1 );
      assertEquals(2,nm.findSpecificEvent(event.getEventID()).getParticipants().size());
    }
    
    @Test
    public void ITfindEventEveryBetweenTwoDateFalse(){
        
    Long today = Tool.ToolDate.getTodayDate();
        
    String email =  "c@a.a";
      
      User user = new User();
      user.setEmail(email);
      user.setGroupName("");
      user.setName("");
      user.setPassword("");
      user.setPublicCalendar(true);
      
      um.save(user);
        
        Event event1 = new Event();
        Event event2 = new Event();
        Event event3 = new Event();
        
        event1.setStartdate(today);
        event2.setStartdate(Tool.ToolDate.shiftDay(today, -1));
        event3.setStartdate(Tool.ToolDate.shiftDay(today, 2));
        
        nm.createEvent(event3, user);
        nm.createEvent(event2, user);
        nm.createEvent(event1, user);
        
        List<Event> events = nm.findEventEveryBetweenTwoDate(Tool.ToolDate.shiftHours(today, -1),
                                                Tool.ToolDate.shiftDay(today,1));
        
        assertEquals(1,events.size());
        
        
    }
    


}
