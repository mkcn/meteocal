/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tool;

import boundary.CONST;
import com.sun.mail.smtp.SMTPTransport;
import java.util.Date;
import java.util.Properties;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * class to handle the send of e-mails
 *
 * @author Mirko
 */
@Stateless
public class SenderMail {

    public final static String adminMail = "<email>";
    // TODO read password from external file
    public final static String adminMailPass = "<pass>"; 
    private final static String Subject = "Meteocal-conti-bertarini";

    /**
     * send a message from the system to an user or from the system to the admin
     * (with the user in the message)
     *
     * @param sender from who
     * @param receiver to who
     */
    public void sendMail(String sender, String receiver, String message) throws MessagingException {
        Properties props = System.getProperties();
        props.put("mail.smtps.host", "smtp.libero.it");
        props.put("mail.smtps.auth", "true");
        Session session = Session.getInstance(props, null);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(adminMail));;
        msg.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(receiver, false));
        msg.setSubject(Subject);
        msg.setText(message);
        msg.setHeader("MeteoCal", "MeteoCal message");
        msg.setSentDate(new Date());
        SMTPTransport t
                = (SMTPTransport) session.getTransport("smtps");
        t.connect("smtp.libero.it", adminMail, adminMailPass);
        t.sendMessage(msg, msg.getAllRecipients());
        t.close();
    }
}
