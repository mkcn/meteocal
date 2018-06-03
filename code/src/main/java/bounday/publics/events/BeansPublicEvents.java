/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import boundary.CONST;
import control.DataLoader;

import entity.Event;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Mirko
 */
@ManagedBean
@ViewScoped
public class BeansPublicEvents implements Serializable {

    @EJB
    private DataLoader dl;

    private List<Event> previewEvents;
    private List<Event> filteredPreviewEvents;
    private Date fromFilter, toFilter;

    /**
     * when the page is opened the list of event is loaded
     */
    @PostConstruct
    public void init() {
        this.fromFilter = getToday();
        this.toFilter = getNext2Month();
        updateList();
        if (this.previewEvents.isEmpty()) {
            CONST.PrintError("publicEvents", "empty list");
        }
    }

    /**
     * get filtered list of event between the two date selected
     */
    public void updateList() {
        this.previewEvents = dl.findPublicEventBetweenTwoDate(fromFilter.getTime(),
                toFilter.getTime());
    }

    private Date getToday() {
        Calendar cal = Calendar.getInstance(Locale.ITALY); 
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }
    
    private Date getNext2Month() {
        Calendar cal = Calendar.getInstance(Locale.ITALY);
        cal.add(Calendar.MONTH, 2);
        return cal.getTime();
    }

    /* getter and setter */
    public Long getFilterStartDate() {
        return this.fromFilter.getTime();
    }

    public List<Event> getPreviewEvents() {
        updateList();
        return previewEvents;
    }

    public void setPreviewEvents(List<Event> previewEvents) {
        this.previewEvents = previewEvents;
    }

    public List<Event> getFilteredPreviewEvents() {
        updateList();
        //TODO fix it
        return filteredPreviewEvents;
    }

    public void setFilteredPreviewEvents(List<Event> filteredPreviewEvents) {
        this.filteredPreviewEvents = filteredPreviewEvents;
    }

    public Date getFromFilter() {
        return fromFilter;
    }

    public void setFromFilter(Date fromFilter) {
        this.fromFilter = fromFilter;
    }

    public Date getToFilter() {
        return toFilter;
    }

    public void setToFilter(Date toFilter) {
        this.toFilter = toFilter;
    }

}
