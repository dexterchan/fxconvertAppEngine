package TravelFxConvert.TravelFxConvertRestful;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import TravelFxConvert.Daemon.FxRateUpdaterThread;

/**
 * Hello world!
 *
 */
@SpringBootApplication
//@ComponentScan("TravelFxConvert.AngularFront")
//@Configuration
//@EnableAutoConfiguration
//@ComponentScan("TravelFxConvert.TravelFxConvertRestful")
public class App 
{
	final static Logger logger = LogManager.getLogger(App.class);
	public static FxRateUpdaterThread FxUpdateThread=null;
	//private static final Logger logger= Logger.getLogger(App.class);
    public static void main( String[] args ) throws Exception
	{
//    	if(logger.isDebugEnabled()){
//			logger.debug("This is debug");
//		}

		ApplicationContext context = new ClassPathXmlApplicationContext("SpringBeanConfig.xml");
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) context.getBean("taskExecutor");
		
		FxUpdateThread = (FxRateUpdaterThread) context.getBean("ApiLayerFxUpdateThread");

		taskExecutor.execute(FxUpdateThread);
		//Thread.currentThread().sleep(5000);
		SpringApplication.run(App.class, args);
	}
}
