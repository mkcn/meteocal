/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Location;
import javax.inject.Inject;
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
public class LocationLoaderIT {
    
    @Inject
    LocationLoader ll;
    
    @Inject
    LocationManager lm;
    
       
    @Deployment
    public static WebArchive createArchiveAndDeploy() {

        
         return ShrinkWrap.create(WebArchive.class)
                .addClass(LocationManager.class)
                .addClass(LocationLoader.class)
                .addPackage(Location.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));        
    }
    
    @Test
    public void testLoad(){
        ll.loadLocation();
        Assert.assertEquals(94, lm.findLocation().size());
    }
    
}
