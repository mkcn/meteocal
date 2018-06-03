/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import javax.faces.bean.ManagedBean;

/**
 * class with global variable about all icon of the resources
 * @author Mirko
 */
@ManagedBean(name = "CONSTREG")
public class CONSTRES {

    public final static String EVENT = "event.png";
    public final static String USER = "user.png";
    public final static String NOTIF_WEATHER = "not_bad_weather.png";
    public final static String NOTIF_WARNING = "not_warning.png";
    public final static String NOTIF_INVITATION = "not_invitation.png";

    public final static String CLEAR_SKY = "01d.png";
    public final static String FEW_CLOUDS = "02d.png";
    public final static String SCATTERED_CLOUDS = "03d.png";
    public final static String BROKEN_CLOUDS = "04d.png";
    public final static String SHOWER_RAIN = "09d.png";
    public final static String RAIN = "10d.png";
    public final static String THUNDERSTORM = "11d.png";
    public final static String SNOW = "13d.png";
    public final static String MIST = "50d.png";

    /* getter */ 
    
    public String getEVENT() {
        return EVENT;
    }

    public String getUSER() {
        return USER;
    }

    public String getNOTIF_WEATHER() {
        return NOTIF_WEATHER;
    }

    public String getNOTIF_WARNING() {
        return NOTIF_WARNING;
    }
    
    public String getNOTIF_INVITATION() {
        return NOTIF_INVITATION;
    }

    public String getCLEAR_SKY() {
        return CLEAR_SKY;
    }

    public String getFEW_CLOUDS() {
        return FEW_CLOUDS;
    }

    public String getSCATTERED_CLOUDS() {
        return SCATTERED_CLOUDS;
    }

    public String getBROKEN_CLOUDS() {
        return BROKEN_CLOUDS;
    }

    public String getSHOWER_RAIN() {
        return SHOWER_RAIN;
    }

    public String getRAIN() {
        return RAIN;
    }

    public String getTHUNDERSTORM() {
        return THUNDERSTORM;
    }

    public String getSNOW() {
        return SNOW;
    }

    public String getMIST() {
        return MIST;
    }

}
