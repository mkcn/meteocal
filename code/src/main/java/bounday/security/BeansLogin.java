/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bounday.security;

import boundary.CONST;
import control.UserDataLoader;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * login and logout class
 *
 * @author Mirko
 */
@ManagedBean
@SessionScoped
public class BeansLogin implements Serializable {

    private String username;
    private String password;
    private boolean logOut = false;

    @Inject
    private UserDataLoader udl;

    public BeansLogin() {
    }

    /**
     * redirect to the home page if the user is already logged in called buy
     * graphic each time the page is loaded
     */
    public void checkIfAlreadyLogged() {
        try {
            //return null if there is no user is logged
            if (udl.getloggedUser() != null && logOut == false) {
                FacesContext context3 = FacesContext.getCurrentInstance();
                HttpServletResponse response
                        = (HttpServletResponse) context3.getExternalContext().getResponse();
                //this method doesn't add the extension of the page
                response.sendRedirect(CONST.FOLDER_USER + CONST.HOME_PAGE + ".xhtml");
            }
        } catch (IOException ex) {
            CONST.PrintError("accessPage - init", ex.toString());
        } catch (Exception e) {
            CONST.PrintError("accessPage - init", e.toString());
        }
    }

    /**
     * login user
     *
     * @return the page where to go
     */
    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request
                = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.login(this.username, this.password);
            CONST.PrintMessage("login", "login of " + this.username);
            context.addMessage(CONST.GENERAL_MSG, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Login", "Done"));
            logOut = false;
        } catch (ServletException e) {
            context.addMessage("loginMsg", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error!", "Username or password wrong"));
            return CONST.ACCESS_PAGE;
        }
        return CONST.FOLDER_USER + CONST.HOME_PAGE;
    }

    /**
     * user for test
     *
     * @return
     */
    public String loginFast() {
        //TODO just for test and to do the login fast
        this.username = "a";
        this.password = "a";
        login();
        return CONST.DEBUG_INDEX;
    }

    /**
     * invalidate the current session and log out the current user
     *
     * @return
     */
    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            logOut = true;
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            request.getSession().invalidate();
            context.addMessage(CONST.GENERAL_MSG, new FacesMessage("Logout done."));
        } catch (Exception e) {
            context.addMessage(CONST.GENERAL_MSG, new FacesMessage("Logout failed."));
        }
        CONST.PrintMessage("Log out", "Done");
        // "/" is needed because the page is in an other folder
        return "/" + CONST.ACCESS_PAGE + "?faces-redirect=true";
    }

    /* getter and setter */
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
