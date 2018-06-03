/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bounday.security;

import boundary.CONST;
import control.DataLoader;
import control.LocationManager;
import control.UserDataLoader;
import control.UserManager;
import entity.Location;
import entity.User;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 * class to handle the registration
 * @author Mirko
 */
@ManagedBean
public class BeansRegistration implements Serializable {

    @Inject
    private LocationManager lm;

    @Inject
    private DataLoader dl;
    private UserDataLoader udl;

    private User user;
    private String selectedProvince;

    public BeansRegistration() {
    }

    /**
     * method to add a new user to the database, 
     * called only after all the data is insert in the write way
     * 
     * @return the accessPage where you have to do the log in
     */
    public String register() {
        CONST.PrintMessage("", "Registration");
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            this.user.setLiveIn(lm.findLocation(Long.valueOf(this.selectedProvince)));
            dl.registerUser(user);
            context.addMessage("registrationMsg",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Registration done"));
//        return "user/home?faces-redirect=true";
        } catch (Exception e) {
            context.addMessage("registrationMsg",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Registration failed"));
        }
        return CONST.ACCESS_PAGE;
    }

    /* getter and setter */

    public String getSelectedProvince() {
        return selectedProvince;
    }

    public void setSelectedProvince(String city) {
        CONST.PrintMessage("", city);
        this.selectedProvince = city;
    }

    public User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
