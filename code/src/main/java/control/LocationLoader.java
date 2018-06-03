/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import boundary.CONST;
import entity.Location;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author andrea
 */
@Stateless
public class LocationLoader {
    
    @Inject
    LocationManager lm;
    
    private final String pathFile = "location.txt";
    
 
    
    public void loadLocation(){
       CONST.PrintError("Starting to load", "");
       try{ 
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(pathFile).getFile());
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
        
            int i = 1;
            String cityName;
                
            while( (cityName = reader.readLine()) != null){
             
                Location loc = new Location();
                loc.setLocationID(new Long(i));
                loc.setLocationName(cityName);
                lm.saveLocation(loc);
                i++;
             
            }
       }catch (Exception e){
             CONST.PrintError("LocationLoad: loadLocation" , "Can not load the location");
       }
                
    }
    
}
