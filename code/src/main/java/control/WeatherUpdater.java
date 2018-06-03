/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Location;
import entity.Weather;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Has the purpose of retrive information about the weather
 * @author andrea
 */
@Stateless
public class WeatherUpdater {
    
    @Inject
    WeatherFromJson wfj;
    
    private final String APPID = "&APPID=ed2ad107f0a93a037367acb341dc3c9c";
    private final String modeMetricCount = "&mode=json&units=metric&cnt=16";
    private final String baseUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?q=";
    
    /**
     * Return 16 days of forecast for a city
     * @param location
     * @return 
     * @throws IOException
     */
    public List<Weather> dailyForecastForLocation(Location location) throws IOException{
  
       //compose the url
       String URL = baseUrl + location.getLocationName() + modeMetricCount + APPID;
       
       //do the http request
       String response = this.httpRequest(URL);
       
       List<Weather> updatedWeather = wfj.getWeatherList(response,location);
                
       return updatedWeather;
    }

    
    //example of URL well formed
    //"http://api.openweathermap.org/data/2.5/forecast/daily?q=Verona&mode=json&units=metric&cnt=10&APPID=ed2ad107f0a93a037367acb341dc3c9c"
    private String httpRequest(String URL) throws IOException{           
       String responseBody = null;
       HttpClient httpClient = new DefaultHttpClient ();
       HttpGet httpget = new HttpGet (URL);
       HttpResponse response = httpClient.execute (httpget);
       InputStream contentStream = null;
       try {
            StatusLine statusLine = response.getStatusLine ();
	    if (statusLine == null) {
		throw new IOException (
		    String.format ("Unable to get a response from OWM server"));
		}
	    int statusCode = statusLine.getStatusCode ();
	    if (statusCode < 200 && statusCode >= 300) {
	        throw new IOException (
		    String.format ("OWM server responded with status code %d: %s", statusCode, statusLine));
		}
	    /* Read the response content */
	    HttpEntity responseEntity = response.getEntity ();
            contentStream = responseEntity.getContent ();
            Reader isReader = new InputStreamReader (contentStream);
            int contentSize = (int) responseEntity.getContentLength ();
            if (contentSize < 0){
		contentSize = 8*1024;
            }
	    StringWriter strWriter = new StringWriter (contentSize);
            char[] buffer = new char[8*1024];
            int n = 0;
            while ((n = isReader.read(buffer)) != -1) {
                    strWriter.write(buffer, 0, n);
		}
       	    responseBody = strWriter.toString ();
	    contentStream.close ();
            } catch (IOException e) {
                throw e;
            } catch (RuntimeException re) {
            	httpget.abort ();
		throw re;
            } finally {
		if (contentStream != null)
			contentStream.close ();
	    }
       return responseBody;
    }
    
  
    
}
