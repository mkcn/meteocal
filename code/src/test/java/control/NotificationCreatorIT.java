/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Event;
import entity.Location;
import java.io.IOException;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author andrea
 */
@RunWith(Arquillian.class)
public class NotificationCreatorIT {
    
            
    @Inject   
    NotificationCreator nc;
    
    @Inject
    LocationManager lm;
    
    @Deployment
    public static WebArchive createArchiveAndDeploy() {
        
         return ShrinkWrap.create(WebArchive.class)
                .addClass(EventManager.class)
                .addClass(UserManager.class)
                .addClass(NotificationCreator.class)
                .addClass(LocationManager.class)
                .addClass(NotificationManager.class)
                .addClass(WeatherUpdater.class)
                .addClass(WeatherManager.class)
                .addClass(WeatherFromJson.class)
                .addClass(WeatherAdviceCreator.class)
                .addPackage(Event.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));        
    }
    
    @Test
    public void test2(){
        nc.createNotification();
    }
    
    @Test
    public void test() throws IOException{
       Location loc = new Location();
       loc.setLocationID(new Long(1));
       loc.setLocationName("Milan");
       
       lm.saveLocation(loc);
       
       nc.createNotification();
    }
    
}
