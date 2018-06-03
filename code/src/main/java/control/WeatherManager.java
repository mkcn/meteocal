/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Location;
import entity.Location_;
import entity.Weather;
import entity.Weather_;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Handle the weather of the db, such as updating, creating
 * deleting them and query the db
 * @author andrea
 */

@Stateless
public class WeatherManager {
    
    @PersistenceContext
    EntityManager em;
    
    /**
     * Merge this list of weather on the DB
     * @param weatherUpdated
     * @param locationID
     */
    public void updateWeather(List<Weather> weatherUpdated, Long locationID){
        for(Weather x: weatherUpdated){
            em.persist(x);            
        }
    }
    
    /**
     * Delete all the weather related to the given location id
     * @param locationId 
     */
    public void deleteWeather(Long locationId){
        List<Weather> weathers = this.getWeatherFromLocation(locationId);
        
        for(Weather x: weathers){
            deleteWeather(x);
        }
        
    }
    
    /**
     * remove a weather entity
     * @param weather 
     */
    public void deleteWeather(Weather weather){
        em.remove(weather);
    }
    
    /**
     * Return all the weather related to a location
     * @param locationId
     * @return 
     */
    public List<Weather> getWeatherFromLocation(Long locationId){
        CriteriaBuilder cb= em.getCriteriaBuilder();
        CriteriaQuery<Weather> cq = cb.createQuery(Weather.class);
        
        Root<Weather> root = cq.from(Weather.class);
        Join<Weather,Location> join = root.join(Weather_.targetLocation);
  
        cq.select(root);
        
        Predicate locationWhere = cb.equal(join.get(Location_.locationID), locationId);
        
        cq.where(locationWhere);
        
        cq.orderBy(cb.asc(root.get(Weather_.weatherDate)));
        
        List<Weather> resultList = em.createQuery(cq).getResultList();
        
        return resultList; 
    }
    
    /**
     * Update an entity weather
     * @param weather 
     */
    public Weather updateWeather(Weather weather){
        return em.merge(weather);
    }
    
    /**
     * Return a list of weather for a specific location between two date
     * @param locationId
     * @param startDate
     * @param endDate
     * @return 
     */    
    public List<Weather> getWeatherFromLocationBetweenTwodate(Long locationId,Long startDate,Long endDate){
        CriteriaBuilder cb= em.getCriteriaBuilder();
        CriteriaQuery<Weather> cq = cb.createQuery(Weather.class);
        
        Root<Weather> root = cq.from(Weather.class);
        Join<Weather,Location> join = root.join(Weather_.targetLocation);
  
        cq.select(root);
        
        Long startDateRemoved = Tool.ToolDate.removeHours(startDate);
        Long endDateRemoved = Tool.ToolDate.removeHours(endDate);
        
        Predicate datewhere = cb.ge(root.get(Weather_.weatherDate),startDateRemoved);        
        Predicate dateWhere2 = cb.le(root.get(Weather_.weatherDate),endDateRemoved);        
        Predicate locationWhere = cb.equal(join.get(Location_.locationID), locationId);
        
        cq.where(cb.and(locationWhere,dateWhere2,datewhere));
        
        cq.orderBy(cb.asc(root.get(Weather_.weatherDate)));
        
        List<Weather> resultList = em.createQuery(cq).getResultList();
        
        return resultList;
    }
    
    /**
     * 
     * @param locationId
     * @param date
     * @return 
     */
    public Weather getWeatherFromLocationAndDate(Long locationId,Long date){
        CriteriaBuilder cb= em.getCriteriaBuilder();
        CriteriaQuery<Weather> cq = cb.createQuery(Weather.class);
        
        Root<Weather> root = cq.from(Weather.class);
        Join<Weather,Location> join = root.join(Weather_.targetLocation);
        
        cq.select(root);
        
        Predicate locationWhere = cb.equal(join.get(Location_.locationID), locationId);
        Predicate dateWhere = cb.equal(root.get(Weather_.weatherDate), date);
        
        cq.where(cb.and(locationWhere,dateWhere));
        
        TypedQuery tq = em.createQuery(cq);
        
        int firstresult = tq.getFirstResult();
        
        List<Weather> list = tq.getResultList();
        
        //if there are no result for a location return null
        if(list.size() > 0){
          return list.get(firstresult);
        } else {
            return null;
        }
    }

    
    
}
