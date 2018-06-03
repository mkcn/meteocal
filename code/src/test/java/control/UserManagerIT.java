/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.validation.ConstraintViolationException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author andrea
 */
@RunWith(Arquillian.class)
public class UserManagerIT {

    @EJB
    UserManager um;
    
    //@PersistenceUnit
    //EntityManager em;
    
    
    private List<User> users;
    

    @Deployment
    public static WebArchive createArchiveAndDeploy() {
        
        return ShrinkWrap.create(WebArchive.class)
                .addClass(UserManager.class)
                .addPackage(User.class.getPackage())
                .addAsResource("test-persistence.xml","META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
    }
    
  
    @Test
    public void UMShouldBeInjected() {
        assertNotNull(um);
    }
  
    
    @Test
    public void newUsersShouldBeValid() {
        String invalidMail = "invalidemail";
        User newUser = new User();
        newUser.setName("");
        newUser.setPassword("");
        newUser.setPublicCalendar(true);
        newUser.setEmail(invalidMail);
        try {
            um.save(newUser);
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof ConstraintViolationException);
        }
        User userfound = um.findUser(invalidMail);
        assertNull(userfound);
    }
    

    
   @Test
    public void publicUserShoulBePublic(){
        this.createSomeUser();
        this.fillInfoPublicUser();
        this.persistSomeuser();

        List<User> publicUsers = um.findPublicUser();
        
        assertTrue(publicUsers.size() == users.size());
        
    }
    
    private void createSomeUser(){
      
      
      this.users = new ArrayList<User>();  
        
      User u1 = new User();
      User u2 = new User();
      User u3 = new User();

      
      users.add(u3);
      users.add(u2);
      users.add(u1);   
    }
    
    private void fillInfoPublicUser(){
        int i =0;
       for(User x: users){
        String chara = Integer.toString(i);
        String mail = chara + "@" + chara + "." + chara;
        x.setName("");
        x.setEmail(mail);
        x.setPassword("password");
        x.setPublicCalendar(true);
        i++;
       }
    }
    
    private void persistSomeuser(){
      for(User x: users){
         um.save(x);
      }
    }
    
}
