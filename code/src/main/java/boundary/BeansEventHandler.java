/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import control.DataLoader;
import control.LocationManager;
import control.UserDataLoader;
import entity.Event;
import entity.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Mirko
 */
@ManagedBean
@SessionScoped
public class BeansEventHandler implements Serializable {

    @Inject
    private UserDataLoader udl;
    @Inject
    private DataLoader dl;
    @Inject
    private LocationManager lm;

    private Event event;
    private Date startEvent, endEvent;
    private String selectedProvince;

    private List<String> selectedInvites;
    private List<User> alreadyInvited;
    private List<User> listUser;

    public BeansEventHandler() {
    }

    /**
     * called each time the page is show, to reset the data and
     * load all the list of user that can be invited (without the owner)
     */
    private void intialPage() {
        try {
            this.listUser = dl.findUsersExceptLoggedOne();
            this.selectedInvites = null;
            this.alreadyInvited = null;
            this.event = null;
        } catch (Exception e) {
            CONST.PrintError("event handler - initialPage", "creation list of user");
        }
    }

    /**
     * get all user matching to autocomplete invitation inputform check for
     * username or email and exclude the already invited users
     *
     * @param query
     * @return
     */
    public List<String> completeText(String query) {
        List<String> results = new ArrayList<>();
        for (int i = 0; i < listUser.size(); i++) {
            if (listUser.get(i).getEmail().startsWith(query)) {
                results.add(listUser.get(i).getEmail());
            }
        }
        return results;
    }

