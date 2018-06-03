
 package boundary.publics.calendars;

import control.DataLoader;
import entity.User;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Mirko
 */
@ManagedBean
@ViewScoped
public class BeansPublicCals implements Serializable {

    @EJB
    private DataLoader dl;

    private List<User> publicUsers;
    private List<User> filteredPublicUsers;

    /**
     * load all the public calendars
     */
    @PostConstruct
    public void init() {
        try {
            this.publicUsers = dl.findPublicUser();
        } catch (Exception e) {
            System.err.println("error load public users");
        }
    }
    
    /* getter and setter */
    
    public List<User> getPublicUsers() {
        return publicUsers;
    }

    public void setPublicUsers(List<User> publicUsers) {
        this.publicUsers = publicUsers;
    }

    public List<User> getFilteredPublicUsers() {
        return filteredPublicUsers;
    }

    public void setFilteredPublicUsers(List<User> filteredPublicUsers) {
        this.filteredPublicUsers = filteredPublicUsers;
    }
}
