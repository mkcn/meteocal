/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bounday.publics.events;

import boundary.CONST;
import control.DataLoader;
import control.UserDataLoader;
import entity.Event;
import entity.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import meteocalexception.EventException;

/**
 *
 * @author Mirko
 */
@ManagedBean
@SessionScoped
public class BeansPublicEventInfo implements Serializable {

    @Inject
    private DataLoader dl;
    @Inject
    private UserDataLoader udl;

    private Event specificEvent;
    private List<User> invitedUser;

    /**
     * is called by the button of public events with the parameter "ID" of the
     * event to load
     *
     * @return the page where to go
     */
    public String viewEventInfo() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
            return viewEventInfo(getEventFromID(params.get(CONST.PAR_ID)));
        } catch (Exception e) {
            CONST.PrintError("viewEventInfo", "Error public event info parameters");
            return CONST.ERROR_PAGE;
        }
    }

    /**
     * is called by the button of public events with the parameter "ID" of the
     * event to load override with direct parameter
     *
     * @return the page where to go
     */
    public String viewEventInfo(Event event) {
        this.specificEvent = event;
        this.invitedUser = new ArrayList(this.specificEvent.getParticipants().values());
        return CONST.PUBLIC_EVENT_INFO;
    }

    /**
     * from a id string get the specific event from the DB
     */
    private Event getEventFromID(String idString) {
        Long idLong = Long.valueOf(idString);
        return dl.loadSpecificEvent(idLong);
    }

    /**
     * a user that see the public event can join it
     */
    public String joinEvent() {
        try {
            dl.joinEvent(this.specificEvent.getEventID());
            FacesContext.getCurrentInstance().addMessage(CONST.GENERAL_MSG,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Event", "Joined"));
            return CONST.HOME_PAGE;
        } catch (EventException e) {
            CONST.PrintError("Join event",e.toString());
            return CONST.ERROR_PAGE;
        }
    }

    /**
     * check if the current user is already in the specific event loaded
     *
     * @return
     */
    public boolean isAlreadyJoined() {
        Map<String, User> map = this.specificEvent.getParticipants();
        return (map.containsKey(udl.getloggedUser().getEmail()));
    }

    /* getter and setter */
    /**
     * convert the long into the date to show
     *
     * @return
     */
    public Date getStartEvent() {
        return new Date(specificEvent.getStartdate());
    }

    /**
     * convert the long into the date to show
     *
     * @return
     */
    public Date getEndEvent() {
        return new Date(specificEvent.getEnddate());
    }

    public Event getSpecificEvent() {
        return specificEvent;
    }

    public void setSpecificEvent(Event specificEvent) {
        this.specificEvent = specificEvent;
    }

    public List<User> getInvitedUser() {
        return this.invitedUser;
    }

    /**
     *
     * @return the type of patter
     */
    public String getPattern() {
        if (this.specificEvent.isAllDay()) {
            return CONST.PATTERN_DATA;
        }
        return CONST.PATTERN_DATA_WITH_HOUR;
    }
}
