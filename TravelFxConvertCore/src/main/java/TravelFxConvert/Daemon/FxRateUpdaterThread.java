package TravelFxConvert.Daemon;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import TravelFxConvert.Controller.FxRateRefresherInterface;
import TravelFxConvert.Model.FXQuote;
import TravelFxConvert.Model.FXRateContainer;
import TravelFxConvert.Model.FxDAG;

public class FxRateUpdaterThread implements Runnable 
{
	private static final Logger log = LogManager.getLogger(FxRateUpdaterThread.class);
	//private static final Logger log= Logger.getLogger(FxRateUpdaterThread.class);
	String name;
	int milliSecond_period;
	boolean alive=true;
	FxRateRefresherInterface refreshInterface=null;
	Set<String> ccySet=null;
	String defaultccy=null;
	private final AtomicLong refreshcounter = new AtomicLong();
	
	
	public String getDefaultccy() {
		return defaultccy;
	}


	public void setDefaultccy(String defaultccy) {
		this.defaultccy = defaultccy;
	}


	public Set<String> getCcySet() {
		return ccySet;
	}


	public void setRefreshInterface(FxRateRefresherInterface refreshInterface) {
		this.refreshInterface = refreshInterface;
	}


	public void setCcySet(Set<String> ccySet) {
		this.ccySet = ccySet;
	}


	public void setName(String name) {
		this.name = name;
		//refreshInterface = ExternalFxRefresherFactory.getExtFxRresher(name);
	}


	public void setMilliSecond_period(int milliSecond_period) {
		this.milliSecond_period = milliSecond_period;
	}
	
	public void setAlive(boolean alive) {
		this.alive = alive;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			while(alive){
				log.info("Running refresh");
				ConcurrentHashMap<String, Double>  fxContainer=FXRateContainer.getExtFxRresher();
				
				
				FXQuote q=refreshInterface.getFx(ccySet, this.defaultccy);
				log.info(q.toString());
				q.quote.forEach( (key,value) -> fxContainer.put(key, value) );
				
				FxDAG d = FXRateContainer.getFxDAG();
	        	d.updateFxRate(q);
		        	
				Thread.currentThread().sleep(milliSecond_period);
			}
		}catch (Exception e){
			log.error(e.toString());
		}
	}

}
