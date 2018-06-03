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
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author andrea
 */
@RunWith(Arquillian.class)
public class NotificationManagerIT {
    
    @Inject 
    NotificationManager nm;
    
    @Inject
    UserManager um;
    
    @Inject
    EventManager em;
    
    @PersistenceContext
    EntityManager ema;
    
    
    
   
    @Deployment
    public static WebArchive createArchiveAndDeploy() {
        
        
         return ShrinkWrap.create(WebArchive.class)
                .addClass(NotificationManager.class)
                .addClass(UserManager.class)
                .addClass(EventManager.class)
                .addPackage(Notification.class.getPackage())
                .addAsResource("test-persistence.xml","META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));        
    }
    

    
    @Test
    public void numberNotification(){
        
        String userMail = "a@a.a";
        
        User user = new User();
        user.setEmail(userMail);
        user.setGroupName("USER");
        user.setName("a");
        user.setPassword("aa");
        user.setPublicCalendar(false);
        

        
        Event evt = new Event();
        
        
        
        um.save(user);
        em.createEvent(evt, user);
        
        
        
        nm.createWarninForOwner(evt, WarningType.ALLERT);
        
        Assert.assertEquals(nm.findAll(userMail).size(), 1);
    }
    
    @Test
    public void ITCreateInvitation(){
 
        String userMail2 = "b@a.a";        
        User user2 = new User();
        user2.setEmail(userMail2);
        user2.setGroupName("USER");
        user2.setName("a");
        user2.setPassword("aa");
        user2.setPublicCalendar(false);
        
        String userMail3 = "c@a.a";        
        User user3 = new User();
        user3.setEmail(userMail3);
        user3.setGroupName("USER");
        user3.setName("a");
        user3.setPassword("aa");
        user3.setPublicCalendar(false);
       
        String userMail4 = "d@a.a";        
        User user4 = new User();
        user4.setEmail(userMail4);
        user4.setGroupName("USER");
        user4.setName("a");
        user4.setPassword("aa");
        user4.setPublicCalendar(false);
        
        Long eventId = new Long(999);
        Event evt2 = new Event();
        evt2.setEventID(eventId);
        
        um.save(user2);
        um.save(user3);
        um.save(user4);
        
        
        em.createEvent(evt2, user2);
            
        
        
        List<User> users = new ArrayList<User>();
        users.add(user3);
        users.add(user4);
        
        nm.createInvitation(users, evt2);
        
        
        Assert.assertEquals(1, nm.findAll(userMail3).size()); 
        Assert.assertEquals(1, nm.findAll(userMail4).size()); 
        Assert.assertEquals(nm.invitationForEventAndUser(userMail4, eventId).size(), 1);
 
    }



}
