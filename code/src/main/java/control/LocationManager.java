/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;



import entity.Location;
import entity.Location_;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
  * Handle the locations of the db, such as updating, creating
 * deleting them and query the db
 * @author andrea
 */
@Stateless
public class LocationManager {
    
    @PersistenceContext
    private EntityManager em;
    
    /**
     *Find a specific location given its id
     * @param locationID
     * @return the found location
     */
    public Location findLocation(Long locationID){
        return em.find(Location.class,locationID);
    }
    
    /**
     * Persist a location on the DB
     * @param location 
     */
    public void saveLocation(Location location){
        em.merge(location);
    }
    
    /**
     * Find all the locations
     * @return a list with all the location
     */
    public List<Location> findLocation(){
        
        CriteriaBuilder cb= em.getCriteriaBuilder();
        CriteriaQuery<Location> cq = cb.createQuery(Location.class);
        
        Root<Location> locationRoot = cq.from(Location.class);
        
        cq.select(locationRoot);
        
        cq.orderBy(cb.asc(locationRoot.get(Location_.locationName)));
        
        return em.createQuery(cq).getResultList();
        
    }
    
}
