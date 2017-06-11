package TravelFxConvert.Controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

public class ExternalFxRefresherFactory {
	private static final Logger log = LogManager.getLogger(ExternalFxRefresherFactory.class);
	//private static final Logger log= Logger.getLogger(ExternalFxRefresherFactory.class);
	static volatile FxRateRefresherInterface myFxInstance=null;
	public static FxRateRefresherInterface getExtFxRresher(String Name){
		if(myFxInstance==null){
			synchronized(ExternalFxRefresherFactory.class){
				if(myFxInstance==null){
					ConfigurableApplicationContext springContext =
					        new ClassPathXmlApplicationContext("FxRefresherConfig.xml");
					myFxInstance=springContext.getBean(Name,FxRateRefresherInterface.class);
				}
			}
		}
		return myFxInstance;
	}
}
