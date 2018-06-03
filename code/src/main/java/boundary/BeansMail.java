/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import Tool.SenderMail;
import control.UserDataLoader;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.mail.MessagingException;

/**
 *
 * @author Mirko
 */
@ManagedBean
@ViewScoped
public class BeansMail implements Serializable {

    @Inject
    private SenderMail sm;
    @Inject
    private UserDataLoader udl;

    private String sender, receiver;
    private String msg;

    private void sendAnEMail() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (sender == null || sender.isEmpty()) {
            this.sender = SenderMail.adminMail;
        }
        if (receiver == null || receiver.isEmpty()) {
            this.receiver = SenderMail.adminMail;
        }
        CONST.PrintMessage("Send mail", this.sender + " to " + this.receiver);
        try {
            sm.sendMail(this.sender, this.receiver, this.msg);
            CONST.PrintMessage("Send mail", "Done");
            context.addMessage(CONST.GENERAL_MSG, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Send mail", "Done"));
        } catch (MessagingException ex) {
            CONST.PrintError("Send mail ", "error send");
            context.addMessage(CONST.GENERAL_MSG, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Send mail", "Error send"));
        }
    }

    /**
     * send a message from the system to an user (with the user in the message)
     *
     */
    public void sendAShareEMail() {
        this.msg = "You are invited to sign up to MeteoCal website by ["
                + udl.getloggedUser().getEmail() + "]\n"
                + "Here you can see the personal message for you"
                + "\n-------------------------------\n"
                + this.msg
                + "\n-------------------------------\n"
                + "link -> http://localhost:8080/MeteoCal/";
        sendAnEMail();
    }

    /**
     * send a message from the system to the admin message)
     *
     */
    public void reportAnIssue() {
        this.msg = "Issue reported by ["
                + this.sender + "]\n"
                + "Here you can see detail of the problem"
                + "\n-------------------------------\n"
                + this.msg
                + "\n-------------------------------\n"
                + "link -> http://localhost:8080/MeteoCal/";
        sendAnEMail();
    }

    /* getter and setter */
    
    /**
     * auto set the user mail to the email of the message 
     * @return 
     */
    public String getSender() {
        if (sender == null || sender.isEmpty()) {
            if (udl.getloggedUser() != null) {
                this.sender = udl.getloggedUser().getEmail();
            }
        }
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
