/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import control.UserDataLoader;
import entity.User;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

/**
 *
 * @author Mirko
 */
@ManagedBean
public class BeansAccountSetting implements Serializable{

    @Inject
    private UserDataLoader udl;

    private User user;
    private String selectedProvince;
    private String newPassword;

    /**
     * when the page is opened the data of current user is loaded and showed
     */
    @PostConstruct
    public void init() {
        this.user = udl.getloggedUser();
        try {
            Long id = this.user.getLiveIn().getLocationID();
            this.selectedProvince = Long.toString(id);
        } catch (Exception e) {
            CONST.PrintError("accountSetting", "error in reading location");
        }
    }

    /**
     * called by the button save settings to modify the data of current user
     * @return the page where to go
     */
    public String modifySettingUser() {
        if(this.newPassword != null && !this.newPassword.isEmpty())
            this.user.setPassword(newPassword);
        udl.updateUser(user);
        return CONST.HOME_PAGE;
    }

    /* getter and setter */
    
    
    public void setSelectedProvince(String selectedProvince) {
        this.selectedProvince = selectedProvince;
    }

    public UserDataLoader getUdl() {
        return udl;
    }

    public void setUdl(UserDataLoader udl) {
        this.udl = udl;
    }

    public String getSelectedProvince() {
        return this.selectedProvince;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
