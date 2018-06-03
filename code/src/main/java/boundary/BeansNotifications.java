/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import control.DataLoader;
import control.UserDataLoader;
import entity.Invitation;
import entity.Notification;
import entity.Warning;
import entity.WarningType;
import entity.WeatherAdvice;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

/**
 *
 * @author Mirko
 */
@ManagedBean
@SessionScoped
public class BeansNotifications implements Serializable {

    @Inject
    private UserDataLoader udl;
    @Inject
    private DataLoader dl;

    private int numNotificationsToRead;
    private List<Notification> previewNotifications;
    private List<Notification> filteredPreviewNotifications;

    /**
     * get the number of notification unseen each n seconds
     */
    public void checkNumNotificationsUnSeen() {
        try {
            this.numNotificationsToRead = udl.getMyNumberNotification();
        } catch (Exception e) {
            this.numNotificationsToRead = -1;
        }
    }

    /**
     * called by user if what to set to "seen" or to "unseen" a notification
     *
     * @param notif
     */
    public void modifySeenNotification(Notification notif, Boolean setSeen) {
        this.dl.setSeenForNotification(notif, setSeen);
    }

    /**
     * show different message for each type of notification
     *
     * @param notif
     * @return
     */
    public String getDetail(Notification notif) {
        try {
            if (notif instanceof Warning) {
                return getMessageFromWarningType(((Warning) notif).getType());
            } else if (notif instanceof WeatherAdvice) {
                return "Weather advice! Change the date of the event. The next day with good weather is "
                        + getShortDateFromLong((((WeatherAdvice) notif).getBestDate()));
            } else if (notif instanceof Invitation) {
                if (((Invitation) notif).isAccepted()) {
                    return "You have already accepted this invitation";
                } else {
                    return "You are invited to the event";
                }
            } else {
                return "Error type of notification";
            }
        } catch (Exception e) {
            CONST.PrintError("notification - get detail", e.toString());
            return "Error conversion";
        }
    }

    private String getShortDateFromLong(Long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String patternToUse = sdf.format(new Date(date));
        return patternToUse;
    }

    private String getMessageFromWarningType(String wt) {
        if (wt.contentEquals(WarningType.ALLERT.toString())) {
            return "The event occour in less that a day!";
        } else if (wt.contentEquals(WarningType.CHANGEDATE.toString())) {
            return "The owner changed the date of the event";
        } else if (wt.contentEquals(WarningType.CHANGE_ADDRESS.toString())) {
            return "The owner changed the address of the event";
        } else if (wt.contentEquals(WarningType.CHANGE_DESCRIPTION.toString())) {
            return "The owner changed the description of the event";
        } else if (wt.contentEquals(WarningType.CHANGE_NAME.toString())) {
            return "The owner changed the name of the event";
        } else if (wt.contentEquals(WarningType.CHANGE_OUSIDE.toString())) {
            return "The owner changed the place of the event";
        } else if (wt.contentEquals(WarningType.CHANGE_PUBLIC.toString())) {
            return "The owner changed the visibility of the event";
        } else if (wt.contentEquals(WarningType.EVENTDELETED.toString())) {
            return "The owner deleted the event";
        } else if (wt.contentEquals(WarningType.MANY_CHANGE.toString())) {
            return "The owner changed some data about the event";
        } else if (wt.contentEquals(WarningType.NEWPARTICIPANT.toString())) {
            return "A user joined the event";
        } else if (wt.contentEquals(WarningType.PARTICIPANTREMOVED.toString())) {
            return "A user is removed from the event";
        } else {
            return "Error type of warning";
        }
    }

    /**
     *
     * @param notif
     * @return a different icon for each type of notification
     */
    public String getIconNotification(Notification notif) {
        if (notif instanceof Warning) {
            return CONSTRES.NOTIF_WARNING;
        } else if (notif instanceof WeatherAdvice) {
            return CONSTRES.NOTIF_WARNING;
        } else if (notif instanceof Invitation) {
            return CONSTRES.NOTIF_INVITATION;
        } else {
            return ""; // error if show this
        }
    }

    /**
     * NOT USED , but keep for future use check if a notification is already
     * seen and return a string message
     *
     * @param notif
     * @return
     */
    public String checkIfSeen(Notification notif) {
        if (!notif.isSeen()) {
            return "set as seen";
        }
        return "set as unseen";
    }

    /**
     * check if the current user is the owner
     *
     * @param notif
     * @return
     */
    public Boolean checkIfOnwer(Notification notif) {
        return (notif.getAbout().getOwner().getEmail().contentEquals(udl.getloggedUser().getEmail()));
    }

    /* getter and setter */
    public int getNumNotificationsToRead() {
        return numNotificationsToRead;
    }

    /**
     * get all my notification and set seen the new ones
     *
     * @return
     */
    public List<Notification> getPreviewNotifications() {
        this.previewNotifications = udl.getMyNotification();
        for (Notification notif : this.previewNotifications) {
            this.dl.setSeenForNotification(notif, true);
        }
        return previewNotifications;
    }

    public void setPreviewNotifications(List<Notification> previewNotifications) {
        this.previewNotifications = previewNotifications;
    }

    public List<Notification> getFilteredPreviewNotifications() {
        return filteredPreviewNotifications;
    }

    public void setFilteredPreviewNotifications(List<Notification> filteredPreviewNotifications) {
        this.filteredPreviewNotifications = filteredPreviewNotifications;
    }

}