    /**
     * called by eventHandler create a new event or modify one if exist already
     * the ID
     *
     * @return the page where to redirect
     */
    public String createOrModifyEvent() {
        try {
            List<User> newInvited = new ArrayList<>();
            //if there is not invited
            if (this.selectedInvites != null) {
                for (String mail : this.selectedInvites) {
                    newInvited.add(dl.findUser(mail));
                }
            }

            event.setStartdate(this.startEvent.getTime());
            event.setEnddate(this.endEvent.getTime());
            event.setLocation(lm.findLocation(Long.valueOf(this.selectedProvince)));

            //if id is null means that it s never been setter so its a new event
            if (this.event.getEventID() == null) {
                udl.createEvent(event, newInvited);
                FacesContext.getCurrentInstance().addMessage(CONST.GENERAL_MSG,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Event", "Created"));
            } else {
                udl.modifyEvent(event, newInvited);
                FacesContext.getCurrentInstance().addMessage(CONST.GENERAL_MSG,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Event", "Modified"));
            }
            return CONST.HOME_PAGE;
        } catch (Exception e) {
            CONST.PrintError("event handler - createOrModifyEvent", "error creation event : " + e);
            return CONST.ERROR_PAGE;
        }
    }

    /**
     * is called by the home page to view or modify an event reading the ID of
     * the event
     *
     * @return the page where to redirect
     */
    public String openPageViewEvent() {
        try {
            intialPage();
            this.event = getEventFromID(readParameters(CONST.PAR_ID));
            this.startEvent = new Date(this.event.getStartdate());
            this.endEvent = new Date(this.event.getEnddate());
            this.alreadyInvited = new ArrayList(this.event.getParticipants().values());
            removeAlreadyInvited();
            return CONST.EVENT_HANDLER;
        } catch (Exception e) {
            CONST.PrintError("event handler - openPageViewEvent", "Error public event info parameters");
            return CONST.ERROR_PAGE;
        }
    }

    /**
     * TODO REMOVE ONE OF TWO is called by the home page to view or modify an
     * event reading the ID of the event override with direct parameter
     *
     * @return the page where to redirect
     */
    public String openPageViewEvent(Event event) {
        try {
            intialPage();
            this.event = event;
            this.startEvent = new Date(this.event.getStartdate());
            this.endEvent = new Date(this.event.getEnddate());
            this.alreadyInvited = new ArrayList(this.event.getParticipants().values());
            removeAlreadyInvited();
            return CONST.EVENT_HANDLER;
        } catch (Exception e) {
            CONST.PrintError("event handler - openPageViewEvent", "Error public event info parameters");
            return CONST.ERROR_PAGE;
        }
    }
    
    private void removeAlreadyInvited() {
        //remove the already invited from the list of 
        for (User u : this.alreadyInvited) {
            for (User u2 : this.listUser) {
                if (u2.getEmail().contentEquals(u.getEmail())) {
                    this.listUser.remove(u2);
                    break;
                }
            }
        }
    }

    /**
     * called by the homePage to load the page handler
     *
     * @return
     */
    public String openPageNewEvent() {
        try {
            intialPage();
            Long idLong = Long.valueOf(readParameters(CONST.PAR_DATE));
            this.startEvent = new Date(idLong);
            this.endEvent = this.startEvent;
            this.selectedProvince = udl.getloggedUser().getLiveIn().getLocationID().toString();
            return CONST.EVENT_HANDLER;
        } catch (Exception e) {
            CONST.PrintError("event handler - openPageNewEvent", "Error public event info parameters" + e.toString());
            return CONST.ERROR_PAGE;
        }
    }

    /**
     * called by the button "create an event" the location of user and the
     * actual day are auto-selected
     *
     * @return
     */
    public String openPageNewEventToday() {
        try {
            intialPage();
            this.event = null;
            this.alreadyInvited = null;
            this.selectedProvince = udl.getloggedUser().getLiveIn().getLocationID().toString();
            this.startEvent = getCurrentDate();
            this.endEvent = this.startEvent;
            return CONST.EVENT_HANDLER;
        } catch (Exception e) {
            CONST.PrintError("event handler - openPageNewEventToday", "Error public event info parameters" + e.toString());
            return CONST.ERROR_PAGE;
        }
    }

    /**
     * from a id string get the specific event from the DB
     */
    private Event getEventFromID(String idString) {
        Long idLong = Long.valueOf(idString);
        return dl.loadSpecificEvent(idLong);
    }

    /**
     * called by the homePage to delete an event reading the ID of the event
     *
     * @return
     */
    public String deleteEvent() {
        try {
            udl.deleteEvent(getEventFromID(readParameters(CONST.PAR_ID)));
            return CONST.HOME_PAGE;
        } catch (Exception e) {
            return CONST.ERROR_PAGE;
        }
    }

    private String readParameters(String par) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        this.event = new Event();
        String readed = params.get(par);
        return readed;
    }

    private Date getCurrentDate() {
        Calendar cal = Calendar.getInstance(Locale.ITALY); 
        return cal.getTime();
    }

    /* getter and setter */
    public void setSelectedProvince(String selectedProvince) {
        this.selectedProvince = selectedProvince;
    }

    /**
     * called by weather list
     *
     * @return
     */
    public Long getLongIdFromSelectedProvince() {
        return Long.valueOf(this.selectedProvince);
    }

    public String getSelectedProvince() {
        if (this.event != null && this.event.getLocation() != null) {
            try {
                Long id = this.event.getLocation().getLocationID();
                this.selectedProvince = Long.toString(id);
            } catch (Exception e) {
                CONST.PrintError("eventHandler", "error in reading location");
                return null;
            }
        }
        return this.selectedProvince;
    }

    public Date getStartEvent() {
        if (this.startEvent == null) {
            this.startEvent = getCurrentDate();
        }
        return startEvent;
    }

    public void setStartEvent(Date startEvent) {
        this.endEvent = startEvent;
        this.startEvent = startEvent;
    }

    public Date getEndEvent() {
        if (this.endEvent == null && this.startEvent != null) {
            this.endEvent = this.startEvent;
        }
        return endEvent;
    }

    /**
     * read all the invited to the event
     */
    public List<User> getAlreadyInvited() {
        return alreadyInvited;
    }

    /**
     * tmp selectedInvites that will be set in event obj when the user save the
     * event
     *
     * @param Invites
     */
    public List<String> getSelectedInvites() {
        return selectedInvites;
    }

    /**
     * set tmp selectedInvites that will be set in event obj when the user save
     * the event
     *
     * @param Invites
     */
    public void setSelectedInvites(List<String> Invites) {
        this.selectedInvites = Invites;
    }

    public void setEndEvent(Date endEvent) {
        this.endEvent = endEvent;
    }

    public Event getEvent() {
        if (this.event == null) {
            this.event = new Event();
        }
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getPattern() {
        if (this.event.isAllDay()) {
            return CONST.PATTERN_DATA;
        }
        return CONST.PATTERN_DATA_WITH_HOUR;
    }

    public String getMsgButtonSave() {
        if (this.event.getEventID() == null) {
            return "Create event";
        } else {
            return "Modify event";
        }
    }
}
