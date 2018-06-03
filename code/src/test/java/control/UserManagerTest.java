
package control;

import control.UserManager;
import entity.Group;
import entity.User;
import entity.User_;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import static org.junit.Assert.assertThat;
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
public class UserManagerTest {
    
   UserManager um;
   CriteriaBuilder cb;
   CriteriaQuery<User>  cq;
   Root<User> root; 
   TypedQuery q; 
    
    @Before
    public void setUp() {
        um = new UserManager();
        um.setEntityManager(mock(EntityManager.class));
        cb = Mockito.mock(CriteriaBuilder.class);
        cq = Mockito.mock(CriteriaQuery.class);
        root = Mockito.mock(Root.class);
        q = mock(TypedQuery.class);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void newUsersShouldBelongToUserGroupAndSavedOnce() {
        User newUser = new User();
        um.save(newUser);
        assertThat(newUser.getGroupName(), is(Group.USER));
        verify(um.getEntityManager(),times(1)).persist(newUser);
    }
    
    @Test
    public void testfindPublicUser(){
        Mockito.when(um.getEntityManager().getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(User.class)).thenReturn(cq);
        Mockito.when(cq.from(User.class)).thenReturn(root);
        Mockito.when(um.getEntityManager().createQuery(cq)).thenReturn(q);  
        Mockito.when(q.getResultList()).thenReturn(new ArrayList<User> ());
        
        um.findPublicUser();
        
        verify(cq,times(1)).select(root);
        verify(cq,times(1)).where(cb.isTrue(root.get(User_.publicCalendar)));
        
    }
    
    
}

