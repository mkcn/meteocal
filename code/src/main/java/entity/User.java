/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * @author andrea
 */
@Entity(name = "USERS")
public class User implements Serializable {

   /*---------------------primary key-----------------------------------------*/ 
    @Id
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
            message = "invalid email")
    @NotNull(message = "May not be empty")
    @Column(name = "USER_ID")
    private String email;
    
    /*---------------------attribute-----------------------------------------*/     
    @NotNull(message = "May not be empty")
    private String password;
    
    @NotNull(message = "May not be empty")
    private String groupName;
    
    @NotNull(message = "May not be empty")
    private String name;
    
    @NotNull(message = "May be not null")
    private Boolean publicCalendar = false;
    
    /*---------------------foreign key-----------------------------------------*/
    @ManyToOne(targetEntity = Location.class)
    private Location liveIn;
    
    //@ManyToMany(mappedBy = "participants")
    //@MapKey
    //@JoinColumn
    //private Map<Long,Event> participateTo;

    
    /*---------------------getter & setter-----------------------------------*/
    public boolean isPublicCalendar() {    
        return publicCalendar;
    }

    public void setPublicCalendar(boolean publicCalendar) {
        this.publicCalendar = publicCalendar;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public void setName(String name){
     this.name = name;
    }
    
    public String getName(){
     return this.name;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Location getLiveIn() {
        return liveIn;
    }

    public void setLiveIn(Location liveIn) {
        this.liveIn = liveIn;
    }
    
    public String getPassword(){
     return this.password;
    }

    public void setPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            BigInteger bigInt = new BigInteger(1, hash);
            this.password = bigInt.toString(16);
//            StringBuilder hexString = new StringBuilder();
//            for (int i = 0; i < hash.length; i++) {
//                hexString.append(Integer.toHexString(0xFF & hash[i]));
//            }
//            this.password = hexString.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
