package boundary;

import control.DataLoader;
import control.UserDataLoader;
import entity.Event;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 * class to handle the home page
 * @author Mirko
 */
@ManagedBean
@ViewScoped
public class BeansHomePage implements Serializable {

    @Inject
    private UserDataLoader udl;
    @Inject
    private DataLoader dl;

    private ScheduleModel lazyEventModel;
    private ScheduleEvent selectedEvent = new DefaultScheduleEvent();
    private Long selectedDate;

    private boolean oneEventSelected, oneDateSelected, isTheOwner;

    /**
     * called each time the range of date change and when the page is loaded
     */
    @PostConstruct
    public void init() {
        lazyEventModel = new LazyScheduleModel() {
            @Override
            public void loadEvents(Date start, Date end) {
                //in this way the selectedEvent loaded on the graphic are ranged
                for (Event event : loadRangedEvents(start, end)) {
                    //(boolean) is needed because without it the function get an object
                    ScheduleEvent scheduleEvent = new DefaultScheduleEvent(event.getEventName(),
                            new Date(event.getStartdate()), new Date(event.getEnddate()), (boolean) event.isAllDay());
                    addEvent(scheduleEvent);
                    //the id is set here because the "addEvent" change it
                    scheduleEvent.setId(event.getEventID().toString());
                }
            }
        };
    }

    private List<Event> loadRangedEvents(Date startDate, Date endDate) {
        return udl.getMyLazyEvent(startDate.getTime(), endDate.getTime());
    }

    /**
     * when an event is selected the event is saved
     * @param selectEvent 
     */
    public void onEventSelect(SelectEvent selectEvent) {
        this.selectedEvent = (ScheduleEvent) selectEvent.getObject();
        this.selectedDate = this.selectedEvent.getStartDate().getTime();
        this.oneDateSelected = true;
        this.oneEventSelected = true;
        Long id =  Long.valueOf(this.selectedEvent.getId());
        this.isTheOwner =
                dl.loadSpecificEvent(id).getOwner().getEmail().contentEquals(udl.getloggedUser().getEmail());
    }

    /**
     * when a date is selected the date is saved
     * @param selectEvent 
     */
    public void onDateSelect(SelectEvent selectEvent) {
        this.selectedDate = ((Date) selectEvent.getObject()).getTime();
        this.oneDateSelected = true;
        this.oneEventSelected = false;
        this.isTheOwner = false;
    }

    /**
     * get the long of today without hour
     * @return 
     */
    public Long getToday() {
        Calendar cal = Calendar.getInstance(Locale.ITALY); 
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime().getTime();
    }

    /**
     * get the long of the day of the next week without hour
     * @return
     */
    public Long getTodayPlusSevenDay() {
        Calendar cal = Calendar.getInstance(Locale.ITALY); 
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.DATE, 7);
        return cal.getTime().getTime();
    }
    
    /* getter and setter */
    
    /**
     * used by button "modify/view event.."
     * @return 
     */
    public String getInfoEventSelected() {
        if (this.oneEventSelected) {
            return this.selectedEvent.getTitle();
        }
        return "";
    }

    /**
     * used by the button "create event on .."
     * @return 
     */
    public String getInfoDaySelected() {
        if (this.oneDateSelected) {
            return new Date(this.selectedDate).getDate() + "th";
        }
        return "";
    }

    public Long getSelectedDate() {
        return this.selectedDate;
    }

    public boolean getOneEventSelected() {
        return this.oneEventSelected;
    }

    public boolean getOneDateSelected() {
        return this.oneDateSelected;
    }

    public ScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }

    public ScheduleEvent getSelectedEvent() {
        return selectedEvent;
    }

    public boolean isIsTheOwner() {
        return isTheOwner;
    }
}
