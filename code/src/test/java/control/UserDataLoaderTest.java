/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Event;
import entity.Location;
import entity.WarningType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author andrea
 */
public class UserDataLoaderTest {
    
    private UserDataLoader udl;
    private Event event;
    private Location location;
    
    @Before
    public void init(){
        udl = new UserDataLoader();
        
        event = new Event();
        event.setAddress("a");
        event.setAllDay(false);
        event.setDescription("a");
        event.setEnddate(Long.MAX_VALUE);
        event.setStartdate(Long.MIN_VALUE);
        event.setPublicEvent(Boolean.TRUE);
        event.setEventName("a");
        event.setOutside(Boolean.TRUE);
        
        location = new Location();
        location.setLocationID(new Long(1));
        
        event.setLocation(location);
        
        udl.eventManger = Mockito.mock(EventManager.class);
        Mockito.when(udl.eventManger.findSpecificEvent(Mockito.anyLong())).thenReturn(event);
    }
    
    
    @Test
    public void testFindWhatChangeOneChange1(){
       Event event2 = new Event();
        event2.setAddress("b");
        event2.setAllDay(false);
        event2.setDescription("a");
        event2.setEnddate(Long.MAX_VALUE);
        event2.setStartdate(Long.MIN_VALUE);
        event2.setPublicEvent(Boolean.TRUE);
        event2.setEventName("a");
        event2.setOutside(Boolean.TRUE);
        event2.setLocation(location);
        
       assertEquals(WarningType.CHANGE_ADDRESS,udl.findWhatCahnged(event2));
    }
    
    @Test
    public void testFindWhatChangeOneChangeNull(){
       Event event2 = new Event();
        event2.setAddress("a");
        event2.setAllDay(false);
        event2.setDescription("a");
        event2.setEnddate(Long.MAX_VALUE);
        event2.setStartdate(Long.MIN_VALUE);
        event2.setPublicEvent(Boolean.TRUE);
        event2.setEventName("a");
        event2.setOutside(Boolean.TRUE);
        event2.setLocation(location);
        
       assertNull(udl.findWhatCahnged(event2));
    }
    
    @Test
    public void testFindWhatChangeOneChangeManyChange(){
       Event event2 = new Event();
        event2.setAddress("b");
        event2.setAllDay(false);
        event2.setDescription("b");
        event2.setEnddate(Long.MAX_VALUE);
        event2.setStartdate(Long.MIN_VALUE);
        event2.setPublicEvent(Boolean.TRUE);
        event2.setEventName("a");
        event2.setOutside(Boolean.TRUE);
        event2.setLocation(location);
        
       assertEquals(WarningType.MANY_CHANGE,udl.findWhatCahnged(event2));
    }
    
}
