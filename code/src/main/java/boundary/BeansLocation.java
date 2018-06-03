/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import control.LocationManager;
import entity.Location;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

/**
 *
 * @author Mirko
 */
@ManagedBean
public class BeansLocation {

    @Inject
    private LocationManager lm;

    private Map<String, String> provinces = new HashMap<String, String>();

    /**
     * load all the location and put them in a combobox
     */
    @PostConstruct
    public void init() {
        List<Location> list = lm.findLocation();
        for (Location loc : list) {
            provinces.put(loc.getLocationName(),Long.toString(loc.getLocationID()));
        }
    }

    /* getter and setter */
    
    public Map<String, String> getProvinces() {
        return provinces;
    }

    public void setProvinces(Map<String, String> provinces) {
        this.provinces = provinces;
    }
    
    public Location getLocation(String idLocation){
        return (lm.findLocation(Long.valueOf(idLocation)));        
    }
}
