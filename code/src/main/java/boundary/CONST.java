/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import javax.faces.bean.ManagedBean;

/**
 * all the global constants used to navigate between the pages
 *
 * @author Mirko
 */
@ManagedBean(name = "CONST")
public class CONST {

    public final static String PAR_ID = "ID";
    public final static String PAR_DATE = "DATE";
    public final static String TIMEZONE_ITA = "GMT+1";
    public final static String NAME_PRIVATE_EVENT = "PRIVATE EVENT";
    public final static String DEBUG_INDEX = "debugIndex";
    public final static String ERROR_PAGE = "errorPage";
    public final static String HOME_PAGE = "homePage";
    public final static String HOME_PAGE_TMP = "home";
    public final static String EVENT_HANDLER = "eventHandler";
    public final static String ACCOUNT_SETTING = "accountSettings";
    public final static String PUBLIC_EVENTS = "publicEvents";
    public final static String PUBLIC_EVENT_INFO = "publicEventInfo";
    public final static String PUBLIC_CALENDARS = "publicCalendars";
    public final static String PUBLIC_CALENDAR_INFO = "publicCalendarInfo";
    public final static String FOLDER_APP = "MeteoCal";
    public final static String FOLDER_USER = "user/";
    public final static String NOTIFICATIONS = "notifications";
    public final static String ACCESS_PAGE = "index";
    public final static String DIALOG_ISSUE = "issueDialog";
    public final static String DIALOG_SHARE = "shareDialog";
    public final static String GENERAL_MSG = "generalMsg";
    

    public final static Integer NUM_LOAD_LIST = 10;
    public final static String SIZE_NAME_PAGE = "25px";
    public final static String SIZE_MENU_COLUMN = "270";
    public final static String SIZE_WEATHER_COLUMN = "220";
    public final static String SIZE_ACTION_ROW = "90";
    public final static String PATTERN_DATA = "dd/MM/yyyy";
    public final static String PATTERN_DATA_WITH_HOUR = "dd/MM/yyyy HH:mm";
    
    /**
     * like a logger
     *
     * @param title title of msg , can be leave empty
     * @param msg message with information
     */
    public static void PrintMessage(String title, String msg) {
        if (title.isEmpty()) {
            System.out.println(msg);
        }
        System.out.println(title + " : " + msg);
    }

    /**
     * like a logger
     *
     * @param title title of msg , can be leave empty
     * @param msg message with information
     */
    public static void PrintError(String title, String msg) {
        if (title.isEmpty()) {
            System.err.println(msg);
        }
        System.err.println(title + " : " + msg);
    }

    /* getter */
    
    public String getSIZE_NAME_PAGE() {
        return SIZE_NAME_PAGE;
    }

    public String getNAME_PRIVATE_EVENT() {
        return NAME_PRIVATE_EVENT;
    }

    public Integer getNUM_LOAD_LIST() {
        return NUM_LOAD_LIST;
    }

    public String getPATTERN_DATA() {
        return PATTERN_DATA;
    }

    public String getPATTERN_DATA_WITH_HOUR() {
        return PATTERN_DATA_WITH_HOUR;
    }

    public String getGENERAL_MSG() {
        return GENERAL_MSG;
    }

    public String getTIMEZONE_ITA() {
        return TIMEZONE_ITA;
    }

    public String getACCESS_PAGE() {
        return ACCESS_PAGE;
    }

    public String getPAR_ID() {
        return PAR_ID;
    }

    public String getPAR_DATE() {
        return PAR_DATE;
    }

    public String getDEBUG_INDEX() {
        return DEBUG_INDEX;
    }

    public String getHOME_PAGE() {
        return HOME_PAGE;
    }

    public String getEVENT_HANDLER() {
        return EVENT_HANDLER;
    }

    public String getACCOUNT_SETTING() {
        return ACCOUNT_SETTING;
    }

    public String getPUBLIC_EVENTS() {
        return PUBLIC_EVENTS;
    }

    public String getPUBLIC_CALENDARS() {
        return PUBLIC_CALENDARS;
    }

    public String getFOLDER_USER() {
        return FOLDER_USER;
    }

    public String getERROR_PAGE() {
        return ERROR_PAGE;
    }

    public String getHOME_PAGE_TMP() {
        return HOME_PAGE_TMP;
    }

    public String getPUBLIC_EVENT_INFO() {
        return PUBLIC_EVENT_INFO;
    }

    public String getPUBLIC_CALENDAR_INFO() {
        return PUBLIC_CALENDAR_INFO;
    }

    public String getNOTIFICATIONS() {
        return NOTIFICATIONS;
    }

    public String getDIALOG_ISSUE() {
        return DIALOG_ISSUE;
    }

    public String getDIALOG_SHARE() {
        return DIALOG_SHARE;
    }

    public String getSIZE_MENU_COLUMN() {
        return SIZE_MENU_COLUMN;
    }

    public String getSIZE_WEATHER_COLUMN() {
        return SIZE_WEATHER_COLUMN;
    }

    public String getSIZE_ACTION_ROW() {
        return SIZE_ACTION_ROW;
    }

    public String getFOLDER_APP() {
        return FOLDER_APP;
    }
    
    
}
