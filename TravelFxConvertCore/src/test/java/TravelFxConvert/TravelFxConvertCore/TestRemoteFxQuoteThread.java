package TravelFxConvert.TravelFxConvertCore;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import TravelFxConvert.Daemon.FxRateUpdaterThread;
import TravelFxConvert.Model.FXRateContainer;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestRemoteFxQuoteThread extends TestCase {

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public TestRemoteFxQuoteThread(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static TestSuite suite() {
		return new TestSuite(TestRemoteFxQuoteThread.class);
	}

	@Test
	public void testApp() {
		this.setName("TestFXRateRetrieval");
		// fail("Not yet implemented");
		int i = 0;
		// ApplicationContext context = new
		// AnnotationConfigApplicationContext(ThreadConfig.class);
		ApplicationContext context = new ClassPathXmlApplicationContext("SpringBeanConfig.xml");
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) context.getBean("taskExecutor");
		// ApplicationContext contextThread = new
		// ClassPathXmlApplicationContext("SpringBeanConfig.xml");

		// ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor)
		// contextThread.getBean("taskExecutor");
		FxRateUpdaterThread Fxtask = (FxRateUpdaterThread) context.getBean("ApiLayerFxUpdateThread");

		taskExecutor.execute(Fxtask);

		//for (;;) 
		{
			int count = taskExecutor.getActiveCount();
			System.out.println("Active Threads : " + count);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//if (count == 0) 
			{
				
				
				
				taskExecutor.shutdown();
				//break;
			}
			
			assertTrue(FXRateContainer.getExtFxRresher().size()>0);
		}
	}

}
