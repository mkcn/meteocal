/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Mirko
 */
@ManagedBean
@SessionScoped
public class BeansConverter implements Serializable{

    /* converter */
    /**
     *
     * @param date in Long type
     * @return a date in Date type
     */
    public Date getDateFromLong(Long date) {
        if (date == null) {
            return null;
        }
        return new Date(date);
    }
}
