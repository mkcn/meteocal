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
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.junit.Before;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 *
 * @author andrea
 */
public class WeatherManagerTest {
    
    
   private WeatherManager wm;
   private CriteriaBuilder cb;
   private CriteriaQuery<Weather>  cq;
   private Root<Weather> root; 
   private TypedQuery q; 
   private MapJoin<Weather,Long,Location> joinMap;


    
    @Before
    public void initMethod(){
        wm = new WeatherManager();
        wm.em = Mockito.mock(EntityManager.class);
        cb = Mockito.mock(CriteriaBuilder.class);
        cq = Mockito.mock(CriteriaQuery.class);
        root = Mockito.mock(Root.class);
        q = mock(TypedQuery.class);
        joinMap = Mockito.mock(MapJoin.class);
        this.beforeQuery();
        Mockito.when(wm.em.createQuery(cq)).thenReturn(q);  
        Mockito.when(q.getResultList()).thenReturn(new ArrayList<Weather> ());
    }
    
    //@Test
    public void testGetWeatherFromLocationAndDate(){
        

        Predicate locationWhere = cb.equal(joinMap.get(Location_.locationID), Long.MAX_VALUE);
        Predicate dateWhere = cb.equal(root.get(Weather_.weatherDate), Long.MAX_VALUE);
        
        wm.getWeatherFromLocationAndDate(Long.MAX_VALUE, Long.MAX_VALUE);
        
        verify(cq,times(1)).select(root);
        verify(cq,times(1)).where(cb.and(locationWhere,dateWhere));        
    }
    
    //@Test
    public void testGetWeatherFromLocationBetweenTwodate(){
        
        Predicate datewhere = cb.or(cb.between(root.get(Weather_.weatherDate), Long.MAX_VALUE, Long.MAX_VALUE));
        Predicate locationWhere = cb.equal(joinMap.get(Location_.locationID), Long.MAX_VALUE);
        
        wm.getWeatherFromLocationBetweenTwodate(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE);
        
        verify(cq,times(1)).select(root);
        verify(cq,times(1)).where(cb.and(datewhere,locationWhere));
        
    }
    
    
    
/*-----------------------------------------------------------------*/
    
    private void beforeQuery(){
        Mockito.when(wm.em.getCriteriaBuilder()).thenReturn(cb);
        Mockito.when(cb.createQuery(Weather.class)).thenReturn(cq);
        Mockito.when(cq.from(Weather.class)).thenReturn(root);
        Mockito.when(root.join(Weather_.targetLocation)).thenReturn(joinMap);
    }
    

}
