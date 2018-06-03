/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary.publics.calendars;

import boundary.CONST;
import control.DataLoader;
import entity.Event;
import entity.User;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author Mirko
 */
@ManagedBean
@SessionScoped
public class BeansPublicCalendarInfo {

    @Inject
    private DataLoader dl;
    
    private User specificUser;
    private ScheduleModel lazyEventModel;
    private ScheduleEvent selectedEvent = new DefaultScheduleEvent();
    private boolean oneEventSelected;

    /**
     * when page is opened the calendar load a range of events (between two date)
     * when the page changes , the method loadEvent is reloaded 
     */
    @PostConstruct
    public void init() {
        lazyEventModel = new LazyScheduleModel() {
            @Override
            public void loadEvents(Date start, Date end) {
                //in this way the selectedEvent loaded on the graphic are ranged
                for (Event event : loadRangedEvents(start, end)) {
                    //(boolean) is needed because without it the function get an object
                    ScheduleEvent scheduleEvent;
                    if (event.isPublicEvent()) {
                        scheduleEvent = new DefaultScheduleEvent(event.getEventName(),
                                new Date(event.getStartdate()), new Date(event.getEnddate()), 
                                (boolean) event.isAllDay());
                    } else {
                        scheduleEvent = new DefaultScheduleEvent(CONST.NAME_PRIVATE_EVENT,
                                new Date(event.getStartdate()), new Date(event.getEnddate()), 
                                (boolean) event.isAllDay());
                    }
                    addEvent(scheduleEvent);
                    //the id is set here because the "addEvent" change it
                    scheduleEvent.setId(event.getEventID().toString());
                }
            }
        };
    }

    private List<Event> loadRangedEvents(Date startDate, Date endDate) {
        return dl.findEventBetweenTwoDateForSpecificUser(this.specificUser.getEmail(),
                startDate.getTime(),endDate.getTime());
       
    } 

    /**
     * method called by ajax when an event is selected in the calendar
     * @param selectEvent 
     */
    public void onEventSelect(SelectEvent selectEvent) {
        this.selectedEvent = (ScheduleEvent) selectEvent.getObject();
        this.oneEventSelected = true;
    }

    /**
     * called by publicCalendars
     * @return 
     */
    public String viewCalendarInfo() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
            this.specificUser = getUserFromID(params.get(CONST.PAR_ID));
            return CONST.PUBLIC_CALENDAR_INFO;
        } catch (Exception e) {
            CONST.PrintError("","Error public event info parameters");
            return CONST.ERROR_PAGE;
        }
    }
    
    /* getter and setter */
    
    public ScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }

    public User getSpecificUser() {
        return this.specificUser;
    }

    private User getUserFromID(String idMail) {
        return dl.findUser(idMail);
    }
    
    public String getInfoEventSelected() {
        if (this.oneEventSelected) {
            return this.selectedEvent.getTitle();
        }
        return "";
    }

    public boolean getOneEventSelected() {
        return this.oneEventSelected;
    }
    
    public boolean isAPrivateEvent(){
        return (this.selectedEvent.getTitle() == CONST.NAME_PRIVATE_EVENT);
    }

    public ScheduleEvent getSelectedEvent() {
        return selectedEvent;
    }
}
